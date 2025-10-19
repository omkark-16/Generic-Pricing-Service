package com.pricingservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricingservice.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByProductCode(String productCode);

	List<Product> findByProductCategory(String productCategory);

}
