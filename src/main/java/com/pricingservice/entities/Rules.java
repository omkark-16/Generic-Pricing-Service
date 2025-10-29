package com.pricingservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rules {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rule_name", nullable = false)
	private String ruleName;

	@Column(name = "priority", nullable = false)
	private Integer priority;

	@Column(name = "strategy_key", nullable = false)
	private String strategyKey;

	@Column(name = "active_from")
	private LocalDateTime activeFrom;

	@Column(name = "active_until")
	private LocalDateTime activeUntil;

	@Column(columnDefinition = "${json.column.type}")
	private String conditions;
	@Column(columnDefinition = "${json.column.type}")
	private String parameters;

	@Column(name = "region")
	private String region;

}
