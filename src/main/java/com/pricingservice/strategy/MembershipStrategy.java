package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MembershipStrategy implements PricingStrategy {
    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        String tier = parameters.getOrDefault("membershipDiscount", "REGULAR").toString();
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        BigDecimal discountPercent = BigDecimal.ZERO;

        switch (tier.toUpperCase()) {
            case "GOLD": discountPercent = BigDecimal.valueOf(10); break;
            case "SILVER": discountPercent = BigDecimal.valueOf(5); break;
            case "PLATINUM": discountPercent = BigDecimal.valueOf(15); break;
        }
        return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String getStrategyKey() {
        return "MEMBERSHIP_DISCOUNT";
    }
}

