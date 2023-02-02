package com.apps.pims.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.apps.pims.entity.Order;
import com.apps.pims.entity.Products;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.ProductService;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	private OrderService orderService;;

	private SupplierService supplierService;
	
	private ProductService productService;

	public HomeController(OrderService orderService, SupplierService supplierService,ProductService productService) {
		super();
		this.orderService = orderService;
		this.supplierService = supplierService;
		this.productService=productService;
	}
	 @GetMapping({ "/", "/index","/home" })
	public String indexPage(ModelMap model) {
		log.info("loaded index page ..");
		
		List<Supplier> supplierList = supplierService.findAllSuppliers();
		List<String> categoryList = supplierList.stream().map(s -> s.getSupplierCategory())
				.collect(Collectors.toList());
		List<String> list = categoryList.stream().distinct().collect(Collectors.toList());
		log.info("distinct categoryList list: " + list);
		List<Order> orders=orderService.getAllOrders();
		List<Products> products=productService.getAllProducts();
		
		model.addAttribute("categoryList", list);
		model.addAttribute("suppliersCount", supplierList.size());
		model.addAttribute("poCount", orders.size());
		model.addAttribute("productsCount", products.size());
		
		return "index";
	}

	//@PostMapping({ "/login" })
	public String login(ModelMap model) {
		log.info("loaded home page..");
		List<Supplier> supplierList = supplierService.findAllSuppliers();
		List<String> categoryList = supplierList.stream().map(s -> s.getSupplierCategory())
				.collect(Collectors.toList());
		List<String> list = categoryList.stream().distinct().collect(Collectors.toList());
		log.info("distinct categoryList list: " + list);
		model.addAttribute("categoryList", list);
		return "home";
	}

	/*
	 * @GetMapping("/home") public String home(Model model, HttpServletRequest
	 * request) { log.info("home page");
	 * 
	 * List<Supplier> supplierList = supplierService.findAllSuppliers();
	 * List<String> categoryList = supplierList.stream().map(s ->
	 * s.getSupplierCategory()) .collect(Collectors.toList()); List<String> list =
	 * categoryList.stream().distinct().collect(Collectors.toList());
	 * log.info("distinct categoryList list: " + list); List<Order>
	 * orders=orderService.getAllOrders(); List<Products>
	 * products=productService.getAllProducts();
	 * 
	 * model.addAttribute("categoryList", list);
	 * model.addAttribute("suppliersCount", supplierList.size());
	 * model.addAttribute("poCount", orders.size());
	 * model.addAttribute("productsCount", products.size());
	 * 
	 * return "home";
	 * 
	 * }
	 */
	
	

}
