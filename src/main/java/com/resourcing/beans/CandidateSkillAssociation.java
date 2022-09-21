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
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "CANDIDATE_SKILL_ASSOCIATION")
public class CandidateSkillAssociation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CandidateSkillAssociation_ID")
	private int candidateSkillAssociationId; // Primary Key

	@Column(name = "CANDIDATE_ID")
	private int candidateId;

	@Column(name = "CANDIDATE_NAME")
	private String candidateName;

	@Column(name = "SKILL_ID")
	private int skillId;

	@Column(name = "SKILL_NAME")
	private String skillName;

	@Column(name = "isActive")
	private char isActive;

	@Column(name = "createdby")
	private int createdby;

	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime createdDate; // DEFAULT now(),

	@Column(name = "updatedby")
	private int updatedby; // character varying(45),

	@Column(name = "updatedDate")
	// @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime updatedDate;

}
