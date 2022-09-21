/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;
import com.resourcing.service.CanSkillAssService;
import com.resourcing.service.CandidateJdAssociationService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.ScheduleService;


@Controller
public class CandidateJdAssociationController {

	static Logger LOGGER = Logger.getLogger(ClientController.class);

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private CandidateJdAssociationService candidateJdAssociationService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private CanSkillAssService canSkillAssService;

	//save the Candidate JD Association when the candidate applying for the JOB
	@GetMapping(value = "/addCJA/{id}")
	public String applyingPage(Model model, @PathVariable(value = "id") int jdId, Candidate candidate,
			@ModelAttribute("objJd") JobDescription jobDescription, CandidateJdAssociation candidateJdAssociation,
			HttpSession session) {
		LOGGER.debug("method started:::::::::;");
		Candidate candidateObj = (Candidate) session.getAttribute("candidateObj");
		//candidate need to be login for applying the JOB
		if (candidateObj != null) {
			LOGGER.debug("session is not null::::::::::");
			LOGGER.debug("id::::" + jdId);
			JobDescription job = jobDescriptionService.getJobDescriptionById(jdId);
			//Checking whether the Candidate is already applied for this job Or Not 
			CandidateJdAssociation candidateJdAssociationObj =  candidateJdAssociationService.findByCandidateIdAndJdId(candidateObj, job);
			if (candidateJdAssociationObj == null){
				LOGGER.debug("candidate not applied for this job::::::::");
				candidateJdAssociation.setCandidate(candidateObj);
				candidateJdAssociation.setCreatedBy(candidateObj.getCandidateId());
				candidateJdAssociation.setIsActive('Y');
				candidateJdAssociation.setCreatedDate(LocalDateTime.now());
				candidateJdAssociation.setJobDescription(job);
				candidateJdAssociation.setStatus("NOT SCHEDULED");
				LOGGER.debug("if condition satisfied::::");
				candidateJdAssociationService.updateCandidateJdAssociation(candidateJdAssociation);
				// this is for redirecting the list of previous location&postion page
				List<JobDescription> list = jobDescriptionService.getAllJobDescriptions();
				List<JobDescription>  jdList = list.stream().filter(obj -> obj.getSkills().contains(job.getSkills().toUpperCase().replaceAll("\s", ""))&& obj.getExp()<=job.getExp()).collect(Collectors.toList());
				
			//	List<JobDescription> jdList = jobDescriptionService
					//	.getJobDescriptionListByLocationAndPosition(job.getSkills(), job.getExp());
				LOGGER.debug("entered into getJdListForUnknown::::" + job.getLocation());
				LOGGER.debug("entered into getJdListForUnknown::::" + job.getPosition());
			//	jobDescriptionService.updateJobDescription(job);
				model.addAttribute("jdList", jdList);
				model.addAttribute("success", "applied");
				LOGGER.debug("if condition satisfied");
				return "jobs-list-layout-full-page-map";
			} else {
				LOGGER.debug("else condition working:::");
				LOGGER.debug("You are already applied for this job");
				model.addAttribute("message", "You are already applied for this job");
				List<JobDescription> list = jobDescriptionService.getAllJobDescriptions();
				List<JobDescription>  jdList = list.stream().filter(obj -> obj.getSkills().contains(job.getSkills().toUpperCase().replaceAll("\s", ""))&& obj.getExp()<=job.getExp()).collect(Collectors.toList());
				LOGGER.debug("entered into getJdListForUnknown::::" + job.getLocation());
				LOGGER.debug("entered into getJdListForUnknown::::" + job.getPosition());
				model.addAttribute("jdList", jdList);
				model.addAttribute("message", "already applied");
				LOGGER.debug("else condition satisfied");
				return "jobs-list-layout-full-page-map";
			}
		} else {
			LOGGER.debug("final else condition::::::");
			model.addAttribute("invalid", "you are unable to apply for this job please login as candidate!!");
			model.addAttribute("candidate", candidate);
			return "candidate_pages_login";
		}
	}
	
	//List of Jobs applied by the Candidate
	@GetMapping("/appliedJdList/{candidateId}")
	public String jdListAppliedByCandidate(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> associationList = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> filteredList = associationList.stream().
				filter(list -> list.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		ArrayList<JobDescription> jdList = new ArrayList<>();
		for (CandidateJdAssociation list: filteredList) {
			JobDescription jd = jobDescriptionService.getJobDescriptionById(list.getJobDescription().getJdId());
			jdList.add(jd);
		}
		model.addAttribute("jdList", jdList);
		return "candidate_jd_list";
	}
	
	
	
	//Feedback of Job
	@GetMapping("/feebackOfJd/{jdId}")
	public String feedBackOfJdByCandidate(@PathVariable int jdId,HttpSession session,Model model) {
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		Candidate candidate = (Candidate) session.getAttribute("candidateObj");
		Schedule schedule = scheduleService.findByCandidateIdAndJdId(candidate, jobDescription);
		if(schedule!=null) {
			List<CandidateJdAssociation> associationList = candidateJdAssociationService.getAllCandidateJdAssociations();
			List<CandidateJdAssociation> filteredList = associationList.stream().
					filter(list -> list.getCandidate().getCandidateId()==candidate.getCandidateId()).collect(Collectors.toList());
			ArrayList<JobDescription> jdList = new ArrayList<>();
			for (CandidateJdAssociation list: filteredList) {
				JobDescription jd = jobDescriptionService.getJobDescriptionById(list.getJobDescription().getJdId());
				jdList.add(jd);
			}
			model.addAttribute("jdList", jdList);
			model.addAttribute("schedule", schedule);
			model.addAttribute("feedback", schedule.getFeedback());
			return "candidate_jd_list";
		}
		else {
			List<CandidateJdAssociation> associationList = candidateJdAssociationService.getAllCandidateJdAssociations();
			List<CandidateJdAssociation> filteredList = associationList.stream().
					filter(list -> list.getCandidate().getCandidateId()==candidate.getCandidateId()).collect(Collectors.toList());
			ArrayList<JobDescription> jdList = new ArrayList<>();
			for (CandidateJdAssociation list: filteredList) {
				JobDescription jd = jobDescriptionService.getJobDescriptionById(list.getJobDescription().getJdId());
				jdList.add(jd);
			}
			model.addAttribute("jdList", jdList);
			model.addAttribute("feedback", "your interview schedule is in progress for the position of "+jobDescription.getPosition());
			return "candidate_jd_list";
		}
	}

	
	
	//testing only..
	@GetMapping("/check/{candidateId}")
	public String check(@PathVariable int candidateId) {
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(56);
		List<CandidateSkillAssociation> list = canSkillAssService.findSkillListByCandidateId(candidateId);
		List<String> check = list.stream().map(CandidateSkillAssociation::getSkillName).collect(Collectors.toList());
		String jdSkills = jobDescription.getSkills();
		String[] itemsLimits = jdSkills.split(",");
		List<String> jdSkillslist = Arrays.asList(itemsLimits);
		System.out.println("candidate::::"+check.toString());
		System.out.println("jd:::::::::::"+jdSkillslist.toString());
		System.out.println(jdSkillslist.stream().anyMatch(check::contains));
		return "redirect:/candidate/dashboard/"+candidateId;
	}

	
	
	
	
	

}
