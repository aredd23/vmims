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
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

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

				if (searchData.getFieldName().equals("ProductNumber")) {
					log.info("field name: ProductNumber");
					ordersList = orderService.getOrderByProductNumber(searchData.getValue());
				}

				if (searchData.getFieldName().equals("ProductName")) {
					log.info("field name: ProductName");
					ordersList = orderService.getOrderByProductName(searchData.getValue());
				}
				if (searchData.getFieldName().equals("GGCode")) {
					log.info("field name: GGCode");
					ordersList = orderService.getOrderByGGCode(searchData.getValue());
				}

			}
		} catch (NumberFormatException e) {
			log.info("NumberFormatException ..." + e.getMessage());
		}

		// log.info("supplier list: " + supplierList);

		model.addAttribute("searchData", searchData);
		
		List<Order> resOrderList = new ArrayList<>();
		if(!ordersList.isEmpty()) {
			
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
		}

		model.addAttribute("suppliersList", supplierList);
		model.addAttribute("ordersList", resOrderList);

		model.addAttribute("noData", null);

		if (supplierList.isEmpty() && (searchData.getFieldName().equals("SupplierId")
				|| searchData.getFieldName().equals("SupplierName"))) {
			model.addAttribute("suppliersList", null);
			model.addAttribute("ordersList", null);
			model.addAttribute("noData", "no data found");
		}

		if (ordersList.isEmpty() && (searchData.getFieldName().equals("ProductNumber")
				|| searchData.getFieldName().equals("ProductName") || searchData.getFieldName().equals("GGCode"))) {
			model.addAttribute("ordersList", null);
			model.addAttribute("suppliersList", null);
			model.addAttribute("noData", "no data found");

		}
		
		if (supplierList.isEmpty() && ordersList.isEmpty()) {
			model.addAttribute("ordersList", null);
			model.addAttribute("suppliersList", null);
			model.addAttribute("noData", "no data found");
		}
		
		if(!supplierList.isEmpty()) {
			model.addAttribute("ordersList", null);
		}
		
		if(!ordersList.isEmpty()) {
			model.addAttribute("suppliersList", null);
		}


		log.info("suppliersList size: " + supplierList.size());
		log.info("ordersList size: " + ordersList.size());

		return "searchList";
	}

}
