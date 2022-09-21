/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Employee;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.employee =?1")
	List<Schedule> findAllSchedulesById(Employee employeeId);

	@Query("select a from Schedule a WHERE a.candidate=?1 AND a.jobDescription=?2")
	public Schedule checkPairOfCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription);
	
	@Query("select a from Schedule a WHERE a.candidate=?1 AND a.jobDescription=?2")
	List<Schedule> listOfCandidateAndJdAssociation(Candidate candidate, JobDescription jobDescription);
	
	@Query("select a from Schedule a WHERE a.candidate=?1 AND a.jobDescription=?2 AND lower(a.interviewPhase)=lower(?3)")
	public Schedule checkPairOfCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription,String interviewPhase);

	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NULL AND a1.employee =?1")
	List<Schedule> unScheduledList(Employee employeeId);

	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.employee =?1")
	List<Schedule> ScheduledList(Employee employeeId);
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.interviewPanel =?1")
	List<Schedule> ScheduledList(InterviewPanel interviewer);
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.employee =?1")
    List<Schedule> finalFeedbackList(Employee employee);
	
	@Query(value =" SELECT a1 FROM Schedule a1 WHERE a1.feedback='SELECTED'")
    List<Schedule> selectedCandidateList();
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.interviewPanel =?1 AND a1.feedback='PENDING'")
    List<Schedule> FeedbackList(InterviewPanel interviewer);
	
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.meetingLink is NOT NULL AND a1.candidate =?1 ")
    List<Schedule> finalFeedbackList(Candidate candidate);
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.employee=?1 AND a1.feedback='SELECTED'")
    List<Schedule> selectedCandidatesListByEmployee(Employee employee);
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.employee=?1 AND  a1.feedback='REJECTED'")
    List<Schedule> rejectedCandidatesListByEmployee(Employee employee);
	
	
	@Query(value = "SELECT a1 FROM Schedule a1 WHERE a1.candidate=?1 AND a1.feedback='SELECTED'")
	List<Schedule> selectedJobListByCandidate(Candidate candidate);

}
