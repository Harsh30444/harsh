/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Education;
import com.resourcing.service.CandidateService;
import com.resourcing.service.EducationService;

@Controller
@RequestMapping("/education")
public class EducationController {

	static Logger LOGGER = Logger.getLogger(EducationController.class);

	@Autowired
	private EducationService educationService;

	@Autowired
	private CandidateService candidateService;

	// get the list of all educations of one candidate
	@GetMapping("/educationList/{candidateId}")
	public String getAllEducationsOfOneCandidate(Model model, Candidate candidate) {
		List<Education> educationList = educationService.getAllEducations(candidate);
		Comparator<Education> course = Comparator.comparing(Education::getCourse);
		List<Education> sortedList = educationList.stream().sorted(course).collect(Collectors.toList());
		model.addAttribute("educationList", sortedList);
		model.addAttribute("candidate", candidate);
		model.addAttribute("TableName", "Education details");
		return "education_list";
	}

	// Insert new education record
    @RequestMapping(value = "/saveEducation", method = RequestMethod.POST)
    public String saveEducation(@ModelAttribute("education") Education education,
            Model model,@ModelAttribute("candidate") Candidate candidate) {
        LOGGER.debug("educationform" + education.getCourse());
        education.setIsActive('Y');
        education.setCreatedDate(LocalDateTime.now());
        education.setCourse(education.getCourse().toUpperCase());
        educationService.addEducation(education);
        List<Education> educationList = educationService.getAllEducations(candidate);
		Comparator<Education> course = Comparator.comparing(Education::getCourse);
		List<Education> sortedList = educationList.stream().sorted(course).collect(Collectors.toList());
		model.addAttribute("educationList", sortedList);
        model.addAttribute("candidate", candidate);
        model.addAttribute("message", "   One education record is added!!");
        model.addAttribute("TableName", "Education details");
        return "education_list";
    }

    // add candidates to the list
    @GetMapping("/addEducation/{candidateId}")
    public String newEducation(Model model, @PathVariable("candidateId") int candidateId, Education education) {
        LOGGER.debug("add Education details method::::" + candidateId);
        model.addAttribute("education", education);
        Candidate candidate=candidateService.getCandidateById(candidateId);
        model.addAttribute("candidate", candidate);
        return "education";
    }

	@GetMapping("/updateEducation/{candidateId}/{educationId}")
	public String updateEducation(Model model, @PathVariable int candidateId, @PathVariable int educationId,
			@ModelAttribute("education") Education education, HttpSession session) {
		Education educationExist = educationService.getEducationById(education.getEducationId());
		LOGGER.debug("createddate:::::" + educationExist.getCreatedDate());
		model.addAttribute("educationExist", educationExist);
		Candidate candiidte = candidateService.getCandidateById(candidateId);
		model.addAttribute("candidate", candiidte);
		return "update_education";
	}

	// updating the education details
	@RequestMapping(value = "/updateEducationDetails", method = RequestMethod.POST)
	public String updateEducationDetails(@ModelAttribute("educationExist") Education education, Model model,
			@ModelAttribute("candidate") Candidate candidate) {
		Education existEducation = educationService.getEducationById(education.getEducationId());
		LOGGER.debug("created date new::::" + existEducation.getCreatedDate());
		education.setCreatedDate(existEducation.getCreatedDate());
		education.setUpdatedDate(LocalDateTime.now());
		education.setIsActive('Y');
		education.setCourse(education.getCourse().toUpperCase());
		educationService.updateEducation(education);
		List<Education> educationList = educationService.getAllEducations(candidate);
		Comparator<Education> course = Comparator.comparing(Education::getCourse);
		List<Education> sortedList = educationList.stream().sorted(course).collect(Collectors.toList());
		model.addAttribute("educationList", sortedList);
		model.addAttribute("candidate", candidate);
		model.addAttribute("message", " One education record is updated!!!");
		model.addAttribute("TableName", "Education details");
		return "education_list";
	}

	// Delete user record http://localhost:8080/emp/deleteEmp/2
	@GetMapping("/deleteEducation/{candidateId}/{educationId}")
	public String deleteEducation(Candidate candidate, Model model,
			@PathVariable(value = "candidateId") int candidateId,
			@PathVariable(value = "educationId") int educationId) {
		educationService.getEducationById(educationId);
		educationService.deleteEducationById(educationId);
		Candidate candidateObj = candidateService.getCandidateById(candidateId);
		List<Education> educationList = educationService.getAllEducations(candidate);
		Comparator<Education> course = Comparator.comparing(Education::getCourse);
		List<Education> sortedList = educationList.stream().sorted(course).collect(Collectors.toList());
		model.addAttribute("educationList", sortedList);
		model.addAttribute("candidate", candidateObj);
		model.addAttribute("message", "  One education record is deleted!!!");
		model.addAttribute("TableName", "Education details");
		return "education_list";
	}

}
