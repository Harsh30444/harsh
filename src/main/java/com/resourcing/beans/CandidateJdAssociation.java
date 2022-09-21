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
@Table(name = "candidate_jd_association")
@Entity(name = "CandidateJdAssociation")
public class CandidateJdAssociation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_jd_association_id")
	private int candidateJdAssociationId;

	@ManyToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "jd_id")
	private JobDescription jobDescription;

	@Column(name = "is_active")
	private Character isActive;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "updated_by")
	private int updatedBy;
	
	private String status;

	

}
