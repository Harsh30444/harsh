/**
  *@author:Praveen Gudimalla
  *
  **/



package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import com.resourcing.beans.Employment;
import com.resourcing.service.CandidateService;
import com.resourcing.service.EmploymentService;


@Controller
@RequestMapping("/employment")
public class EmploymentController {
	
	static Logger LOGGER = Logger.getLogger(CandidateController.class);
	
	@Autowired
	private EmploymentService employmentService;
	
	@Autowired
	private CandidateService candidateService;
	
	@GetMapping("/employmentList/{candidateId}")
	public String getAllEmploymentsOfOneCandidate(Model model,@PathVariable int candidateId) {
		Candidate candidate=candidateService.getCandidateById(candidateId);
		List<Employment> employmentList= employmentService.getAllEmployments(candidate);
		model.addAttribute("employmentList", employmentList);
		model.addAttribute("candidate", candidate);
		model.addAttribute("TableName", "Experience details");
		return "employment_list";
	}
	
	// Insert new education record
		@RequestMapping(value = "saveEmployment", method = RequestMethod.POST)
		public String saveEmployment(Employment employment,Model model,
				@ModelAttribute("candidate") Candidate candidate) {
			LOGGER.debug("save method entered:::");
			employment.setIsActive('Y');
			employment.setCreatedDate(LocalDateTime.now());
			employment.setTool(employment.getTool().toUpperCase());
			LOGGER.debug("date created");
			employmentService.addEmployment(employment);
			List<Employment> employmentList= employmentService.getAllEmployments(candidate);
			model.addAttribute("employmentList", employmentList);
			model.addAttribute("message", "  One Experience record is added");
			model.addAttribute("TableName", "Experience details");
			return "employment_list";
		}
		
		@GetMapping("/addEmployment/{candidateId}")
		public String newEmployment(Model model,Employment employment) {
			model.addAttribute("employment", employment);
			return "employment";
		}

		// set Education as a model attribute to pre-populate the form
		@GetMapping("/updateEmployment/{candidateId}/{employmentId}")
		public String updateEmployment(Model model,@PathVariable int candidateId,@PathVariable int employmentId,
				@ModelAttribute("employment") Employment employment) {
			Employment employmentExist=employmentService.getEmploymentById(employment.getEmploymentId());
			LOGGER.debug("createddate:::::"+employmentExist.getCreatedDate());
			model.addAttribute("employmentExist", employmentExist);
			Candidate candidate=candidateService.getCandidateById(candidateId);
			model.addAttribute("candidate", candidate);
			return "update_employment";
		}

		// updating the education details
		@RequestMapping(value = "/updateEmploymentDetails", method = RequestMethod.POST)
		public String updateEmploymentDetails(@ModelAttribute("employmentExist") Employment employment,Model model,
				@ModelAttribute("candidate") Candidate candidate) {
			Employment existEmployment=employmentService.getEmploymentById(employment.getEmploymentId());
			employment.setCreatedDate(existEmployment.getCreatedDate());
			employment.setUpdatedDate(LocalDateTime.now());
			employment.setIsActive('Y');
			employment.setTool(employment.getTool().toUpperCase());
			employmentService.updateEmployment(employment);
			List<Employment> employmentList= employmentService.getAllEmployments(candidate);
			model.addAttribute("employmentList", employmentList);
			model.addAttribute("message", "  One Experience record is updated!!");
			model.addAttribute("TableName", "Experience details");
			return "employment_list";
		}

		// Delete user record http://localhost:8080/emp/deleteEmp/2
		@GetMapping("/deleteEmployment/{candidateId}/{employmentId}")
		public String deleteEmployment( Model model,
				@PathVariable(value = "candidateId") int candidateId,
				@PathVariable(value = "employmentId") int employmentId) {
			employmentService.deleteEmploymentById(employmentId);
			Candidate candidate = candidateService.getCandidateById(candidateId);
			List<Employment> employmentList= employmentService.getAllEmployments(candidate);
			model.addAttribute("employmentList", employmentList);
			model.addAttribute("message", "  One Experience record is deleted!!");
			model.addAttribute("TableName", "Experience details");
			return "employment_list";
		}
}
