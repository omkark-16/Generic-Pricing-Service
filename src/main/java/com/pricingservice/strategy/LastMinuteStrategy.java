package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class LastMinuteStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        LocalDateTime purchaseTime = (LocalDateTime) parameters.getOrDefault("requestTime", LocalDateTime.now());
        LocalDateTime eventTime = LocalDateTime.parse(parameters.getOrDefault("eventTime", "2025-12-31T00:00:00").toString());

        long hoursBefore = Duration.between(purchaseTime, eventTime).toHours();

        BigDecimal thresholdHours = new BigDecimal(parameters.getOrDefault("thresholdHours", "24").toString());
        BigDecimal surchargePercent = new BigDecimal(parameters.getOrDefault("surchargePercent", "20").toString());

        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

        if (BigDecimal.valueOf(hoursBefore).compareTo(thresholdHours) < 0) {
            
            return total.add(total.multiply(surchargePercent).divide(BigDecimal.valueOf(100)));
        }

        return total;
    }

    @Override
    public String getStrategyKey() {
        return "LAST_MINUTE";
    }
}
