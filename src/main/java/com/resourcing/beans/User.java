/* 
 * Author : Srivani Tudi */

package com.resourcing.beans;

import java.time.LocalDateTime;
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

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class User {

	// Mapping the column of branchDetilas to this table
	@ManyToOne
	@JoinColumn(name = "branch_id") // Adding the name to column
	public Branch branch;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Client> client;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId")
	public int userId;

	@Column(name = "userName")
	public String userName;

	@Column(name = "emailId")
	@NonNull
	private String emailId;

	@Column(name = "password")
	private String password;

	@Column(name = "userRole")
	private String userRole;

	@Column(name = "mobileNumber")
	private long mobileNumber;

	@Column(name = "isActive")
	private String isActive;

	@Column(name = "profilePhotolink")
	private String profilePhotolink;

	@Column(name = "createdBy")
	private int createdBy;

	@Column(name = "createdDate")
	private LocalDateTime createdDate;

	@Column(name = "updatedBy")
	private int updatedBy;

	@Column(name = "updatedDate")
	private LocalDateTime updatedDate;

}
