//Auther: Vineeth kumar Mudham

package com.resourcing.beans;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "BRANCH")
public class Branch {

	@OneToMany(mappedBy = ("branch"), cascade = CascadeType.ALL)
	private List<User> user;

	// Mapping the column companyId of company to this table
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id") // Adding the name to column
	private Company company;

	// linking oneBranchDetails to Many Recruiters....
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Employee> employeeObj;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<InterviewPanel> interviewPanel;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "branch_id")
	public int branchId;

	@Column(name = "branch_name")
	public String branchName;

	@Column(name = "branch_address")
	public String branchAddress;

	@Column(name = "email_id")
	public String emailId;

	@Column(name = "mobileNo")
	public long mobileNo;

	@Column(name = "isActive")
	private String isActive;

	@Column(name = "createdBy")
	public int createdBy;

	@Column(name = "updatedBy")
	private int updatedBy;

	@Column(name = "latitude")
	private double latitude;

	@Column(name = "longitude")
	private double longitude;


	@Column(name = "createdDate", insertable = false, updatable = false)
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

}
