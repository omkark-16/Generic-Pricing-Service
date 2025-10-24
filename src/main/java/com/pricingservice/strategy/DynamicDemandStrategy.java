package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class DynamicDemandStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        String demandLevel = parameters.getOrDefault("demandLevel", "MEDIUM").toString().toUpperCase();

      
        BigDecimal highMultiplier = new BigDecimal(parameters.getOrDefault("highMultiplier", "1.25").toString());
        BigDecimal mediumMultiplier = new BigDecimal(parameters.getOrDefault("mediumMultiplier", "1.10").toString());
        BigDecimal lowMultiplier = new BigDecimal(parameters.getOrDefault("lowMultiplier", "0.90").toString());

        BigDecimal demandMultiplier;

        switch (demandLevel) {
            case "HIGH":
                demandMultiplier = highMultiplier;
                break;
            case "MEDIUM":
                demandMultiplier = mediumMultiplier;
                break;
            case "LOW":
                demandMultiplier = lowMultiplier;
                break;
            default:
                demandMultiplier = BigDecimal.ONE;
        }

        BigDecimal finalPrice = total.multiply(demandMultiplier);

        

        return finalPrice;
    }

    @Override
    public String getStrategyKey() {
        return "DYNAMIC_DEMAND";
    }
}
