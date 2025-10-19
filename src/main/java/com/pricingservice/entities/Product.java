package com.pricingservice.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_code", unique = true, nullable = false)
	private String productCode;

	@Column(name = "product_name")
	private String name;

	@Column(name = "base_price", precision = 10, scale = 2)
	private BigDecimal basePrice;

	@Column(name = "currency")
	private String currency;

	@Column(name = "product_category")
	private String productCategory;

	@Column(name = "available_stock")
	private Integer availableStock;

	@Column(name = "attributes", columnDefinition = "jsonb")
	private String attributes;

}
