package com.resourcing.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "INTERVIEWFEEDBACK")
@Data
public class InterviewFeedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feedbackId")
	private int feedbackId;

	@Column(name = "status")
	private String status;

	@Column(name = "isActive")
	private char isActive;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "createdDate")
	@DateTimeFormat(pattern = "dd-mm-yyyy'T'HH:mm")
	private Timestamp createDate;

	@Column(name = "updatedBy")
	private String updatedBy;

	@Column(name = "updatedDate")
	@DateTimeFormat(pattern = "dd-mm-yyyy'T'HH:mm")
	private Timestamp updatedDate;

}
