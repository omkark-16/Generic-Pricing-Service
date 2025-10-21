package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class LoyaltyPointsStrategy implements PricingStrategy {
    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        int points = Integer.parseInt(parameters.getOrDefault("loyaltyPoints", 100).toString());
        BigDecimal discount = BigDecimal.valueOf(points / 10.0); // ₹1 off per 10 points
        return total.subtract(discount);
    }

    @Override
    public String getStrategyKey() {
        return "LOYALTY_POINTS";
    }
}
