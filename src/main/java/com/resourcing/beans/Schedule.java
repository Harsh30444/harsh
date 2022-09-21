/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "schedule")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scheduleId")
	private int scheduleId;

	@ManyToOne
	@JoinColumn(name = "candidateId")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "jdId")
	private JobDescription jobDescription;

	@ManyToOne
	@JoinColumn(name = "employeeId")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "interviewerId")
	private InterviewPanel interviewPanel;

	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "status")
	private String status;

	@Column(name = "meetingLink")
	private String meetingLink;

	private String feedback;

	private String comments;
	
	private String interviewPhase;

	private String message;
}
