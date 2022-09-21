/* 
 * Author : Srivani Tudi */

package com.resourcing.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.beans.Client;
import com.resourcing.beans.Doc;
import com.resourcing.beans.Education;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.Employment;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.InterviewerAvailability;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;
import com.resourcing.beans.Skill;
import com.resourcing.beans.SkillInterviewerAssosiation;
import com.resourcing.repository.SkillRepository;
import com.resourcing.service.CanSkillAssService;
import com.resourcing.service.CandidateJdAssociationService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.ClientService;
import com.resourcing.service.DocStorageService;
import com.resourcing.service.EducationService;
import com.resourcing.service.EmailSenderService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.EmploymentService;
import com.resourcing.service.InterviewPanelService;
import com.resourcing.service.InterviewerAvailabilityService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.ScheduleService;
import com.resourcing.service.SkillInterviewerAssociationService;
import com.resourcing.service.SkillService;
import com.resourcing.utilities.Utilities;

@Controller
@RequestMapping("/emp")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SkillService skillService;

	@Autowired
	private SkillInterviewerAssociationService interviewerSkillService;

	@Autowired
	private InterviewerAvailabilityService inAvailabilityService;

	@Autowired
	private InterviewPanelService interviewPanelService;

	@Autowired
	private EmployeeClientAssociationService employeeClientAssociationService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CandidateJdAssociationService candidateJdAssociationService;

	@Autowired
	private EducationService educationService;

	@Autowired
	private EmploymentService employmentService;

	@Autowired
	private CanSkillAssService canSkillAssService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	DocStorageService docStorageService;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	ClientService clientService;

	static Logger LOGGER = Logger.getLogger(EmployeeController.class);

	// employee basic validations==========================================//
	// View loginPage with employee Mail and password
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView employeeloginPage(Employee employeeObj) {
		LOGGER.debug("entered into employee Controller:::: login method");
		ModelAndView mav = new ModelAndView("employee_login_page");
		mav.addObject("newUserDetails", employeeObj);
		return mav;
	}

	// validate employee by mailId and password
	@PostMapping("/employeeValidation")
	public ModelAndView validatemethod(@ModelAttribute("newUserDetails") Employee employeeObj,
			HttpServletRequest request, Model model) {
		LOGGER.debug("emp ped::" + employeeObj.getPassword());
		String strEncPassword = Utilities.getEncryptSecurePassword(employeeObj.getPassword(), "RESOURCING");
		LOGGER.debug("after encryt::" + strEncPassword);
		Employee employeeExist = employeeService.findByUserNameIgnoreCaseAndPassword(employeeObj.getEmailId(),
				strEncPassword);
		if (employeeExist != null) {
			HttpSession session = request.getSession();
			session.setAttribute("employeeObj", employeeExist);
			LOGGER.debug("session is set::" + session.getAttribute("employeeObj"));
			LOGGER.debug("session object::" + session.getAttribute("employeeObj"));
			LOGGER.debug("employee controller validation method employee mail id  is:" + employeeExist.getEmailId());
			LOGGER.debug("employee dashboard page is displayed");
			ModelAndView mav = new ModelAndView("employee_dashboard");
			mav.addObject("clientsCount",
					employeeClientAssociationService.findClientsByEmployeeId(employeeExist).size());
			List<EmployeeClientAssociation> ecalist = employeeClientAssociationService
					.findClientsByEmployeeId(employeeExist);
			mav.addObject("clientsCount", ecalist.size());
			List<Schedule> objSchedule = scheduleService.getAllSchedulesById(employeeExist);
			List<Schedule> selctedCandidatesList = objSchedule.stream()
					.filter(scheduleObject -> scheduleObject.getStatus() != null
							&& scheduleObject.getStatus().equals("SELECTED"))
					.toList();
			mav.addObject("selectedCandidatedCount", selctedCandidatesList.size());
			System.out.println("selected candidateList::count::" + selctedCandidatesList.size());
			List<Schedule> rejctedCandidatesList = objSchedule.stream()
					.filter(scheduleObject -> scheduleObject.getStatus() != null
							&& scheduleObject.getStatus().equals("REJECTED"))
					.toList();
			mav.addObject("rejectedCandidatedCount", rejctedCandidatesList.size());
			List<Candidate> myCandidatesList = candidateService.getCandidateListOfRecruiter(employeeExist);
			mav.addObject("myCandidatesCount", myCandidatesList.size());
			model.addAttribute("sessionEmployee", employeeExist);
			List<EmployeeClientAssociation> ecalist1 = employeeClientAssociationService
					.getAllEmployeeClientAssociations();
			List<EmployeeClientAssociation> eca2 = ecalist1.stream().filter(eca3 -> eca3.getEmployee() == employeeExist)
					.collect(Collectors.toList());
			LOGGER.debug("client Count with id 18::" + eca2.size());
			LOGGER.debug("session name::" + employeeExist.getEmailId());
			LOGGER.debug("my candidates count" + myCandidatesList.size());
			mav.addObject("sessionEmployee", employeeExist);
			List<JobDescription> allJds = jobDescriptionService.getAllJobDescriptions();
			List<Schedule> allSchedules = scheduleService.getAllSchedulesById(employeeExist);
			List<Schedule> todaySchedules = allSchedules.stream()
					.filter(scheduleObj -> scheduleObj.getDate().equals(LocalDate.now()))
					.toList();
			System.out.println("todayScheduleCount::" + todaySchedules.size());
			mav.addObject("todaySchedulesCount", todaySchedules.size());
			List<Schedule> upComingSchedules = allSchedules.stream()
					.filter(scheduleobj -> scheduleobj.getStatus()==null && !(scheduleobj.getDate().equals(LocalDate.now())))
					.toList();
			mav.addObject("upComingschedules", upComingSchedules);
			mav.addObject("upComingSchedulesCount", upComingSchedules.size());
			System.out.println("upComingschedulesCount:::"+upComingSchedules.size());
			return mav;

		} else {
			LOGGER.debug("else block of validation:::: Use valid details");
			ModelAndView mav2 = new ModelAndView("employee_login_page");
			mav2.addObject("newUserDetails", new Employee());
			mav2.addObject("errorMessage", "invalid Credentials!");
			return mav2;
		}
	}

	// access employee dashBoard from any where throughout the session is live
	@RequestMapping(value = "/employeeDashboard", method = RequestMethod.GET)
	public ModelAndView employeeDashboard(Model model, HttpSession session) {
		Employee sessionEmployee = (Employee) session.getAttribute("employeeObj");
		System.out.println("chekc:::"+sessionEmployee.getEmailId());
		ModelAndView mav = new ModelAndView("employee_dashboard");
		mav.addObject("jobsCount", jobDescriptionService.getAllJobDescriptions().size());
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(sessionEmployee);
		List<Schedule> selctedCandidatesList = objSchedule.stream()
				.filter(scheduleObject -> scheduleObject.getStatus().equals("SELECTED")).toList();
		mav.addObject("selectedCandidatedCount", selctedCandidatesList.size());
		System.out.println("selected candidateList::count::" + selctedCandidatesList.size());
		List<Schedule> rejectedCandidatesList = scheduleService.rejectedCandidatesListByEmployee(sessionEmployee);
		mav.addObject("rejectedCandidatedCount", rejectedCandidatesList.size());
		List<Candidate> myCandidatesList = candidateService.getCandidateListOfRecruiter(sessionEmployee);
		mav.addObject("myCandidatesCount", myCandidatesList.size());
		model.addAttribute("sessionEmployee", sessionEmployee);
		List<EmployeeClientAssociation> ecalist = employeeClientAssociationService
				.findClientsByEmployeeId(sessionEmployee);
		mav.addObject("clientsCount", ecalist.size());
		mav.addObject("sessionEmployee", sessionEmployee);
		return mav;
	}

	// =============employee Candidate Association===================//
