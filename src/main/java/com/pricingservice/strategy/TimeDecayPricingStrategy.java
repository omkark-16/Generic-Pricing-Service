package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class TimeDecayPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

         LocalDateTime createdTime;
        try {
            createdTime = LocalDateTime.parse(parameters.getOrDefault("createdTime", "2025-01-01T00:00:00").toString());
        } catch (Exception e) {
            createdTime = LocalDateTime.now();
        }

        long daysElapsed = Math.max(0, Duration.between(createdTime, LocalDateTime.now()).toDays());

        BigDecimal decayRate = new BigDecimal(parameters.getOrDefault("decayRate", "1.0").toString());
        BigDecimal totalDiscount = total.multiply(decayRate).multiply(BigDecimal.valueOf(daysElapsed))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);

         BigDecimal maxDiscount = total.multiply(BigDecimal.valueOf(0.7)); 
        if (totalDiscount.compareTo(maxDiscount) > 0) {
            totalDiscount = maxDiscount;
        }

        BigDecimal finalPrice = total.subtract(totalDiscount);

       

        return finalPrice;
    }

    @Override
    public String getStrategyKey() {
        return "TIME_DECAY";
    }
}
