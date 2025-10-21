package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class LoyaltyStrategy implements PricingStrategy {
    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        BigDecimal loyaltyDiscount = new BigDecimal(parameters.getOrDefault("loyaltyDiscount", 5).toString());
        return total.subtract(total.multiply(loyaltyDiscount).divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String getStrategyKey() {
        return "LOYALTY_DISCOUNT";
    }
}
