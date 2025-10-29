package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class BehavioralPricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		boolean isReturningCustomer = Boolean
				.parseBoolean(parameters.getOrDefault("returningCustomer", "false").toString());
		int loyaltyPoints = Integer.parseInt(parameters.getOrDefault("loyaltyPoints", "0").toString());
		boolean abandonedCartPreviously = Boolean
				.parseBoolean(parameters.getOrDefault("abandonedCart", "false").toString());

		BigDecimal discountPercent = BigDecimal.ZERO;

		if (isReturningCustomer) {
			discountPercent = discountPercent.add(BigDecimal.valueOf(5));
		}

		if (loyaltyPoints > 1000) {
			discountPercent = discountPercent.add(BigDecimal.valueOf(5));
		}

		if (abandonedCartPreviously) {
			discountPercent = discountPercent.add(BigDecimal.valueOf(3));
		}

		return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
	}

//	@Override
//	public String getStrategyKey() {
//		return "BEHAVIORAL_PRICING";
//	}
		@Override
	public String getStrategyKey() {
//		return "BEHAVIORAL_PRICING";
			return "1";
	}
}
