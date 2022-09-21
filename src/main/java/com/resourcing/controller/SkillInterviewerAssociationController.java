//*author: HarshKashyap

package com.resourcing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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

import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.beans.Education;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.Skill;
import com.resourcing.beans.SkillInterviewerAssosiation;
import com.resourcing.service.InterviewPanelService;
import com.resourcing.service.SkillInterviewerAssociationService;
import com.resourcing.service.SkillService;

@Controller
public class SkillInterviewerAssociationController {
	static Logger LOGGER = Logger.getLogger(SkillInterviewerAssociationController.class);

	@Autowired
	private SkillService skillService;
	@Autowired
	private InterviewPanelService interviewerService;
	@Autowired
	private SkillInterviewerAssociationService skillInterAssService;

//	@GetMapping("/addSkills")
//	public String getSkills(Model model, InterviewPanel interviewer,Skill_Interviewer_Assosiation sia,Skill skill ) {
//		model.addAttribute("interviewer", interviewer);
//		List<Skill> list=skillService.getAllSkills();
//		model.addAttribute("list", list);
//		return "skillform";
	@GetMapping("/addSkills/{id}")
	public String getSkillsPage(Model model, @PathVariable("id") int interviewerId, InterviewPanel interviewer,
			SkillInterviewerAssosiation sia) {
		InterviewPanel objInterviewer = interviewerService.getInterviewerById(interviewerId);
		model.addAttribute("interviewer", objInterviewer);

		List<Skill> list = skillService.getAllSkills();
		model.addAttribute("list", list);

		return "interviewer_skill";
	}

	@PostMapping(value = "/saveSkills/{id}")
	public String getCsaList(Model model, @PathVariable("id") int interviewerId,
			@ModelAttribute("csa") SkillInterviewerAssosiation csa, @ModelAttribute("list") Skill skill,
			@ModelAttribute("interviewer") InterviewPanel interviewer) {
		InterviewPanel objInterviewer = interviewerService.getInterviewerById(interviewerId);
		model.addAttribute("interviewer", objInterviewer);
		LOGGER.debug("Inside eca saving eca METHOD candiadte Id::::::" + csa.getInterviewerId());
		LOGGER.debug("Inside eca saving eca METHOD skill Name::::::" + skill.getSkillName());
		csa.setSkillId(skill.getSkillId());
		csa.setSkillName(skill.getSkillName());
		csa.setInterviewerId(objInterviewer.getInterviewerId());
		LOGGER.debug("csa" + csa.getInterviewerId());
		csa.setInterviewerName(interviewer.getInterviewerName());
		if (skillInterAssService.findBySkillIdAndInterviewerId(interviewerId, csa.getSkillId()) == null) {
			skillInterAssService.addNewSia(csa);
			LOGGER.debug("saved csa::::::::::" + csa.getSkillId());
			LOGGER.debug("skill not added");
			List<SkillInterviewerAssosiation> csaList = skillInterAssService
					.findSkillListByInterviewerId(objInterviewer.getInterviewerId());
			model.addAttribute("csaList", csaList);
			return "interviewer_skill_list";
		} else if (skillInterAssService.findBySkillIdAndInterviewerId(interviewerId, csa.getSkillId()) != null) {
			List<SkillInterviewerAssosiation> csaList = skillInterAssService
					.findSkillListByInterviewerId(objInterviewer.getInterviewerId());
			LOGGER.debug("skill already added");
			model.addAttribute("csaList", csaList);
			List<Skill> list = skillService.getAllSkills();
			model.addAttribute("list", list);
			model.addAttribute("errorMessage", "already added");
			return "interviewer_skill";
		}
		return "interviewer_skill_list";

	}

	@GetMapping(value = "/skills/{id}")
	public String getSkillList(Model model, @PathVariable("id") int interviewerId,HttpSession session ) {

		InterviewPanel objInterviewer = interviewerService.getInterviewerById(interviewerId);
		model.addAttribute("interviewer", objInterviewer);
	
		LOGGER.debug("skill list");
		List<SkillInterviewerAssosiation> csaList = skillInterAssService
				.findSkillListByInterviewerId(objInterviewer.getInterviewerId());
		session.getAttribute("interviewerName");
		LOGGER.debug("skill list visible"+csaList);
		model.addAttribute("csaList", csaList);
		model.addAttribute("interviewer", objInterviewer);
		LOGGER.debug("success:::");
		return "interviewer_skill_list";
	}

	@RequestMapping(value = "/updateSkills/{interviewerId}/{skillId}", method = RequestMethod.POST)
	public String updateEducationDetails(@ModelAttribute("educationExist") Education education, Model model,
			SkillInterviewerAssosiation association, @PathVariable(value = "interviewerId") int interviewerId,
			@PathVariable(value = "skillId") int skillId) {
		SkillInterviewerAssosiation existSkill = skillInterAssService
				.getAssociationById(association.getAssociationId());
		
		skillInterAssService.updateSia(association);
		return "redirect:/skills/" + interviewerId;
	}

	@GetMapping("/deleteSkills/{interviewerId}/{associationId}")
	public String deleteSkill(CandidateSkillAssociation association, Model model,
			@PathVariable(value = "interviewerId") int interviewerId,
			@PathVariable(value = "associationId") int associationId) {
//				canSkillAssociationService.getEducationById(educationId);
		LOGGER.debug("id:::" + association.getCandidateSkillAssociationId());
		LOGGER.debug("id2::" + associationId);
		skillInterAssService.deleteSkillInterviewerAssociationBySkillId(associationId);
		return "redirect:/skills/" + interviewerId;
	}

}
