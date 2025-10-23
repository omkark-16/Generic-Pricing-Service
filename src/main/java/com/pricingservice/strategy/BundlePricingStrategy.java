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
		if (items.size() >= 3) {
			return total.subtract(total.multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100)));
		}
		return total;
	}

	@Override
	public String getStrategyKey() {
		return "BUNDLE_PRICING";
	}
}
