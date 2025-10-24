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

	public Product(Long id, String productCode, String name, BigDecimal basePrice, String currency,
			String productCategory, Integer availableStock, String attributes) {
		super();
		this.id = id;
		this.productCode = productCode;
		this.name = name;
		this.basePrice = basePrice;
		this.currency = currency;
		this.productCategory = productCategory;
		this.availableStock = availableStock;
		this.attributes = attributes;
	}

	public Product() {
	};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

}
