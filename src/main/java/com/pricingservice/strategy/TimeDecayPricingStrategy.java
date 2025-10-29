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
		// Base total from parameters
		BigDecimal baseTotal = (BigDecimal) parameters.getOrDefault("baseTotal", BigDecimal.ZERO);

		// Base decay rate (e.g., 2%)
		BigDecimal baseDecayRate = new BigDecimal(parameters.getOrDefault("decayRate", "1.0").toString());

		// Strategy valid until date
		LocalDateTime strategyDate = (LocalDateTime) parameters.get("validUntil");

		// Current request time
		LocalDateTime requestTime = (LocalDateTime) parameters.getOrDefault("requestTime", LocalDateTime.now());

		// 🚫 If no expiry date → no strategy applies
		if (strategyDate == null) {
			return baseTotal;
		}

		// 📅 Calculate days before expiry
		long daysBeforeExpiry = Duration.between(requestTime, strategyDate).toDays();

		// ❌ Only apply strategy within last 10 days
		if (daysBeforeExpiry > 10 || daysBeforeExpiry < 0) {
			return baseTotal;
		}


		BigDecimal extraRate = BigDecimal.valueOf((10 - daysBeforeExpiry) * 1);
		BigDecimal effectiveDecayRate = baseDecayRate.add(extraRate);

		BigDecimal totalDiscount = baseTotal
				.multiply(effectiveDecayRate)
				.divide(BigDecimal.valueOf(100));

		BigDecimal finalTotal = baseTotal.subtract(totalDiscount);

		return finalTotal.max(BigDecimal.ZERO);
	}





	@Override
	public String getStrategyKey() {
		return "TIME_DECAY";
	}
}
