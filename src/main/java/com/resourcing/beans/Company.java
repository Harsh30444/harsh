// Auther: Vineeth Kumar Mudham.

package com.resourcing.beans;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company")
@Setter
@Getter
public class Company {

	@OneToMany(mappedBy = ("company"), cascade = CascadeType.ALL)
	private List<Branch> branch;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "company_id")
	private int companyId;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "company_address")
	private String address;

	@Column(name = "email")
	private String email;

	@Column(name = "mobileNumber")
	private long mobileNumber;

	@Column(name = "year_Of_Established")
	private int yearOfEstablished;

	private String isActive;

	@Column(name = "createdBy")
	private int createdBy;

	private LocalDateTime createdDate;

	@Column(name = "updatedBy")
	private int updatedBy;

	private LocalDateTime updatedDate;

	@Column(name = "latitude")
	private double latitude;
	
	@Column(name = "longitude")
	private double longitude;


	

}