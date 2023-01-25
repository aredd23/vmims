package com.apps.pims.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.apps.pims.entity.Supplier;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	private SupplierService supplierService;

	public HomeController(SupplierService supplierService) {
		super();
		this.supplierService = supplierService;
	}

	 @GetMapping({ "/", "/index" })
	public String homePage(ModelMap model) {
		log.info("loaded index page ..");
		
		List<Supplier> supplierList = supplierService.findAllSuppliers();
		List<String> categoryList = supplierList.stream().map(s -> s.getSupplierCategory())
				.collect(Collectors.toList());
		List<String> list = categoryList.stream().distinct().collect(Collectors.toList());
		log.info("distinct categoryList list: " + list);
		model.addAttribute("categoryList", list);
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

	@GetMapping("/home")
	public String home(Model model, HttpServletRequest request) {
		log.info("home page");
		List<Supplier> supplierList = supplierService.findAllSuppliers();
		List<String> categoryList = supplierList.stream().map(s -> s.getSupplierCategory())
				.collect(Collectors.toList());
		List<String> list = categoryList.stream().distinct().collect(Collectors.toList());
		log.info("distinct categoryList list: " + list);
		model.addAttribute("categoryList", list);
		return "home";

	}
	
	@GetMapping("/tabs")
	public String tabs(Model model, HttpServletRequest request) {
		log.info("tabs page");

		model.addAttribute("categoryList", "tabs");
		return "tabs";

	}

}
