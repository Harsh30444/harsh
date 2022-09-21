//*author: HarshKashyap

package com.resourcing.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.resourcing.beans.InterviewPanel;

public interface InterviewPanelService {
	void addInterviewer(InterviewPanel interviewer);

	void saveInterviewer(InterviewPanel interviewer);

	void updateInterviewer(InterviewPanel interviewer);

	public InterviewPanel findByInterviewerMailIgnoreCaseAndPassword(String interviewerMail, String Password);

	InterviewPanel getInterviewerById(int interviewerId);

	InterviewPanel findByEmailId(String stringinterviewerMail);

	void saveInterviewerr(MultipartFile file, InterviewPanel interviewerProfile);

	List<InterviewPanel> getAllInterviewers();

	// ============service ========//
}
