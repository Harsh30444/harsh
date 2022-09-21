
package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;
import com.resourcing.beans.Skill;
import com.resourcing.beans.SkillInterviewerAssosiation;
import com.resourcing.service.CanSkillAssService;
import com.resourcing.service.CandidateJdAssociationService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.EmailSenderService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.InterviewPanelService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.ScheduleService;
import com.resourcing.service.SkillInterviewerAssociationService;
import com.resourcing.service.SkillService;

@Controller
public class ScheduleController {

	static Logger LOGGER = Logger.getLogger(CandidateController.class);

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private SkillInterviewerAssociationService skillInterAssotnService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CanSkillAssService canSkillAssService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private InterviewPanelService interviewPanelService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private SkillService skillService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	EmployeeClientAssociationService employeeClientAssociationService;

	@Autowired
	private CandidateJdAssociationService candidateJdAssociationService;

	// ================================Employee selecting jd and Scheduling
	// Interview For candidate//=======================//

	// adding schedule to candidate based unapplied jd's
	@GetMapping("/addSchedule/{id}")
	public String addSchedulePage(@PathVariable int id, Model model, HttpServletRequest request,
			EmployeeClientAssociation eca) {
		LOGGER.debug("method invoked::::::::");
		Candidate candidateObj = candidateService.getCandidateById(id);
		LOGGER.debug("candidateList::::::");
		List<Skill> skillList = skillService.getAllSkills();
		List<CandidateSkillAssociation> candidateSkills = canSkillAssService.findSkillListByCandidateId(id);
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		LOGGER.debug("jdListinterviewerList:::::::::::::::::");
		List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
		LOGGER.debug("jdList:::::::::::::::::");
		model.addAttribute("skillList", skillList);
		model.addAttribute("interviewersList", interviewersList);
		model.addAttribute("jdList", jdList);
		model.addAttribute("candidateSkills", candidateSkills);
		final StringBuffer url = request.getRequestURL();
		HttpSession session = request.getSession();
		session.setAttribute("url", url);
		model.addAttribute("candidate", candidateObj);
		return "employee_add_schedule_form";
	}

