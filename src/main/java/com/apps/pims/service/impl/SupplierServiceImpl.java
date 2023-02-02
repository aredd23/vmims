package com.apps.pims.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apps.pims.entity.Supplier;
import com.apps.pims.repository.SupplierRepository;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService{

	private SupplierRepository supplierRepository;
	
	public SupplierServiceImpl(SupplierRepository supplierRepository) {
		super();
		this.supplierRepository = supplierRepository;
	}

	@Override
	public Page<Supplier> getAllSuppliers(int pageNo, int size) {
		Pageable pageable = PageRequest.of(pageNo-1,size);
		return supplierRepository.findAll(pageable);
	}

	@Override
	public Supplier saveSupplier(Supplier supplier) throws IOException {
		log.info("saveSupplier().."+supplier);
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

	@Override
	public List<Supplier> getSupplierBySupplierName(String value) {
		return supplierRepository.findSupplierBySupplierName(value);
	}

	@Override
	public List<Supplier> findAllSuppliers() {
		return supplierRepository.findAll();
	}

	@Override
	public Page<Supplier> findSupplierBySupplierCategory(String category, int pageSize,int pageNo) {
		Pageable pageable = PageRequest.of(pageNo-1,pageSize);
		return supplierRepository.findSupplierBySupplierCategory(category,pageable);
	}

	@Override
	public List<Supplier> findSupplierBySupplierName(String supplierName) {
		
		return supplierRepository.findSupplierBySupplierName(supplierName);
	}

}
