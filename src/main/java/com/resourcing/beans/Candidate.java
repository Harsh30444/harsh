/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.beans;

import java.time.LocalDate;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "Candidate")
@Table(name = "candidate")
public class Candidate {

	@Id
	@Column(name = "candidate_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int candidateId;

	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
	private Set<Education> education;

	@OneToMany(mappedBy = "candidate")
	private Set<Employment> employment;
	
	@OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
	private List<CandidateJdAssociation> candidateJdAssociation;

	@OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
	private List<Schedule> schedule;

	// this is for assigning candidates to the employee
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employeeObj;

	@NonNull
	@Column(name = "first_name")
	private String firstName;

	@NonNull
	@Column(name = "last_name")
	private String lastName;

	@NonNull
	@Column(name = "email")
	private String email;

	@NonNull
	@Column(name = "password")
	private String password;

	private String gender;

	@Column(name = "mobile_number")
	private long mobileNumber;

	@Column(name = "address")
	private String address;

	@Column(name = "is_active")
	private Character isActive;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dateOfBirth")
	private LocalDate dateOfBirth;

	@Column(name = "age")
	private String age;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Lob
	@Column(name = "image")
	private String image;

	@Column(name = "about")
	private String about;

	private String captcha;

	private String userCaptcha;


}