	@PostMapping("/saveSchedule/{id}")
	public String saveSchedule(@ModelAttribute("jdList") JobDescription jobDescription, Model model,
			@ModelAttribute("interviewersList") InterviewPanel interviewPanel, @PathVariable int id,
			@ModelAttribute("candidate") Candidate candidate, Schedule schedule, HttpSession session) {
		LOGGER.debug("invoked:::::::::::::");
		LOGGER.debug("candidateId:::" + candidate.getCandidateId());
		Employee employee = employeeService.getEmployeeById(id);
		// System.out.println("time:::"+schedule.getdate());
		schedule.setCandidate(candidate);
		schedule.setEmployee(employee);
		schedule.setInterviewPanel(interviewPanel);
		schedule.setJobDescription(jobDescription);
		schedule.setInterviewPhase(schedule.getInterviewPhase());
		System.out.println("........");
		// ======filter JD skills with candidate skills========//
		List<CandidateSkillAssociation> canSkillAsList = canSkillAssService
				.findSkillListByCandidateId(candidate.getCandidateId());
		List<String> canSkillsList = canSkillAsList.stream().map(CandidateSkillAssociation::getSkillName)
				.collect(Collectors.toList());
		String jdSkills = jobDescription.getSkills();
		String[] itemsLimits = jdSkills.split(",");
		List<String> jdSkillslist = Arrays.asList(itemsLimits);
		boolean isAnyMatchInJDSkillANDcanSkill = jdSkillslist.stream().allMatch(canSkillsList::contains);
		System.out.println("canskills any match with jd skill:::" + isAnyMatchInJDSkillANDcanSkill);
//======== End=== filter JD skills with candidate skills=========//
//======filter JD skills with Interviewer skills========//
		List<SkillInterviewerAssosiation> interSkillAs = skillInterAssotnService
				.findSkillListByInterviewerId(interviewPanel.getInterviewerId());
		List<String> interSkillsList = interSkillAs.stream().map(SkillInterviewerAssosiation::getSkillName)
				.collect(Collectors.toList());
		boolean isAnyMatchInJDSkillANDinterSkill = jdSkillslist.stream().allMatch(interSkillsList::contains);
		System.out.println("InterSkills any match with jd skill:::" + isAnyMatchInJDSkillANDinterSkill);
		System.out.println("jobD Skills:" + jdSkillslist);
		System.out.println("candidate Skills:" + canSkillsList);
		System.out.println("interviewer Skills:" + interSkillsList);
// End=== filter JD skills with Interviewer skills=========//
		System.out.println("candidateId::" + candidate.getCandidateId() + "  JDID::" + jobDescription.getJdId()
				+ "  interviewPhase::" + schedule.getInterviewPhase());
		List<Schedule> listOfSchedules = scheduleService.getAllSchedulesById(employee);
		List<Schedule> candidateSchedulesForJD = listOfSchedules.stream()
				.filter(ScheduleObj -> ScheduleObj.getJobDescription().getJdId() == jobDescription.getJdId()
						&& ScheduleObj.getCandidate().getCandidateId() == candidate.getCandidateId())
				.toList();
//========schedule======================//
		if (isAnyMatchInJDSkillANDcanSkill == true) {
			if (isAnyMatchInJDSkillANDinterSkill == true) {
				// this is for candidate already scheduled for this job or not
				if (candidateSchedulesForJD.size() == 0) {
					System.out.println(
							"/saveSchedule/{id}::if condition::candidate, jd, interviewphase NOT exist:: add new schedule ");
					LOGGER.debug("candidate:::" + candidate.getFirstName());
					CandidateJdAssociation candidateJdAssociation = candidateJdAssociationService
							.findByCandidateIdAndJdId(candidate, jobDescription);
					// employee schedule an interview to the candidate before candidate applying for
					// the job
					if (candidateJdAssociation == null) {
						CandidateJdAssociation canJDAss = new CandidateJdAssociation();
						canJDAss.setCandidate(candidate);
						canJDAss.setStatus("SCHEDULED");
						canJDAss.setJobDescription(jobDescription);
						canJDAss.setCreatedDate(LocalDateTime.now());
						candidateJdAssociationService.addNewCandidateJdAssociation(canJDAss);
						System.out.println("Candidate previously not applied for thus JD::: now applied  by employee");
						schedule.setFeedback("PENDING");
						scheduleService.saveSchedule(schedule);
						List<Schedule> schedulesToAddMeetingLinks = scheduleService.unScheduledList(employee);
						model.addAttribute("scheduleList", schedulesToAddMeetingLinks);
						model.addAttribute("secheduleSuccessMessage",
								candidate.getFirstName() + " scheduled for interview" + " with interviewer "
										+ interviewPanel.getInterviewerName());
						model.addAttribute("scheduleTable",
								"Scheduled List (Add Meeting Links for Secheduled interviewes)");
						return "employee_candidate_interview_schedule_list";
					}
					// if candidate applied but employee not scheduled an interview
					else if (candidateJdAssociation != null
							&& candidateJdAssociation.getStatus().equalsIgnoreCase("NOT SCHEDULED")) {
						System.out.println("else if:: candidate applied:: but not scheduled");
						candidateJdAssociation.setStatus("SCHEDULED");
						candidateJdAssociation.setUpdatedDate(LocalDateTime.now());
						candidateJdAssociationService.updateCandidateJdAssociation(candidateJdAssociation);
						System.out.println("candidateJd Assoication in updated:: status changed to APPLIED");
						schedule.setFeedback("PENDING");
						scheduleService.saveSchedule(schedule);
						System.out.println("schedule is done:: ");
						List<Schedule> schedulesToAddMeetingLinks = scheduleService.unScheduledList(employee);
						model.addAttribute("scheduleList", schedulesToAddMeetingLinks);
						model.addAttribute("secheduleSuccessMessage",
								candidate.getFirstName() + " scheduled for interview" + " with interviewer "
										+ interviewPanel.getInterviewerName());
						model.addAttribute("scheduleTable",
								"Scheduled List (Add Meeting Links for Secheduled interviewes)");
						return "employee_candidate_interview_schedule_list";

					}
					// this is an un-necessary else condition we can skip this else condition, this
					// could be replaced by next else i.e 218-227
					else {
						System.out.println(
								"else:: candidate already applied:: and also scheduled:: employee cannot schedule again");
						model.addAttribute("message", candidate.getFirstName() + " already applied for this JD:: "
								+ jobDescription.getPosition());
						LOGGER.debug("outer else condition:::::::");
						model.addAttribute("scheduleList", candidateSchedulesForJD);
						model.addAttribute("scheduleTable", "Candidate " + candidate.getFirstName()
								+ " Already Scheduled for this JOB::" + jobDescription.getPosition());
						return "employee_feedback_list";
					}
				}
				// this is for candidate already scheduled for this JOB..
				else {
					System.out.println(
							"/saveSchedule/{id}:: else :: candidate, jd, interviewphase exist:: cannot sechedule ");
					model.addAttribute("message", "candidate:: " + candidate.getFirstName()
							+ " already Scheduled for this JD:: " + jobDescription.getPosition());
					LOGGER.debug("outer else condition:::::::");
					model.addAttribute("scheduleList", candidateSchedulesForJD);
					model.addAttribute("scheduleTable", "Candidate " + candidate.getFirstName()
							+ " Already Scheduled for this JOB::" + jobDescription.getPosition());
					return "employee_feedback_list";
				}
			}
			// interviewer skills are not matched with JOB..
			else {
				System.out.println("Interviewr skills not match with jd skills:: select another Interviewer");
				List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
				List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
				model.addAttribute("interviewersList", interviewersList);
				model.addAttribute("jdList", jdList);
				model.addAttribute("candidate", candidate);
				model.addAttribute("schedule", new Schedule());
				model.addAttribute("message",
						"Interviewr skills doesn't match with Job skills:: select another Interviewer");
				return "employee_add_schedule_form";
			}
		}
		// candidate Skills are not matched with JOB
		else {
			System.out.println("can skills not matching with jd skills::: select another JD");
			List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
			List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
			model.addAttribute("interviewersList", interviewersList);
			model.addAttribute("jdList", jdList);
			model.addAttribute("candidate", candidate);
			model.addAttribute("schedule", new Schedule());
			model.addAttribute("message", "candidate skills doesn't match with Job skills::: select another JD");
			return "employee_add_schedule_form";
		}
	}
//================================Employee selecting jd and  Scheduling Interview For candidate// end=======================//

//==================== adding schedule to candidate based on applied jd's=================================================//
	// employee can see the candidate applied JOBs and scheduled there
	@GetMapping("/candidateJdSchedule/{candidateId}/{jdId}")
	public String candidateJdSchedule(@PathVariable int candidateId, @PathVariable int jdId, HttpServletRequest request,
			Model model) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		final StringBuffer url = request.getRequestURL();
		HttpSession session = request.getSession();
		Schedule schedule = new Schedule();
		model.addAttribute("schedule", schedule);
		session.setAttribute("url", url);
		model.addAttribute("jobDescription", jobDescription);
		model.addAttribute("candidate", candidate);
		model.addAttribute("interviewersList", interviewersList);
		LOGGER.debug("before save::" + candidateId + ",,," + jdId);
		return "employee_candidate_jd_schedule";
	}

	// employee saving schedule based on candidate applied JOBs and Already
	// Scheduled JOBs
	@PostMapping("/saveCandidateJdSchedule/{employeeId}")
	public String saveCandidateJdSchedule(@ModelAttribute("jobDescription") JobDescription jobDescription,
			@ModelAttribute("interviewersList") InterviewPanel interviewPanel,
			@ModelAttribute("schedule") Schedule schedule, Model model,
			@ModelAttribute("candidate") Candidate candidate, @PathVariable int employeeId) {
		LOGGER.debug("save cand jd schedule method::invoked::::");
		Employee employee = employeeService.getEmployeeById(employeeId);
		LOGGER.debug("before if::::" + jobDescription.getJdId());
		LOGGER.debug("candidateName::::" + candidate.getFirstName());
		System.out.println("schedule Id::" + schedule.getScheduleId());
		System.out.println("candidate  schedule:: jd::" + jobDescription.getPosition() + " interviewphase is::"
				+ schedule.getInterviewPhase());
		// Candidate applied but not scheduled yet.. && for next scheduling means
		// technical2,3,4 etc
		if (schedule.getScheduleId() == 0) {
			System.out.println(" outer if condition //saveCandidateJdSchedule/{employeeId} :: scheduleId==0");
			CandidateJdAssociation canJdAs = candidateJdAssociationService.findByCandidateIdAndJdId(candidate,
					jobDescription);
			// candidate applied but not scheduled yet
			if (canJdAs.getStatus().equals("NOT SCHEDULED")) {
				System.out.println("inner if :: if canJDAs:: status=== NOTscheduled:::" + canJdAs.getStatus());
				schedule.setInterviewPanel(interviewPanel);
				schedule.setJobDescription(jobDescription);
				schedule.setEmployee(employee);
				schedule.setInterviewPhase(schedule.getInterviewPhase());
				schedule.setFeedback("PENDING");
				schedule.setComments(null);
				schedule.setMeetingLink(null);
				scheduleService.saveSchedule(schedule);
				canJdAs.setStatus("SCHEDULED");
				canJdAs.setUpdatedDate(LocalDateTime.now());
				candidateJdAssociationService.updateCandidateJdAssociation(canJdAs);
				System.out.println("new schdule is done");
				List<Schedule> listOfSchedules = scheduleService.unScheduledList(employee);
				model.addAttribute("scheduleList", listOfSchedules);
				model.addAttribute("secheduleSuccessMessage", candidate.getFirstName() + " scheduled for interview"
						+ " with interviewer " + interviewPanel.getInterviewerName());
				model.addAttribute("scheduleTable", "Scheduled List (Add Meeting Links for Secheduled interviewes)");
				return "employee_candidate_interview_schedule_list";
			}
			// scheduleId==0 because saving another schedule that's why id will be zero..
			else {
				System.out.println("Inner else:: canJdAs :: status :::SCHEDULED");
				schedule.setInterviewPanel(interviewPanel);
				schedule.setJobDescription(jobDescription);
				schedule.setEmployee(employee);
				schedule.setInterviewPhase(schedule.getInterviewPhase());
				schedule.setFeedback("PENDING");
				schedule.setComments(null);
				schedule.setMeetingLink(null);
				scheduleService.saveSchedule(schedule);
				System.out.println("next schedule is done");
				List<Schedule> listOfSchedules = scheduleService.unScheduledList(employee);
				model.addAttribute("scheduleList", listOfSchedules);
				model.addAttribute("secheduleSuccessMessage", candidate.getFirstName() + " scheduled for interview"
						+ " with interviewer " + interviewPanel.getInterviewerName());
				model.addAttribute("scheduleTable", "Scheduled List (Add Meeting Links for Secheduled interviewes)");
				return "employee_candidate_interview_schedule_list";
			}
		}
		// re-Schedule an interview.. because scheduleId!=0 here..
		else {
			System.out.println("Outer else:: Reschedule");
			Schedule scheduleObj = scheduleService.getScheduleById(schedule.getScheduleId());
			System.out.println("inner else:: schedule::");
			scheduleObj.setInterviewPanel(interviewPanel);
			scheduleObj.setInterviewPhase(schedule.getInterviewPhase());
			scheduleObj.setFeedback("PENDING");
			scheduleObj.setComments(null);
			scheduleObj.setMeetingLink(null);
			scheduleService.updateSchedule(scheduleObj);
			System.out.println("Re schedule is done::");
			List<Schedule> listOfSchedules = scheduleService.unScheduledList(employee);
			model.addAttribute("scheduleList", listOfSchedules);
			model.addAttribute("secheduleSuccessMessage", candidate.getFirstName() + " scheduled for interview"
					+ " with interviewer " + interviewPanel.getInterviewerName());
			model.addAttribute("scheduleTable", "Scheduled List (Add Meeting Links for Secheduled interviewes)");
			return "employee_candidate_interview_schedule_list";
		}
	}

