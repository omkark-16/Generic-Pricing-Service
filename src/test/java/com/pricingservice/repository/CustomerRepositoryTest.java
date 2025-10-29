package com.pricingservice.repository;

import com.pricingservice.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindByEmail() {
        Customer customer = new Customer();
        customer.setName("Hrutwik Kale");
        customer.setTier("GOLD");
        customer.setEmail("hrutwik@example.com");
        customer.setJoinedAt(LocalDateTime.now());
        customer.setMetadata("{\"loyaltyPoints\":500}");

        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByEmail("hrutwik@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getTier()).isEqualTo("GOLD");
    }

    @Test
    void testFindByTier() {
        Customer c1 = new Customer(null, "Alice", "GOLD", "alice@mail.com", LocalDateTime.now(), "{\n" +
                "  \"region\": \"IN\",\n" +
                "  \"referred\": true,\n" +
                "  \"hasPoints\": true\n" +
                "}");
        Customer c2 = new Customer(null, "Bob", "SILVER", "bob@mail.com", LocalDateTime.now(), "{\n" +
                "  \"region\": \"IN\",\n" +
                "  \"referred\": true,\n" +
                "  \"hasPoints\": true\n" +
                "}");
        customerRepository.saveAll(List.of(c1, c2));

        List<Customer> goldCustomers = customerRepository.findByTier("GOLD");

        assertThat(goldCustomers).hasSize(1);
        assertThat(goldCustomers.get(0).getName()).isEqualTo("Alice");
    }
}
