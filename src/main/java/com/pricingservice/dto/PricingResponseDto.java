package com.pricingservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
 
 

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponseDto {
	
	public PricingResponseDto() {};
    public PricingResponseDto(String customerId, BigDecimal baseTotal, BigDecimal finalTotal,
			List<StrategyDiscountDetailDTO> strategyDetails, BigDecimal totalDiscountGiven,
			LocalDateTime calculatedAt) {
 		this.customerId = customerId;
		this.baseTotal = baseTotal;
		this.finalTotal = finalTotal;
		this.strategyDetails = strategyDetails;
		this.totalDiscountGiven = totalDiscountGiven;
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
	public LocalDateTime getCalculatedAt() {
		return calculatedAt;
	}
	public void setCalculatedAt(LocalDateTime calculatedAt) {
		this.calculatedAt = calculatedAt;
	}
	private String customerId;
    private BigDecimal baseTotal;
    private BigDecimal finalTotal;
    private List<StrategyDiscountDetailDTO> strategyDetails; 
    private BigDecimal totalDiscountGiven; // Sum of all discount amounts
    private LocalDateTime calculatedAt;
}