//============END====== adding schedule to candidate based on applied jd's=================================================//

//=======Start======= Employee Rescheduling Interview================================================//

	// candidate not attended and reschedule for that
	@GetMapping("/candidateJdReSchedule/{candidateId}/{jdId}/{scheduleId}")
	public String candidateJdReSchedule(@PathVariable int candidateId, @PathVariable int jdId,
			@PathVariable int scheduleId, HttpServletRequest request, Model model) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		final StringBuffer url = request.getRequestURL();
		HttpSession session = request.getSession();
		session.setAttribute("url", url);
		model.addAttribute("jobDescription", jobDescription);
		model.addAttribute("candidate", candidate);
		model.addAttribute("interviewersList", interviewersList);
		model.addAttribute("schedule", schedule);
		LOGGER.debug("before save::" + candidateId + ",,," + jdId);
		return "employee_candidate_jd_schedule";
	}

//=======END======= Employee Rescheduling Interview===(reschedule form is directed to'/saveCandidateJdSchedule/{employeeId}' )=============================================//

	@GetMapping("/scheduleList")
	public String listOfSchedules(Model model, HttpSession session) {
		Employee sessionEmployee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.ScheduledList(sessionEmployee);
		model.addAttribute("scheduleList", objSchedule);
		model.addAttribute("scheduleTable", "List Of Schedules");
		return "employee_schedule_list";
	}

	@GetMapping("/scheduleList/{id}")
	public String listOfSchedulesOfEmployee(Model model, HttpSession session) {
		Employee sessionEmployee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.unScheduledList(sessionEmployee);
		model.addAttribute("scheduleList", objSchedule);
		model.addAttribute("scheduleTable", "List Of Schedules");
		return "employee_candidate_interview_schedule_list";
	}

