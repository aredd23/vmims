package com.apps.pims.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apps.pims.entity.Order;
import com.apps.pims.repository.OrderRepository;
import com.apps.pims.service.OrderService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	@Override
	public List<Order> findAllOrders() throws IOException {

		List<Order> ordersList = orderRepository.findAll();
		List<Order> resOrderList = new ArrayList<>();
		for (Order order : ordersList) {
			if (order.getOrderReceiptImageData() != null) {
				order.setOrderReceiptImageBase64(Base64.getEncoder()
						.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData())));
			}
			resOrderList.add(order);
		}
		return resOrderList;
	}

	@Override
	public Order saveOrder(Order order) throws IOException {
		log.info("saveOrder()..");
		order.setCreatedDate(new Date());

		if (order.getOrderReceiptImage() != null) {
			order.setOrderReceiptImageData(ImageUtils.compressImage(order.getOrderReceiptImage().getBytes()));
			order.setOrderReceiptImageName(order.getOrderReceiptImage().getOriginalFilename());

		} else {
			order.setOrderReceiptImageData(null);
			order.setOrderReceiptImageName(null);
		}
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Long id) throws IOException {

		Order order = orderRepository.findById(id).get();


		if (order.getOrderReceiptImageData() != null) {
			order.setOrderReceiptImageBase64(
					Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData())));

		}

		return order;
	}

	@Override
	public Order updateOrder(Order order) throws IOException {

		// get Order from database by id
		Order existingOrder = getOrderById(order.getId());

		// log.info(" order details from db:: "+order);

		existingOrder.setId(order.getId());
		existingOrder.setSupplierId(order.getSupplierId());
		existingOrder.setUpdatedDate(new Date());
		existingOrder.setAdditionalCost(order.getAdditionalCost());
		existingOrder.setGSTCharges(order.getGSTCharges());
		existingOrder.setShippingCost(order.getShippingCost());
		existingOrder.setQuantity(order.getQuantity());
		existingOrder.setTotalCost(order.getTotalCost());
		existingOrder.setComments(order.getComments());
		existingOrder.setOtherDetails(order.getOtherDetails());
		existingOrder.setCarrier(order.getCarrier());
		existingOrder.setShippedBy(order.getShippedBy());
		//existingOrder.setShippedDate(order.getShippedDate());
		existingOrder.setShippingWeight(order.getShippingWeight());
		existingOrder.setShippedPersonContactNo(order.getShippedPersonContactNo());
		existingOrder.setShippingAddress(order.getShippingAddress());
		existingOrder.setShippingAmountPerKg(existingOrder.getShippingAmountPerKg());
		existingOrder.setShippedPersonEmail(order.getShippedPersonEmail());

		if (existingOrder.getOrderReceiptImage() != null) {
			log.info("existingOrder.getOrderReceiptImage() != null");
			// existingOrder.setOrderReceiptImageData(ImageUtils.compressImage(order.getOrderReceiptImage().getBytes()));
		}

		if (!order.getOrderReceiptImage().isEmpty() && order.getOrderReceiptImage() != null) {
			log.info("order.getOrderReceiptImage() != null");
			existingOrder.setOrderReceiptImageName(order.getOrderReceiptImage().getOriginalFilename());
			existingOrder.setOrderReceiptImageData(ImageUtils.compressImage(order.getOrderReceiptImage().getBytes()));
		}

		// log.info("updated order details: "+existingOrder);

		return orderRepository.save(existingOrder);
	}

	@Override
	public void deleteOrderById(Long id) {
		orderRepository.deleteById(id);
	}

	@Override
	public Page<Order> findOrderBySupplierId(Long id, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return orderRepository.findOrderBySupplierId(id, pageable);
	}

	@Override
	public Page<Order> getAllOrders(int pageNo, int pageSize) throws IOException {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return orderRepository.findAll(pageable);
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAll();
	}


	

}
