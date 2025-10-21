package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class VolumeBasedStrategy implements PricingStrategy {
    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");
        int totalQuantity = items.stream().mapToInt(ItemDTO::getQuentity).sum();

        if (totalQuantity > 5) {
            BigDecimal discountPercent = new BigDecimal("7");
            return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
        }
        return total;
    }

    @Override
    public String getStrategyKey() {
        return "VOLUME_BASED_DISCOUNT";
    }
}

