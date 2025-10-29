package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class WholesaleStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal totalQuantity = BigDecimal.ZERO;
		for (ItemDTO item : items) {
			totalQuantity = totalQuantity.add(BigDecimal.valueOf(item.getQuentity()));
		}

		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		int minQuantity = (int) parameters.getOrDefault("minQuantity", 50);
		double discountPercent = ((Number) parameters.getOrDefault("wholesaleDiscount", 20)).doubleValue();

		if (totalQuantity.compareTo(BigDecimal.valueOf(minQuantity)) >= 0) {
			BigDecimal discount = total.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100));
			return total.subtract(discount);
		}
		return total;
	}

	@Override
	public String getStrategyKey() {
		return "WHOLESALE_DISCOUNT";
	}
}
