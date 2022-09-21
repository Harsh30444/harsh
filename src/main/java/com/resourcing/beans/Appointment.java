

package com.resourcing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "appointment_id")
	private long appointmentId;

	@Column(name = "appointment_validity")
	private static int appointmentValidity = 4;

//	@Column(name = "patient_purpose")
//	private String patientPurpose;

	public InterviewPanel getInterviewPanel() {
		return interviewPanel;
	}

	public void setInterviewPanel(InterviewPanel interviewPanel) {
		this.interviewPanel = interviewPanel;
	}

	@Column(name = "slot")
	private String slot;
	
	private String link;
	
//	private int hospitalBranchId;
	

//	private String consultantType;
//	private String paymentStatus;
	
	@Column(name="date")
	private String date;

	// Child
	// For patient name,age,gender from Patient
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "patientId")
//	private Patients patient;

	// Child
	// For patient name,age,gender from Patient
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interviewerId")
	public InterviewPanel interviewPanel;

	private char isactive;
	private String createdBy;
	private String updateBy;
//	private Date updated;
//	private Date created;

	// Parant
	// For HospitalBranch name/location, Doctor Name,patient (name,purpose,age) to
	// Prescription
//	@OneToMany(mappedBy = "appointment")
//	private Set<Prescription> prescription = new HashSet<Prescription>();

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
	
//	public int getHospitalBranchId() {
//		return hospitalBranchId;
//	}
//
//	public void setHospitalBranchId(int hospitalBranchId) {
//		this.hospitalBranchId = hospitalBranchId;
//	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

//	public String getConsultantType() {
//		return consultantType;
//	}
//
//	public void setConsultantType(String consultantType) {
//		this.consultantType = consultantType;
//	}

	public static int getAppointmentValidity() {
		return appointmentValidity;
	}

	public static void setAppointmentValidity(int appointmentValidity) {
		Appointment.appointmentValidity = appointmentValidity;
	}

//	public String getPatientPurpose() {
//		return patientPurpose;
//	}
//
//	public void setPatientPurpose(String patientPurpose) {
//		this.patientPurpose = patientPurpose;
//	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

//	public Doctor getDoctor() {
//		return doctor;
//	}
//
//	public void setDoctor(Doctor doctor) {
//		this.doctor = doctor;
//	}
//
//	public Patients getPatient() {
//		return patient;
//	}
//
//	public void setPatient(Patients patient) {
//		this.patient = patient;
//	}

	public char getIsactive() {
		return isactive;
	}

	public void setIsactive(char isactive) {
		this.isactive = isactive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

//	public Date getUpdated() {
//		return updated;
//	}
//
//	public void setUpdated(Date updated) {
//		this.updated = updated;
//	}
//
//	public Date getCreated() {
//		return created;
//	}
//
//	public void setCreated(Date created) {
//		this.created = created;
//	}



//	public String getPaymentStatus() {
//		return paymentStatus;
//	}
//
//	public void setPaymentStatus(String paymentStatus) {
//		this.paymentStatus = paymentStatus;
//	}
	

}
