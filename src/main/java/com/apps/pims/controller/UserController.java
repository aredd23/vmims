package com.apps.pims.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.apps.pims.entity.Supplier;
import com.apps.pims.entity.User;
import com.apps.pims.repository.UserRepository;
import com.apps.pims.service.SupplierService;

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

	//@PostMapping("/login")
	public String login(@ModelAttribute("userdetails") User userdetails, Model model, HttpServletRequest request) {

	
		
		log.info("login method().. request details: "+userdetails);
		try {
			User userdetailsRes = userRepository.findByEmail(userdetails.getEmail());
		
			log.info("User details from  db:: "+userdetailsRes);
			if(userdetailsRes==null) {
				log.info("Invalid User details, Please try again."+userdetailsRes);
				model.addAttribute("failureMessage", "Incorrect Email address. Please try again.");
				return "login";
			}
			else if(userdetailsRes!=null && !userdetailsRes.getPassword().equals(userdetails.getPassword())) {
				log.info("Invalid User password, Please try again."+userdetailsRes);
				model.addAttribute("failureMessage", "Incorrect password. Please try again.");
				return "login";
			}
			model.addAttribute("userdetails", userdetailsRes);
			
			model.addAttribute("sccessMessage", "Welcome to Supplier Information Managemnet System.");
			
			List<Supplier> supplierList = supplierService.findAllSuppliers();
			List<String> categoryList=supplierList.stream().map(s->s.getSupplierCategory()).collect(Collectors.toList());
			List<String> list=categoryList.stream().distinct().collect(Collectors.toList());
			log.info("distinct categoryList list: " + list);
			model.addAttribute("categoryList", list);
			return "home";
		} catch (Exception e) {

			log.error("{}", e);
			model.addAttribute("failureMessage", "Unable to login. Please try again.");
			return "login";
		}
	}
	
	//@GetMapping("/login")
	public String login( Model model, HttpServletRequest request){
		return "login";
		
	}
	
	@GetMapping("/logout")
	public String logout( Model model, HttpServletRequest request){
		
		request.getSession().invalidate();
		return "redirect:/login";
		
	}

}
