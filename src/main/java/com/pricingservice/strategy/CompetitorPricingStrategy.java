//package com.pricingservice.strategy;
//
//import com.pricingservice.dto.ItemDTO;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class CompetitorPricingStrategy implements PricingStrategy {
//
//	@Override
//	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
//		// Safely get base total
//		BigDecimal baseTotal = parameters.containsKey("baseTotal")
//				? new BigDecimal(parameters.get("baseTotal").toString())
//				: BigDecimal.ZERO;
//
//		// Get competitor price adjustment from parameters; default = baseTotal
//		BigDecimal competitorPrice = new BigDecimal(
//				parameters.getOrDefault("priceAdjustment", baseTotal).toString()
//		);
//
//		// Beat the competitor by ₹10 (can also be parameterized)
//		BigDecimal beatAmount = BigDecimal.valueOf(10);
//
//		return competitorPrice.subtract(beatAmount);
//	}
//
//	@Override
//	public String getStrategyKey() {
//		return "COMPETITOR_PRICING";
//	}
//}
