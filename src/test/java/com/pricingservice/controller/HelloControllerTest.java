package com.pricingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pricingservice.dto.*;
import com.pricingservice.exception.GlobalExceptionHandler;
import com.pricingservice.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HelloControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private HelloController helloController;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {

        // Proper LocalDateTime handling
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(helloController)
                .setControllerAdvice(new GlobalExceptionHandler()) // optional
                .build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(helloController)
                .setValidator(validator)                     // enable @Valid
                .setControllerAdvice(new GlobalExceptionHandler()) // handle validation errors
                .build();
    }


    @Test
    void calculate_success_returns200() throws Exception {
        PricingRequestDto req = new PricingRequestDto(
                1L,
                "IN",

                "CARD",
                "DIWALI2026",
                List.of(new ItemDTO("P001","electronics", 2)),
                LocalDateTime.now(),
                true,
                false
        );

        PricingResponseDto resp = new PricingResponseDto(
                1L,
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(180),
                List.of(),                     // strategy details
                BigDecimal.valueOf(20),        // total discount
                true,                           // referred
                false,                          // newLaunchOffer
                LocalDateTime.now()             // calculatedAt
        );

        Mockito.when(pricingService.calculateTotal(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.baseTotal").value(200))
                .andExpect(jsonPath("$.finalTotal").value(180))
                .andExpect(jsonPath("$.totalDiscountGiven").value(20))
                .andExpect(jsonPath("$.referred").value(true))
                .andExpect(jsonPath("$.newLaunchOffer").value(false))
                .andExpect(jsonPath("$.strategyDetails").isArray())
                .andExpect(jsonPath("$.calculatedAt").exists());
    }

    @Test
    void calculate_serviceThrows_returns500() throws Exception {
        PricingRequestDto req = new PricingRequestDto(
                99L,
                "IN",

                "CARD",
                "WELCOME",
                List.of(new ItemDTO("P009","electronics", 1)),
                LocalDateTime.now(),
                false,
                false
        );

        Mockito.when(pricingService.calculateTotal(any()))
                .thenThrow(new RuntimeException("Boom!"));

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void calculate_missingRequiredFields_returns400() throws Exception {
        PricingRequestDto req = new PricingRequestDto(); // all fields null/empty

        mockMvc.perform(post("/api/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.customerId").value("Customer ID cannot be blank"))
                .andExpect(jsonPath("$.details.region").value("Region cannot be blank"))
                .andExpect(jsonPath("$.details.paymentMode").value("Payment mode cannot be blank"))
                .andExpect(jsonPath("$.details.items").value("Items list cannot be empty"))
                .andExpect(jsonPath("$.details.requestTime").value("Request time cannot be null"));
    }

}
