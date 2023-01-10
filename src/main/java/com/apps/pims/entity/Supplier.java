package com.apps.pims.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suppliers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	

	private String supplierCategory;
	private String supplierType;
	
	@Column(name = "supplier_name", nullable = false)
	private String supplierName;
	private String supplierGSTNo;
	
	private String supplierAddress;
	private String supplierEmailId;
	
	private String supplierContactNo;
	private String instagramId;
	
	private String facebookId;
	private String twitterId;
	private String website;
	
	private double registrationFee;
	private double balanceAmountWithVendor;
	
	private String bankName;
	private String bankIFSCCode;
	
	private String accountHolderName;
	private Integer accountNumber; 
	
	private String source;
	private String comments;
	
	private String otherDetails;
		
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date updatedDate;
	
	
}
