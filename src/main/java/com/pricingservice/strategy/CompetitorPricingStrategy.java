package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class CompetitorPricingStrategy implements PricingStrategy {
	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		BigDecimal competitorPrice = new BigDecimal(parameters.getOrDefault("competitorPrice", total).toString());
		return competitorPrice.subtract(BigDecimal.valueOf(10)); // Beat by ₹10
	}

	@Override
	public String getStrategyKey() {
		return "COMPETITOR_PRICING";
	}
}
