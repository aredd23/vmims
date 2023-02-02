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
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Products {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long orderId;
	@Column(name = "product_name", nullable = false)
	private String productName;
	private String productCategory;
	private String productNumber;
	private String productDescription;
	
	private double price;
	private Integer quantity;
	private Integer soldQty;
	//formula: quantity-soldQty
	private Integer balanceQty;
	
	private String GGCode; 

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

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date updatedDate;
	

}
