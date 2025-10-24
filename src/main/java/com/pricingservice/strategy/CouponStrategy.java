package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class CouponStrategy implements PricingStrategy {

    @Override
    public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
        String couponCode = parameters.getOrDefault("couponCode", "").toString();
        BigDecimal total = (BigDecimal) parameters.get("baseTotal");

        BigDecimal discountPercent = BigDecimal.ZERO;

         if (parameters.containsKey("discountPercent")) {
            discountPercent = new BigDecimal(parameters.get("discountPercent").toString());
        }
         else if (couponCode.equalsIgnoreCase("WELCOME10")) {
            discountPercent = BigDecimal.valueOf(10);
        } else if (couponCode.equalsIgnoreCase("SAVE20")) {
            discountPercent = BigDecimal.valueOf(20);
        }

        System.out.println("=== Coupon Strategy Invoked ===");
        System.out.println("Coupon Code: " + couponCode);
        System.out.println("Parameters from DB: " + parameters);
        System.out.println("Discount percent used: " + discountPercent);

        return total.subtract(total.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String getStrategyKey() {
        return "COUPON";
    }
}
