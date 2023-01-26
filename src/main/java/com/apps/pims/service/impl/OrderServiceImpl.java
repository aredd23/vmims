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
			if (order.getPoImageData() != null) {
				order.setPoImageBase64(
						Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getPoImageData())));
			}
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
		if (order.getPoOriginalImage() != null) {
			order.setPoImageData(ImageUtils.compressImage(order.getPoOriginalImage().getBytes()));
			order.setPoImageName(order.getPoOriginalImage().getOriginalFilename());
		} else {
			order.setPoImageData(null);
			order.setPoImageName(null);
		}
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

		if (order.getPoImageData() != null) {
			order.setPoImageBase64(
					Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getPoImageData())));
		}
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
		existingOrder.setProductDescription(order.getProductDescription());
		existingOrder.setProductCategory(order.getProductCategory());
		existingOrder.setProductDescription(order.getProductDescription());
		existingOrder.setGGCode(order.getGGCode());
		existingOrder.setUpdatedDate(new Date());
		existingOrder.setAddistionalCost(order.getAddistionalCost());
		existingOrder.setGSTCharges(order.getGSTCharges());
		existingOrder.setMultiplier(order.getMultiplier());
		existingOrder.setShippingCost(order.getShippingCost());
		existingOrder.setPrice(order.getPrice());
		existingOrder.setQuantity(order.getQuantity());
		existingOrder.setTotalCost(order.getTotalCost());
		existingOrder.setComments(order.getComments());
		existingOrder.setOtherDetails(order.getOtherDetails());

		if (existingOrder.getPoOriginalImage() != null) {
			log.info("existingOrder.getPoOriginalImage() != null");
			// existingOrder.setPoImageName(order.getPoOriginalImage().getOriginalFilename());
			// existingOrder.setPoImageData(ImageUtils.compressImage(order.getPoOriginalImage().getBytes()));
		}
		if (!order.getPoOriginalImage().isEmpty() && order.getPoOriginalImage() != null) {
			log.info("order.getPoOriginalImage() != null");
			existingOrder.setPoImageName(order.getPoOriginalImage().getOriginalFilename());
			existingOrder.setPoImageData(ImageUtils.compressImage(order.getPoOriginalImage().getBytes()));
		}

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
	public List<Order> getOrderByProductName(String value) {
		return orderRepository.findOrderByProductName(value);
	}

	@Override
	public List<Order> getOrderByGGCode(String value) {
		return orderRepository.findOrderByGGCode(value);
	}

	@Override
	public List<Order> getOrderByProductNumber(String valueOf) {
		return orderRepository.findOrderByProductNumber(valueOf);
	}

	@Override
	public Page<Order> getAllOrders(int pageNo, int pageSize) throws IOException {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return orderRepository.findAll(pageable);
	}

}
