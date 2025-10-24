package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class CompetitorPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        BigDecimal competitorPrice = total;
        BigDecimal finalPrice;

        try {
            competitorPrice = new BigDecimal(parameters.getOrDefault("competitorPrice", total).toString());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid competitorPrice format: " + parameters.get("competitorPrice"));
        }

         BigDecimal adjustment = new BigDecimal(parameters.getOrDefault("competitorAdjustment", "10").toString());
        String adjustmentType = parameters.getOrDefault("adjustmentType", "FLAT").toString().toUpperCase();

        if (adjustmentType.equals("PERCENT")) {
            BigDecimal discount = competitorPrice.multiply(adjustment).divide(BigDecimal.valueOf(100));
            finalPrice = competitorPrice.subtract(discount);
        } else {  
            finalPrice = competitorPrice.subtract(adjustment);
        }

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO; 
        }

         
        return finalPrice.min(total);  
    }

    @Override
    public String getStrategyKey() {
        return "COMPETITOR_PRICING";
    }
}
