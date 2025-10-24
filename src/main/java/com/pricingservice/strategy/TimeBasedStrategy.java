package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Component
public class TimeBasedStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

        int startHour = Integer.parseInt(parameters.getOrDefault("startHour", "22").toString());
        int endHour = Integer.parseInt(parameters.getOrDefault("endHour", "6").toString());
        BigDecimal discountPercent = new BigDecimal(parameters.getOrDefault("discountPercent", "5").toString());

        LocalTime now = LocalTime.now();
        boolean isDiscountTime = now.isAfter(LocalTime.of(startHour, 0)) || now.isBefore(LocalTime.of(endHour, 0));

        BigDecimal finalTotal = total;

        if (isDiscountTime) {
            BigDecimal discount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));
            finalTotal = total.subtract(discount);
 
        }

        return finalTotal;
    }

    @Override
    public String getStrategyKey() {
        return "TIME_BASED";
    }
}
