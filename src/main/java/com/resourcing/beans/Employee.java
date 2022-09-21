
/* 
 * Author : Srivani Tudi */

package com.resourcing.beans;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Employee {

	// Mapping the column of branchDetilas to this table
	@ManyToOne
	@JoinColumn(name = "branch_id") // Adding the name to column
	public Branch branch;

	// linking Candidates to Recruiters...
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Candidate> candidateSet;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<EmployeeClientAssociation> employeeClientAssociation;

	// linking employee to skills ...
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Skill> skillSet;

	// linking Employee to education...
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Education> educationSet;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "employee_id")
	public int employeeId;

	@Column(name = "employeeName")
	private String employeeName;

	@Column(name = "employee_role")
	private String employeeRole;

	@Column(name = "email_id")
	@NonNull
	private String emailId;

	@Column(name = "password")
	private String password;

	@Column(name = "mobileNo")
	private long mobileNo;


	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;
	
	private String isActive;
	
	private int createdBy;
	
	private int updatedBy;
	
	@Lob
	@Column(name = "image")
	private String image;


}
