package com.apps.pims.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.pims.dto.SearchOrder;
import com.apps.pims.entity.Order;
import com.apps.pims.service.OrderService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {
	
	
	private OrderService orderService;

	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}

	// handler method to handle list Orders and return mode and view
	@GetMapping("/orders")
	public String listOrders(Model model) {

		List<Order> orderList = orderService.getAllOrders();

		log.info("order list: " + orderList);

		model.addAttribute("orders", orderList);
		return "order";
	}

	@PostMapping("/order/searchOrders")
	public String searchOrders(@ModelAttribute("searchOrder") SearchOrder searchOrder, Model model) {

		log.info("Search Order details: " + searchOrder);

		List<Order> orderList = orderService.getAllOrders();

		log.info("order list: " + orderList);

		model.addAttribute("order", orderList);
		return "order";
	}

	@GetMapping("/order/new")
	public String createOrderForm(Model model) {

		// create Order object to hold Order form data
		Order order = new Order();
		model.addAttribute("order", order);
		return "create_order";

	}

	@GetMapping("/order/searchOrders")
	public String seraOrders(Model model) {

		// create Order object to hold Order form data
		SearchOrder searchOrder = new SearchOrder();
		model.addAttribute("searchOrder", searchOrder);
		return "search_order";

	}

	@PostMapping("/order")
	public String saveOrder(@ModelAttribute("order") Order order) throws IOException {
		orderService.saveOrder(order);
		return "redirect:/orders";
	}

	@GetMapping("/order/edit/{id}")
	public String editOrderForm(@PathVariable Long id, Model model) {
		Order order = orderService.getOrderById(id);
		model.addAttribute("order", order);
		return "edit_order";
	}

	@PostMapping("/order/{id}")
	public String updateOrder(@PathVariable Long id, @ModelAttribute("order") Order order, Model model)
			throws IOException {

		// get Order from database by id
		Order existingOrder = orderService.getOrderById(id);
		existingOrder.setId(id);
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
		
		
		if (order.getPoOriginalImage() != null) {
			existingOrder.setPoImageName(order.getPoOriginalImage().getOriginalFilename());
			existingOrder.setPoImageData(ImageUtils.compressImage(order.getPoOriginalImage().getBytes()));
		}
		

		// save updated Order object
		orderService.updateOrder(existingOrder);
		return "redirect:/orders";
	}

	// handler method to handle delete Order request

	@GetMapping("/order/{id}")
	public String deleteOrder(@PathVariable Long id) {
		orderService.deleteOrderById(id);
		return "redirect:/order";
	}


}
