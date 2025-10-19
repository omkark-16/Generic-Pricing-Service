package com.pricingservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricingservice.dto.PricingRequestDto;

@RestController
@RequestMapping("/api/v1/calculate")
public class HelloController {

	@PostMapping
	public PricingRequestDto calculate(@Valid @RequestBody PricingRequestDto requestDTO){

		return requestDTO;
	}

}
