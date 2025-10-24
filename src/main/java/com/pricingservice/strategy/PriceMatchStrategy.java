package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class PriceMatchStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

        BigDecimal competitorPrice = total;
        try {
            if (parameters.containsKey("competitorPrice")) {
                competitorPrice = new BigDecimal(parameters.get("competitorPrice").toString());
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid competitorPrice format: " + parameters.get("competitorPrice"));
        }

        if (competitorPrice.compareTo(BigDecimal.ZERO) <= 0) {
            competitorPrice = total;  
        }

        BigDecimal finalPrice = competitorPrice.min(total);

         

        return finalPrice;
    }

    @Override
    public String getStrategyKey() {
        return "PRICE_MATCH";
    }
}
