//*author: HarshKashyap

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.InterviewerAvailability;

public interface InterviewerAvailabilityService {

	void saveInterviewerAvailability(InterviewerAvailability inter);

	void updateInterviewerAvailability(InterviewerAvailability objInter);

	InterviewerAvailability getInterviewerAvailabilityById(int interviewerAvailabilityId);

	List<InterviewerAvailability> getAllInterviewerAvailabilities(InterviewPanel interviewer);

	List<InterviewerAvailability> getAllInterviewerAvailabilities();

	void deleteInterviewerAvailabilityById(int interviewerAvailabilityId);

	List<InterviewerAvailability> availableInterviewer(int interviewerId);

	List<InterviewerAvailability> availabilityListByInterviewerId(int interviewerId, String parameter);
}
