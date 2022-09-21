//*author: HarshKashyap

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Employee;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.Schedule;

@Repository
public interface InterviewPanelRepository extends JpaRepository<InterviewPanel, Integer> {

	@Query("select u from InterviewPanel u WHERE lower(u.interviewerMail)=lower(?1) AND u.Password=?2")
	public InterviewPanel findByInterviewerMailIgnoreCaseAndPassword(String interviewerMail, String Password);

	@Query("select u from InterviewPanel u WHERE lower(u.interviewerMail)=lower(?1)")
	public InterviewPanel findByEmailId(String interviewerMail);
	
//	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.interviewer =?1")
//	List<Schedule> ScheduledList(InterviewPanel interviewerId);
}