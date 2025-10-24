package com.pricingservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingRequestDto {
	@NotBlank(message = "Customer ID cannot be blank")
	private String customerId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}
	

	public PricingRequestDto(@NotBlank(message = "Customer ID cannot be blank") String customerId,
			@NotBlank(message = "Region cannot be blank") String region,
			@NotBlank(message = "Category cannot be blank") String category,
			@NotBlank(message = "Payment mode cannot be blank") String paymentMode,
			@NotEmpty(message = "Items list cannot be empty") @Valid List<ItemDTO> items,
			@NotNull(message = "Request time cannot be null") LocalDateTime requestTime) {
		super();
		this.customerId = customerId;
		this.region = region;
		this.category = category;
		this.paymentMode = paymentMode;
		this.items = items;
		this.requestTime = requestTime;
	}
	
	public PricingRequestDto()
	{};

	@NotBlank(message = "Region cannot be blank")
	private String region;

	@NotBlank(message = "Category cannot be blank")
	private String category;

	@NotBlank(message = "Payment mode cannot be blank")
	private String paymentMode;


	@NotEmpty(message = "Items list cannot be empty")
	@Valid
	private List<ItemDTO> items;

	@NotNull(message = "Request time cannot be null")
	private LocalDateTime requestTime;
	private boolean referred;
	private boolean newLaunchOffer;

	 
}
