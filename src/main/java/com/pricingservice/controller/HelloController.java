package com.pricingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricingservice.dto.PricingRequestDto;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
	
	 @PostMapping
	public String hello(@RequestBody PricingRequestDto request) {
		  return "Hello from Pricing API!"
          + ", Product ID: " + request.getProductId()
          + ", Payment Mode: " + request.getPaymentMode();
	}

}
