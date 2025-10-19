package com.pricingservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pricingservice.entities.Rules;

public interface RuleRepository extends JpaRepository<Rules, Long> {

	@Query("""
			SELECT r FROM Rules r
			WHERE (:region IS NULL OR r.region = :region)
			  AND (r.activeFrom IS NULL OR r.activeFrom <= :currentTime)
			  AND (r.activeUntil IS NULL OR r.activeUntil >= :currentTime)
			ORDER BY r.priority ASC
			""")
	List<Rules> findActiveRulesByRegion(String region, LocalDateTime currentTime);

	List<Rules> findByStrategyKey(String strategyKey);

	@Query("""
			SELECT r FROM Rules r
			WHERE r.region IS NULL
			  AND (r.activeFrom IS NULL OR r.activeFrom <= :currentTime)
			  AND (r.activeUntil IS NULL OR r.activeUntil >= :currentTime)
			ORDER BY r.priority ASC
			""")
	List<Rules> findGlobalActiveRules(LocalDateTime currentTime);

}
