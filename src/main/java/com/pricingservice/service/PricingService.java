package com.pricingservice.service;


import com.pricingservice.dto.ItemDTO;
import com.pricingservice.dto.PricingRequestDto;
import com.pricingservice.dto.PricingResponseDto;
import com.pricingservice.dto.StrategyDiscountDetailDTO;
import com.pricingservice.entities.Product;
import com.pricingservice.entities.Rules;
import com.pricingservice.repository.ProductRepository;
import com.pricingservice.repository.RuleRepository;
import com.pricingservice.strategy.PricingStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PricingService {

    private final ProductRepository productRepository;
    private final RuleRepository ruleRepository;
    private final PricingStrategyFactory strategyFactory;

    public PricingService(ProductRepository productRepository,
                          RuleRepository ruleRepository,
                          PricingStrategyFactory strategyFactory) {
        this.productRepository = productRepository;
        this.ruleRepository = ruleRepository;
        this.strategyFactory = strategyFactory;
    }



    public PricingResponseDto calculateTotal(PricingRequestDto requestDto) {
        BigDecimal baseTotal = BigDecimal.ZERO;

        for (ItemDTO item : requestDto.getItems()) {
            Product product = productRepository.findByProductCode(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            baseTotal = baseTotal.add(product.getBasePrice()
                    .multiply(BigDecimal.valueOf(item.getQuentity())));
        }

        List<Rules> rules = ruleRepository.findActiveRulesByRegion(requestDto.getRegion(), LocalDateTime.now());
        if (rules.isEmpty()) {
            rules = ruleRepository.findGlobalActiveRules(LocalDateTime.now());
        }

        rules.sort(Comparator.comparing(Rules::getPriority));

        BigDecimal finalTotal = baseTotal;
        List<StrategyDiscountDetailDTO> strategyDetails = new ArrayList<>();


        for (Rules rule : rules) {
            PricingStrategy strategy = strategyFactory.getStrategy(rule.getStrategyKey());
            if (strategy != null) {
                Map<String, Object> parameters = parseParameters(rule.getParameters());
                parameters.put("baseTotal", finalTotal);
                parameters.put("isReferred", requestDto.isReferred());
                parameters.put("newProductDiscount", requestDto.isNewLaunchOffer());

                BigDecimal discountedTotal = strategy.applyStrategy(requestDto.getItems(), parameters);

                if (discountedTotal.compareTo(finalTotal) < 0) {
                    BigDecimal discountAmount = finalTotal.subtract(discountedTotal);
                    BigDecimal discountPercent = discountAmount
                            .divide(finalTotal, 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

                    strategyDetails.add(new StrategyDiscountDetailDTO(
                            strategy.getStrategyKey(),
                            discountPercent,
                            discountAmount,
                            discountedTotal
                    ));

                    finalTotal = discountedTotal;
                }
            }
        }

        BigDecimal totalDiscountGiven = strategyDetails.stream()
                .map(StrategyDiscountDetailDTO::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        PricingResponseDto response = new PricingResponseDto();
        response.setCustomerId(requestDto.getCustomerId());
        response.setBaseTotal(baseTotal);
        response.setFinalTotal(finalTotal);
        response.setStrategyDetails(strategyDetails);
        response.setTotalDiscountGiven(totalDiscountGiven);
        response.setReferred(requestDto.isReferred());
        response.setNewLaunchOffer(requestDto.isNewLaunchOffer());
        response.setCalculatedAt(LocalDateTime.now());

        return response;
    }



//    public PricingResponseDto calculateTotal(PricingRequestDto requestDto) {
//        double baseTotal = 0.0;
//
//        for (ItemDTO item : requestDto.getItems()) {
//            Product product = productRepository.findByProductCode(item.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
//
//            baseTotal += product.getBasePrice().doubleValue() * item.getQuentity();
//        }
//
//        List<Rules> rules = ruleRepository.findActiveRulesByRegion(requestDto.getRegion(), LocalDateTime.now());
//        if (rules.isEmpty()) {
//            rules = ruleRepository.findGlobalActiveRules(LocalDateTime.now());
//        }
//
//        rules.sort(Comparator.comparing(Rules::getPriority));
//
//        double finalTotal = baseTotal;
//        List<StrategyDiscountDetailDTO> strategyDetails = new ArrayList<>();
//
//        for (Rules rule : rules) {
//            PricingStrategy strategy = strategyFactory.getStrategy(rule.getStrategyKey());
//            if (strategy != null) {
//                Map<String, Object> parameters = parseParameters(rule.getParameters());
//                parameters.put("baseTotal", finalTotal);
//
//                BigDecimal discountedTotalBD = strategy.applyStrategy(requestDto.getItems(), parameters);
//                double discountedTotal = discountedTotalBD.doubleValue();
//
//                if (discountedTotal < finalTotal) {
//                    double discountAmount = finalTotal - discountedTotal;
//                    double discountPercent = (discountAmount / finalTotal) * 100.0;
//
//                    strategyDetails.add(new StrategyDiscountDetailDTO(
//                            strategy.getStrategyKey(),
//                            discountPercent,
//                            discountAmount,
//                            discountedTotal
//                    ));
//
//                    finalTotal = discountedTotal;
//                }
//            }
//        }
//
//        double totalDiscountGiven = strategyDetails.stream()
//                .mapToDouble(StrategyDiscountDetailDTO::getDiscountAmount)
//                .sum();
//
//        PricingResponseDto response = new PricingResponseDto();
//        response.setCustomerId(requestDto.getCustomerId());
//        response.setBaseTotal(baseTotal);
//        response.setFinalTotal(finalTotal);
//        response.setStrategyDetails(strategyDetails);
//        response.setTotalDiscountGiven(totalDiscountGiven);
//        response.setCalculatedAt(LocalDateTime.now());
//
//        return response;
//    }


    /**
     * Simple key-value parser for parameters stored as strings.
     * Example: "discountPercent=10,season=WINTER"
     */
    private Map<String, Object> parseParameters(String paramString) {
        Map<String, Object> map = new HashMap<>();
        if (paramString == null || paramString.isBlank()) return map;

        String[] pairs = paramString.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("[:=]");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }

}