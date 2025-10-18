package com.pricingservice.dto;

public class PricingRequestDto {
	
	 private Long productId;
	 private String paymentMode;
	 
	 
	 
	
	public PricingRequestDto() {
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	 
		@Override
		public String toString() {
			return "PricingRequestDto [productId=" + productId + ", paymentMode=" + paymentMode + "]";
		}
	 

}
