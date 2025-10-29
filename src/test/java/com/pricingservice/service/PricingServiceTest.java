package com.pricingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricingservice.dto.ItemDTO;
import com.pricingservice.dto.PricingRequestDto;
import com.pricingservice.dto.PricingResponseDto;
import com.pricingservice.dto.StrategyDiscountDetailDTO;
import com.pricingservice.entities.Customer;
import com.pricingservice.entities.Product;
import com.pricingservice.entities.Rules;
import com.pricingservice.exception.ProductNotFoundException;
import com.pricingservice.repository.CustomerRepository;
import com.pricingservice.repository.ProductRepository;
import com.pricingservice.repository.RuleRepository;
import com.pricingservice.strategy.PricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PricingServiceTest {

    @InjectMocks
    private PricingService pricingService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private PricingStrategyFactory strategyFactory;

    // real ObjectMapper for parseJson()
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PricingRequestDto requestDto;
    private Rules ruleFestival;
    private Rules ruleMembership;
    private Rules rulePaymentModeNotMatched;
    private Rules regionalRuleIN;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject real ObjectMapper (PricingService constructor expects it)
        // Since we are using @InjectMocks, set field via reflection or re-create pricingService manually.
        // For simplicity, recreate pricingService with constructor injection here:
        pricingService = new PricingService(
                customerRepository,
                productRepository,
                ruleRepository,
                strategyFactory,
                objectMapper

        );

        // Request with one item
        requestDto = new PricingRequestDto();
        requestDto.setCustomerId(1L);
        requestDto.setRegion("IN");
//        requestDto.setCategory("electronics");
        requestDto.setPaymentMode("CARD");
        requestDto.setRequestTime(LocalDateTime.now());
        requestDto.setItems(List.of(new ItemDTO("P001","electronics" , 2)));
        requestDto.setReferred(true);
        requestDto.setNewLaunchOffer(false);

        // Customer mock (GOLD tier)
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(new Customer(1L, "John Doe", "GOLD", "john@example.com", LocalDateTime.now(), "{}")));

        // Product mock (base price 100)
        when(productRepository.findByProductCode("P001"))
                .thenReturn(Optional.of(new Product(1L, "P001", "Iphone 17" ,  BigDecimal.valueOf(100), "INR", "electronics", 100)));

        // Rule 1: Festival discount, applicable
        ruleFestival = new Rules();
        ruleFestival.setStrategyKey("FESTIVAL_DISCOUNT");
        ruleFestival.setPriority(1);
        ruleFestival.setRegion("IN");
        ruleFestival.setConditions("{\"category\":\"electronics\"}");
        ruleFestival.setParameters("{\"festivalDiscount\":10}");
        ruleFestival.setActiveFrom(LocalDateTime.now().minusDays(1));
        ruleFestival.setActiveUntil(LocalDateTime.now().plusDays(10));

        // Rule 2: Membership discount, applicable
        ruleMembership = new Rules();
        ruleMembership.setStrategyKey("MEMBERSHIP_DISCOUNT");
        ruleMembership.setPriority(2);
        ruleMembership.setRegion("IN");
        ruleMembership.setConditions("{\"tier\":\"GOLD\"}");
        ruleMembership.setParameters("{\"membershipDiscount\":15}");
        ruleMembership.setActiveFrom(LocalDateTime.now().minusDays(1));
        ruleMembership.setActiveUntil(LocalDateTime.now().plusDays(10));

        // Rule 3: Payment mode condition that does NOT match request -> should be skipped
        rulePaymentModeNotMatched = new Rules();
        rulePaymentModeNotMatched.setStrategyKey("PAYMENT_MODE");
        rulePaymentModeNotMatched.setPriority(3);
        rulePaymentModeNotMatched.setRegion("GLOBAL");
        rulePaymentModeNotMatched.setConditions("{\"paymentMode\":\"UPI\"}"); // request uses CARD
        rulePaymentModeNotMatched.setParameters("{\"paymentModeDiscount\":5}");
        rulePaymentModeNotMatched.setActiveFrom(LocalDateTime.now().minusDays(1));
        rulePaymentModeNotMatched.setActiveUntil(LocalDateTime.now().plusDays(10));

        // Regional rule for REGIONAL_PRICING for IN region (applies at the end)
        regionalRuleIN = new Rules();
        regionalRuleIN.setStrategyKey("REGIONAL_PRICING");
        regionalRuleIN.setPriority(11);
        regionalRuleIN.setRegion("IN");
        regionalRuleIN.setConditions("{ }");
        regionalRuleIN.setParameters("{\"regionalAdjustment\":5}");
    }

    @Test
    void calculateTotal_fullFlow_appliesStrategiesAndRegion() {
        // Arrange - Mock rules
        when(ruleRepository.findActiveRules(
                eq("IN"),
                any(LocalDateTime.class),
                any(String[].class),
                eq("GOLD"),
                eq(true)
        )).thenReturn(new ArrayList<>(Arrays.asList(ruleFestival, ruleMembership, rulePaymentModeNotMatched)));

        // Regional rule
        when(ruleRepository.findActiveRegionalRule(eq("REGIONAL_PRICING"), eq("IN")))
                .thenReturn(new ArrayList<>(List.of(regionalRuleIN)));

        // Mock strategies
        PricingStrategy festivalStrategy = mock(PricingStrategy.class);
        PricingStrategy membershipStrategy = mock(PricingStrategy.class);
        PricingStrategy regionStrategy = mock(PricingStrategy.class);

        when(strategyFactory.getStrategy("FESTIVAL_DISCOUNT")).thenReturn(festivalStrategy);
        when(strategyFactory.getStrategy("MEMBERSHIP_DISCOUNT")).thenReturn(membershipStrategy);
        when(strategyFactory.getStrategy("PAYMENT_MODE")).thenReturn(mock(PricingStrategy.class)); // skipped
        when(strategyFactory.getStrategy("REGIONAL_PRICING")).thenReturn(regionStrategy);

        // Simulate strategy outputs
        when(festivalStrategy.applyStrategy(anyList(), anyMap())).thenReturn(BigDecimal.valueOf(180));
        when(membershipStrategy.applyStrategy(anyList(), anyMap())).thenReturn(BigDecimal.valueOf(160));
        when(regionStrategy.applyStrategy(anyList(), anyMap())).thenReturn(BigDecimal.valueOf(168));

        // Act
        PricingResponseDto resp = pricingService.calculateTotal(requestDto);

        // Assert - Base and Final totals
        assertEquals(BigDecimal.valueOf(200), resp.getBaseTotal());
        assertEquals(BigDecimal.valueOf(168), resp.getFinalTotal());

        // Strategy details
        List<StrategyDiscountDetailDTO> details = resp.getStrategyDetails();
        assertNotNull(details);
        assertTrue(details.size() >= 2);
        StrategyDiscountDetailDTO last = details.get(details.size() - 1);
        assertEquals("Region Based TAX", last.getStrategyName());

        // Verify strategies applied in correct order
        InOrder inOrder = inOrder(festivalStrategy, membershipStrategy, regionStrategy);
        inOrder.verify(festivalStrategy).applyStrategy(anyList(), anyMap());
        inOrder.verify(membershipStrategy).applyStrategy(anyList(), anyMap());
        inOrder.verify(regionStrategy).applyStrategy(anyList(), anyMap());

        // Repository call verifications
        verify(ruleRepository, times(1)).findActiveRules(any(), any(), any(), any(), any());
        verify(ruleRepository, times(1)).findActiveRegionalRule("REGIONAL_PRICING", "IN");
        verify(customerRepository, times(1)).findById(1L);

        // ✅ Updated from 1 → 2 calls because service reuses productRepository
        verify(productRepository, times(2)).findByProductCode("P001");
    }



    @Test
    void calculateTotal_noRules_throwsProductNotFoundException() {
        when(ruleRepository.findActiveRules(any(), any(), any(), any(), any()))
                .thenReturn(List.of()); // no rules

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> pricingService.calculateTotal(requestDto));
        assertTrue(ex.getMessage().contains("No active pricing rules"));
    }

    @Test
    void calculateBaseTotal_productMissing_throwsProductNotFoundException() {
        // product missing
        when(productRepository.findByProductCode("P001")).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> pricingService.calculateTotal(requestDto));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void calculateTotal_customerMissing_throwsRuntimeException() {
        // customer missing
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        when(ruleRepository.findActiveRules(any(), any(), any(), any(), any()))
                .thenReturn(List.of(ruleFestival)); // provide rule list so code proceeds to get customer

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pricingService.calculateTotal(requestDto));
        assertTrue(ex.getMessage().contains("Customer not found"));
    }

    @Test
    void ruleWithConditionsNotSatisfied_isSkipped() {
        // Arrange
        // Return a single rule that requires paymentMode = UPI (not matching CARD)
        when(ruleRepository.findActiveRules(any(), any(), any(), any(), any()))
                .thenReturn(new ArrayList<>(List.of(rulePaymentModeNotMatched))); // ✅ mutable list

        // Mock the PAYMENT_MODE strategy (it should NOT be called)
        PricingStrategy pmStrategy = mock(PricingStrategy.class);
        when(strategyFactory.getStrategy("PAYMENT_MODE")).thenReturn(pmStrategy);

        // Mock product and customer repositories
        when(productRepository.findByProductCode("P001"))
                .thenReturn(Optional.of(new Product(
                        1L, "P001", "Iphone 17", BigDecimal.valueOf(100),
                        "INR", "electronics", 100
                )));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(new Customer(
                        1L, "John", "GOLD", "a@b.com",
                        LocalDateTime.now(), "{}"
                )));

        // Act
        PricingResponseDto resp = pricingService.calculateTotal(requestDto);

        // Assert
        // ✅ Ensure no strategy applied
        assertEquals(BigDecimal.valueOf(200), resp.getBaseTotal(), "Base total should remain unchanged");
        assertEquals(BigDecimal.valueOf(200), resp.getFinalTotal(), "Final total should remain unchanged");
        assertTrue(resp.getStrategyDetails().isEmpty(), "No strategy should be applied");

        // ✅ Verify strategy method not called
        verify(pmStrategy, never()).applyStrategy(anyList(), anyMap());
    }

}
