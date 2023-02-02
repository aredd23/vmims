package com.apps.pims.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.apps.pims.entity.Supplier;

public interface SupplierService {
	
	Page<Supplier> getAllSuppliers(int pageNo, int size);
	
	Supplier saveSupplier(Supplier Supplier) throws IOException;
	
	Supplier getSupplierById(Long id);
	
	Supplier updateSupplier(Supplier Supplier);
	
	void deleteSupplierById(Long id);

	List<Supplier> getSupplierBySupplierName(String value);

	List<Supplier> findAllSuppliers();

	Page<Supplier> findSupplierBySupplierCategory(String category, int pageSize,int pageNo);

	List<Supplier> findSupplierBySupplierName(String supplierName);
}
