
package com.pricingservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricingservice.dto.*;
import com.pricingservice.entities.Customer;
import com.pricingservice.entities.Product;
import com.pricingservice.entities.Rules;
import com.pricingservice.exception.ProductNotFoundException;
import com.pricingservice.exception.ProductOutOfStockException;
import com.pricingservice.repository.CustomerRepository;
import com.pricingservice.repository.ProductRepository;
import com.pricingservice.repository.RuleRepository;
import com.pricingservice.strategy.PricingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PricingService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final RuleRepository ruleRepository;
    private final PricingStrategyFactory strategyFactory;
    private final ObjectMapper objectMapper;

    public PricingService(CustomerRepository customerRepository,
                          ProductRepository productRepository,
                          RuleRepository ruleRepository,
                          PricingStrategyFactory strategyFactory,
                          ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.ruleRepository = ruleRepository;
        this.strategyFactory = strategyFactory;
        this.objectMapper = objectMapper;

    }




    public PricingResponseDto calculateTotal(PricingRequestDto requestDto) {

        log.info("🧾 Pricing calculation started for Customer: {}", requestDto.getCustomerId());

        BigDecimal baseTotal = calculateBaseTotal(requestDto);
        Customer customer = getCustomer(requestDto.getCustomerId());
        List<Product> productsToUpdate = new ArrayList<>();
        Product productForUse=null;

        for (ItemDTO item : requestDto.getItems()) {
            Product product = productRepository.findByProductCode(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "❌ Product not found: " + item.getProductId()
                    ));

            productForUse=product;
            if (product.getAvailableStock() == null || product.getAvailableStock() < item.getQuentity()) {
                throw new ProductOutOfStockException(
                        "🚫 Product '" + product.getName() + "' is out of stock or insufficient quantity. " +
                                "Available: " + product.getAvailableStock() + ", Requested: " + item.getQuentity()
                );
            }

            productsToUpdate.add(product);
        }

        List<String> itemCategories = requestDto.getItems().stream()
                .map(i -> i.getCategory() != null ? i.getCategory().toLowerCase() : "")
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        String[] categoriesArray = itemCategories.isEmpty() ? null : itemCategories.toArray(new String[0]);

        List<Rules> rules = ruleRepository.findActiveRules(
                requestDto.getRegion(),
                requestDto.getRequestTime(),
                categoriesArray,
                customer.getTier(),
                requestDto.isReferred()
        );

        if (rules.isEmpty()) {
            throw new ProductNotFoundException(
                    "No active pricing rules found for region: " + requestDto.getRegion()
            );
        }

        rules.sort(Comparator.comparing(Rules::getPriority));
        log.info("✅ {} Rules applied in sorted order.", rules.size());

        BigDecimal finalTotal = baseTotal;
        List<StrategyDiscountDetailDTO> strategyDetails = new ArrayList<>();
        BigDecimal cumulativeDiscountPercent = BigDecimal.ZERO;
        boolean wholesaleApplied=false;

        for (Rules rule : rules) {

            PricingStrategy strategy = strategyFactory.getStrategy(rule.getStrategyKey());
            if (strategy == null) {
                log.warn("❌ Strategy {} not registered!", rule.getStrategyKey());
                continue;
            }

            if (wholesaleApplied && !"REGIONAL_PRICING".equalsIgnoreCase(strategy.getStrategyKey())) {
                log.info("⏭ Skipping {} because WHOLESALE strategy already applied.", strategy.getStrategyKey());
                continue;
            }

            Map<String, Object> parameters = parseJson(rule.getParameters());
            Map<String, Object> conditions = parseJson(rule.getConditions());


            parameters.put("baseTotal", finalTotal);
            parameters.put("membershipDiscount", customer.getTier());
            parameters.put("availableStock",productForUse.getAvailableStock());
            parameters.put("isReferred", requestDto.isReferred());
            parameters.put("lastMinute", requestDto.getRequestTime());
            parameters.put("couponCode",requestDto.getCoupon());
            parameters.put("strategyDateFromDB", rule.getActiveUntil());
            parameters.put("paymentMode", requestDto.getPaymentMode());
            parameters.put("requestTime", requestDto.getRequestTime());
            parameters.put("validUntil", rule.getActiveUntil());
            parameters.put("region", rule.getRegion());

            if (!areConditionsSatisfied(conditions, requestDto, customer, rule, requestDto.getItems())) {
                log.info("⏩ Skipping {}: Conditions not satisfied.", rule.getStrategyKey());
                continue;
            }

            BigDecimal discountedTotal = strategy.applyStrategy(requestDto.getItems(), parameters);
            discountedTotal = discountedTotal.max(BigDecimal.ZERO);

            BigDecimal discountAmount = finalTotal.subtract(discountedTotal).max(BigDecimal.ZERO);
            BigDecimal discountPercent = BigDecimal.ZERO;

            if (finalTotal.compareTo(BigDecimal.ZERO) > 0) {
                discountPercent = discountAmount.divide(finalTotal, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            BigDecimal newCumulativePercent = cumulativeDiscountPercent.add(discountPercent);

            if (newCumulativePercent.compareTo(BigDecimal.valueOf(80)) > 0) {
                log.warn("⚠ Combined discount exceeded 40%. Stopping further strategy applications.");
                break;
            }

            cumulativeDiscountPercent = newCumulativePercent;
            strategyDetails.add(new StrategyDiscountDetailDTO(
                    strategy.getStrategyKey(),
                    discountPercent,
                    discountAmount,
                    discountedTotal
            ));

            log.info("✅ Strategy {} applied → Discount: {}% | Cumulative: {}%",
                    rule.getStrategyKey(), discountPercent, cumulativeDiscountPercent);

            finalTotal = discountedTotal;
            if ("WHOLESALE_DISCOUNT".equalsIgnoreCase(strategy.getStrategyKey())) {
                wholesaleApplied = true;
            }
        }

        // 🗺 Apply region-based strategy only if cumulative < 40%

            StrategyDiscountDetailDTO regionStrategy = applyRegionBasedStrategy(requestDto, strategyDetails, finalTotal);
            if (regionStrategy != null) {
                strategyDetails.add(regionStrategy);
                finalTotal = regionStrategy.getTotalAfterDiscount();
            }

        for (ItemDTO item : requestDto.getItems()) {
            Product product = productsToUpdate.stream()
                    .filter(p -> p.getProductCode().equals(item.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException("Product not found during stock update."));

            int newStock = product.getAvailableStock() - item.getQuentity();
//            product.setAvailableStock(newStock);
            productRepository.updateAvailableStock(item.getProductId(),newStock);

            log.info("📦 Stock updated for {} → Remaining: {}", product.getName(), newStock);
        }


        BigDecimal totalDiscount = strategyDetails.stream()
                .map(StrategyDiscountDetailDTO::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PricingResponseDto response = new PricingResponseDto();
        response.setCustomerId(requestDto.getCustomerId());
        response.setBaseTotal(baseTotal);
        response.setFinalTotal(finalTotal.max(BigDecimal.ZERO));
        response.setStrategyDetails(strategyDetails);
        response.setTotalDiscountGiven(totalDiscount);
        response.setReferred(requestDto.isReferred());
        response.setNewLaunchOffer(requestDto.isNewLaunchOffer());
        response.setCalculatedAt(LocalDateTime.now());

        log.info("🎯 Pricing Completed → Final Amount: {} (Total Discount: {}%)",
                finalTotal, cumulativeDiscountPercent);

        return response;
    }














    private BigDecimal calculateBaseTotal(PricingRequestDto requestDto) {
        BigDecimal baseTotal = BigDecimal.ZERO;
        for (ItemDTO item : requestDto.getItems()) {
            Product product = productRepository.findByProductCode(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + item.getProductId()));
            baseTotal = baseTotal.add(product.getBasePrice()
                    .multiply(BigDecimal.valueOf(item.getQuentity())));
        }
        log.info("🛒 Base Amount Calculated: {}", baseTotal);
        return baseTotal;
    }

    private Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private Map<String, Object> parseJson(String jsonString) {
        if (jsonString == null || jsonString.isBlank()) return new HashMap<>();
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("Failed to parse JSON: {}", jsonString, e);
            return new HashMap<>();
        }
    }





    private boolean areConditionsSatisfied(Map<String, Object> conditions,
                                           PricingRequestDto requestDto,
                                           Customer customer,
                                           Rules rule,
                                           List<ItemDTO> items) {

        if (conditions == null || conditions.isEmpty()) return true;

        LocalDateTime requestTime = Optional.ofNullable(requestDto.getRequestTime())
                .orElse(LocalDateTime.now());

        int totalQty = items.stream()
                .mapToInt(ItemDTO::getQuentity)
                .sum();

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            String key = entry.getKey().toLowerCase();
            Object conditionValue = entry.getValue();

            switch (key) {


                case "category":
                    try {

                        List<String> requiredCategories = new ArrayList<>();

                        if (conditionValue instanceof List<?> list) {

                            requiredCategories = list.stream()
                                    .map(Object::toString)
                                    .map(String::toLowerCase)
                                    .toList();
                        } else if (conditionValue != null) {
                            requiredCategories = List.of(conditionValue.toString().toLowerCase());
                        }


                        Set<String> itemCategories = items.stream()
                                .map(ItemDTO::getCategory)
                                .filter(Objects::nonNull)
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());

                        // ✅ Check if all required categories are present in items
                        boolean allPresent = requiredCategories.stream()
                                .allMatch(itemCategories::contains);

                        if (!allPresent) return false;

                        log.debug("✅ Category condition passed: required={}, itemCategories={}", requiredCategories, itemCategories);
                    } catch (Exception e) {
                        log.error("❌ Error evaluating category condition for rule {}: {}", rule.getRuleName(), e.getMessage());
                        return false;
                    }
                    break;



                case "tier":
                    try {
                        String customerTier = customer.getTier();
                        String requiredTier = conditionValue.toString();

                        if (!customerTier.equalsIgnoreCase(requiredTier)) {
                            log.debug("❌ Skipping Loyalty Strategy — Customer tier {} does not match required tier {}.", customerTier, requiredTier);
                            return false;
                        }

                        log.debug("✅ Loyalty Strategy applicable — Customer tier {} matches required tier {}.", customerTier, requiredTier);

                    } catch (Exception e) {
                        log.error("❌ Error evaluating tier condition: {}", e.getMessage());
                        return false;
                    }
                    break;


                case "customertype":
                    if (!customer.getTier().equalsIgnoreCase(conditionValue.toString())) return false;
                    break;

                case "referred":
                    if (Boolean.parseBoolean(conditionValue.toString()) != requestDto.isReferred()) return false;
                    break;

                case "newlaunch":
                    if (Boolean.parseBoolean(conditionValue.toString()) != requestDto.isNewLaunchOffer()) return false;
                    break;

                case "paymentmode":
                    String ruleRegion = rule.getRegion();
                    String requestRegion = requestDto.getRegion();

                    if ("IN".equalsIgnoreCase(ruleRegion) && !"IN".equalsIgnoreCase(requestRegion)) return false;
                    if ("GLOBAL".equalsIgnoreCase(ruleRegion) && "IN".equalsIgnoreCase(requestRegion)) return false;

                    if (conditionValue instanceof String) {
                        if (!requestDto.getPaymentMode().equalsIgnoreCase(conditionValue.toString())) return false;
                    } else if (conditionValue instanceof List<?>) {
                        List<String> modes = ((List<?>) conditionValue).stream()
                                .map(Object::toString)
                                .toList();
                        if (!modes.stream().anyMatch(m -> m.equalsIgnoreCase(requestDto.getPaymentMode()))) return false;
                    }
                    break;

                case "minquantity":
                    if (totalQty < Integer.parseInt(conditionValue.toString())) return false;
                    break;

                case "region":
                    if (!requestDto.getRegion().equalsIgnoreCase(conditionValue.toString())) return false;
                    break;

//                case "purchasedaysbefore":
//                    if (!conditions.containsKey("eventTime")) break;
//                    LocalDateTime eventTime = LocalDateTime.parse(conditions.get("eventTime").toString());
//                    long daysBefore = java.time.Duration.between(requestTime, eventTime).toDays();
//                    if (daysBefore < Integer.parseInt(conditionValue.toString())) return false;
//                    break;

                case "timeslot":
                    String slot = (requestTime.getHour() >= 9 && requestTime.getHour() <= 21) ? "peak" : "offpeak";
                    if (!slot.equalsIgnoreCase(conditionValue.toString())) return false;
                    break;
                case "purchasedaysbefore":
                    try {
                        LocalDateTime expiryOrEventDate = rule.getActiveUntil();
                        if (expiryOrEventDate == null) {
                            log.warn("⚠ Rule {} has no active_until date, cannot evaluate purchasedaysbefore.", rule.getRuleName());
                            return false;
                        }

                        long daysBefore = Duration.between(requestTime, expiryOrEventDate).toDays();
                        int requiredDays = Integer.parseInt(conditionValue.toString());

                        String strategyKey = rule.getStrategyKey().toUpperCase();

                        if (strategyKey.contains("EARLY") || strategyKey.contains("TIME_BASED")) {
                            if (daysBefore < requiredDays) {
                                log.debug("❌ [{}] - Purchase made {} days before, required minimum {} days.", strategyKey, daysBefore, requiredDays);
                                return false;
                            }
                        }
                        else if (strategyKey.contains("LAST_MINUTE")) {
                            if (daysBefore > requiredDays || daysBefore < 0) {
                                log.debug("❌ [{}] - Purchase made {} days before, exceeds {}-day window.", strategyKey, daysBefore, requiredDays);
                                return false;
                            }
                        }

                        else {
                            log.debug("ℹ [{}] - purchaseDaysBefore condition evaluated as {} days before event.", strategyKey, daysBefore);
                        }

                    } catch (Exception e) {
                        log.error("❌ Error evaluating purchasedaysbefore for rule {}: {}", rule.getRuleName(), e.getMessage());
                        return false;
                    }
                    break;
                case "productagedays":
                    try {
                        // 🕒 Get product created time
                        LocalDateTime createdTime = LocalDateTime.parse(
                                conditions.getOrDefault("createdTime", LocalDateTime.now().toString()).toString()
                        );

                        // 📅 Get expiry date (active until)
                        LocalDateTime expiryDate = rule.getActiveUntil();
                        if (expiryDate == null) {
                            log.warn("⚠ Rule {} has no active_until date, cannot evaluate productagedays.", rule.getRuleName());
                            return false;
                        }

                        // 🧮 Calculate product age and days before expiry
                        long daysElapsed = Duration.between(createdTime, requestTime).toDays();   // Product age
                        long daysBeforeExpiry = Duration.between(requestTime, expiryDate).toDays(); // Days left before expiry

                        int threshold = Integer.parseInt(conditionValue.toString());

                        // 🚫 If product is not old enough → skip
                        if (daysElapsed < threshold) {
                            log.debug("❌ [{}] - Product age {} days is less than threshold {}.",
                                    rule.getStrategyKey(), daysElapsed, threshold);
                            return false;
                        }

                        // 🚫 If not within last 10 days before expiry → skip
                        if (daysBeforeExpiry > 10 || daysBeforeExpiry < 0) {
                            log.debug("❌ [{}] - Not within 10-day window (daysBeforeExpiry={}).",
                                    rule.getStrategyKey(), daysBeforeExpiry);
                            return false;
                        }

                        // ✅ Passed both conditions
                        log.debug("✅ [{}] - ProductAgeDays condition passed (age={}, daysBeforeExpiry={}).",
                                rule.getStrategyKey(), daysElapsed, daysBeforeExpiry);

                    } catch (Exception e) {
                        log.error("❌ Error evaluating productAgeDays condition for rule {}: {}", rule.getRuleName(), e.getMessage());
                        return false;
                    }
                    break;




                case "dayofweek":
                    DayOfWeek currentDay = requestTime.getDayOfWeek();
                    if (!currentDay.name().equalsIgnoreCase(conditionValue.toString())) return false;
                    break;

                case "timerange":
                    try {
                        String[] parts = conditionValue.toString().split("-");
                        LocalTime start = LocalTime.parse(parts[0]);
                        LocalTime end = LocalTime.parse(parts[1]);
                        LocalTime now = requestTime.toLocalTime();

                        if (now.isBefore(start) || now.isAfter(end)) return false;
                    } catch (Exception e) {
                        log.warn("⚠ Invalid timeRange format: {}", conditionValue);
                        return false;
                    }
                    break;
                case "couponcodes":
                    String requestedCoupon = requestDto.getCoupon(); // assuming you have couponCode in request
                    if (conditionValue instanceof List<?>) {
                        List<String> validCoupons = ((List<?>) conditionValue)
                                .stream()
                                .map(Object::toString)
                                .map(String::toUpperCase)
                                .toList();
                        if (!validCoupons.contains(requestedCoupon.toUpperCase())) {
                            return false; // ❌ Not a valid coupon
                        }
                    }
                    break;

                default:
                    if (!Boolean.parseBoolean(conditionValue.toString())) return false;
                    break;
            }
        }

        return true;
    }


    private StrategyDiscountDetailDTO applyRegionBasedStrategy(
            PricingRequestDto requestDto,
            List<StrategyDiscountDetailDTO> strategyDetails,
            BigDecimal finalTotalRef) {

        PricingStrategy regionStrategy = strategyFactory.getStrategy("REGIONAL_PRICING");

        if (regionStrategy == null) {
            log.warn("⚠ REGIONAL_PRICING strategy not registered.");
            return null;
        }

        Rules regionRule = ruleRepository.findActiveRegionalRule(
                "REGIONAL_PRICING",
                requestDto.getRegion()
        ).stream().findFirst().orElse(null);

        if (regionRule == null) {
            log.info("🌍 No regional rule found for: {}", requestDto.getRegion());
            return null;
        }

        Map<String, Object> regionParams = parseJson(regionRule.getParameters());
        regionParams.put("baseTotal", finalTotalRef);

        BigDecimal updatedTotal = regionStrategy.applyStrategy(
                requestDto.getItems(),
                regionParams
        );

        if (updatedTotal == null) {
            log.warn("⚠ Regional pricing calculation failed!");
            return null;
        }


        BigDecimal adjustment = finalTotalRef.subtract(updatedTotal);

        if (adjustment.compareTo(BigDecimal.ZERO) == 0) {
            log.info("🌍 No region adjustment applied.");
            return null;
        }
        BigDecimal percent = new BigDecimal(regionParams.get("regionalAdjustment").toString());


        StrategyDiscountDetailDTO dto = new StrategyDiscountDetailDTO(
                "Region Based TAX",
               percent,
                adjustment,
                updatedTotal
        );

        if (adjustment.compareTo(BigDecimal.ZERO) > 0) {
            log.info("✅ Region Discount: {}", adjustment);
        } else {
            log.info("⚠ Region Surcharge: {}", adjustment);
        }

        return dto;
    }




}
