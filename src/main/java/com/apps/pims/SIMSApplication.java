package com.apps.pims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SIMSApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(SIMSApplication.class, args);
	}

}
