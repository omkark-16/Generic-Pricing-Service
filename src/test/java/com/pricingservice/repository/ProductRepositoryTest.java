package com.pricingservice.repository;

import com.pricingservice.GenericPricingServiceApplication;
import com.pricingservice.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@ContextConfiguration(classes = GenericPricingServiceApplication.class) // 👈 Important
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        Product p = new Product();
        p.setProductCode("P100");
        p.setName("Test Product");
        p.setBasePrice(java.math.BigDecimal.valueOf(1000));
        p.setCurrency("INR");
        p.setProductCategory("electronics");
        p.setAvailableStock(50);

        Product saved = productRepository.save(p);
        assertThat(saved.getId()).isNotNull();
    }
}
