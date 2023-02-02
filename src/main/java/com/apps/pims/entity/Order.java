package com.apps.pims.entity;

import java.util.Date;

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
	
	
	//sum of all products qty which are there in a purchase order
	private Integer quantity;
	private double shippingCost;
	private double GSTCharges;
	private double additionalCost; 
	
	private double shippingWeight;
	private double shippingAmountPerKg;
	private double totalCost;
	private String otherDetails;
	private String  comments;
	private String shippingAddress;
	private String carrier;
	private String shippedBy;
	//@Temporal(TemporalType.DATE)
	//@DateTimeFormat(pattern = "yyyy/MM/dd")
	//private Date shippedDate;
	private String shippedPersonEmail;
	private String shippedPersonContactNo;

	
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
