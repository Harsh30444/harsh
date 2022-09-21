/**
 * @author Nikhil Gundla
 *
 */

package com.resourcing.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.Client;
import com.resourcing.beans.Department;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Position;
import com.resourcing.beans.Schedule;
import com.resourcing.service.CandidateJdAssociationService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.ClientService;
import com.resourcing.service.DepartmentService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.PositionService;
import com.resourcing.service.ScheduleService;
import com.resourcing.utilities.Utilities;

@Controller
@RequestMapping("/client")
public class ClientController {

	static Logger LOGGER = Logger.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CandidateJdAssociationService candidateJdAssociationService;

	@Autowired
	private EmployeeClientAssociationService employeeClientAssociationService;

	// View loginPage with username and password in clientLoginForm
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView clientLoginPage(Client loginClient) {
		ModelAndView mav = new ModelAndView("client_pages_login");
		mav.addObject("newClientDetails", loginClient);
		return mav;
	}

	@RequestMapping("/validateClient")
	public ModelAndView validatemethod(@ModelAttribute("newClientDetails") Client client, Model model, HttpServletRequest request) {
		LOGGER.debug("entered into validation method");
		String strEncPassword = Utilities.getEncryptSecurePassword(client.getPassword(), "GLAM");
		Client clientObj = clientService.findByUserNameIgnoreCaseAndPassword(client.getEmail(), strEncPassword);
		if (clientObj != null && (client.getCaptcha().equals(client.getClientCaptcha()))) {
			HttpSession session = request.getSession();
			session.setAttribute("clientObj", clientObj);
			ModelAndView mav = new ModelAndView("client_dashboard");
			List<JobDescription> allJds=jobDescriptionService.findAllJobDescriptionByClient(clientObj);
			List<JobDescription> activeJds=allJds.stream().filter(jdObj->jdObj.getIsActive()=='Y').toList();
			List<JobDescription> inActiveJds=allJds.stream().filter(jdObj->jdObj.getIsActive()=='N').toList();
			mav.addObject("allJdsCount", allJds.size());
			mav.addObject("ActiveJdsCount", activeJds.size());
			mav.addObject("inActiveJdsCount", inActiveJds.size());
			List<EmployeeClientAssociation> allAssocEmployees=employeeClientAssociationService.getAllEmployeeClientAssociations();
			List<EmployeeClientAssociation> myEmployees=allAssocEmployees.stream().filter(eca->eca.getClient().getClientId()==clientObj.getClientId()).toList();
			mav.addObject("myEmployeesCount", myEmployees.size());
			LOGGER.debug("client dashboard  is displayed");
			return mav;
		} else {
			LOGGER.debug("else block of validation:::: Use valid details");
			ModelAndView mav = new ModelAndView("client_pages_login");
			LOGGER.debug("invalid credentials");
			model.addAttribute("s1", "Invalid Credentials,Try Again");
			model.addAttribute("newClientDetails", new Client());
			return mav;
		}
	}


