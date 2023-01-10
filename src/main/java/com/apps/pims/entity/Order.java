package com.apps.pims.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "purchase_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long supplierId;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	private String productCategory;
	private double price;
	private Integer quantity;
	private String productNumber;
	private String GGCode; 
	private String productDescription;
	private double shippingCost;
	private double GSTCharges;
	private double AddistionalCost; 
	private int multiplier;
	
	private String  comments;
	@Transient
	private MultipartFile poOriginalImage;
	
	@Transient
	private String poImageBase64;
	
	@Lob
	private byte[] poImageData;
	
	private String poImageName;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date updatedDate;
	
}
