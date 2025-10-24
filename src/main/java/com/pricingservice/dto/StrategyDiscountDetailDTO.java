package com.pricingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrategyDiscountDetailDTO {

	private String strategyName; // e.g., "FestivalDiscount"
	private BigDecimal discountPercent; // e.g., 10%
	private BigDecimal discountAmount; // e.g., ₹500
	private BigDecimal totalAfterDiscount; // total after this strategy applied

	public StrategyDiscountDetailDTO(String strategyName, BigDecimal discountPercent, BigDecimal discountAmount,
			BigDecimal totalAfterDiscount) {
		super();
		this.strategyName = strategyName;
		this.discountPercent = discountPercent;
		this.discountAmount = discountAmount;
		this.totalAfterDiscount = totalAfterDiscount;
	}

	public StrategyDiscountDetailDTO() {
	};

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public BigDecimal getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(BigDecimal discountPercent) {
		this.discountPercent = discountPercent;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getTotalAfterDiscount() {
		return totalAfterDiscount;
	}

	public void setTotalAfterDiscount(BigDecimal totalAfterDiscount) {
		this.totalAfterDiscount = totalAfterDiscount;
	}

}
