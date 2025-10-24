package com.pricingservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponseDto {

	private String customerId;
	private BigDecimal baseTotal;
	private BigDecimal finalTotal;
	private List<StrategyDiscountDetailDTO> strategyDetails;
	private BigDecimal totalDiscountGiven;
	private boolean referred;
	private boolean newLaunchOffer;
	private LocalDateTime calculatedAt;

	public PricingResponseDto() {
	};

	public PricingResponseDto(String customerId, BigDecimal baseTotal, BigDecimal finalTotal,
			List<StrategyDiscountDetailDTO> strategyDetails, BigDecimal totalDiscountGiven, boolean referred,
			boolean newLaunchOffer, LocalDateTime calculatedAt) {
		super();
		this.customerId = customerId;
		this.baseTotal = baseTotal;
		this.finalTotal = finalTotal;
		this.strategyDetails = strategyDetails;
		this.totalDiscountGiven = totalDiscountGiven;
		this.referred = referred;
		this.newLaunchOffer = newLaunchOffer;
		this.calculatedAt = calculatedAt;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(BigDecimal baseTotal) {
		this.baseTotal = baseTotal;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public List<StrategyDiscountDetailDTO> getStrategyDetails() {
		return strategyDetails;
	}

	public void setStrategyDetails(List<StrategyDiscountDetailDTO> strategyDetails) {
		this.strategyDetails = strategyDetails;
	}

	public BigDecimal getTotalDiscountGiven() {
		return totalDiscountGiven;
	}

	public void setTotalDiscountGiven(BigDecimal totalDiscountGiven) {
		this.totalDiscountGiven = totalDiscountGiven;
	}

	public boolean isReferred() {
		return referred;
	}

	public void setReferred(boolean referred) {
		this.referred = referred;
	}

	public boolean isNewLaunchOffer() {
		return newLaunchOffer;
	}

	public void setNewLaunchOffer(boolean newLaunchOffer) {
		this.newLaunchOffer = newLaunchOffer;
	}

	public LocalDateTime getCalculatedAt() {
		return calculatedAt;
	}

	public void setCalculatedAt(LocalDateTime calculatedAt) {
		this.calculatedAt = calculatedAt;
	}

}
