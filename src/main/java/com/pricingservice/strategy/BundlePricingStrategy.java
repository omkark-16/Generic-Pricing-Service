package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class BundlePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

         int minBundleSize = Integer.parseInt(parameters.getOrDefault("minBundleSize", "3").toString());
        BigDecimal discountPercent = new BigDecimal(parameters.getOrDefault("bundleDiscount", "10").toString());

        if (items.size() >= minBundleSize) {
            return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
        }

        return total;
    }

    @Override
    public String getStrategyKey() {
        return "BUNDLE_PRICING";
    }
}
