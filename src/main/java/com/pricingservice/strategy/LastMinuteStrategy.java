package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class LastMinuteStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {

		LocalDateTime purchaseTime = (LocalDateTime) parameters.get("requestTime");
		LocalDateTime strategyExpiry = (LocalDateTime) parameters.get("strategyDateFromDB");

		if (strategyExpiry == null || purchaseTime == null) {
			return (BigDecimal) parameters.get("baseTotal");
		}

		long hoursBeforeExpiry = java.time.Duration.between(purchaseTime, strategyExpiry).toHours();



		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		if (hoursBeforeExpiry <= 24 && hoursBeforeExpiry >= 0) {
			BigDecimal lastMinuteDiscountPercent = parameters.containsKey("lastMinuteDiscount")
					? new BigDecimal(parameters.get("lastMinuteDiscount").toString())
					: BigDecimal.ZERO;
			BigDecimal discountAmount = total.multiply(lastMinuteDiscountPercent)
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			return total.subtract(discountAmount);
		}


		return total;
	}

	@Override
	public String getStrategyKey() {
		return "LAST_MINUTE";
	}
}
