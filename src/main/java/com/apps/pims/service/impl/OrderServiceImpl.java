package com.apps.pims.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apps.pims.entity.Order;
import com.apps.pims.repository.OrderRepository;
import com.apps.pims.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order saveOrder(Order order) throws IOException {
		order.setCreatedDate(new Date());
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).get();
	}

	@Override
	public Order updateOrder(Order Order) {
		return orderRepository.save(Order);
	}

	@Override
	public void deleteOrderById(Long id) {
		orderRepository.deleteById(id);
	}

}
