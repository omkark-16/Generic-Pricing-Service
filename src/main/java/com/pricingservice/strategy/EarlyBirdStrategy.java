package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class EarlyBirdStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        LocalDateTime purchaseTime = (LocalDateTime) parameters.getOrDefault("requestTime", LocalDateTime.now());
        LocalDateTime eventTime = LocalDateTime.parse(parameters.getOrDefault("eventTime", "2025-12-31T00:00:00").toString());

        long daysBefore = Duration.between(purchaseTime, eventTime).toDays();

        BigDecimal minDays = new BigDecimal(parameters.getOrDefault("minDays", "30").toString());
        BigDecimal discountPercent = new BigDecimal(parameters.getOrDefault("discountPercent", "10").toString());
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

        if (BigDecimal.valueOf(daysBefore).compareTo(minDays) >= 0) {
            BigDecimal discount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));
            BigDecimal finalPrice = total.subtract(discount);
 

            return finalPrice;
        }

        return total;
    }

    @Override
    public String getStrategyKey() {
        return "EARLY_BIRD";
    }
}
