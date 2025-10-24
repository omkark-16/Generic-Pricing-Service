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

	    BigDecimal discountPercent = BigDecimal.ZERO;

	    if (paymentMode.equals("CREDIT_CARD")) {
	        discountPercent = new BigDecimal(parameters.getOrDefault("creditCardDiscount", "5").toString());
	    } else if (paymentMode.equals("WALLET")) {
	        discountPercent = new BigDecimal(parameters.getOrDefault("walletDiscount", "3").toString());
	    } else if (paymentMode.equals("CASH")) {
	        discountPercent = new BigDecimal(parameters.getOrDefault("cashDiscount", "1").toString());
	    }

	    return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
	}


	@Override
	public String getStrategyKey() {
		return "PAYMENT_MODE";
	}
}
