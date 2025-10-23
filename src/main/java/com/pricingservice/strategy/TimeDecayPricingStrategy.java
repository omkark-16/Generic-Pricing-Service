package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class TimeDecayPricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		LocalDateTime createdTime = LocalDateTime
				.parse(parameters.getOrDefault("createdTime", "2025-01-01T00:00:00").toString());
		long daysElapsed = Duration.between(createdTime, LocalDateTime.now()).toDays();

		BigDecimal decayRate = new BigDecimal(parameters.getOrDefault("decayRate", "1.0").toString());
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		BigDecimal totalDiscount = total.multiply(decayRate).multiply(BigDecimal.valueOf(daysElapsed))
				.divide(BigDecimal.valueOf(100));

		return total.subtract(totalDiscount);
	}

	@Override
	public String getStrategyKey() {
		return "TIME_DECAY";
	}
}
