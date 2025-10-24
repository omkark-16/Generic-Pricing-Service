package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class PaymentModeStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		String paymentMode = parameters.getOrDefault("paymentMode", "").toString().toUpperCase();
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		BigDecimal adjustmentPercent = BigDecimal.ZERO;

		switch (paymentMode) {
		case "CREDIT_CARD":
			adjustmentPercent = BigDecimal.valueOf(2);
			total = total.add(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100)));
			break;

		case "WALLET":
			adjustmentPercent = BigDecimal.valueOf(5);
			total = total.subtract(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100)));
			break;

		case "CASH":
			adjustmentPercent = BigDecimal.valueOf(1);
			total = total.subtract(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100)));
			break;

		default:
			break;
		}

		return total;
	}

	@Override
	public String getStrategyKey() {
		return "PAYMENT_MODE";
	}
}
