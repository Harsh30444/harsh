// Author:Vineeth Kumar Mudham 
package com.resourcing.beans;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "adminUser")
@Table(name = "adminUser")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AdminUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "admin_id")
	public int adminId;

	@Column(name = "AdminName")
	private String adminName;

	private String emailId;

	@Column(name = "password")

	private String password;

	private long mobileNo;

	@Column(name = "profilePhotolink")
	private String profilePhotolink;

	private String captcha;

	private String userCaptcha;

	@Column(name = "isActive")
	private String isActive;


	@Column(name = "createdBy", nullable = false)
	private int createdBy;

	@Basic(optional = false)
	private LocalDateTime createdDate;

	

	@Column(name = "updatedBy", nullable = true)
	private String updatedBy;

	private LocalDateTime updatedDate;

	

}
