package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import com.pricingservice.dto.StrategyDiscountDetailDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegionalPricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {

		BigDecimal baseTotal = (BigDecimal) parameters.get("baseTotal");
		BigDecimal adjustmentPercent = new BigDecimal(parameters.get("regionalAdjustment").toString());

		// ✅ If negative → discount, If positive → surcharge
		BigDecimal adjustmentAmount = baseTotal
				.multiply(adjustmentPercent)
				.divide(BigDecimal.valueOf(100));

		BigDecimal updatedTotal = baseTotal.add(adjustmentAmount);

		return updatedTotal;
	}

	@Override
	public String getStrategyKey() {
		return "REGIONAL_PRICING";
	}
}
