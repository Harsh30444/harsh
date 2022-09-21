
/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Employee;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;

public interface ScheduleService {
	Schedule getScheduleById(int id);

	Schedule updateSchedule(Schedule schedule);

	List<Schedule> getAllSchedules();

	void saveSchedule(Schedule schedule);

	List<Schedule> getAllSchedulesById(Employee employeeId);

	Schedule findByCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription);
	
	Schedule findByCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription, String interviewPhase);

	List<Schedule> listOfCandidateAndJdAssociation(Candidate candidate, JobDescription jobDescription);

	List<Schedule> ScheduledList(Employee employeeId);

	List<Schedule> unScheduledList(Employee employeeId);

	List<Schedule> finalFeedbackList(Employee employee);

	List<Schedule> selectedCandidateList();

	void saveFeedback(Schedule feedback);

	List<Schedule> ScheduleList(InterviewPanel interviewer);

	List<Schedule> FeedbackList(InterviewPanel interviewer);

	List<Schedule> finalFeedbackList(InterviewPanel interviewer);
	
	List<Schedule> finalFeedbackList(Candidate candidate);
	
    List<Schedule> selectedCandidatesListByEmployee(Employee employee);
    
    List<Schedule> rejectedCandidatesListByEmployee(Employee employee);

    List<Schedule> selectedJobListByCandidate(Candidate candidate);




}
