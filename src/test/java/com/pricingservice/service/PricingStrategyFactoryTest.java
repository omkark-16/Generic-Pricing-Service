package com.pricingservice.service;

import com.pricingservice.strategy.PricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PricingStrategyFactoryTest {

    private PricingStrategyFactory factory;

    private PricingStrategy strategyA;
    private PricingStrategy strategyB;

    @BeforeEach
    void setup() {
        // Create mock strategies
        strategyA = mock(PricingStrategy.class);
        strategyB = mock(PricingStrategy.class);

        when(strategyA.getStrategyKey()).thenReturn("STRATEGY_A");
        when(strategyB.getStrategyKey()).thenReturn("STRATEGY_B");

        // Initialize factory with the strategies
        factory = new PricingStrategyFactory(List.of(strategyA, strategyB));
    }

    @Test
    void getStrategy_returnsCorrectStrategy() {
        PricingStrategy resultA = factory.getStrategy("STRATEGY_A");
        PricingStrategy resultB = factory.getStrategy("STRATEGY_B");

        assertNotNull(resultA);
        assertNotNull(resultB);
        assertEquals(strategyA, resultA);
        assertEquals(strategyB, resultB);
    }

    @Test
    void getStrategy_returnsNullForUnknownKey() {
        PricingStrategy result = factory.getStrategy("UNKNOWN");
        assertNull(result);
    }

    @Test
    void getAllStrategies_returnsAll() {
        var allStrategies = factory.getAllStrategies();
        assertEquals(2, allStrategies.size());
        assertTrue(allStrategies.contains(strategyA));
        assertTrue(allStrategies.contains(strategyB));
    }

//    @Test
//    void strategies_applyDiscountMockedBehavior() {
//        when(strategyA.applyStrategy(BigDecimal.valueOf(100))).thenReturn(BigDecimal.valueOf(90));
//        when(strategyB.applyStrategy(BigDecimal.valueOf(100))).thenReturn(BigDecimal.valueOf(80));
//
//        assertEquals(BigDecimal.valueOf(90), strategyA.applyDiscount(BigDecimal.valueOf(100)));
//        assertEquals(BigDecimal.valueOf(80), strategyB.applyDiscount(BigDecimal.valueOf(100)));
//    }
}

