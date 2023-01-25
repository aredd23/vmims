package com.apps.pims.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apps.pims.dto.SearchOrder;
import com.apps.pims.entity.Order;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/order")
public class OrderController {

	private OrderService orderService;

	private SupplierService supplierService;

	public OrderController(OrderService orderService, SupplierService supplierService) {
		super();
		this.orderService = orderService;
		this.supplierService = supplierService;
	}

	// handler method to handle list Orders and return mode and view
	@GetMapping()
	public String listOrders(Model model) throws IOException {

		int pageSize = 10;
		int pageNo = 1;
		List<Order> orderList = new ArrayList<>();
		Page<Order> ordr = orderService.getAllOrders(pageNo, pageSize);
		if (ordr != null) {
			orderList = ordr.getContent();
			model.addAttribute("totalPages", ordr.getTotalPages());
			model.addAttribute("totalRecords", ordr.getTotalElements());
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orders", orderList);
		} else {
			model.addAttribute("totalPages", 0);
			model.addAttribute("totalRecords", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("orders", orderList);
		}

		return "orders";
	}

	@GetMapping("/ordersList")
	public String allOrders(Model model) throws IOException {

		int pageSize = 10;
		int pageNo = 1;
		List<Order> orderList = new ArrayList<>();
		Page<Order> ordr = orderService.getAllOrders(pageNo, pageSize);

		List<Order> resOrderList = new ArrayList<>();
		if (ordr != null) {
			orderList = ordr.getContent();

			for (Order order : orderList) {
				if (order.getPoImageData() != null) {
					order.setPoImageBase64(
							Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getPoImageData())));
				}
				if (order.getOrderReceiptImageData() != null) {
					order.setOrderReceiptImageBase64(Base64.getEncoder()
							.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData())));
				}
				resOrderList.add(order);

				model.addAttribute("totalPages", ordr.getTotalPages());
				model.addAttribute("totalRecords", ordr.getTotalElements());
				model.addAttribute("currentPage", pageNo);
				model.addAttribute("orders", resOrderList);
			}
		} else {
			model.addAttribute("totalPages", 0);
			model.addAttribute("totalRecords", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("orders", orderList);
		}

		return "ordersList";
	}

	@GetMapping("/new/{supplierId}")
	public String createOrderForm(@PathVariable Long supplierId, Model model) {
		log.info("createOrderForm()...");
		// create Order object to hold Order form data
		Order order = new Order();
		Supplier supplier = supplierService.getSupplierById(supplierId);
		order.setSupplierId(supplier.getId());
		order.setSupplierName(supplier.getSupplierName());
		// log.info("supplier details:"+supplier);
		model.addAttribute("order", order);
		return "create_order";

	}

	@GetMapping("/{id}")
	public String getOrders(@PathVariable Long id, Model model) throws IOException {
		Order order = orderService.getOrderById(id);
		model.addAttribute("order", order);
		return "edit_order";

	}

	@PostMapping("/createOrder")
	public String saveOrder(@ModelAttribute("order") Order order) throws IOException {
		orderService.saveOrder(order);
		return "redirect:/order/ordersList";
	}

	@GetMapping("/edit/{id}")
	public String editOrderForm(@PathVariable Long id, Model model) throws IOException {
		Order order = orderService.getOrderById(id);
		if (order.getPoImageData() != null) {
			log.info("Product Image Name from db: " + order.getPoImageName());
			// log.info("Product Image Data from db: " + order.getPoImageData());
			order.setPoImageBase64(
					Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getPoImageData())));
		}
		if (order.getOrderReceiptImageData() != null) {
			// log.info("Order Receipt Data from db: " + order.getOrderReceiptImageData());
			order.setOrderReceiptImageBase64(
					Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData())));
		}
		model.addAttribute("order", order);
		return "edit_order";
	}

	@PostMapping("/{id}")
	public String updateOrder(@PathVariable Long id, @ModelAttribute("order") Order order, Model model)
			throws IOException {
		log.info("save updated Order object");
		order.setId(id);
		orderService.updateOrder(order);
		return "redirect:/order/ordersList";
	}

	// handler method to handle delete Order request

	@GetMapping("/delete/{id}")
	public String deleteOrder(@PathVariable Long id) {
		orderService.deleteOrderById(id);
		return "redirect:/order";
	}

	@PostMapping("/searchOrders")
	public String searchOrders(@ModelAttribute("searchOrder") SearchOrder searchOrder, Model model) throws IOException {

		log.info("Search Order details: " + searchOrder);
		int pageSize = 10;
		int pageNo = 1;
		Page<Order> ordr = orderService.getAllOrders(1, pageSize);
		List<Order> orderList = ordr.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", ordr.getTotalPages());
		model.addAttribute("totalRecords", ordr.getTotalElements());

		model.addAttribute("order", orderList);
		return "orders";
	}

	@GetMapping("/receipt/image/download/{id}")
	public void downloadOrderReceipt(@PathVariable Long id, Model model, HttpServletResponse response)
			throws IOException {
		Order order = orderService.getOrderById(id);
		log.info("downloadOrderReceipt() ...order id: " + id);
		if (order != null) {
			response.setContentType("image/jpeg, image/jpg,,image/JPG,image/JPEG, image/png, image/gif, image/pdf");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + order.getOrderReceiptImageName();
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			if (order.getOrderReceiptImageData() != null) {
				byte[] image = ImageUtils.decompressImage(order.getOrderReceiptImageData());
				outputStream.write(image);

			} else {
				log.warn("image data is null");
			}
			outputStream.close();
		}
	}

	@GetMapping("receipt/image/preview/{id}")
	public String showOrderReceipt(@PathVariable Long id, Model model) throws ServletException, IOException {
		log.info("showOrderImage() ...order id: " + id);

		Order order = orderService.getOrderById(id);

		String orderProductImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData()));
		model.addAttribute("orderReceiptImageBase64", orderProductImageBase64);

		return "productReceiptPreview";
	}

	@GetMapping("/product/image/download/{id}")
	public void downloadOrderImage(@PathVariable Long id, Model model, HttpServletResponse response)
			throws IOException {
		Order order = orderService.getOrderById(id);
		log.info("downloadOrderReceipt() ...order id: " + id);
		if (order != null) {
			response.setContentType("image/jpeg, image/jpg,,image/JPG,image/JPEG, image/png, image/gif, image/pdf");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + order.getPoImageName();
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			if (order.getPoImageData() != null) {
				byte[] image = ImageUtils.decompressImage(order.getPoImageData());
				outputStream.write(image);

			} else {
				log.warn("image data is null");
			}
			outputStream.close();
		}
	}

	@GetMapping("product/image/preview/{id}")
	public String showOrderImage(@PathVariable Long id, Model model) throws ServletException, IOException {
		log.info("showOrderImage() ...order id: " + id);
		Order order = orderService.getOrderById(id);

		String orderProductImageBase64 = Base64.getEncoder()
				.encodeToString(ImageUtils.decompressImage(order.getPoImageData()));
		model.addAttribute("orderProductImageBase64", orderProductImageBase64);

		return "productImagePreview";
	}

	@GetMapping("/page/{pageNo}")
	public String display(@PathVariable(value = "pageNo") int pageNo, Model model) throws IOException {

		int pageSize = 10;
		Page<Order> ordr = orderService.getAllOrders(pageNo, pageSize);
		List<Order> orderList = ordr.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", ordr.getTotalPages());
		model.addAttribute("totalRecords", ordr.getTotalElements());

		model.addAttribute("orders", orderList);
		return "ordersList";

	}

}
