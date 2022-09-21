/** ======================
 * @author Nikhil Gundla
 * ======================*/
package com.resourcing.beans;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Client")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ClientId")
	private int clientId;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private List<JobDescription> jobDescription;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private List<EmployeeClientAssociation> employeeCientAssociation;

	@Column(name = "ClientName")
	private String clientName;

	@Column(name = "MobileNo")
	private long mobile;

	@Column(name = "Email")
	private String email;

	@Column(name = "ClientCompany")
	private String clientCompany;

	@Column(name = "CompanyProfile")
	private String companyProfile;

	@Column(name = "Password")
	private String password;

	@Column(name = "isActive")
	private String isActive;

	@Column(name = "createdby")
	private int createdby;

	@Column(name = "createdDate")
	private LocalDateTime createdDate;

	@Column(name = "updatedby")
	private int updatedby; 

	@Column(name = "updatedDate")
	private LocalDateTime updatedDate;

	private String captcha;

	private String clientCaptcha;

	
	@Lob
	@Column(name = "image")
	private String image;
	

}
