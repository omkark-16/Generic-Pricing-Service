package com.pricingservice.controller;

import com.pricingservice.dto.PricingResponseDto;
import com.pricingservice.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricingservice.dto.PricingRequestDto;

@RestController
@RequestMapping("/api/v1/calculate")
public class HelloController {

	@Autowired
	private PricingService pricingService;

	public HelloController(PricingService pricingService){
		this.pricingService=pricingService;
	}

	@PostMapping
	public ResponseEntity<PricingResponseDto> calculate(@Valid @RequestBody PricingRequestDto requestDTO) {

		PricingResponseDto response = pricingService.calculateTotal(requestDTO);
		return ResponseEntity.ok(response);

	}

}
