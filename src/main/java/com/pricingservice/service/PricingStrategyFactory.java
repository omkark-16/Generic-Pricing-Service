package com.pricingservice.service;

import com.pricingservice.strategy.PricingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PricingStrategyFactory {

    private final Map<String, PricingStrategy> strategyMap;

    public PricingStrategyFactory(List<PricingStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(PricingStrategy::getStrategyKey, s -> s));
    }

    public PricingStrategy getStrategy(String key) {
        return strategyMap.get(key);
    }

    public List<PricingStrategy> getAllStrategies() {
        return List.copyOf(strategyMap.values());
    }
}