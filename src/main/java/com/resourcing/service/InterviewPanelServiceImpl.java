//*author: HarshKashyap

package com.resourcing.service;

import java.util.Base64;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.resourcing.beans.InterviewPanel;
import com.resourcing.controller.CandidateController;
import com.resourcing.repository.InterviewPanelRepository;

@Service
public class InterviewPanelServiceImpl implements InterviewPanelService {

	static Logger LOGGER = Logger.getLogger(CandidateController.class);
	
	@Autowired
	InterviewPanelRepository interviewerRepository;

	@Override
	public void saveInterviewer(InterviewPanel interviewer) {
		this.interviewerRepository.save(interviewer);

	}

	@Override
	public void addInterviewer(InterviewPanel interviewer) {
		this.interviewerRepository.save(interviewer);

	}

	@Override
	public void updateInterviewer(InterviewPanel interviewer) {
		// TODO Auto-generated method stub
		this.interviewerRepository.save(interviewer);
	}

	@Override
	public InterviewPanel findByInterviewerMailIgnoreCaseAndPassword(String interviewerMail, String Password) {
		return interviewerRepository.findByInterviewerMailIgnoreCaseAndPassword(interviewerMail, Password);
	}

	@Override
	public InterviewPanel getInterviewerById(int interviewerId) {
		return interviewerRepository.findById(interviewerId).get();
	}

	@Override
	public InterviewPanel findByEmailId(String interviewerMail) {
		return interviewerRepository.findByEmailId(interviewerMail);
	}
	// ==============serviceImpl======//

	@Override
	public void saveInterviewerr(MultipartFile file, InterviewPanel interviewerProfile) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		LOGGER.debug(fileName);
		if (fileName.contains("..")) {
			LOGGER.debug("not a valid file");
		}
		try {
			interviewerProfile.setProfPic(Base64.getEncoder().encodeToString(file.getBytes()));
			LOGGER.debug(interviewerProfile.getProfPic());

		} catch (Exception e) {
			e.printStackTrace();
		}
		interviewerRepository.save(interviewerProfile);
	}

	@Override
	public List<InterviewPanel> getAllInterviewers() {
		return interviewerRepository.findAll();
	}

}
