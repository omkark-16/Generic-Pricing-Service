package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class BundlePricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, java.util.Map<String, Object> parameters) {

		// Base total passed from service
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		// Discount percentage configured in rule
		BigDecimal discountPercent = parameters.containsKey("bundleDiscount")
				? new BigDecimal(parameters.get("bundleDiscount").toString())
				: BigDecimal.ZERO;

		if (discountPercent.compareTo(BigDecimal.ZERO) > 0) {
			// Apply percentage discount on total
			BigDecimal discountAmount = total.multiply(discountPercent)
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			return total.subtract(discountAmount);
		}

		// No discount
		return total;
	}

	@Override
	public String getStrategyKey() {
		return "BUNDLE_PRICING";
	}
}
