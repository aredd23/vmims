package com.apps.pims.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductDto {
	
	
		private Long id;
	
		private String productName;
		private String productDescripton;
		private String productNumber;
		private double price;
		
		private MultipartFile productImage;

		private String manufacturerName;
		private String manufacturerAddress;
		private String manufacturerEmailId;
		private String manufacturerContactNo;
		private String manufacturerType;
		

}
