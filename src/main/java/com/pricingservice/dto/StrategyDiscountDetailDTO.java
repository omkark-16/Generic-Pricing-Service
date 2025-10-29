package com.pricingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrategyDiscountDetailDTO {

	private String strategyName;         // e.g., "FestivalDiscount"
    private BigDecimal discountPercent;  // e.g., 10%
    private BigDecimal discountAmount;   // e.g., ₹500
    private BigDecimal totalAfterDiscount; // total after this strategy applied
}
