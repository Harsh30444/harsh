
/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Employee;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;
import com.resourcing.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Override
	public List<Schedule> getAllSchedules() {
		return scheduleRepository.findAll();
	}

	@Override
	public void saveSchedule(Schedule schedule) {
		scheduleRepository.save(schedule);
	}

	@Override
	public List<Schedule> getAllSchedulesById(Employee employeeId) {
		return scheduleRepository.findAllSchedulesById(employeeId);
	}

	@Override
	public Schedule findByCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription) {
		return scheduleRepository.checkPairOfCandidateIdAndJdId(candidate, jobDescription);
	}

	@Override
	public Schedule getScheduleById(int id) {
		Optional<Schedule> optional = scheduleRepository.findById(id);
		Schedule schedule = null;
		if (optional.isPresent()) {
			schedule = optional.get();
		} else {
			throw new RuntimeException("repository:::: Schedule not found for id:::" + id);
		}

		return schedule;	
		}

	@Override
	public Schedule updateSchedule(Schedule schedule) {
		return scheduleRepository.save(schedule);
	}

	@Override
	public List<Schedule> ScheduledList(Employee employeeId) {
		return scheduleRepository.ScheduledList(employeeId);
	}

	@Override
	public List<Schedule> unScheduledList(Employee employeeId) {
		return scheduleRepository.unScheduledList(employeeId);
	}

	@Override
	public List<Schedule> finalFeedbackList(Employee employee) {
		return scheduleRepository.finalFeedbackList(employee);
	}

	@Override
	public List<Schedule> selectedCandidateList() {
		return scheduleRepository.selectedCandidateList();
	}

	@Override
	public void saveFeedback(Schedule feedback) {
		scheduleRepository.save(feedback);
	}

	@Override
	public List<Schedule> ScheduleList(InterviewPanel interviewer) {
		return scheduleRepository.ScheduledList(interviewer);
	}

	@Override
	public List<Schedule> FeedbackList(InterviewPanel interviewer) {
		return scheduleRepository.FeedbackList(interviewer);
	}

	@Override
	public List<Schedule> finalFeedbackList(InterviewPanel interviewer) {
		return scheduleRepository.ScheduledList(interviewer);
	}

	@Override
	public List<Schedule> finalFeedbackList(Candidate candidate) {
		return scheduleRepository.finalFeedbackList(candidate);
	}

	
	@Override
	public List<Schedule> selectedCandidatesListByEmployee(Employee employee) {
		return scheduleRepository.selectedCandidatesListByEmployee(employee);
	}

	@Override
	public List<Schedule> rejectedCandidatesListByEmployee(Employee employee) {
		return scheduleRepository.rejectedCandidatesListByEmployee(employee);
	}

	@Override
	public List<Schedule> selectedJobListByCandidate(Candidate candidate) {
		return scheduleRepository.selectedJobListByCandidate(candidate);
	}

	@Override
	public Schedule findByCandidateIdAndJdId(Candidate candidate, JobDescription jobDescription,
			String interviewPhase) {
				return scheduleRepository.checkPairOfCandidateIdAndJdId(candidate, jobDescription, interviewPhase);
	}

	@Override
	public List<Schedule> listOfCandidateAndJdAssociation(Candidate candidate, JobDescription jobDescription) {
		return scheduleRepository.listOfCandidateAndJdAssociation(candidate, jobDescription);
	}

}
