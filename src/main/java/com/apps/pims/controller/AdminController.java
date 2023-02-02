package com.apps.pims.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping("/userdetails")
	public String userdetails(Model model, HttpServletRequest request) {
		log.info("tabs page");

		model.addAttribute("categoryList", "tabs");
		return "admin/users-profile";

	}

}
