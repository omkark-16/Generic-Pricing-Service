package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class RegionalPricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		BigDecimal multiplier = new BigDecimal(parameters.getOrDefault("regionMultiplier", "1.05").toString());
		return total.multiply(multiplier);
	}

	@Override
	public String getStrategyKey() {
		return "REGIONAL_PRICING";
	}
}