//========after schedule add meeting link===============================//

	@PostMapping("/saveMeeting/{id}")
	public String link(Model model, @PathVariable int id, @ModelAttribute("scheduleList") Schedule schedule,
			Candidate objCandidate, InterviewPanel objInter, final HttpServletRequest request,
			final HttpServletResponse response, HttpSession session) {
		Schedule schedules = scheduleService.getScheduleById(id);
		LOGGER.debug(" To add meeting link::candidate mail from schedule is::" + schedules.getCandidate().getEmail());
		LOGGER.debug("  To add meeting link::InterViewer mail from schedule is::"
				+ schedules.getInterviewPanel().getInterviewerMail());
		LOGGER.debug("  To add meeting link:: from Recruiter mail from schedule is::"
				+ schedules.getEmployee().getEmailId());
		LOGGER.debug("getLink:" + schedule.getMeetingLink());
		schedules.setMeetingLink(schedule.getMeetingLink());
		schedules.setComments(null);
		schedules.setFeedback("PENDING");
		scheduleService.updateSchedule(schedules);
		model.addAttribute("schedule", schedule);
		LOGGER.debug("138 link of meeting::" + schedules.getMeetingLink());
		LOGGER.debug("139 link of meeting candidate Id::" + schedules.getCandidate().getCandidateId());
		LOGGER.debug("140 link of meeting interviewerId::" + schedules.getInterviewPanel().getInterviewerId());
		LOGGER.debug("candidate mailId:" + schedules.getCandidate().getEmail());
		LOGGER.debug("Interviewer MailId:::" + schedules.getInterviewPanel().getInterviewerMail());
		scheduleService.updateSchedule(schedules);
		model.addAttribute("meetingLink", schedule.getMeetingLink());
		SimpleMailMessage mailMessage = new SimpleMailMessage(); // mailMessage.setTo(objCandidate.getEmail());
		mailMessage.setCc(schedules.getInterviewPanel().getInterviewerMail(), schedules.getEmployee().getEmailId()); //
		mailMessage.setTo(schedules.getCandidate().getEmail());
		mailMessage.setSubject("360Resourcing-Interview Schedule!");
		mailMessage.setFrom("t.srivani488@gmail.com");
		mailMessage.setText("Your Interview is Scheduled  " + "Timings:" + schedules.getDate() + ", "
				+ schedules.getTime() + " please click here : " + schedules.getMeetingLink());
		emailSenderService.sendEmail(mailMessage);
		model.addAttribute("candidateMailId", schedules.getCandidate().getEmail());
		model.addAttribute("interviewerMailId", schedules.getInterviewPanel().getInterviewerMail());
		return "link.html";
	}

