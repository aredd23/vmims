package com.apps.pims.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.pims.entity.Products;

public interface ProductRepository extends JpaRepository<Products, Long>{
	
	List<Products> findProductsByProductName(String value);

	List<Products> findProductsByGGCode(String value);

	List<Products> findProductsByProductNumber(String valueOf);

	Page<Products> findProductsByOrderId(Long orderId, Pageable pageable);

	List<Products> findProductsByOrderId(Long id);

}
