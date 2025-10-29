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
        BigDecimal demandIndex = new BigDecimal(parameters.getOrDefault("minPrice", "1.2").toString());
        return total.multiply(demandIndex);
    }

    @Override
//    public String getStrategyKey() {
//        return "DYNAMIC_DEMAND";
//    }
    public String getStrategyKey() {
        return "3";
    }
}