	// ==========================forgot password and reset======================//
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView UserforgotPasswordPage(Client newUser) {
		LOGGER.debug("entered into user/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("client_forgotPassword");
		mav.addObject("newClientDetails", newUser);
		return mav;
	}

	@PostMapping(value = "/validateEmailId")
	public String checkMailId(Model model, Client tempUser) {
		LOGGER.debug("entered into user/controller::::check EmailId existing or not");
		LOGGER.debug("UI given mail Id:" + tempUser.getEmail());
		Client pUser = clientService.findByEmail(tempUser.getEmail());
		if (pUser != null) {
			String s1 = "";
			model.addAttribute("message", s1);
			LOGGER.debug("UI given mail Id:" + pUser.getEmail());
			model.addAttribute("newClientDetails", tempUser);
			return "client_resetPassword";
		} else {
			LOGGER.debug("Invalid Mail");
			String s1 = "Email-Id Not Exists";
			model.addAttribute("message", s1);
			model.addAttribute("newClientDetails", new Client());
			return "client_forgotPassword";
		}

	}

	@RequestMapping(value = "/updateClientPassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("newClientDetails") Client tempUser) {
		Client pUser = clientService.findByEmail(tempUser.getEmail());
		LOGGER.debug("inside updateClientPassword after update id is:::" + tempUser.getClientId());
		LOGGER.debug("password:::" + tempUser.getPassword());
		String strEncPassword = Utilities.getEncryptSecurePassword(tempUser.getPassword(), "GLAM");
		tempUser.setPassword(strEncPassword);
		pUser.setPassword(tempUser.getPassword());
		LOGGER.debug(" in update method Client created date::" + pUser.getCreatedDate());
		LOGGER.debug("in update method pUser:: name " + pUser.getClientName());
		clientService.updateClient(pUser);
		pUser.setUpdatedDate(LocalDateTime.now());
		LOGGER.debug("password is updated sucessfully");
		ModelAndView mav = new ModelAndView("client_pages_login");
		LOGGER.debug("login page is displayed");
		model.addAttribute("newClientDetails", pUser);
		return mav;
	}

	// ==========================forgot password and reset======================//

//=============================================================basic validation completed=============================================				

	// Go To Dashboard Directly
	@GetMapping("/GoToDashboard/{id}")
	public ModelAndView goToDashboard(Model model, @PathVariable("id") int clientId, JobDescription jobDescription,HttpSession session,
			Client uiClient) {
		Client clientObj = (Client) session.getAttribute("clientObj");
		ModelAndView mav = new ModelAndView("client_dashboard");
		List<JobDescription> allJds=jobDescriptionService.findAllJobDescriptionByClient(clientObj);
		List<JobDescription> activeJds=allJds.stream().filter(jdObj->jdObj.getIsActive()=='Y').toList();
		List<JobDescription> inActiveJds=allJds.stream().filter(jdObj->jdObj.getIsActive()=='N').toList();
		List<EmployeeClientAssociation> allAssocEmployees=employeeClientAssociationService.getAllEmployeeClientAssociations();
		List<EmployeeClientAssociation> myEmployees=allAssocEmployees.stream().filter(eca->eca.getClient().getClientId()==clientObj.getClientId()).toList();
		mav.addObject("allJdsCount", allJds.size());
		mav.addObject("ActiveJdsCount", activeJds.size());
		mav.addObject("inActiveJdsCount", inActiveJds.size());
		mav.addObject("myEmployeesCount", myEmployees.size());
		LOGGER.debug("client dashboard  is displayed");
		return mav;
	}

	// Go to Client Details Edit Page
	@GetMapping("/updateClientDetails/{id}")
	public String UpdateClientById(Model model, @PathVariable("id") int clientId, JobDescription jobDescription,
			HttpSession session) {
		Client tclient = clientService.getClientById(clientId);
		LOGGER.debug("inside updateClient id is:::" + tclient.getClientId());
		model.addAttribute("editClientDetails", tclient);
		return "client_dashboard_settings";
	}

	@PostMapping("/saveUpdatedGoToDashboard/{id}")
	public String saveUpdatesdGoToManage(Model model, @PathVariable("id") int clientId, JobDescription jobDescription,
			@RequestParam("file") MultipartFile file, @ModelAttribute("editClientDetails") Client uiClient,
			HttpSession session) throws IOException {
		Client tclient = clientService.getClientById(uiClient.getClientId());
		LOGGER.debug("inside updateClient after update id is:::" + uiClient.getClientId());
		uiClient.setIsActive("Y");
		//uiClient.setBranch(tclient.getBranch());
		uiClient.setUpdatedby(clientId);
		uiClient.setUser(tclient.getUser());
		uiClient.setUpdatedDate(LocalDateTime.now());
		uiClient.setCreatedDate(tclient.getCreatedDate());
		uiClient.setCompanyProfile(tclient.getCompanyProfile());
		uiClient.setClientCompany(tclient.getClientCompany());
		if (file.getOriginalFilename() == "") {
			uiClient.setImage(tclient.getImage());
		} else {
			uiClient.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
		}
		LOGGER.debug("From Object got by client::Client Company is::::::::::" + tclient.getClientCompany());
		uiClient.setCreatedby(tclient.getCreatedby());
		LOGGER.debug("From Object got by client is::::::::::" + tclient.getMobile());
		clientService.updateClient(uiClient);
		LOGGER.debug("From Object got by client is::::::::::" + uiClient.getMobile());
		model.addAttribute("editClientDetails", tclient);
		return "client_dashboard_settings";
	}

	// Go To Manage Jobs Page
	@GetMapping(value = "/dashboard-manage-jobs/{id}")
	public String getManageJobsPage(Model model, @PathVariable("id") int ClientId, JobDescription jobDescription) {
		Client objClient = clientService.getClientById(ClientId);
		LOGGER.debug("Inside the Posting a Job with Client Id:::::::::" + objClient.getClientId());
		LOGGER.debug("inside post-a-job page");
		model.addAttribute("jobDescription", jobDescription);
		model.addAttribute("objClient", objClient);
		return "client_dashboard_manage_jobs";
	}

//  To Get Job Descriptions List	
	@GetMapping("/jdList")
	public String getJdList(Model model) {
		List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
		model.addAttribute("objJobDescription", jdList);
		return "client_dashboard_jobList";
	}

	@GetMapping("/jdList/{id}")
	public String getJdListById(Model model, @PathVariable("id") int clientId, JobDescription jobDescription,
			HttpSession session) {
		Client client = clientService.getClientById(clientId);
		List<JobDescription> jdList = jobDescriptionService.findAllJobDescriptionByClient(client);
		Comparator<JobDescription> isActive = Comparator.comparing(JobDescription::getIsActive);
		List<JobDescription> sortedList = jdList.stream().sorted(isActive.reversed()).collect(Collectors.toList());
		model.addAttribute("objJobDescription", sortedList);
		model.addAttribute("TableName", jdList.size() + " Jobs Posted By " + client.getClientName().toUpperCase()
				+ " from " + client.getClientCompany().toUpperCase());
		return "client_dashboard_jobList";
	}

//====================================For DropDown==============================================//

	@GetMapping(value = "/dashboard-post-a-job/{id}")
	public String getAllJds(Model model, @PathVariable("id") int clientId, JobDescription jobDescription) {
		Client objClient = clientService.getClientById(clientId);
		LOGGER.debug("Inside the Posting a Job with Client Id:::::::::" + objClient.getClientId());
		LOGGER.debug("inside post-a-job page");
		List<Department> departmentList = departmentService.getAllDepartment();
		model.addAttribute("departmentList", departmentList);
		model.addAttribute("objClient", objClient);
		// return "test";
		return "client_dashboard_post_a_job";
	}

	// To Save JD and Go to Manage
	@RequestMapping(value = "/saveJdGoToManage", method = RequestMethod.POST)
	public String addJd(Model model, @ModelAttribute("jobDescription") JobDescription jobDescription,
			final HttpServletRequest request) {
		System.out.println("clientId" + jobDescription.getClient().getClientId());
		int id = jobDescription.getClient().getClientId();
		Client objClient = clientService.getClientById(id);
		model.addAttribute("clientDetails", objClient);
		List<Department> departmentList = departmentService.getAllDepartment();
		model.addAttribute("departmentList", departmentList);
		String getQry = request.getParameter("getQry");
		String deptName = request.getParameter("deptName");
		LOGGER.debug("getQry::::" + getQry);
		LOGGER.debug("deptId::::" + deptName);
		if (getQry != null && getQry.equals("getposition") && deptName != null && deptName != "") {
			System.out.println("if::::::::");
			List<Position> positionList = positionService.findPositionByDepartment(deptName);
			model.addAttribute("positionList", positionList);
			LOGGER.debug("Inside the Posting a Job with Client Id:::::::::" + objClient.getClientId());
			LOGGER.debug("inside post-a-job page");
			model.addAttribute("departmentList", departmentList);
			model.addAttribute("objClient", objClient);
			// return "test";
			return "client_dashboard_post_a_job";
		}
		List<Position> positionList = positionService.findPositionByDepartment(deptName);
		System.out.println("Getting Position List::::::" + positionList);
		model.addAttribute("positionList", positionList);
		model.addAttribute("jobDescription", jobDescription);
		jobDescription.setCreatedDate(
				LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + ", " + LocalDate.now().getYear());
		jobDescription.setCreatedby(id);
		jobDescription.setUpdatedby(id);
		jobDescription.setIsActive('Y');
		jobDescription.setSkills(jobDescription.getSkills().toUpperCase().replaceAll("\s", ""));
		jobDescription.setInterviewPhase(jobDescription.getInterviewPhase().toUpperCase().replaceAll("\s", ""));
		// At the tine of Jd posting, No of positions filled will be zero
		JobDescription newJobDescription = jobDescriptionService.addJobDescription(jobDescription);
		List<JobDescription> jdList = jobDescriptionService.findAllActiveJobDescriptionByClient(objClient);
		model.addAttribute("jobDescription", newJobDescription);
		model.addAttribute("objJobDescription", jdList);
		return "client_dashboard_jobList";
	}

	@GetMapping(value = "/getPosition", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Position> getPosition(@RequestParam String deptName) {
		List<Position> list = positionService.findPositionByDepartment(deptName);
		LOGGER.debug("deptname " + deptName);
		return list;
	}

	@GetMapping("/selectedCandidates/{JdId}")
	public String selectedCandidatesByJd(@PathVariable int jdId, Model model) {
		List<Schedule> candidateList = scheduleService.getAllSchedules();
		List<Schedule> filteredList = candidateList.stream().filter(jd -> jd.getJobDescription().getJdId() == jdId)
				.collect(Collectors.toList());
		List<Schedule> feedbackList = filteredList.stream()
				.filter(feedback -> feedback.getFeedback().equalsIgnoreCase("selected")).collect(Collectors.toList());
		model.addAttribute("feedbackList", feedbackList);
		return "client_candidate_feedback";
	}

	// =========methods for dashboard having all the details about the Schedule//
	// ==========================================================================//

	@GetMapping("/allJdsList")
	public String getJdList(Model model, HttpSession session) {
		Client client = (Client) session.getAttribute("clientObj");
		List<JobDescription> jdList = jobDescriptionService.findAllActiveJobDescriptionByClient(client);
		model.addAttribute("objJobDescription", jdList);
		System.out.println("allJdsPosted::" + jdList.size());
		model.addAttribute("TableName", jdList.size() + " JOBs are posted by you!! ");
		return "client_dashboard_jobList";
	}

	@GetMapping("/appliedCandidates/{jdId}")
	public String appliedCandidatesForJd(Model model, HttpSession session, @PathVariable int jdId) {
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> canJdAs = list.stream().filter(obj -> obj.getJobDescription().getJdId() == jdId).collect(Collectors.toList());
		model.addAttribute("candidate", canJdAs);
		model.addAttribute("TableName", canJdAs.size() + " Candidates are applied for " + jobDescription.getPosition());
		return "client_jd_status";
	}

	@GetMapping("/detailViewOfSchedule/{candidateId}/{jdId}")
	public String detaileViewOfSchedule(@PathVariable int candidateId, @PathVariable int jdId, Model model) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<Schedule> schedule = scheduleService.listOfCandidateAndJdAssociation(candidate, jobDescription);
		model.addAttribute("schedule", schedule);
		model.addAttribute("TableName",
				"Candidate Feedback for all Interview Phases for " + jobDescription.getPosition().toUpperCase());
		return "client_jd_feedback";
	}

	@GetMapping("/activeJdList")
	public String activeJdList(Model model, HttpSession session) {
		Client client = (Client) session.getAttribute("clientObj");
		List<JobDescription> jdList = jobDescriptionService.findAllActiveJobDescriptionByClient(client);
		model.addAttribute("objJobDescription", jdList);
		model.addAttribute("TableName", jdList.size() + " Active JOBs !! ");
		return "client_dashboard_jobList";
	}

	@GetMapping("/inActiveJdList")
	public String inActiveJdList(Model model, HttpSession session) {
		Client client = (Client) session.getAttribute("clientObj");
		List<JobDescription> list = jobDescriptionService.findInActiveJobDescriptionByClient(client);
		model.addAttribute("objJobDescription", list);
		model.addAttribute("TableName", list.size() + " inActive JOBs !! ");
		return "client_dashboard_jobList";
	}

	@GetMapping("/employeesList")
	public String employeeList(Model model, HttpSession session) {
		Client client = (Client) session.getAttribute("clientObj");
		List<EmployeeClientAssociation> list = employeeClientAssociationService.findEmpListByClient(client);
		model.addAttribute("empList", list);
		model.addAttribute("TableName", list.size() + " Employees assigned to you !! ");
		return "client_dashboard_employee_list";
	}

	
	// ================== Dashboard Closed =================================//

}
