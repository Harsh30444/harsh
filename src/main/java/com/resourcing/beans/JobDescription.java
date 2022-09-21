/**
 * @author Nikhil Gundla
 *
 */
package com.resourcing.beans;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "JOB_DESCRIPTION")
public class JobDescription {
	
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@OneToMany(mappedBy = "jobDescription",cascade = CascadeType.ALL)
	private List<CandidateJdAssociation> candidateJdAssociation;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "JD_ID")
	private int jdId; // Primary Key

	@Column(name = "PROCESS")
	private String process;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "POSITION")
	private String position;

	@Column(name = "lpa")
	private float lpa;

	@Column(name = "VACANCIES")
	private int vacancies;
	
	@Column(name = "filled", columnDefinition = "int default 0 ")
	private int filled;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "SKILLS")
	private String skills;

	@Column(name = "QUALIFICATION")
	private String qualification;

	@Column(name = "YEAR_OF_PASSING")
	private int yearOfPassing;

	@Column(name = "SHIFTS")
	private String shifts;

	@Column(name = "EXPERIENCE")
	private float exp;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "createdby")
	private int createdby;

	@Column(name = "updatedby")
	private int updatedby;

	@Column(name = "updatedDate")
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private LocalDate updatedDate;

	@Column(name = "isActive")
	private char isActive;
	
	@Column(name="created_date")
	private String createdDate;
	
	@Column(name="interview_phase")
	private String interviewPhase;

}
