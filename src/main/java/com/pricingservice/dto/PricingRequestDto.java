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
	@NotNull(message = "Customer ID cannot be blank")
	private Long customerId;

	@NotBlank(message = "Region cannot be blank")
	private String region;


	@NotBlank(message = "Payment mode cannot be blank")
	private String paymentMode;
	@NotBlank(message = "Coupen mode cannot be blank")
	private String coupon;


	@NotEmpty(message = "Items list cannot be empty")
	@Valid
	private List<ItemDTO> items;

	@NotNull(message = "Request time cannot be null")
	private LocalDateTime requestTime;
	private boolean referred;
	private boolean newLaunchOffer;

	 
}