//	get the list of candidates assigned to recruiter(employee)
	@GetMapping("/candidateList/{employeeId}")
	public String getAllCandidates(Model model, Candidate candidate,
			@PathVariable(value = "employeeId") int employeeId) {
		LOGGER.debug(" all Candidates assigned to this recruiter:::");
		LOGGER.debug("candidates assigned to you");
		Employee employeeExist = employeeService.getEmployeeById(employeeId);
		LOGGER.debug("employee found with name::" + employeeExist.getEmployeeName());
		List<Candidate> candidateList = candidateService.getCandidateListOfRecruiter(employeeExist);
		List<Education> educationList = educationService.getAllEducations();
		List<CandidateSkillAssociation> skillList = canSkillAssService.getAllCandidateSkillAssociations();
		LOGGER.debug("candidates found with employee::" + employeeExist.getEmployeeName()
				+ "no of candidates found are::" + candidateList.size());
		model.addAttribute("educationList", educationList);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("count", candidateList.size());
		model.addAttribute("csaList", skillList);
		model.addAttribute("TableName",candidateList.size()+ " CANDIDATES are assigned!!");
		return "employee_dashboard_candidatelist";
	}

	// get all Candidates List
	@GetMapping("/candidateList")
	public String getAllCandidates(Model model, Candidate candidate) {
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<Candidate> candidateList = candidateService.getAllCandidates();
		List<Education> educationList = educationService.getAllEducations();
		List<CandidateSkillAssociation> skillList = canSkillAssService.getAllCandidateSkillAssociations();
		model.addAttribute("csaList", skillList);
		LOGGER.debug("skillList::::" + skillList);
		model.addAttribute("educationList", educationList);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("count", candidateList.size());
		model.addAttribute("allCandidatesList", "List of All Candidates");
		return "employee_all_candidate_list";
	}

	// view complete candidate details in candidate dashBoard page visible in view
	// format
	@GetMapping("/candidate/dashboardSettings/{id}")
	public String dashboardSettingsPage(Model model, @PathVariable int id) {
		Candidate candidate = candidateService.getCandidateById(id);
		LOGGER.debug(candidate.getCandidateId());
		model.addAttribute("candidate", candidate);
		return "employee_candidate_dashboard_settings";
	}

	// get candidate Education details
	@GetMapping("/candidate/educationList/{candidateId}")
	public String getAllEducationsOfOneCandidate(Model model, Candidate candidate) {
		List<Education> educationList = educationService.getAllEducations(candidate);
		model.addAttribute("educationList", educationList);
		model.addAttribute("candidate", candidate);
		return "employee_candidate_educationlist";
	}

	// get candidates previous company details or experience
	@GetMapping("/candidate/employmentList/{candidateId}")
	public String getAllEmploymentsOfOneCandidate(Model model, @PathVariable int candidateId, Candidate candidate) {
		Candidate conadidateObj = candidateService.getCandidateById(candidateId);
		List<Employment> employmentList = employmentService.getAllEmployments(conadidateObj);
		model.addAttribute("employmentList", employmentList);
		model.addAttribute("candidate", candidate);
		return "employee_candidate_employmentList";
	}

	// ==========================forgot password reset and logout of
	// employee==================================//

	// view email verification page on forgot password
	@RequestMapping(value = "/employeeForgotPassword", method = RequestMethod.GET)
	public ModelAndView employeeForgotPasswordPage(Employee newUser) {
		LOGGER.debug("entered into employee/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("employee_forgot_password");
		mav.addObject("newUserDetails", newUser);
		return mav;
	}

	// validate employee email//
	@PostMapping(value = "/validateEmployeeEmailId")
	public String checkMailId(Model model, Employee tempUser) {
		LOGGER.debug("entered into employee/controller::::check EmailId existing or not");
		LOGGER.debug("UI given mail Id:" + tempUser.getEmailId());
		Employee pUser = employeeService.findByEmailId(tempUser.getEmailId());
		if (pUser != null) {
			model.addAttribute("newUserDetails", tempUser);
			return "employee_reset_password";
		} else {
			model.addAttribute("newUserDetails", tempUser);
			model.addAttribute("message", "email doesn't exist...!");
			return "employee_forgot_password";
		}
	}

	// update password and redirect to login page
	@RequestMapping(value = "/updateEmployeePassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("newUserDetails") Employee tempEmployeeObj,
			HttpSession session) {
		Employee employeeObjExist = employeeService.findByEmailId(tempEmployeeObj.getEmailId());
		LOGGER.debug("in update method employee:: name " + employeeObjExist.getEmployeeName());
		employeeObjExist.setUpdatedDate(LocalDateTime.now());
		LOGGER.debug("updated password before Ency::" + tempEmployeeObj.getPassword());
		String strEncPassword = Utilities.getEncryptSecurePassword(tempEmployeeObj.getPassword(), "RESOURCING");
		tempEmployeeObj.setPassword(strEncPassword); // while saving the user put this to encrypt pwd.
		employeeObjExist.setPassword(strEncPassword);
		LOGGER.debug("emp password::" + tempEmployeeObj.getPassword());
		employeeObjExist.setIsActive("Y");
		employeeService.updateEmployee(employeeObjExist);
		LOGGER.debug("db paswd::" + employeeObjExist.getPassword());
		LOGGER.debug("password is updated sucessfully");
		ModelAndView mav = new ModelAndView("employee_login_page");
		LOGGER.debug("login page is displayed");
		mav.addObject("passwordUdatedMessage", "password Updated successfully");
		mav.addObject("newUserDetails", employeeObjExist);
		return mav;
	}

	// employee logout and session set to null
	@RequestMapping(value = "/employeeLogout", method = RequestMethod.GET)
	public ModelAndView UserLogout(HttpSession session, Employee newEmployee) {
		LOGGER.debug("entered into employee/controller::::logged out");
		ModelAndView mav = new ModelAndView("redirect:/resourcing");
		mav.addObject("newUserDetails", newEmployee);
		session.setAttribute("employeeObj", null);
		LOGGER.debug("session object after logout:::" + session.getAttribute("employeeObj"));
		return mav;
	}

	// employee dashBoard
	// settings==========================================================================//
	// view employee dashBoard settings page
	@RequestMapping(value = "/employeeDasboardSettings/{id}", method = RequestMethod.GET)
	public ModelAndView employeeDasboardSettingPage(@PathVariable(value = "id") int id) {
		LOGGER.debug("entered into emp/controller:::: dashboard setting page will display");
		LOGGER.debug("employee Dashborad settings");
		Employee employeeExist = employeeService.getEmployeeById(id);
		LOGGER.debug("employeeExist Name:::" + employeeExist.getEmployeeName());
		LOGGER.debug("employee Name is :::" + employeeExist.getEmployeeName());
		ModelAndView mav = new ModelAndView("employee_dashboard_settingss");
		mav.addObject("employeeObj", employeeExist);
		mav.addObject("branch", employeeExist.getBranch().branchId);
		return mav;
	}

	// update employee Details record
	@RequestMapping(value = "/updateEmployeeDetails", method = RequestMethod.POST)
	public ModelAndView saveEmployeeDetails(Model model, @ModelAttribute("employeeObj") Employee employee) {
		LOGGER.debug("employee dash board:: employeeName::" + employee.getEmployeeName());
		LOGGER.debug("employee dash board:: employeeRole::" + employee.getEmployeeRole());
		LOGGER.debug("employee dash board:: employeeMobile::" + employee.getMobileNo());
		Employee employeeExist = employeeService.getEmployeeById(employee.getEmployeeId());
		ModelAndView mav = new ModelAndView("redirect:/emp/employeeDashboard");
		LOGGER.debug("entered into employee update method");
		mav.addObject("newUserDetails", employeeExist);
		LOGGER.debug("branch:::" + employeeExist.getBranch());
		LOGGER.debug("in update method employee:: name " + employeeExist.getEmployeeName());
		employee.setUpdatedDate(LocalDateTime.now());
		employee.setUpdatedBy(employeeExist.getEmployeeId());
		employee.setCreatedDate(employeeExist.getCreatedDate());
		employee.setIsActive("Y");
		LOGGER.debug("employeeName is::" + employee.getEmployeeName());
		employeeService.updateEmployee(employee);
		LOGGER.debug(" Employee fields are updated sucessfully");
		LOGGER.debug("employee details updated sucessfully");
		return mav;
	}

//===================================client association=====================================================================//
	@GetMapping("/employeeClients")
	public String getEcaListForASingleEmployee(Model model,HttpSession session
			) {
		Employee employee=(Employee)session.getAttribute("employeeObj");
		List<EmployeeClientAssociation> ClientListForEmployee = employeeClientAssociationService
				.findClientsByEmployeeId(employee);
		System.out.println("my clients::"+ClientListForEmployee.size());
		LOGGER.debug("employee client association list::::" + ClientListForEmployee);
		model.addAttribute("clientList", ClientListForEmployee);
		model.addAttribute("TableName", ClientListForEmployee.size()+" clients are ASSIGNED!!");
		return "employee_associated_clientlist";
	}

//========================================================client association============================================//

//=======================Interviewers Association==========================================//
	@GetMapping("/interviewersList")
	public String getAllInterviewers(Model model, Candidate candidate) {
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		List<SkillInterviewerAssosiation> skillList = interviewerSkillService.getAllSkillInterviewerAssociations();
		model.addAttribute("csaList", skillList);
		LOGGER.debug("skillList::::" + skillList);
		model.addAttribute("allInterviewers", interviewersList);
		model.addAttribute("count", interviewersList.size());
		model.addAttribute("interviewersList", "List of all Interviewers");
		return "employee_dashboard_interviewerslist";
	}

	@GetMapping("/interviewerAvailability/{id}")
	public String getAllInterviewerAvailabilty(Model model, @PathVariable(value = "id") int interviewerId) {
		LOGGER.debug(" interviewers availabilty sechedule:::");
		InterviewPanel interviewer = interviewPanelService.getInterviewerById(interviewerId);
		List<InterviewerAvailability> availabilityList = inAvailabilityService.getAllInterviewerAvailabilities();
		model.addAttribute("interviewer", interviewer);
		model.addAttribute("interviewerAvailabilityLis", availabilityList);
		return "employee_dashboard_interviewer_availability";
	}

	@GetMapping("/feedbackList/{id}")
	public String feedbackList(Model model, @PathVariable int id, HttpSession session) {
		LOGGER.debug("method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		model.addAttribute("scheduleList", objSchedule);
		model.addAttribute("count", objSchedule.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "Feedack List By Interviewer of Secheduled Candidates(updated)");
		return "employee_feedback_list";
	}

	@GetMapping("/reScheduleList/{id}")
	public String reScheduleList(Model model, @PathVariable int id, HttpSession session) {
		LOGGER.debug("reScheduleList/{id}::method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		List<Schedule> reScheduleList = objSchedule.stream().filter(scheduleObj -> scheduleObj.getFeedback() != null
				&& scheduleObj.getFeedback().contentEquals("NOT ATTENDED")).toList();
		System.out.println("Reschedule List Size::" + reScheduleList.size());
		model.addAttribute("scheduleList", reScheduleList);
		model.addAttribute("count", reScheduleList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "Re-Schedule Following candidates");
		return "employee_feedback_list";
	}

//	/emp/candidate/doc/{id}

	@GetMapping("/candidate/docList/{candidateId}")
	public String ListOfCandidateDocs(Model model, @PathVariable int candidateId) {
		LOGGER.debug("cnadudate Id for doc list::" + candidateId);
		LOGGER.debug("candidateId in list method:::::" + candidateId);
		Candidate candidate = candidateService.getCandidateById(candidateId);
		List<Doc> docs = docStorageService.getAllFilesById(candidateId);
		model.addAttribute("docs", docs);
		model.addAttribute("candidate", candidate);
		return "employee_candidate_doclist";
	}

	@GetMapping("/candidate/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) throws Exception {
		Doc doc = docStorageService.getFileById(fileId);
		LOGGER.debug("download candidate documents:::");

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + doc.getDocName() + "\"")
				.body(new ByteArrayResource(doc.getData()));

	}

	// ==================== creating skills================//

	@GetMapping("/addSkillByManager/{id}")
	public String addingSkillsByManager(@PathVariable int id, Model model, Skill skill) {
		LOGGER.debug("before if condition:::");
		LOGGER.debug("if condition:::::::");
		model.addAttribute("skill", skill);
		return "employee_skill_added_by_manager";
	}

	@PostMapping("/saveSkillsByManager")
	public String saveSkillsByManager(@ModelAttribute("skill") Skill skill, Model model) {
		Skill skillName = skillService.getSkillBySkillName(skill.getSkillName());
		if (skillName != null) {
			model.addAttribute("message", skill.getSkillName() + " is already added!! please add a new skill");
			model.addAttribute("skill", skill);
			return "employee_skill_added_by_manager";
		} else {
			skillRepository.save(skill);
			model.addAttribute("message", skill.getSkillName() + " is added successfully");
			List<Skill> skillList = skillService.getAllSkills();
			model.addAttribute("skillList", skillList);
			model.addAttribute("count", skillList.size());
			return "employee_skill_list_by_manager";
		}
	}

	@GetMapping("/deleteSkill/{skillId}")
	public String deleteSkillByManager(Model model, @PathVariable int skillId) {
		skillRepository.deleteById(skillId);
		model.addAttribute("message", "one skill record is deleted");
		List<Skill> skillList = skillService.getAllSkills();
		model.addAttribute("skillList", skillList);
		model.addAttribute("count", skillList.size());
		return "employee_skill_list_by_manager";
	}

	@GetMapping("/skillListByManager/{id}")
	public String skillListAddedByRecruiter(Model model) {
		List<Skill> skillList = skillService.getAllSkills();
		Comparator<Skill> skillByName = Comparator.comparing(Skill::getSkillName);
		List<Skill> sortedSkills = skillList.stream().sorted(skillByName).collect(Collectors.toList());
		LOGGER.debug("sorted order:::" + sortedSkills);
		model.addAttribute("skillList", sortedSkills);
		model.addAttribute("count", skillList.size());
		return "employee_skill_list_by_manager";
	}

	@GetMapping("/eca/JdList/{id}")
	public String jdListbyClient(Model model, @PathVariable int id) {
		List<Skill> skillList = skillService.getAllSkills();
		model.addAttribute("skillList", skillList);
		return "employee_skill_list_by_manager";
	}

	@GetMapping("/clientJdList/{id}")
	public String jdListbyClients(Model model, @PathVariable int id, HttpSession session) {
		Client client = clientService.getClientById(id);
		List<JobDescription> jdList = jobDescriptionService.findAllActiveJobDescriptionByClient(client);
		Employee sessionEmployee = (Employee) session.getAttribute("employeeObj");
		model.addAttribute("sessionEmployee", sessionEmployee);
		model.addAttribute("objJobDescription", jdList);
		model.addAttribute("count", jdList.size());
		model.addAttribute("TableName",
				"Jobs Posted By Client " + client.getClientName() + " from Company ::" + client.getClientCompany());
		LOGGER.debug("jd list count:::");
		return "employee_client_jdlist";
	}

	@GetMapping("/selectedCandidatesListForJD")
	public String selectedCandidatesListForJD(Model model, HttpSession session) {
		LOGGER.debug("selectedCandidatesListForJD:::method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		List<Schedule> finalSelectedCandidatesList = objSchedule.stream().filter(
				scheduleObject -> scheduleObject.getStatus() != null && scheduleObject.getStatus().equals("SELECTED"))
				.toList();
		model.addAttribute("scheduleList", finalSelectedCandidatesList);
		model.addAttribute("count", finalSelectedCandidatesList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "FeedbackList:: SELECTED Candidates" + finalSelectedCandidatesList.size());
		System.out.println("selected candidates:: count" + finalSelectedCandidatesList.size());
		return "employee_jd_final_feedback_list";
	}

	@GetMapping("/rejectedCandidatesListForJD")
	public String rejectedCandidatesListForJD(Model model, HttpSession session) {
		LOGGER.debug(" rejecteddCandidatesListForJD/{id}::method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		List<Schedule> finalRejectedCandidatesList = objSchedule.stream().filter(
				scheduleObject -> scheduleObject.getStatus() != null && scheduleObject.getStatus().equals("REJECTED"))
				.toList();
		model.addAttribute("scheduleList", finalRejectedCandidatesList);
		model.addAttribute("count", finalRejectedCandidatesList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "FeedbackList:: REJECTED Candidates");
		return "employee_jd_final_feedback_list";
	}

	@GetMapping("/finalFeedbackList")
	public String finalFeedbackList(Model model, HttpSession session) {
		LOGGER.debug("finalfeedbackList method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		List<Schedule> finalFeedbackList = objSchedule.stream()
				.filter(scheduleObject -> scheduleObject.getStatus() != null &&( scheduleObject.getStatus().equals("REJECTED") || scheduleObject.getStatus().equals("SELECTED") ) ).toList();
		model.addAttribute("scheduleList", finalFeedbackList);
		model.addAttribute("count", finalFeedbackList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "Feedback List");
		return "employee_jd_final_feedback_list";
	}
//=============candidates and JD======================//

	// candidateList by jobDescription who are assigned to an employee
	@GetMapping("/listOfAppliedCandidates/{jdId}")
	public String canndidateListByJd(@PathVariable int jdId, HttpSession session, Model model) {
		LOGGER.debug("invoked::::::::::::::");
		List<CandidateJdAssociation> associationList = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> filteredList = associationList.stream()
				.filter(list -> list.getJobDescription().getJdId() == jdId).collect(Collectors.toList());
		ArrayList<Candidate> candidateList = new ArrayList<>();
		for (CandidateJdAssociation list : filteredList) {
			Candidate candidate = candidateService.getCandidateById(list.getCandidate().getCandidateId());
			candidateList.add(candidate);
		}
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("size", candidateList.size());
		model.addAttribute("jobDescription", jobDescription);
		LOGGER.debug("jdPoistion:::" + jobDescription.getPosition());
		return "employee_candidate_jd_list";
	}

	@GetMapping("/listOfSelectedCandidates/{jdId}")
	public String listOfSelectedCandidates(Model model, @PathVariable int jdId, HttpSession session) {
		LOGGER.debug("finalfeedbackList method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(employee);
		List<Schedule> finalFeedbackList = objSchedule.stream()
				.filter(scheduleObject -> scheduleObject.getStatus() != null
						&& scheduleObject.getStatus().equals("SELECTED")
						&& scheduleObject.getJobDescription().getJdId() == jdId)
				.toList();
		model.addAttribute("scheduleList", finalFeedbackList);
		model.addAttribute("count", finalFeedbackList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("scheduleTable", "Selected Candidates List");
		return "employee_jd_final_feedback_list";
	}

	@GetMapping("/listOfJdsAppliedByCandidates/{candidateId}")
	public String listOfJdsAppliedByCandidates(Model model, @PathVariable int candidateId, HttpSession session) {
		LOGGER.debug("finalfeedbackList method invoked:::::::::");
		Employee employee = (Employee) session.getAttribute("employeeObj");
		Candidate candidate = candidateService.getCandidateById(candidateId);
		List<CandidateJdAssociation> associationList = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> filteredList = associationList.stream()
				.filter(list -> list.getCandidate().getCandidateId() == candidateId).collect(Collectors.toList());
		System.out.println("can Jd applied List::count::" + filteredList.size());
		model.addAttribute("canJDAsosList", filteredList);
		model.addAttribute("count", filteredList.size());
		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("candidate", candidate);
		model.addAttribute("scheduleTable", "Candidate::" + candidate.getFirstName() + " Applied JOBS");
		return "employee_jd_list_applied_by_candidate";
	}

//Dashboard======URL==========================
	@GetMapping("/todaySchedules")
	public String todaySchedules(Model model, HttpSession session) {
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> allschedules = scheduleService.getAllSchedulesById(employee);
		List<Schedule> todaySchedules = allschedules.stream()
				.filter(scheduleobj -> scheduleobj.getDate().equals(LocalDate.now()))
				.toList();
		model.addAttribute("scheduleList", todaySchedules);
		model.addAttribute("size", todaySchedules.size());
		model.addAttribute("scheduleTable", "Today Schedules List");
		return "employee_schedule_list";

	}
	
	@GetMapping("/upcomingSchedules")
	public String upcomingSchedules(Model model, HttpSession session) {
		Employee employee = (Employee) session.getAttribute("employeeObj");
		List<Schedule> allSchedules = scheduleService.getAllSchedulesById(employee);
		List<Schedule> upComingSchedules = allSchedules.stream()
				.filter(scheduleobj -> scheduleobj.getStatus()==null && !(scheduleobj.equals(LocalDate.now())))
				.toList();
		System.out.println("upComingschedulesCount:::"+upComingSchedules.size());
		model.addAttribute("scheduleList", upComingSchedules);
		model.addAttribute("size", upComingSchedules.size());
		model.addAttribute("scheduleTable", "UpComing Schedules");
		return "employee_schedule_list";

	}
}
