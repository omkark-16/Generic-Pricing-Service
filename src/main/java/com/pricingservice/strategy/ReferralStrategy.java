package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ReferralStrategy implements PricingStrategy {
//    @Override
//    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
//        boolean referred = Boolean.parseBoolean(parameters.getOrDefault("isReferred", "false").toString());
//        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
//        if (referred) {
//            return total.subtract(total.multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(100)));
//        }
//        return total;
//    }

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        // Safely extract the referred flag
        boolean referred = false;
        if (parameters.containsKey("isReferred")) {
            referred = Boolean.parseBoolean(parameters.get("isReferred").toString());
        }

        // Safely extract total
        Object totalObj = parameters.get("baseTotal");
        if (totalObj == null) {
            throw new IllegalArgumentException("Base total is missing in parameters");
        }

        BigDecimal total = new BigDecimal(totalObj.toString());

        // Apply 5% referral discount if referred = true
        if (referred) {
            BigDecimal discountPercent = BigDecimal.valueOf(5);
            BigDecimal discountAmount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));
            return total.subtract(discountAmount);
        }

        // Return unchanged total if not referred
        return total;
    }


    @Override
    public String getStrategyKey() {
        return "REFERRAL_DISCOUNT";
    }
}