//================NextSchedule for SAME JOB===============================//

	// next schedule for same JOB
	@GetMapping("/candidateJdNextSchedule/{candidateId}/{jdId}/{scheduleId}")
	public String candidateJdNextSchedule(@PathVariable int candidateId, @PathVariable int jdId,
			@PathVariable int scheduleId, HttpServletRequest request, Model model) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<Schedule> allSchedules = scheduleService.finalFeedbackList(candidate);
		List<Schedule> Schedules = allSchedules.stream()
				.filter(ScheduleObject -> ScheduleObject.getJobDescription().getJdId() == jdId
						&& ScheduleObject.getStatus() == null)
				.toList();
		for (Schedule objSchedule : Schedules) {
			objSchedule.setStatus("NA");
			scheduleService.updateSchedule(objSchedule);
		}
		Schedule schedule = new Schedule();
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		final StringBuffer url = request.getRequestURL();
		HttpSession session = request.getSession();
		session.setAttribute("url", url);
		model.addAttribute("jobDescription", jobDescription);
		model.addAttribute("candidate", candidate);
		model.addAttribute("interviewersList", interviewersList);
		model.addAttribute("schedule", schedule);
		LOGGER.debug("next Schedule:: candidate::" + candidateId + "JOBID::" + jdId);
		return "employee_candidate_jd_schedule";
	}

	// =====final status of schedule for
	// JD=============================================================//

	@PostMapping("/saveFinalStatus/{id}")
	public String candidateFinalStatusOfJD(Model model, @PathVariable int id,
			@ModelAttribute("schedule") Schedule schedule, @ModelAttribute("scheduleList") Schedule scheduleObj,
			HttpSession session) {
		LOGGER.debug("schedule feedback::" + schedule);
		Schedule scheduleExist = scheduleService.getScheduleById(id);
		System.out.println("Scheduleexist::candId::" + scheduleExist.getCandidate().getCandidateId() + "JD Id::"
				+ scheduleExist.getJobDescription().getJdId());
		List<Schedule> schedules = scheduleService.finalFeedbackList(scheduleExist.getCandidate());
		System.out.println("all schedules of candidate::" + schedules.size());
		LOGGER.debug("selected schedule::" + scheduleExist.getFeedback());
		LOGGER.debug("selected schedule::" + scheduleExist);
		scheduleExist.setStatus(schedule.getStatus());
		scheduleExist.setMessage(schedule.getMessage());
		System.out.println("from UI status is::" + schedule.getStatus() + " message is::  " + schedule.getMessage());
		scheduleService.updateSchedule(scheduleExist);
		System.out.println(
				"updated in DB::" + scheduleExist.getStatus() + " message is::  " + scheduleExist.getMessage());
		// update is done// to get final status for JD from Employee
		// =======================update JD with incrementing filled count========//
		if (scheduleExist.getStatus().equals("SELECTED")) {
			JobDescription jdObject = jobDescriptionService
					.getJobDescriptionById(scheduleExist.getJobDescription().getJdId());
			jdObject.setFilled(jdObject.getFilled() + 1);
			if (jdObject.getVacancies() == jdObject.getFilled()) {
				jdObject.setIsActive('N');
				jobDescriptionService.updateJobDescription(jdObject);
			}
			jobDescriptionService.updateJobDescription(jdObject);
			System.out.println("JD vacancies count::" + jdObject.getVacancies() + " filled Count::" + jdObject.getFilled());
		}
		CandidateJdAssociation canJdAs = candidateJdAssociationService
				.findByCandidateIdAndJdId(scheduleExist.getCandidate(), scheduleExist.getJobDescription());
		canJdAs.setStatus(scheduleExist.getStatus());
		candidateJdAssociationService.updateCandidateJdAssociation(canJdAs);
		List<Schedule> allSchedules = scheduleService.finalFeedbackList(scheduleExist.getCandidate());
		List<Schedule> Schedules = allSchedules.stream().filter(
				ScheduleObject -> ScheduleObject.getJobDescription().getJdId() == canJdAs.getJobDescription().getJdId()
						&& ScheduleObject.getStatus() == null)
				.toList();
		for (Schedule objSchedule : Schedules) {
			objSchedule.setStatus("NA");
			scheduleService.updateSchedule(objSchedule);
		}
		return "redirect:/emp/feedbackList/" + scheduleExist.getEmployee().getEmployeeId();
	}

	@GetMapping("/scheduleFeedback/{candidateId}/{jdId}")
	public String scheduleFeedbackOfJD(Model model, @PathVariable int candidateId, @PathVariable int jdId,
			HttpSession session) {
		LOGGER.debug("schedule feedback Of JD::");
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<Schedule> allSchedules = scheduleService.getAllSchedules();
		List<Schedule> filteredSchedules = allSchedules.stream()
				.filter(schedule -> schedule.getCandidate().getCandidateId() == candidateId
						&& schedule.getJobDescription().getJdId() == jdId)
				.toList();
		CandidateJdAssociation canJdAs = candidateJdAssociationService.findByCandidateIdAndJdId(candidate,
				jobDescription);
		canJdAs.setStatus("SELECTED");
		candidateJdAssociationService.updateCandidateJdAssociation(canJdAs);
		System.out.println("filteredSchedules of canididate of JD::" + filteredSchedules.size());
		model.addAttribute("scheduleList", filteredSchedules);
		model.addAttribute("count", filteredSchedules.size());
		model.addAttribute("scheduleTable", "Feedback List");
		return "employee_feedback_list";
	}
}
