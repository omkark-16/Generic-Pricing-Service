package com.pricingservice.strategy;

 
import com.pricingservice.dto.ItemDTO;
 import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class GeoDemandStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        BigDecimal demandFactor = new BigDecimal(parameters.getOrDefault("demandFactor", "1.10").toString());
        return total.multiply(demandFactor);
    }

    @Override
//    public String getStrategyKey() {
//        return "GEO_DEMAND";
//    }
    public String getStrategyKey() {
        return "";
    }

}
