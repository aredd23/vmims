package com.apps.pims.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.apps.pims.dto.SearchDto;
import com.apps.pims.entity.Order;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SearchController {

	private OrderService orderService;

	private SupplierService supplierService;

	public SearchController(OrderService orderService, SupplierService supplierService) {
		super();
		this.orderService = orderService;
		this.supplierService = supplierService;
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
		Supplier supplier = null;
		Order order = null;
		
		
	
		try {

		if (searchData != null) {

			if (searchData.getFieldName().equals("SupplierId")) {
				log.info("field name: SupplierId");
				supplier = supplierService.getSupplierById(Long.valueOf(searchData.getValue()));
			}

			if (searchData.getFieldName().equals("SupplierName")) {
				log.info("field name: SupplierName");
				supplierList = supplierService.getSupplierBySupplierName(searchData.getValue());
				log.info("supplier: "+supplierList);
			}

			if (searchData.getFieldName().equals("ProductNumber")) {
				log.info("field name: ProductNumber");
				order = orderService.getOrderByProductNumber(searchData.getValue());
			}

			if (searchData.getFieldName().equals("ProductName")) {
				log.info("field name: ProductName");
				order = orderService.getOrderByProductName(searchData.getValue());
			}
			if (searchData.getFieldName().equals("GGCode")) {
				log.info("field name: GGCode");
				order = orderService.getOrderByGGCode(searchData.getValue());
			}

		}
		}catch (NumberFormatException e) {
			log.info("NumberFormatException ..."+e.getMessage());
		}
		supplierList.add(supplier);
		ordersList.add(order);
		// log.info("supplier list: " + supplierList);

		model.addAttribute("searchData", searchData);

		model.addAttribute("suppliersList", supplierList);
		model.addAttribute("ordersList", ordersList);
		
		log.info("suppliersList: "+supplierList.size());
		log.info("ordersList: "+ordersList.size());

		if (supplierList.isEmpty()) {
			model.addAttribute("suppliersList", null);
		}

		if (ordersList.isEmpty()) {
			model.addAttribute("ordersList", null);

		}
		
		log.info("suppliersList: "+supplierList);
		log.info("ordersList: "+ordersList);
		
		return "searchList";
	}

}
