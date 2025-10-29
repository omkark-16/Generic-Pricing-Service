package com.pricingservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pricingservice.entities.Rules;
import org.springframework.data.repository.query.Param;

public interface RuleRepository extends JpaRepository<Rules, Long> {


@Query(value = """
    SELECT *
    FROM rules r
    WHERE (
            r.region = :region
            OR r.region = 'GLOBAL'
          )
      AND (r.active_from IS NULL OR r.active_from <= :currentTime)
      AND (r.active_until IS NULL OR r.active_until >= :currentTime)
      AND (
            r.conditions->'category' IS NULL
            OR :categories IS NULL
            OR (
                (jsonb_typeof(r.conditions->'category') = 'array'
                    AND (
                        SELECT COUNT(*) 
                        FROM jsonb_array_elements_text(r.conditions->'category') AS c(category)
                        WHERE lower(c.category) = ANY(:categories)
                    ) = jsonb_array_length(r.conditions->'category')
                )
                OR (jsonb_typeof(r.conditions->'category') = 'string'
                    AND lower(r.conditions->>'category') = ANY(:categories)
                )
            )
          )
      AND (
            r.conditions->>'tier' IS NULL
            OR :tier IS NULL
            OR r.conditions->>'tier' = :tier
          )
      AND (
            r.conditions->>'referred' IS NULL
            OR :referred IS NULL
            OR (r.conditions->>'referred')::boolean = :referred
          )
      -- 🚫 Exclude Regional Pricing strategies
      AND r.strategy_key <> 'REGIONAL_PRICING'
    ORDER BY 
        CASE 
            WHEN r.region = :region THEN 0
            WHEN r.region = 'GLOBAL' THEN 1
            ELSE 2
        END,
        r.priority ASC
""", nativeQuery = true)
List<Rules> findActiveRules(
		@Param("region") String region,
		@Param("currentTime") LocalDateTime currentTime,
		@Param("categories") String[] categories,
		@Param("tier") String tier,
		@Param("referred") Boolean referred
);


	@Query("SELECT r FROM Rules r " +
			"WHERE r.strategyKey = :strategyKey " +
			"AND (r.region = :region OR r.region = 'GLOBAL') " +
			"AND r.activeFrom <= CURRENT_TIMESTAMP " +
			"AND r.activeUntil >= CURRENT_TIMESTAMP " +
			"ORDER BY r.priority ASC")
	List<Rules> findActiveRegionalRule(
			@Param("strategyKey") String strategyKey,
			@Param("region") String region
	);


}
