//*author: HarshKashyap

package com.resourcing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.InterviewerAvailability;
import com.resourcing.repository.InterviewerAvailabilityRepository;

@Service
public class InterviewerAvailabilityServiceImpl implements InterviewerAvailabilityService {

	@Autowired
	InterviewerAvailabilityRepository interRepository;

	@Override
	public void saveInterviewerAvailability(InterviewerAvailability inter) {
		this.interRepository.save(inter);

	}

	@Override
	public void updateInterviewerAvailability(InterviewerAvailability objInter) {
		this.interRepository.save(objInter);

	}

	@Override
	public InterviewerAvailability getInterviewerAvailabilityById(int interviewerAvailabilityId) {
		return interRepository.findById(interviewerAvailabilityId).get();
	}

	@Override
	public List<InterviewerAvailability> getAllInterviewerAvailabilities() {
		return interRepository.findAll();
	}

	@Override
	public List<InterviewerAvailability> getAllInterviewerAvailabilities(InterviewPanel interviewer) {
		return interRepository.findAllInterviewerAvailability(interviewer);
	}

	@Override
	public void deleteInterviewerAvailabilityById(int interviewerAvailabilityId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InterviewerAvailability> availableInterviewer(int interviewerId) {
		// TODO Auto-generated method stub
		return interRepository.availableInterviewer(interviewerId);	}

	@Override
	public List<InterviewerAvailability> availabilityListByInterviewerId(int interviewerId, String parameter) {
		// TODO Auto-generated method stub
		return interRepository.findAll();
	}

}
