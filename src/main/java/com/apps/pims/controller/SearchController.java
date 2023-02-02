package com.apps.pims.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.apps.pims.dto.SearchDto;
import com.apps.pims.entity.Order;
import com.apps.pims.entity.Products;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.ProductService;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SearchController {

	private OrderService orderService;

	private SupplierService supplierService;
	
	private ProductService productService;

	public SearchController(OrderService orderService, SupplierService supplierService,ProductService productService) {
		super();
		this.orderService = orderService;
		this.supplierService = supplierService;
		this.productService=productService;
	}

	@GetMapping("/search")
	public String search(Model model) {

		SearchDto searchSupplier = new SearchDto();
		log.info("search....");
		model.addAttribute("searchData", searchSupplier);

		return "search";
	}

	@PostMapping("/search")
	public String searchSuppliers(@ModelAttribute("searchData") SearchDto searchData, Model model) throws IOException {

		log.info("Search Data details: " + searchData);

		List<Supplier> supplierList = new ArrayList<>();
		List<Order> ordersList = new ArrayList<>();
		
		List<Products> productsList = new ArrayList<>();
		Supplier supplier = null;
		Order order=null;

		try {

			if (searchData != null) {

				if (searchData.getFieldName().equals("SupplierId")) {
					log.info("field name: SupplierId");
					supplier = supplierService.getSupplierById(Long.valueOf(searchData.getValue()));
					if (supplier != null) {
						supplierList.add(supplier);
					}
				}

				if (searchData.getFieldName().equals("SupplierName")) {
					log.info("field name: SupplierName");
					supplierList = supplierService.getSupplierBySupplierName(searchData.getValue());
					log.info("supplier: " + supplierList);
				}

				if (searchData.getFieldName().equals("OrderId")) {
					log.info("field name: OrderId");
					order = orderService.getOrderById(Long.valueOf(searchData.getValue()));
					if (order != null) {
						ordersList.add(order);
					}
				}
				if (searchData.getFieldName().equals("ProductNumber")) {
					log.info("field name: ProductNumber");
					productsList = productService.getProductsByProductNumber(searchData.getValue());
				}

				if (searchData.getFieldName().equals("ProductName")) {
					log.info("field name: ProductName");
					productsList = productService.getProductsByProductName(searchData.getValue());
				}
				if (searchData.getFieldName().equals("GGCode")) {
					log.info("field name: GGCode");
					productsList = productService.getProductsByGGCode(searchData.getValue());
				}

			}
		} catch (NumberFormatException e) {
			log.info("NumberFormatException ..." + e.getMessage());
		}

		// log.info("supplier list: " + supplierList);

		model.addAttribute("searchData", searchData);
		
		List<Order> resOrderList = new ArrayList<>();
		if(!ordersList.isEmpty()) {
			
			for (Order order1 : ordersList) {
			
				if (order1.getOrderReceiptImageData() != null) {
					order1.setOrderReceiptImageBase64(Base64.getEncoder()
							.encodeToString(ImageUtils.decompressImage(order1.getOrderReceiptImageData())));
				}
				resOrderList.add(order1);
			}
		}
		
		List<Products> productsNewList = new ArrayList<>();

		if (productsList != null && !productsList.isEmpty()) {
			
			for (Products productO : productsList) {
				if (productO.getPoImageData() != null) {
					productO.setPoImageBase64(Base64.getEncoder().encodeToString(ImageUtils.decompressImage(productO.getPoImageData())));
				}
				productsNewList.add(productO);			
			}
			
		} 

		model.addAttribute("suppliersList", supplierList);
		model.addAttribute("ordersList", resOrderList);
		model.addAttribute("productsList", productsNewList);

		model.addAttribute("noData", null);

		if (supplierList.isEmpty() && (searchData.getFieldName().equals("SupplierId")
				|| searchData.getFieldName().equals("SupplierName"))) {
			model.addAttribute("suppliersList", null);
			model.addAttribute("ordersList", null);
			model.addAttribute("productsList", null);
			model.addAttribute("noData", "no data found");
		}
		
		if (ordersList.isEmpty() && (searchData.getFieldName().equals("OrderId"))) {
			model.addAttribute("suppliersList", null);
			model.addAttribute("ordersList", null);
			model.addAttribute("productsList", null);
			model.addAttribute("noData", "no data found");
		}

		if (productsList.isEmpty() && (searchData.getFieldName().equals("ProductNumber")
				|| searchData.getFieldName().equals("ProductName") || searchData.getFieldName().equals("GGCode"))) {
			model.addAttribute("ordersList", null);
			model.addAttribute("suppliersList", null);
			model.addAttribute("productsList", null);
			model.addAttribute("noData", "no data found");

		}
		
		if (supplierList.isEmpty() && ordersList.isEmpty() && productsList.isEmpty()) {
			model.addAttribute("ordersList", null);
			model.addAttribute("suppliersList", null);
			model.addAttribute("productsList", null);
			model.addAttribute("noData", "no data found");
		}
		
		if(ordersList.isEmpty()) {
			model.addAttribute("ordersList", null);
		}
		
		if(supplierList.isEmpty()) {
			model.addAttribute("suppliersList", null);
		}

		if(productsList.isEmpty()) {
			model.addAttribute("productsList", null);
		}

		log.info("suppliersList size: " + supplierList.size());
		log.info("ordersList size: " + ordersList.size());
		log.info("productsList size: " + productsList.size());

		return "searchList";
	}

}
