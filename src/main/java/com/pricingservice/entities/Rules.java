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
	
	
	

	public Rules(Long id, String ruleName, Integer priority, String strategyKey, LocalDateTime activeFrom,
			LocalDateTime activeUntil, String conditions, String parameters, String region) {
		super();
		this.id = id;
		this.ruleName = ruleName;
		this.priority = priority;
		this.strategyKey = strategyKey;
		this.activeFrom = activeFrom;
		this.activeUntil = activeUntil;
		this.conditions = conditions;
		this.parameters = parameters;
		this.region = region;
	}
	
	public Rules() {};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getStrategyKey() {
		return strategyKey;
	}

	public void setStrategyKey(String strategyKey) {
		this.strategyKey = strategyKey;
	}

	public LocalDateTime getActiveFrom() {
		return activeFrom;
	}

	public void setActiveFrom(LocalDateTime activeFrom) {
		this.activeFrom = activeFrom;
	}

	public LocalDateTime getActiveUntil() {
		return activeUntil;
	}

	public void setActiveUntil(LocalDateTime activeUntil) {
		this.activeUntil = activeUntil;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

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

	@Column(name = "conditions", columnDefinition = "jsonb")
	private String conditions;
	@Column(name = "parameters", columnDefinition = "jsonb")
	private String parameters;

	@Column(name = "region")
	private String region;

}
