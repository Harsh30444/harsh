/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "Employment")
@Table(name = "employment")
public class Employment {

	// mapping the candidateDetails column to this table
	@ManyToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@Id
	@Column(name = "employment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employmentId;

	@Column(name = "tool")
	private String tool;

	@Column(name = "organisation")
	private String organisation;

	@Column(name = "fromDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fromDate;

	@Column(name = "toDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate toDate;

	@Column(name = "lpa")
	private float lpa;

	@Column(name = "yearOfExperience")
	private float yearOfExperience;

	@Column(name = "isActive")
	private Character isActive;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "createdDate")
	private LocalDateTime createdDate;

	@Column(name = "updatedBy")
	private String updatedBy;

	@Column(name = "updatedDate")
	private LocalDateTime updatedDate;

	

}
