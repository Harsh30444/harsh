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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "EMPLOYEE_CLIENT_ASSOCIATION")
public class EmployeeClientAssociation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ECA_ID")
	private int ecaId; // Primary Key

	
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Column(name = "isActive")
	private char isActive;
	
	@Column(name = "createdby")
	private int createdby;
	
	@Column(name = "createdDate")
	// @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime createdDate; // DEFAULT now(),

	@Column(name = "updatedby")
	private int updatedby; // character varying(45),

	@Column(name = "updatedDate")
	// @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime updatedDate;

}
