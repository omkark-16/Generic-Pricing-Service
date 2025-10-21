package com.pricingservice.strategy;


import com.pricingservice.dto.ItemDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PricingStrategy {

    BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters);

    String getStrategyKey(); // Each strategy defines its key
}