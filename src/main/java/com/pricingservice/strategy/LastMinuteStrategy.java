package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class LastMinuteStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		LocalDateTime purchaseTime = (LocalDateTime) parameters.get("requestTime");
		LocalDateTime eventTime = LocalDateTime
				.parse(parameters.getOrDefault("eventTime", "2025-12-31T00:00:00").toString());

		long hoursBefore = java.time.Duration.between(purchaseTime, eventTime).toHours();
		BigDecimal surchargePercent = hoursBefore < 24 ? BigDecimal.valueOf(20) : BigDecimal.ZERO;

		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		return total.add(total.multiply(surchargePercent).divide(BigDecimal.valueOf(100)));
	}

	@Override
	public String getStrategyKey() {
		return "LAST_MINUTE";
	}
}
