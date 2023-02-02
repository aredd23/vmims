package com.apps.pims.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apps.pims.entity.Products;
import com.apps.pims.repository.ProductRepository;
import com.apps.pims.service.ProductService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired

	private ProductRepository productRepository;

	@Override
	public Page<Products> getAllProducts(int pageNo, int pageSize) {
		log.info("getAllProducts()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return productRepository.findAll(pageable);
	}

	@Override
	public Optional<Products> getProductById(Long id) {

		return productRepository.findById(id);
	}

	@Override
	public Products saveProduct(Products product) throws IOException {

		Products pp = null;

		if (product.getId() != null) {
			Optional<Products> productDb = productRepository.findById(product.getId());
			if (productDb.isPresent()) {
				Products p = productDb.get();
				p.setSoldQty(product.getSoldQty());
				p.setQuantity(product.getQuantity());
				p.setBalanceQty(product.getQuantity()-product.getSoldQty());
				p.setComments(product.getComments());
				p.setGGCode(product.getGGCode());
				p.setOtherDetails(product.getOtherDetails());
				p.setPrice(product.getPrice());
				p.setProductDescription(product.getProductDescription());
				p.setProductCategory(product.getProductCategory());
				
				p.setUpdatedDate(new Date());

				if (product.getPoOriginalImage() != null) {
					p.setPoImageData(ImageUtils.compressImage(product.getPoOriginalImage().getBytes()));
					p.setPoImageName(product.getPoOriginalImage().getOriginalFilename());

				} else {
					p.setPoImageData(null);
					p.setPoImageName(null);
				}
			
				pp = productRepository.save(p);
				log.info("product updtaed successfully."+pp);
			}
		} else {
			
			if (product.getPoOriginalImage() != null) {
				product.setPoImageData(ImageUtils.compressImage(product.getPoOriginalImage().getBytes()));
				product.setPoImageName(product.getPoOriginalImage().getOriginalFilename());

			} else {
				product.setPoImageData(null);
				product.setPoImageName(null);
				product.setCreatedDate(new Date());
			}
			pp = productRepository.save(product);
			log.info("product added successfully."+pp);
		}

		return pp;

	}

	@Override
	public List<Products> getProductsByProductName(String value) {
		return productRepository.findProductsByProductName(value);
	}

	@Override
	public List<Products> getProductsByGGCode(String value) {
		return productRepository.findProductsByGGCode(value);
	}

	@Override
	public List<Products> getProductsByProductNumber(String valueOf) {
		return productRepository.findProductsByProductNumber(valueOf);
	}

	@Override
	public Page<Products> getAllOrders(int pageNo, int pageSize) throws IOException {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Products> getAllProductsByOrderId(Long orderId, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return productRepository.findProductsByOrderId(orderId, pageable);
	}

	@Override
	public List<Products> getAllProductsByOrderId(Long id) {
		return productRepository.findProductsByOrderId(id);
	}

	@Override
	public List<Products> getAllProducts() {
		return productRepository.findAll();
	}

}
