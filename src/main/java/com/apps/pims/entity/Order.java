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
import lombok.ToString;

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
	private String supplierName;
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
	private double multiplier;
	private double totalCost;
	private String otherDetails;
	private String  comments;
	@Transient
	private MultipartFile poOriginalImage;
	
	@Transient
	@ToString.Exclude
	private String poImageBase64;
	
	@Lob
	@ToString.Exclude
	private byte[] poImageData;
	
	private String poImageName;
	
	@Transient
	private MultipartFile orderReceiptImage;
	
	@Transient
	@ToString.Exclude
	private String orderReceiptImageBase64;
	
	@Lob
	@ToString.Exclude
	private byte[] orderReceiptImageData;
	
	private String orderReceiptImageName;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date updatedDate;
	
}
