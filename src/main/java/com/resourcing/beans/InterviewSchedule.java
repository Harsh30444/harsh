package com.resourcing.beans;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "INTERVIEWSCHEDULE")
@Data
public class InterviewSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "interviewScheduleId")
	private int interviewScheduleId;

	@Column(name = "availableDate")
	private Date availableDate;

	@Column(name = "availableTime")
	private Time availableTime;

	@Column(name = "typeOfInterview")
	private String typeOfInterview;

	@Column(name = "venue")
	private String venue;

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
