//*author: HarshKashyap

package com.resourcing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity(name = "InterviewerAvailability")
@Table(name = "interviewer_availability")
public class InterviewerAvailability {

	public InterviewPanel getInterviewPanel() {
		return interviewPanel;
	}

	public void setInterviewPanel(InterviewPanel interviewPanel) {
		this.interviewPanel = interviewPanel;
	}

	@ManyToOne
	@JoinColumn(name = "interviewer_id")
	private InterviewPanel interviewPanel;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
	@Column(name = "interviewerAvailabilityId")
	private int InterviewerAvailabilityId;

	private String fromDate;
	private String toDate;
	private String slot;

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public int getInterviewerAvailabilityId() {
		return InterviewerAvailabilityId;
	}

	public void setInterviewerAvailabilityId(int interviewerAvailabilityId) {
		InterviewerAvailabilityId = interviewerAvailabilityId;
	}

}
