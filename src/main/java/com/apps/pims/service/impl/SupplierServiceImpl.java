package com.apps.pims.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apps.pims.entity.Supplier;
import com.apps.pims.repository.SupplierRepository;
import com.apps.pims.service.SupplierService;

@Service
public class SupplierServiceImpl implements SupplierService{

	private SupplierRepository supplierRepository;
	
	public SupplierServiceImpl(SupplierRepository supplierRepository) {
		super();
		this.supplierRepository = supplierRepository;
	}

	@Override
	public List<Supplier> getAllSuppliers() {
		return supplierRepository.findAll();
	}

	@Override
	public Supplier saveSupplier(Supplier supplier) throws IOException {
		supplier.setCreatedDate(new Date());
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier getSupplierById(Long id) {
		return supplierRepository.findById(id).get();
	}

	@Override
	public Supplier updateSupplier(Supplier Supplier) {
		return supplierRepository.save(Supplier);
	}

	@Override
	public void deleteSupplierById(Long id) {
		supplierRepository.deleteById(id);	
	}

}
