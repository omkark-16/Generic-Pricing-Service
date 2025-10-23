package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Component
public class TimeBasedStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		LocalTime now = LocalTime.now();

		BigDecimal discount = (now.isAfter(LocalTime.of(22, 0)) || now.isBefore(LocalTime.of(6, 0)))
				? BigDecimal.valueOf(5)
				: BigDecimal.ZERO;

		return total.subtract(total.multiply(discount).divide(BigDecimal.valueOf(100)));
	}

	@Override
	public String getStrategyKey() {
		return "TIME_BASED";
	}
}
