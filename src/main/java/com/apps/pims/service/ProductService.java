package com.apps.pims.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.apps.pims.entity.Products;

public interface ProductService {

	Page<Products> getAllProducts(int pageNo, int pageSize);

	Optional<Products> getProductById(Long id);

	Products saveProduct(Products product) throws IOException;

	List<Products> getProductsByProductName(String value);

	List<Products> getProductsByGGCode(String value);

	List<Products> getProductsByProductNumber(String valueOf);

	Page<Products> getAllOrders(int pageNo, int pageSize) throws IOException;

	Page<Products> getAllProductsByOrderId(Long orderId, int pageNo, int pageSize);

	List<Products> getAllProductsByOrderId(Long id);

	List<Products> getAllProducts();

}
