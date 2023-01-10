package com.apps.pims.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.apps.pims.dto.SearchSupplier;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SupplierController {

	private SupplierService supplierService;

	public SupplierController(SupplierService supplierService) {
		super();
		this.supplierService = supplierService;
	}

	// handler method to handle list Suppliers and return mode and view
	@GetMapping("/suppliers")
	public String listSuppliers(Model model) {

		List<Supplier> supplierList = supplierService.getAllSuppliers();

		log.info("supplier list: " + supplierList);

		model.addAttribute("suppliers", supplierList);
		return "supplier";
	}

	@PostMapping("/supplier/searchSuppliers")
	public String searchSuppliers(@ModelAttribute("searchSupplier") SearchSupplier searchSupplier, Model model) {

		log.info("Search Supplier details: " + searchSupplier);

		List<Supplier> supplierList = supplierService.getAllSuppliers();

		log.info("supplier list: " + supplierList);

		model.addAttribute("supplier", supplierList);
		return "supplier";
	}

	@GetMapping("/supplier/new")
	public String createSupplierForm(Model model) {

		// create Supplier object to hold Supplier form data
		Supplier supplier = new Supplier();
		model.addAttribute("supplier", supplier);
		return "create_supplier";

	}

	@GetMapping("/supplier/searchSuppliers")
	public String seraSuppliers(Model model) {

		// create Supplier object to hold Supplier form data
		SearchSupplier searchSupplier = new SearchSupplier();
		model.addAttribute("searchSupplier", searchSupplier);
		return "search_supplier";

	}

	@PostMapping("/supplier")
	public String saveSupplier(@ModelAttribute("supplier") Supplier supplier) throws IOException {
		supplierService.saveSupplier(supplier);
		return "redirect:/suppliers";
	}

	@GetMapping("/supplier/edit/{id}")
	public String editSupplierForm(@PathVariable Long id, Model model) {
		Supplier supplier = supplierService.getSupplierById(id);
		model.addAttribute("supplier", supplier);
		return "edit_supplier";
	}

	@PostMapping("/supplier/{id}")
	public String updateSupplier(@PathVariable Long id, @ModelAttribute("supplier") Supplier supplier, Model model)
			throws IOException {

		// get Supplier from database by id
		Supplier existingSupplier = supplierService.getSupplierById(id);
		existingSupplier.setId(id);
		existingSupplier.setSupplierAddress(supplier.getSupplierAddress());
		existingSupplier.setSupplierContactNo(supplier.getSupplierContactNo());
		existingSupplier.setUpdatedDate(new Date());
		existingSupplier.setSupplierName(supplier.getSupplierName());
		existingSupplier.setSupplierEmailId(supplier.getSupplierEmailId());
		existingSupplier.setSupplierType(supplier.getSupplierType());

		existingSupplier.setSupplierGSTNo(supplier.getSupplierGSTNo());

		existingSupplier.setSupplierCategory(supplier.getSupplierCategory());
		existingSupplier.setSupplierType(supplier.getSupplierType());
		existingSupplier.setSupplierName(supplier.getSupplierName());

		// save updated Supplier object
		supplierService.updateSupplier(existingSupplier);
		return "redirect:/suppliers";
	}

	// handler method to handle delete Supplier request

	@GetMapping("/supplier/{id}")
	public String deleteSupplier(@PathVariable Long id) {
		supplierService.deleteSupplierById(id);
		return "redirect:/supplier";
	}

}
