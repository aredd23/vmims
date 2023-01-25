package com.apps.pims.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.apps.pims.entity.Order;

public interface OrderService {
	
	Page<Order> getAllOrders(int pageNo, int pageSize) throws IOException;
	
	Order saveOrder(Order Order) throws IOException;
	
	Order getOrderById(Long id) throws IOException;
	
	Order updateOrder(Order Order) throws IOException;
	
	void deleteOrderById(Long id);

	Page<Order> findOrderBySupplierId(Long id, int pageNo, int pageSize);

	Order getOrderByProductName(String value);

	Order getOrderByGGCode(String value);

	Order getOrderByProductNumber(String valueOf);

	List<Order> findAllOrders() throws IOException;
}
