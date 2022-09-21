/**
  *	
  *@author:Praveen Gudimalla
  *
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.beans.Education;
import com.resourcing.beans.Skill;
import com.resourcing.service.CanSkillAssService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.SkillService;

@Controller
@RequestMapping("/csa")
public class CanSkillAssController {

	static Logger LOGGER = Logger.getLogger(CanSkillAssController.class);

	@Autowired
	private SkillService skillService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CanSkillAssService canSkillAssociationService;

	// Registration Page for Client
	@GetMapping("/addSkills/{id}")
	public String getSkillsPage(Model model, @PathVariable("id") int candidateId, Candidate candidate,
			CandidateSkillAssociation csa) {
		Candidate objCandidate = candidateService.getCandidateById(candidateId);
		model.addAttribute("candidate", objCandidate);
		List<Skill> list = skillService.getAllSkills();
		model.addAttribute("list", list);
		return "candidate_add_skill";
	}

	@PostMapping(value = "/saveSkills/{id}")
	public String getCsaList(Model model, @PathVariable("id") int candidateId,
			@ModelAttribute("csa") CandidateSkillAssociation csa, @ModelAttribute("list") Skill skill,
			@ModelAttribute("candidate") Candidate candidate) {
		Candidate objCandidate = candidateService.getCandidateById(candidateId);
		model.addAttribute("candidate", objCandidate);
		LOGGER.debug("Inside eca saving eca METHOD candiadte Id::::::" + csa.getCandidateId());
		LOGGER.debug("Inside eca saving eca METHOD skill Name::::::" + skill.getSkillName());
		csa.setSkillId(skill.getSkillId());
		csa.setSkillName(skill.getSkillName());
		csa.setCandidateId(objCandidate.getCandidateId());
		LOGGER.debug("csa" + csa.getCandidateId());
		csa.setIsActive('Y');
		csa.setCreatedby(objCandidate.getCandidateId());
		csa.setCandidateName(objCandidate.getFirstName());
		csa.setCreatedDate(LocalDateTime.now());
		if (canSkillAssociationService.findBySkillIdAndCanId(candidateId, csa.getSkillId()) == null) {
			LOGGER.debug("skill id:::::" + csa.getSkillId());
			canSkillAssociationService.addNewCsa(csa);
			LOGGER.debug("skill not added");
			List<CandidateSkillAssociation> csaList = canSkillAssociationService
					.findSkillListByCandidateId(objCandidate.getCandidateId());
			model.addAttribute("csaList", csaList);
			model.addAttribute("message", csa.getSkillName() + " skill is added to you");
			model.addAttribute("TableName", "Your Skill");
			return "candidate_skill_list";
		} else {
			Candidate candidateObj = candidateService.getCandidateById(candidateId);
			model.addAttribute("candidate", candidateObj);
			List<Skill> list = skillService.getAllSkills();
			model.addAttribute("list", list);
			model.addAttribute("message", csa.getSkillName() + "  is already added");
			return "candidate_add_skill";
		}
	}

	@GetMapping("/skillList/{candidateId}")
	public String getAllCandidatesSkillList(Model model, @PathVariable int candidateId) {
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<CandidateSkillAssociation> skilllList = canSkillAssociationService.findSkillListByCandidateId(candidateId);
		model.addAttribute("csaList", skilllList);
		model.addAttribute("TableName", "Your Skill");
		LOGGER.debug("success:::");
		return "candidate_skill_list";
	}

	@RequestMapping(value = "/updateSkills/{candidateId}/{skillId}", method = RequestMethod.POST)
	public String updateEducationDetails(@ModelAttribute("educationExist") Education education, Model model,
			CandidateSkillAssociation association, @PathVariable(value = "candidateId") int candidateId,
			@PathVariable(value = "skillId") int skillId) {
		CandidateSkillAssociation existSkill = canSkillAssociationService
				.getAssociationById(association.getCandidateSkillAssociationId());
		LOGGER.debug("created date new::::" + existSkill.getCreatedDate());
		association.setCreatedDate(existSkill.getCreatedDate());
		association.setUpdatedDate(LocalDateTime.now());
		association.setIsActive('Y');
		canSkillAssociationService.updateCandidateSkill(association);
		return "redirect:/csa/skillList/" + candidateId;
	}

	@GetMapping("/deleteSkills/{candidateId}/{associationId}")
	public ModelAndView deleteSkill(CandidateSkillAssociation association, Model model,
			@PathVariable(value = "candidateId") int candidateId,
			@PathVariable(value = "associationId") int associationId) {
		LOGGER.debug("id:::" + association.getCandidateSkillAssociationId());
		LOGGER.debug("id2::" + associationId);
		ModelAndView mav = new ModelAndView("candidate_skill_list");
		mav.addObject("message", "one skill record is deleted!!");
		canSkillAssociationService.deleteCandidateSkillAssociationBySkillId(associationId);
		List<CandidateSkillAssociation> skilllList = canSkillAssociationService.findSkillListByCandidateId(candidateId);
		mav.addObject("csaList", skilllList);
		mav.addObject("TableName", "Your Skill");
		return mav;
	}

	@GetMapping("/listOfSkills")
	public String listOfSkills(CandidateSkillAssociation candidateSkillAssociation, Model model) {
		LOGGER.debug("all skills list::::::");
		List<Skill> list = skillService.getAllSkills();
		LOGGER.debug("list::::" + list);
		model.addAttribute("list", list);
		return "skill_list";
	}

	// list of candidates for the one particular skill
	@GetMapping("/candidatesList")
	public String listOfCandidatesBySkillId(Model model, @ModelAttribute("list") Skill skill) {
		LOGGER.debug("skill Id:::" + skill.getSkillId());
		List<CandidateSkillAssociation> candidateList = canSkillAssociationService
				.findCandidateListBySkillId(skill.getSkillId());
		model.addAttribute("candidateList", candidateList);
		return "candidate_list_by_skill";
	}

}
