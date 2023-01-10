package com.apps.pims.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.apps.pims.entity.Supplier;
import com.apps.pims.entity.UserDetails;
import com.apps.pims.repository.UserRepository;
import com.apps.pims.service.SupplierService;
import com.apps.pims.util.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	
	private SupplierService supplierService;

	public UserController(SupplierService supplierService) {
		super();
		this.supplierService = supplierService;
	}

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public String login(UserDetails userdetails, Model model, HttpServletRequest request) {

		log.info("login method().. request details: "+userdetails);
		try {
			UserDetails userdetailsRes = userRepository.findByEmail(userdetails.getEmail());
			log.info("Invalid User details, Please try again."+userdetailsRes);

			if(userdetailsRes==null) {
				model.addAttribute("failureMessage", "Unable to login- User details not found. Please try again.");
				return "redirect:/login";
			}
			model.addAttribute("userdetails", userdetailsRes);
			
			model.addAttribute("sccessMessage", "Welcome to Supplier Information Managemnet System.");
			
			
			List<Supplier> supplierList = supplierService.getAllSuppliers();
			
			log.info("supplier list: "+supplierList);

			model.addAttribute("supplier", supplierList);

			return "supplier";
		} catch (Exception e) {

			log.error("{}", e);
			model.addAttribute("failureMessage", "Unable to login. Please try again.");
			return "redirect:/login";
		}
	}

}
