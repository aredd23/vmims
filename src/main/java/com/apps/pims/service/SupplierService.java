package com.apps.pims.service;

import java.io.IOException;
import java.util.List;

import com.apps.pims.entity.Supplier;

public interface SupplierService {
	List<Supplier> getAllSuppliers();
	
	Supplier saveSupplier(Supplier Supplier) throws IOException;
	
	Supplier getSupplierById(Long id);
	
	Supplier updateSupplier(Supplier Supplier);
	
	void deleteSupplierById(Long id);
}
