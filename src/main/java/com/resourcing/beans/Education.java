/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.beans;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "Education")
@Table(name = "education")
public class Education {

	// mapping the candidateDetails column to this table
	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = true)
	private Candidate candidate;

	@Id
	@Column(name = "education_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int educationId;

	@Column(name = "candidate_name")
	private String candidateName;

	@Column(name = "course")
	private String course;

	@Column(name = "instistution")
	private String instistution;

	@Column(name = "university")
	private String university;

	@Column(name = "year")
	private int year;

	@Column(name = "percentage")
	private float percentage;

	@Column(name = "isActive")
	private Character isActive;

	@Column(name = "createdBy")
	private int createdBy;

	@Column(name = "createdDate")
	private LocalDateTime createdDate;

	@Column(name = "updatedBy")
	private int updatedBy;
	

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	

}
