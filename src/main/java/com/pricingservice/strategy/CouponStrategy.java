package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class CouponStrategy implements PricingStrategy {

	private static final List<String> VALID_COUPONS = List.of("WELCOME10", "SAVE20");

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		String couponCode = parameters.getOrDefault("couponCode", "").toString();
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

//		if (!VALID_COUPONS.contains(couponCode.toUpperCase())) {
//			return total; // ❌ Invalid coupon → no discount
//		}

		BigDecimal discountPercent = parameters.containsKey("discountPercent")
				? new BigDecimal(parameters.get("discountPercent").toString())
				: BigDecimal.ZERO;

		BigDecimal discountAmount = total.multiply(discountPercent)
				.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

		return total.subtract(discountAmount);
	}

	@Override
	public String getStrategyKey() {
		return "COUPON";
	}
}
