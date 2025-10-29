package com.pricingservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "tier")
	private String tier;

	@Column(name = "email")
	private String email;

	@Column(name = "joined_at")
	private LocalDateTime joinedAt;


	@Column(name = "metadata",columnDefinition = "${json.column.type}")
	private String metadata;

}
