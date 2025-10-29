package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

//@Component
//public class PaymentModeStrategy implements PricingStrategy {
//
//	@Override
//	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
//		String paymentMode = parameters.getOrDefault("paymentModeDiscount", "").toString().toUpperCase();
//		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
//
//		BigDecimal adjustmentPercent = BigDecimal.ZERO;
//
//		switch (paymentMode) {
//		case "CREDIT_CARD":
//			adjustmentPercent = BigDecimal.valueOf(2);
//			total = total.add(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100)));
//			break;
//
//
//		default:
//			break;
//		}
//
//		return total;
//	}
//
//	@Override
//	public String getStrategyKey() {
//		return "PAYMENT_MODE";
//	}
//}






@Component
public class PaymentModeStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		String paymentMode = parameters.getOrDefault("paymentMode", "").toString().toUpperCase();
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		BigDecimal adjustmentPercent = BigDecimal.ZERO;

		switch (paymentMode) {
			case "CREDIT_CARD":
			case "DEBIT_CARD":
				adjustmentPercent = BigDecimal.valueOf(2);
				total = total.subtract(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
				break;

			case "UPI":
			case "CASH":
				adjustmentPercent = BigDecimal.valueOf(3);
				total = total.subtract(total.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
				break;

			default:
				adjustmentPercent = BigDecimal.ZERO; // No adjustment
				// Optionally, you can log or handle unknown payment modes here
				break;
		}

		return total;
	}

	@Override
	public String getStrategyKey() {
		return "PAYMENT_MODE";
	}
}
