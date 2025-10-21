package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class SeasonalStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal discountPercent = new BigDecimal(parameters.getOrDefault("seasonalDiscount", 5).toString());
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String getStrategyKey() {
        return "SEASONAL_DISCOUNT";
    }
}
