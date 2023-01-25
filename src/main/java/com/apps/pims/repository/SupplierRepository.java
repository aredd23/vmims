package com.apps.pims.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.pims.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long>{
	List<Supplier> findSupplierBySupplierName(String value);
	Page<Supplier> findSupplierBySupplierCategory(String category, Pageable pageable);
}
