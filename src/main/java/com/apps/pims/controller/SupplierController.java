package com.apps.pims.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apps.pims.dto.SearchDto;
import com.apps.pims.entity.Order;
import com.apps.pims.entity.Supplier;
import com.apps.pims.service.OrderService;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/supplier")
public class SupplierController {

	private SupplierService supplierService;
	
	private OrderService orderService;

	public SupplierController(SupplierService supplierService,OrderService orderService) {
		super();
		this.supplierService = supplierService;
		this.orderService=orderService;
	}

	// handler method to handle list Suppliers and return mode and view
	@GetMapping()
	public String listSuppliers(Model model) {

		int pageSize =10;
		int pageNo=1;
		Page<Supplier> supp=supplierService.getAllSuppliers(1, pageSize);
		List<Supplier> supplierList = supp.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", supp.getTotalPages());
		model.addAttribute("totalRecords", supp.getTotalElements());

		//log.info("supplier list: " + supplierList);

		model.addAttribute("suppliers", supplierList);
		return "supplier";
	}
	
	@GetMapping("/suppliersList")
	public String allSuppliers(Model model) {
		
		int pageSize =10;
		int pageNo=1;
		Page<Supplier> supp=supplierService.getAllSuppliers(pageNo, pageSize);
		List<Supplier> supplierList = supp.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", supp.getTotalPages());
		model.addAttribute("totalRecords", supp.getTotalElements());

		//log.info("supplier list: " + supplierList);

		model.addAttribute("suppliers", supplierList);
		return "suppliersList";
	}
	
	@GetMapping("/category/{category}")
	public String listSuppliers(@PathVariable String category,Model model) {
		log.info("supplier list by catetory: "+category);
		model.addAttribute("category", category);
		
		int pageSize =10;
		int pageNo=1;
		Page<Supplier> supp= supplierService.findSupplierBySupplierCategory(category,pageSize,pageNo);
		List<Supplier> supplierList =supp.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", supp.getTotalPages());
		model.addAttribute("totalRecords", supp.getTotalElements());
		
		log.info("supplier list by category: " + supplierList);

		model.addAttribute("suppliers", supplierList);
		return "supplier";
	}


	@GetMapping("/new")
	public String createSupplierForm(Model model) {
		log.info("createSupplierForm()");
		// create Supplier object to hold Supplier form data
		Supplier supplier = new Supplier();
		model.addAttribute("supplier", supplier);
		return "create_supplier";

	}

	@GetMapping("/searchSuppliers")
	public String seraSuppliers(Model model) {

		// create Supplier object to hold Supplier form data
		SearchDto searchSupplier = new SearchDto();
		model.addAttribute("searchSupplier", searchSupplier);
		return "search_supplier";
	}

	@PostMapping()
	public String saveSupplier(@ModelAttribute("supplier") Supplier supplier,Model model) throws IOException {
		supplierService.saveSupplier(supplier);
		model.addAttribute("message", "Supplier Details added successfully.");
		
		int pageSize =10;
		int pageNo=1;
		Page<Supplier> supp=supplierService.getAllSuppliers(pageNo, pageSize);
		List<Supplier> supplierList = supp.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", supp.getTotalPages());
		model.addAttribute("totalRecords", supp.getTotalElements());

		//log.info("supplier list: " + supplierList);

		model.addAttribute("suppliers", supplierList);
		
		return "suppliersList";
	}

	@GetMapping("/edit/{id}")
	public String editSupplierForm(@PathVariable Long id, Model model) {
		Supplier supplier = supplierService.getSupplierById(id);
		model.addAttribute("supplier", supplier);
		return "edit_supplier";
	}

	@PostMapping("/{id}")
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
		existingSupplier.setRegistrationFee(supplier.getRegistrationFee());
		existingSupplier.setBalanceAmountWithVendor(supplier.getBalanceAmountWithVendor());
		existingSupplier.setSupplierGSTNo(supplier.getSupplierGSTNo());

	
		existingSupplier.setSupplierCategory(supplier.getSupplierCategory());
		existingSupplier.setSupplierType(supplier.getSupplierType());
		existingSupplier.setSupplierName(supplier.getSupplierName());

		// save updated Supplier object
		supplierService.updateSupplier(existingSupplier);
		return "redirect:/supplier/suppliersList";
	}

	// handler method to handle delete Supplier request

	@GetMapping("/delete/{id}")
	public String deleteSupplier(@PathVariable Long id) {
		supplierService.deleteSupplierById(id);
		return "redirect:/supplier";
	}
	
	@GetMapping("/{id}/orders")
	public String getOrdersBySupplierID(@PathVariable Long id,Model model) {
		log.info("getOrdersBySupplierID()..");
		
		int pageSize = 10;
		int pageNo = 1;
		Page<Order> ordr=orderService.findOrderBySupplierId(id,pageNo,pageSize);
		List<Order> orderList=ordr.getContent();
		//log.info("orders list: " + orderList);
		model.addAttribute("supplierId", id);
		log.info("supplierId: "+id);
		
		List<Order> resOrderList = new ArrayList<>();
		for (Order order : orderList) {

			if (order.getOrderReceiptImageData() != null) {
				order.setOrderReceiptImageBase64(Base64.getEncoder().encodeToString(ImageUtils.decompressImage(order.getOrderReceiptImageData())));
			}
			resOrderList.add(order);
		}
		
	
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", ordr.getTotalPages());
		model.addAttribute("totalRecords", ordr.getTotalElements());
		
		model.addAttribute("orders", resOrderList);
		return "orders";
	}
	
	@GetMapping("/page/{pageNo}")
	 public String display(@PathVariable (value = "pageNo") int pageNo, Model model) {
	  int pageSize =10;   // How many records on per page
	  Page<Supplier> supp=supplierService.getAllSuppliers(pageNo, pageSize);
	  List<Supplier> supplierList = supp.getContent();
	  model.addAttribute("currentPage", pageNo);
	  model.addAttribute("totalPages", supp.getTotalPages());
	  model.addAttribute("totalRecords", supp.getTotalElements());
	  model.addAttribute("suppliers", supplierList);
	  return "suppliersList";
	  
	 }

}
