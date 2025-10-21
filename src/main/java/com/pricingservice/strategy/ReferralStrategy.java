package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ReferralStrategy implements PricingStrategy {
    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        boolean referred = Boolean.parseBoolean(parameters.getOrDefault("isReferred", "false").toString());
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        if (referred) {
            return total.subtract(total.multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(100)));
        }
        return total;
    }

    @Override
    public String getStrategyKey() {
        return "REFERRAL_DISCOUNT";
    }
}
