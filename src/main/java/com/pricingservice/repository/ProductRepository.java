package com.pricingservice.repository;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pricingservice.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByProductCode(String productCode);

	List<Product> findByProductCategory(String productCategory);

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.availableStock = :stock WHERE p.productCode = :productCode")
	int updateAvailableStock(@Param("productCode") String productCode, @Param("stock") Integer stock);


}
