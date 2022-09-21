//*author: HarshKashyap
package com.resourcing.beans;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "InterviewPanel")
@AllArgsConstructor

@NoArgsConstructor
public class InterviewPanel {

	@OneToMany(mappedBy = "interviewPanel")
	private Set<InterviewerAvailability> interviewerAvailability;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int interviewerId;

	private String interviewerName;

	private long interviewerMobileNo;

	public String interviewerMail;
	
	
	@OneToMany(mappedBy = "interviewPanel")
	private List<Schedule> schedule;

	@ManyToOne
	@JoinColumn(name = "skill_id")
	private Skill skill;

	@Column(name = "password")
	@NonNull
	private String Password;

	@Column(name = "Designation")
	private String designation;

	@Column(name = "Technology")
	private String technology;

	private String isActive;

	private int createdby;

	public String experienceInYears;

	private String previousCompany;

	private String skills;

	private String certifications;

	@Lob
	@Column(name = "prof_pic")
	private String profPic;
	

	@Column(name = "userCaptcha")
	private String userCaptcha;

	@Column(name = "captcha")
	private String captcha;

	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime createdDate;

	private int updatedby;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
	private LocalDateTime updatedDate;

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Set<InterviewerAvailability> getInterviewerAvailability() {
		return interviewerAvailability;
	}

	public void setInterviewerAvailability(Set<InterviewerAvailability> interviewerAvailability) {
		this.interviewerAvailability = interviewerAvailability;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public long getInterviewerMobileNo() {
		return interviewerMobileNo;
	}

	public void setInterviewerMobileNo(long interviewerMobileNo) {
		this.interviewerMobileNo = interviewerMobileNo;
	}

	public String getInterviewerMail() {
		return interviewerMail;
	}

	public void setInterviewerMail(String interviewerMail) {
		this.interviewerMail = interviewerMail;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(int updatedby) {
		this.updatedby = updatedby;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getInterviewerId() {
		return interviewerId;
	}

	public void setInterviewerId(int interviewerId) {
		this.interviewerId = interviewerId;
	}

	public String getPreviousCompany() {
		return previousCompany;
	}

	public void setPreviousCompany(String previousCompany) {
		this.previousCompany = previousCompany;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getCertifications() {
		return certifications;
	}

	public void setCertifications(String certifications) {
		this.certifications = certifications;
	}

	public String getExperienceInYears() {
		return experienceInYears;
	}

	public void setExperienceInYears(String experienceInYears) {
		this.experienceInYears = experienceInYears;
	}

	public String getProfPic() {
		return profPic;
	}

	public void setProfPic(String profPic) {
		this.profPic = profPic;
	}

	public String getUserCaptcha() {
		return userCaptcha;
	}

	public void setUserCaptcha(String userCaptcha) {
		this.userCaptcha = userCaptcha;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}
