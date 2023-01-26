package com.apps.pims.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apps.pims.entity.Supplier;
import com.apps.pims.entity.User;
import com.apps.pims.repository.UserRepository;
import com.apps.pims.service.SupplierService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/user")
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
	@PostMapping
	public String addUser(@ModelAttribute("userdetails") User userdetails, Model model, HttpServletRequest request) {
		
		log.info("login method().. request details: "+userdetails);
		try {
			User userdetailsRes = userRepository.save(userdetails);
		
			log.info("User details from  db:: "+userdetailsRes);
			
			model.addAttribute("userdetails", userdetailsRes);
			
			model.addAttribute("sccessMessage", "User added successfully.");

			return "user";
		} catch (Exception e) {

			log.error("{}", e);
			model.addAttribute("failureMessage", "Unable to add user details. Please try again.");
			return "user";
		}
	}
	
	
	@GetMapping("/{mailId}")
	public String getUserDetails(@PathVariable String mailId, Model model){
		
		User userdetailsRes = userRepository.findByEmail(mailId);
		model.addAttribute("user", userdetailsRes);
		return "userDetails";
		
	}
	
	@GetMapping("/logout")
	public String logout( Model model, HttpServletRequest request){
		
		request.getSession().invalidate();
		return "redirect:/login";
		
	}

}
