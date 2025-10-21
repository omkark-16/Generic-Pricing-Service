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
    private List<StrategyDiscountDetailDTO> strategyDetails; // Detailed list
    private BigDecimal totalDiscountGiven; // Sum of all discount amounts
    private LocalDateTime calculatedAt;
}
