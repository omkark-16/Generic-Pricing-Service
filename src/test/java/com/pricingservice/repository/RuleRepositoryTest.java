package com.pricingservice.repository;

import com.pricingservice.entities.Rules;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RuleRepositoryTest {

    @Autowired
    private RuleRepository ruleRepository;

    @Test
    void testSaveAndFindAllRules() {
        Rules rule = new Rules();
        rule.setRuleName("Regional Pricing");
        rule.setPriority(1);
        rule.setStrategyKey("REGIONAL");
        rule.setActiveFrom(LocalDateTime.now().minusDays(1));
        rule.setActiveUntil(LocalDateTime.now().plusDays(10));
        rule.setConditions("{\"region\":\"IN\"}");
        rule.setParameters("{\"discount\":5}");
        rule.setRegion("IN");

        ruleRepository.save(rule);

        List<Rules> rulesList = ruleRepository.findAll();
        assertThat(rulesList).hasSize(1);
        assertThat(rulesList.get(0).getStrategyKey()).isEqualTo("REGIONAL");
    }

    @Test
    void testFindByRegion() {
        Rules rule = new Rules(null, "Holiday Sale", 2, "DISCOUNT", null, null, "{\"category\":\"electronics\"}", "{\"festivalDiscount\":10}", "US");
        ruleRepository.save(rule);

        List<Rules> found = ruleRepository.findAll();
        assertThat(found).extracting(Rules::getRegion).contains("US");
    }
}
