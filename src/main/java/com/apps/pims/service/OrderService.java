package com.apps.pims.service;

import java.io.IOException;
import java.util.List;

import com.apps.pims.entity.Order;

public interface OrderService {
	List<Order> getAllOrders();
	
	Order saveOrder(Order Order) throws IOException;
	
	Order getOrderById(Long id);
	
	Order updateOrder(Order Order);
	
	void deleteOrderById(Long id);
}
