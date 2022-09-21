/* 
 * Author : Srivani Tudi */

package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.resourcing.beans.Branch;
import com.resourcing.beans.Candidate;
import com.resourcing.beans.Client;
import com.resourcing.beans.Company;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.SkillInterviewerAssosiation;
import com.resourcing.beans.User;
import com.resourcing.service.BranchService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.ClientService;
import com.resourcing.service.EducationService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.EmploymentService;
import com.resourcing.service.InterviewPanelService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.ScheduleService;
import com.resourcing.service.SkillInterviewerAssociationService;
import com.resourcing.service.SkillService;
import com.resourcing.service.UserService;
import com.resourcing.utilities.Utilities;

@Controller
@RequestMapping("/user")

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	BranchService branchService;

	@Autowired
	SkillService skillService;

	@Autowired
	EmploymentService employmentService;

	@Autowired
	InterviewPanelService interviewPanelService;

	@Autowired
	EducationService educationService;

	@Autowired
	JobDescriptionService jobDescriptionService;
	@Autowired
	ClientService clientService;

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	private EmployeeClientAssociationService employeeClientAssociationService;

	@Autowired
	private SkillInterviewerAssociationService skillInterviewerAssociationService;

	static Logger LOGGER = Logger.getLogger(UserController.class);

	// View loginPage with username and password in userLoginForm.html
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView employeeloginPage(User loginUser, HttpSession session, Object String) {
		LOGGER.debug("entered into user/controller:::: login method");
		ModelAndView mav = new ModelAndView("user_login_page");
		mav.addObject("newUserDetails", loginUser);
		return mav;
	}

	@PostMapping("/validateUser")
	public ModelAndView validatemethod(User objUser, HttpSession session, Model model) {
		LOGGER.debug("entered into validation method");
		LOGGER.debug("Before::controller validation method user mail id  is:" + objUser.getEmailId());
		String strEncPassword = Utilities.getEncryptSecurePassword(objUser.getPassword(), "RESOURCING");
		User userObj = userService.findByUserNameIgnoreCaseAndPassword(objUser.getEmailId(), strEncPassword);
		if (userObj != null) {
			session.setAttribute("userObj", userObj);
			session.setAttribute("userName", userObj.getUserName());
			session.setAttribute("userRole", userObj.getUserRole());
			session.setAttribute("userId", userObj.getUserId());
			session.setAttribute("branchName", userObj.getBranch().getBranchName());
			session.setAttribute("branchId", userObj.getBranch().getBranchId());
			session.setAttribute("companyName", userObj.getBranch().getCompany().getCompanyName());
			session.setMaxInactiveInterval(10 * 60);
			LOGGER.debug("name" + session.getClass());
			LOGGER.debug("max time interval" + session.getMaxInactiveInterval());
			LOGGER.debug("last access time" + session.getLastAccessedTime());
			LOGGER.debug("creation time" + session.getCreationTime());
			LOGGER.debug("attribute Names" + session.getAttributeNames());
			LOGGER.debug("after:::controller validation method user mail id  is:" + userObj.getEmailId());
			LOGGER.debug("controller validation method user password id  is:" + userObj.getPassword());
			ModelAndView mav = new ModelAndView("user_dashboard");
			LOGGER.debug("dashboard page is displayed");
//			mav.addObject("sessionUser", sessionUser);
			mav.addObject("candidatesCount", candidateService.getAllCandidates().size());
			mav.addObject("jobsCount", jobDescriptionService.getAllJobDescriptions().size());
			mav.addObject("clientsCount", clientService.getAllClients().size());
			mav.addObject("slectedCandidatesCount", scheduleService.selectedCandidateList().size());
			LOGGER.debug("user company Name::" + userObj.getBranch().getCompany().getCompanyName());
			return mav;
		} else {
			LOGGER.debug("else block of validation:::: Use valid details");
			ModelAndView mav = new ModelAndView("user_login_page");
			mav.addObject("newUserDetails", new User());
			mav.addObject("errorMessage", "Invalid credentials or May not be Registered");
			return mav;
		}
	}

	// User Dashboard
	@RequestMapping(value = "/userDashboard", method = RequestMethod.GET)
	public ModelAndView userDashboard(Model model, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		ModelAndView mav = new ModelAndView("user_dashboard");
		LOGGER.debug("dashboard page is displayed");
		mav.addObject("sessionUser", sessionUser);
		mav.addObject("candidatesCount", candidateService.getAllCandidates().size());
		mav.addObject("jobsCount", jobDescriptionService.getAllJobDescriptions().size());
		mav.addObject("clientsCount", clientService.getAllClients().size());
		mav.addObject("slectedCandidatesCount", scheduleService.selectedCandidateList().size());
		return mav;
	}

	// UserDashboard Settings page
	@RequestMapping(value = "/user-dasboard-settings/{id}", method = RequestMethod.GET)
	public ModelAndView userDasboardSettingPage(@PathVariable(value = "id") int id, User loginUser, Model model,
			HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller:::: dashboard setting page will display");
		User userObj = userService.getUserById(id);
		LOGGER.debug("id::::" + id);
		LOGGER.debug("userObj Id:::" + userObj.getUserName());
		ModelAndView mav = new ModelAndView("user_dashboard_settings");
		mav.addObject("newUserDetails", userObj);
		mav.addObject("sessionUser", sessionUser);
		return mav;
	}

	// update User Details
	@RequestMapping(value = "/updateUserDetails", method = RequestMethod.POST)
	public ModelAndView saveUserDetails(Model model, @ModelAttribute("newUserDetails") User userObj,
			HttpSession session) {
		User existUser = userService.findByUserNameIgnoreCaseAndPassword(userObj.getEmailId(), userObj.getPassword());
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");

		LOGGER.debug(" in update method employee created date::" + userObj.getCreatedDate());
		LOGGER.debug("in update method userObj:: name " + userObj.getUserName());
		userObj.setUpdatedBy(existUser.getUserId());
		userObj.setBranch(existUser.getBranch());
		userObj.setUpdatedDate(LocalDateTime.now());
		userObj.setCreatedBy(existUser.getCreatedBy());
		userObj.setCreatedDate(existUser.getCreatedDate());
		userService.updateUser(userObj);
		LOGGER.debug("fields are updated sucessfully");
		ModelAndView mav = new ModelAndView("user_dashboard");
		LOGGER.debug("dashboard page is displayed");
		model.addAttribute("userName", userObj.getUserName());
		mav.addObject("newUserDetails", userObj);
		mav.addObject("message", "details updated");
		mav.addObject("sessionUser", sessionUser);

		return mav;
	}

//=============================================================basic validation completeed=============================================	//

// ==================branch and Usr Association===============================//
	// BranchList
	@GetMapping("/BranchsList")
	public String viewHomePages(Model model, Branch branchObj, Company companyObj, Employee newEmployee,
			HttpSession session) {
		session.getAttribute("userObj");
		String companyName = (String) session.getAttribute("companyName");
		User sessionUser = (User) session.getAttribute("userObj");
		List<Branch> branch = branchService.getAllBranchs();
		model.addAttribute("listBranches", branch);
		model.addAttribute("companyObj", companyObj);
		model.addAttribute("newUserDetails", newEmployee);
		session.getAttribute("userObj");
		List<String> branchNames = branchService.getBranchNamesByActiveStatus();
		LOGGER.debug("branchNames" + branchNames);
		List<Branch> branchList = branchService.getByIsActive();
		model.addAttribute("branch", branchList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("TableName", "List of Branches in " + companyName);
		return "user_branchlist";
	}

//===============================user and Employee Association==================================================================//			

	// Add New Employee in any Branch
	@RequestMapping(value = "/addEmployeeForm", method = RequestMethod.GET)
	public ModelAndView employeeRegisterForm(Branch branch, Employee newEmployee, Model model, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller::::add employee method");
		List<Branch> branchList = branchService.getByIsActive();
		LOGGER.debug("branchNames" + branchList);
		ModelAndView mav = new ModelAndView("user_add_employee_form");
		mav.addObject("branch", branchList);
		mav.addObject("newUserDetails", newEmployee);
		mav.addObject("sessionUser", sessionUser);
		return mav;
	}

	// all Recruiters List of User Branch
	@GetMapping(value = "/allemployeesList")
	public String getAllEmployeesOfThisBranch(Model model, Branch branch, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		String branchName = (String) session.getAttribute("branchName");
		LOGGER.debug("inside getAllEmployees this will get the all employees:::");
		List<Branch> branchList = branchService.getByIsActive();
		int branchId = (int) session.getAttribute("branchId");
//		List<Employee> allEmpList = employeeService.getEmployeesListofThisbranch();
		List<Employee> allEmpList = employeeService.allEmployeesList();
		Stream<Employee> empStream = allEmpList.stream();
		List<Employee> empList = empStream.filter(employee -> employee.getBranch().getBranchId() == (branchId))
				.toList();
		LOGGER.debug("branchNames" + branchList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("listOfEmployees", empList);
		model.addAttribute("TableName", "Employees List  Of " + branchName);
		model.addAttribute("branch", branchList);
		return "user_dashboard_employeelist";
	}

	// All Employees List of Company
	@GetMapping(value = "/allEmployeesList")
	public String getAllEmployeesOfCompany(Model model, Branch branch, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
//		LOGGER.debug("user compant Name::"+sessionUser.branch.getCompany().getCompanyName());
		LOGGER.debug("inside getAllEmployees this will get the all employees:::");
		List<Employee> allEmpList = employeeService.allEmployeesList();
		List<Branch> branchList = branchService.getByIsActive();
		LOGGER.debug("branchNames" + branchList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("listOfEmployees", allEmpList);
		model.addAttribute("TableName", "All Employees List Of Company");
		model.addAttribute("branch", branchList);
		return "user_dashboard_employeelist";
	}

	// Employees List of this branch
	@GetMapping(value = "/employeesList/{id}")
	public String getAllEmployeesListOfBranch(@PathVariable(value = "id") int id, HttpSession session, Model model) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		Branch branch = branchService.getBranchById(id);
		LOGGER.debug("Employee List by branchId:::" + id);
		List<String> branchNames = branchService.getBranchNamesByActiveStatus();
		LOGGER.debug("branchNames in employee list " + branchNames);
		LOGGER.debug("inside getAllEmployees this will get the all employees:::");
		List<Employee> allEmpList = employeeService.getEmployeesListofThisbranch(branch);
//		Stream<Employee> empStream = allEmpList.stream();
		List<Employee> empList = employeeService.allEmployeesList();
		LOGGER.debug("Employee List of this branch" + empList);
		model.addAttribute("listOfEmployees", allEmpList);
		model.addAttribute("branch", branch);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("branchNames", branchNames);
		model.addAttribute("TableName", "Employees List  Of " + branch.branchName);
		return "user_dashboard_employeelist";
	}

	// Recruiters List of this branch
	@GetMapping(value = "/employeesList/recruiters")
	public String getRecruitersList(HttpSession session, Model model) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		LOGGER.debug("We get Recruiters List of this Branch:::");
		List<Employee> allEmpList = employeeService.getEmployeesListofThisbranch(branch);
		Stream<Employee> empStream = allEmpList.stream();
		List<Employee> empList = empStream.filter(employee -> employee.getEmployeeRole().equals("Recruiter")).toList();
		LOGGER.debug("Employee List of this branch" + empList);
		model.addAttribute("listOfEmployees", empList);
		model.addAttribute("branch", branch);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("TableName", "All Recruiters Of " + branch.branchName);
		return "user_dashboard_employeelist";
	}

	// all Interviewers list
	@GetMapping(value = "/employeesList/interviewers")
	public String getInterviewersList(HttpSession session, Model model) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("We get Interviewwers List of this Branch:::");
		String companyName = (String) session.getAttribute("companyName");
		List<InterviewPanel> interviewersList = interviewPanelService.getAllInterviewers();
		List<SkillInterviewerAssosiation> csaList = skillInterviewerAssociationService
				.getAllSkillInterviewerAssociations();
		LOGGER.debug("interviewersList ::" + interviewersList.size());
		model.addAttribute("interviewersList", interviewersList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("csaList", csaList);
		model.addAttribute("TableName", "All Interviewers List of " + companyName);
		return "user_dashboard_interviewerslist";
	}

	// Delete Employee
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") Integer id, Employee employee, Model model,
			HttpSession session) {
		session.getAttribute("userObj");
		int userId = (int) session.getAttribute("userId");

		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("delete employee By user:: employee Id from UI is:::::::" + id);
		Employee employeeObj = employeeService.getEmployeeById(id);
		employeeObj.setIsActive("N");
		employeeObj.setUpdatedBy(userId);
		employeeObj.setUpdatedDate(LocalDateTime.now());
		employeeService.updateEmployee(employeeObj);
		LOGGER.debug("employee deleted sucessfully::" + id);
		return "redirect:/user/employeesList";
	}

//=====================update Employee details to add bank account========================//

	// update Employee basic Details page
	@RequestMapping(value = "/updateEmployeePersonalDetailsForm/{id}", method = RequestMethod.GET)
	public ModelAndView employeeDasboardPersonalDetails(@PathVariable(value = "id") int id, User loginUser,
			@ModelAttribute("employeeObj") Employee employee, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user---controller:::: update employee :::: userName" + loginUser.userName);
		Employee empObj = employeeService.getEmployeeById(id);
		LOGGER.debug("userObj verified employee name:::" + empObj.getEmployeeName());
		LOGGER.debug("employeeName" + empObj.getEmployeeName());
		LOGGER.debug("employeeId" + empObj.getEmployeeId());
		LOGGER.debug("branchId" + empObj.branch.getBranchId());
		LOGGER.debug("company" + empObj.branch.getCompany().getCompanyName());
		LOGGER.debug("userObj verified employee branch Id:::" + empObj.branch.getBranchId());
		ModelAndView mav = new ModelAndView("user_update_employee_details");
		mav.addObject("employeeObj", empObj);
		mav.addObject("branch", empObj.branch);
		mav.addObject("sessionUser", sessionUser);
		return mav;
	}

	// update employee Details
	@RequestMapping(value = "/updateEmployeeDetails", method = RequestMethod.POST)
	public ModelAndView updateEmployeeDetails(Model model, @ModelAttribute("newUserDetails") Employee employee,
			HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		Employee employeeObj = employeeService.getEmployeeById(employee.getEmployeeId());
		LOGGER.debug(" in update method employee created date::" + employeeObj.getCreatedDate());
		LOGGER.debug("in update method userObj:: name " + employeeObj.getEmployeeName());
		LOGGER.debug("branch::::"+employeeObj.getBranch());
		employee.setUpdatedDate(LocalDateTime.now());
		employee.setIsActive("Y");
		employee.setBranch(employeeObj.getBranch());
		employeeService.updateEmployee(employee);
		LOGGER.debug(" Employee fields are updated sucessfully");
		ModelAndView mav = new ModelAndView("employee_dashboard");
		LOGGER.debug("dashboard page is displayed");
		model.addAttribute("userName", employeeObj.getEmployeeName());
		mav.addObject("newUserDetails", employeeObj);
		mav.addObject("sessionUser", sessionUser);
		return mav;
	}

//============================forgot password   and Logout ==============================//

	// User Forgot Password Page
	@RequestMapping(value = "/userForgotPassword", method = RequestMethod.GET)
	public ModelAndView UserforgotPasswordPage(User newUser, HttpSession session) {
		session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("user_forgot_password");
		mav.addObject("newUserDetails", newUser);
		return mav;
	}

	// Validate User Email
	@PostMapping(value = "/validateEmailId")
	public String checkMailId(Model model, User userObj) {
//		session.getAttribute("userObj");
//		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller::::check EmailId existing or not");
		LOGGER.debug("UI given mail Id:" + userObj.getEmailId());
		User user = userService.findByEmailId(userObj.getEmailId());
		if (user != null) {
			LOGGER.debug("UI given mail Id:" + userObj.getEmailId());
			model.addAttribute("newUserDetails", user);
			return "user_reset_password";
		} else {
			LOGGER.debug("UI given mail Id:" + userObj.getEmailId());
			model.addAttribute("message", "email doesn't exist...!");
			model.addAttribute("newUserDetails", userObj);
			return "user_forgot_password";
		}
	}

	// Update Password
	@RequestMapping(value = "/updateUserPassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("newUserDetails") User userObj) {
		LOGGER.debug("user controller::: update password method");
		User userObjExist = userService.findByEmailId(userObj.getEmailId());
		userObjExist.setUpdatedDate(LocalDateTime.now());
		String strEncPassword = Utilities.getEncryptSecurePassword(userObj.getPassword(), "RESOURCING");
		userObj.setPassword(strEncPassword);
		userObjExist.setPassword(userObj.getPassword());
		userService.updateUser(userObjExist);
		LOGGER.debug("password is updated sucessfully");
		LOGGER.debug("password is updated sucessfully");
		ModelAndView mav = new ModelAndView("user_login_page");
		mav.addObject("newUserDetails", userObjExist);
		mav.addObject("passwordUdatedMessage", "paswword updated successfully!");
		LOGGER.debug("login page is displayed");
		return mav;
	}

	// Logout //End of Session::: becomes null
	@RequestMapping(value = "/userLogout", method = RequestMethod.GET)
	public ModelAndView UserLogout(HttpSession session, User newUser) {
		LOGGER.debug("entered into user/controller::::logged out");
		ModelAndView mav = new ModelAndView("redirect:/resourcing");
		mav.addObject("newUserDetails", newUser);
		session.setAttribute("userObj", null);
		return mav;
	}

//=============================candidate Association=========================================================================//
	// get newly Registered Candidate
	@GetMapping("/newCandidateList")
	public String getAllCandidates(Model model, HttpSession session, Employee employee) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		model.addAttribute("newUserDetails", sessionUser);
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<Candidate> candidateList = candidateService.newCandidatesList();
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("employeeObj", employee);
		model.addAttribute("CandidateTableTile", "List of New Candidates");
		List<Employee> employeeList = employeeService.getActiveRecruitersListOfThisbranch(branch);
		LOGGER.debug("recruitersList of this branch::" + employeeList.size());
		model.addAttribute("employeeList", employeeList);
		return "user_dashboard_newCandidatelist";
	}

	// Assigning Candidate to Some Employee with Role of Recruiter who is active
	@RequestMapping(value = "/assignCandidateToEmployee/{candidateId}", method = RequestMethod.POST)
	public ModelAndView assignEmployeeToCandidate(Model model, @ModelAttribute("candidateList") Candidate candidateList,
			@ModelAttribute("employeeList") Employee employeeObj, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		int userId = (int) session.getAttribute("userId");
		Candidate candidateExist = candidateService.getCandidateById(candidateList.getCandidateId());
		Employee employeeExist = employeeService.getEmployeeById(employeeObj.getEmployeeId());
		candidateExist.setUpdatedDate(LocalDateTime.now());
		candidateExist.setUpdatedBy(userId);
		candidateExist.setEmployeeObj(employeeExist);
		candidateService.updateCandidate(candidateExist);
		model.addAttribute("sessionUser", sessionUser);
		List<Employee> employeeList = employeeService.getActiveRecruitersListOfThisbranch(branch);
		List<Candidate> candidatesList = candidateService.newCandidatesList();
		model.addAttribute("candidateList", candidatesList);
		model.addAttribute("assignedCandidateName", candidateExist.getFirstName());
		model.addAttribute("assignedRecruiterName", employeeExist.getEmployeeName());
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("CandidateTableTile", "List of New Candidates");
		model.addAttribute("assignSuccessMessage", " Successfully Assigned to  ");
		ModelAndView mav = new ModelAndView("user_dashboard_newCandidatelist");
		return mav;
	}
//	

	// Reassign Candidate to Some Employee with Role of Recruiter who is active
	@RequestMapping(value = "/reAssignCandidateToEmployee/{candidateId}", method = RequestMethod.POST)
	public ModelAndView reAssignEmployeeToCandidate(Model model,
			@ModelAttribute("candidateList") Candidate candidateList,
			@ModelAttribute("employeeList") Employee employeeObj, HttpSession session) {
		session.getAttribute("userObj");
		String branchName = (String) session.getAttribute("branchName");
		int userId = (int) session.getAttribute("userId");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("candidateId:::" + candidateList.getCandidateId());
		Candidate candidateExist = candidateService.getCandidateById(candidateList.getCandidateId());
		LOGGER.debug("candidateExist:: Name:" + candidateExist.getFirstName());
		LOGGER.debug("candidateExist:: mobile:" + candidateExist.getMobileNumber());
		LOGGER.debug("employeeList:: id:" + employeeObj.employeeId);
		candidateExist.setUpdatedDate(LocalDateTime.now());
		candidateExist.setEmployeeObj(employeeObj);
		candidateExist.setUpdatedBy(userId);
		candidateService.updateCandidate(candidateExist);
		LOGGER.debug("Candidate updated");
		Candidate c = candidateService.getCandidateById(candidateExist.getCandidateId());
		LOGGER.debug("after update:: candidate id:" + c.getCandidateId());
		LOGGER.debug("after update:: candidate Name:" + c.getFirstName());
		ModelAndView mav = new ModelAndView("user_dashboard_assignedCandidatelist");
		List<Employee> employeeList = employeeService.getActiveRecruitersListOfThisbranch(branch);
		List<Candidate> candidatesList = candidateService.getassignedCandidateList();
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("candidateList", candidatesList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("assignedCandidateName", candidateExist.getFirstName());
		model.addAttribute("assignedRecruiterName", employeeObj.getEmployeeName());
		model.addAttribute("assignSuccessMessage", " Successfully Re_Assigned to  ");
		model.addAttribute("TableName", " CandidateList Assigned to Recruiters of " + branchName);
		return mav;
	}

	// get Assigned Candidates List
	@RequestMapping(value = "/assignedCandidateList", method = RequestMethod.GET)
	public String assignedCandidateList(Model model, HttpSession session) {
		session.getAttribute("userObj");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		User sessionUser = (User) session.getAttribute("userObj");
		String branchName = (String) session.getAttribute("branchName");
		List<Employee> employeeList = employeeService.getActiveRecruitersListOfThisbranch(branch);
		List<Candidate> candidateList = candidateService.getassignedCandidateList();
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("TableName", " CandidateList Assigned to Recruiters of " + branchName);
		return "user_dashboard_assignedCandidatelist";
	}

	// List of Candidates Assigned to Employee having Role of Recruiter
	@GetMapping("/candidateList/{id}")
	public String getAssignedCanididateListOfEmployee(Model model, @PathVariable("id") int id, HttpSession session) {
		session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller:::: ");
		User sessionUser = (User) session.getAttribute("userObj");
		Employee employeeExist = employeeService.getEmployeeById(id);
		List<Candidate> candidateList = candidateService.getCandidateListOfRecruiter(employeeExist);
		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("candidateList", candidateList);
		model.addAttribute("TableName", "Candidates Assigned To  " + employeeExist.getEmployeeName());
		return "user_dashboard_assignedCandidatelist";
	}

//===========================manage EmployeeListt===========================================================================//
	//
	@RequestMapping(value = "/manageEmployees", method = RequestMethod.GET)
	public ModelAndView manageEmployeesPage(HttpSession session) {
		session.getAttribute("userObj");
		String branchName = (String) session.getAttribute("branchName");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller:::: ");
		int branchId = (int) session.getAttribute("branchId");
		Branch branch = branchService.getBranchById(branchId);
		ModelAndView mav = new ModelAndView("user_dashboard_manage_employeelist");
		List<Employee> allEmployeesList = employeeService.getEmployeesListofThisbranch(branch);
		List<Branch> branchList = branchService.getAllBranchs();
		mav.addObject("listOfEmployees", allEmployeesList);
		mav.addObject("sessionUser", sessionUser);
		mav.addObject("TableName", "employees List of " + branchName);
		mav.addObject("branchList", branchList);

		return mav;
	}

	@RequestMapping(value = "/updateEmployeeDetailsForm/{id}", method = RequestMethod.GET)
	public ModelAndView updateEmployeePage(HttpSession session, @PathVariable(value = "id") int id) {
		session.getAttribute("userObj");
		Employee employeeObj = employeeService.getEmployeeById(id);
		String branchName = (String) session.getAttribute("branchName");
		ModelAndView mav = new ModelAndView("user_employee_update_form");
		User sessionUser = (User) session.getAttribute("userObj");
		mav.addObject("employeeObj", employeeObj);
		mav.addObject("sessionUser", sessionUser);
		mav.addObject("branch", employeeObj.branch.getBranchName());
		List<Branch> branchList = branchService.getByIsActive();
		mav.addObject("branchList", branchList);

		return mav;
	}

	@RequestMapping(value = "/updateEmployeeBasicDetails", method = RequestMethod.POST)
	public ModelAndView updateEmployeePersonalDetails(@ModelAttribute("employeeObj") Employee employee,
			@ModelAttribute("branch") Branch branch, HttpSession session) {
		session.getAttribute("userObj");
		int userId = (int) session.getAttribute("userId");
		String branchName = (String) session.getAttribute("branchName");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into emp/controller:::: dashboard setting page will display");
		Employee employeeExist = employeeService.getEmployeeById(employee.getEmployeeId());
		LOGGER.debug("employee id is::" + employeeExist.getEmployeeId());
		employeeExist.setEmployeeName(employee.getEmployeeName());
		employeeExist.setEmployeeRole(employee.getEmployeeRole());
		employeeExist.setEmailId(employee.getEmailId());
		employeeExist.setBranch(branch);
		employeeExist.setUpdatedBy(userId);
		employeeExist.setUpdatedDate(LocalDateTime.now());
		employeeService.updateEmployee(employeeExist);
		LOGGER.debug(" Employee details Updates " + employeeExist);
		ModelAndView mav = new ModelAndView("user_dashboard_manage_employeelist");
		List<Employee> allEmployeesList = employeeService.getEmployeesListofThisbranch(branch);
		List<Branch> branchList = branchService.getAllBranchs();
		mav.addObject("listOfEmployees", allEmployeesList);
		mav.addObject("TableName", "Employees List of " + branchName);
		mav.addObject("sessionUser", sessionUser);
		mav.addObject("branchList", branchList);
		mav.addObject("updatemessage", employee.getEmployeeName() + " profile is updated");
		return mav;
	}

	// Set is active status of employee to "Y"
	@RequestMapping(value = "/activateEmployee/{id}", method = RequestMethod.GET)
	public ModelAndView saveEmployeeAfterUpdate(Model model, @ModelAttribute("newUserDetails") Employee employee,
			@PathVariable("id") int id, @ModelAttribute("branch") Branch branch, HttpSession session) {
		session.getAttribute("userObj");
		int branchId = (int) session.getAttribute("branchId");
		Branch Userbranch = branchService.getBranchById(branchId);
		int userId = (int) session.getAttribute("userId");
		User sessionUser = (User) session.getAttribute("userObj");
		String branchName = (String) session.getAttribute("branchName");
		ModelAndView mav = new ModelAndView("user_dashboard_manage_employeelist");
		Employee employeeObj = employeeService.getEmployeeById(id);
		if (employeeObj.getIsActive().equalsIgnoreCase("N")) {
			employeeObj.setIsActive("Y");
			employeeObj.setUpdatedBy(userId);
			employeeObj.setUpdatedDate(LocalDateTime.now());
			employeeObj.setBranch(Userbranch);
			employeeService.updateEmployee(employeeObj);
			LOGGER.debug("employee deleted sucessfully::" + id);
			List<Employee> allEmployeesList = employeeService.getEmployeesListofThisbranch(Userbranch);
			List<Branch> branchList = branchService.getAllBranchs();
			ModelAndView mav2 = new ModelAndView("user_dashboard_manage_employeelist");
			mav2.addObject("listOfEmployees", allEmployeesList);
			mav2.addObject("TableName", "Employees List of " + branchName);
			mav2.addObject("sessionUser", sessionUser);
			mav2.addObject("branchList", branchList);
			mav2.addObject("updatemessage", employeeObj.getEmployeeName() + " profile is Activated");
			return mav2;
		} else if (employeeObj.getIsActive().equalsIgnoreCase("Y")) {
			LOGGER.debug("employee active status is::" + employeeObj.getIsActive());
			employeeObj.setIsActive("N");
			employeeObj.setUpdatedBy(userId);
			employeeObj.setUpdatedDate(LocalDateTime.now());
			employeeObj.setBranch(Userbranch);
			employeeService.updateEmployee(employeeObj);
			LOGGER.debug("employee deleted sucessfully::" + id);
			List<Employee> allEmployeesList = employeeService.getEmployeesListofThisbranch(Userbranch);
			List<Branch> branchList = branchService.getAllBranchs();
			ModelAndView mav3 = new ModelAndView("user_dashboard_manage_employeelist");
			mav3.addObject("listOfEmployees", allEmployeesList);
			mav3.addObject("TableName", "Employees List of " + branchName);
			mav3.addObject("sessionUser", sessionUser);
			mav3.addObject("branchList", branchList);
			mav3.addObject("updatemessage", employeeObj.getEmployeeName() + " profile is inActivated");
			return mav3;
		}
		List<Employee> allEmployeesList = employeeService.getEmployeesListofThisbranch(Userbranch);
		List<Branch> branchList = branchService.getAllBranchs();
		mav.addObject("listOfEmployees", allEmployeesList);
		mav.addObject("sessionUser", sessionUser);
		mav.addObject("TableName", "employees List of " + branchName);
		mav.addObject("branchList", branchList);
		return mav;

	}

	// Add New Employee
	@RequestMapping(value = "/addEmployeeForm/{id}", method = RequestMethod.GET)
	public ModelAndView employeeRegisterPage(@Validated @PathVariable("id") int id, Employee newEmployee, Model model,
			HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		LOGGER.debug("entered into user/controller::::add employee method");
		Branch branchObj = branchService.getBranchById(id);
		LOGGER.debug("entered into user/controller::::add employee for branch Id" + branchObj.getBranchId());
		ModelAndView mav = new ModelAndView("user_add_employee");
		model.addAttribute("branch", branchObj);
		mav.addObject("newUserDetails", newEmployee);
		mav.addObject("sessionUser", sessionUser);
		return mav;
	}

	// Add New Employee in User Branch
	@PostMapping("/addNewEmployee")
	public ModelAndView addNewEmployeeinDB(@ModelAttribute("newUserDetails") Employee newEmployee,
			@ModelAttribute("branch") Branch branch, Model model, HttpSession session) {
		session.getAttribute("userObj");
		User sessionUser = (User) session.getAttribute("userObj");
		Employee employeeExist = employeeService.findByEmailId(newEmployee.getEmailId());
		if (employeeExist != null) {
			ModelAndView mav = new ModelAndView("user_add_employee");
			mav.addObject("message", "email already exist");
			mav.addObject("branch", branch);
			mav.addObject("newUserDetails", newEmployee);
			mav.addObject("sessionUser", sessionUser);

			return mav;
		} else {
			LOGGER.debug("add employee by user::::" + newEmployee.getEmailId());
			LOGGER.debug("employee::branchId::" + branch.getBranchId());
			LOGGER.debug("employee::branchName::" + branch.getBranchName());
			int userId = (int) session.getAttribute("userId");
			newEmployee.setBranch(branch);
			newEmployee.setIsActive("Y");
			newEmployee.setCreatedDate(LocalDateTime.now());
			newEmployee.setUpdatedBy(userId);
			newEmployee.setUpdatedDate(LocalDateTime.now());
			newEmployee.setCreatedBy(userId);
			String branchName = (String) session.getAttribute("branchName");
			LOGGER.debug("mep pwd user::" + newEmployee.getPassword());
			String strEncPassword = Utilities.getEncryptSecurePassword(newEmployee.getPassword(), "RESOURCING");
			LOGGER.debug("enPWD user::" + strEncPassword);

			newEmployee.setPassword(strEncPassword); // while saving the user put this to encrypt pwd.
			employeeService.addNewEmployee(newEmployee);
			ModelAndView mav = new ModelAndView("user_dashboard_employeelist");
			mav.addObject("message", newEmployee.getEmployeeName() + " new Recruiter is added in " + branchName);
			List<Employee> empList = employeeService.getEmployeesListofThisbranch(branch);
			model.addAttribute("listOfEmployees", empList);
			model.addAttribute("branch", branch);
			model.addAttribute("sessionUser", sessionUser);
			model.addAttribute("TableName", "Employees List of " + branchName);
			return mav;
		}
	}
	
	/*
	@author: vineeth kumar Mudham	
	
	*/	
	
	//Add Client in user list
    @RequestMapping(value = "/clientregister/{userId}", method = RequestMethod.GET)
    public String clientRegisterPageUsingUserId(@PathVariable("userId") int userId, Client newClient, Model model) {
        LOGGER.debug("entered into user/controller::::register method");
        User user = userService.getUserById(userId);
        model.addAttribute("newClientDetails", newClient);
        model.addAttribute("branch", user);
        return "user_client_pages_register.html";
    }
        @PostMapping("/saveClient")
    public ModelAndView registerClient(@ModelAttribute("newClientDetails") Client objClient, Model model,
            @ModelAttribute("branch") Branch branch, User user) {
        System.out.println("branch" + branch.getBranchId());
        if (!objClient.getCaptcha().equals(objClient.getClientCaptcha())) {
            model.addAttribute("ClientMessage", "Please enter valid Captcha");
            ModelAndView mav = new ModelAndView("user_client_pages_register.html");
            return mav;
        }
        Client objClientSecond = clientService.findByEmail(objClient.getEmail());
        if (objClientSecond == null) {
            LOGGER.debug("register::::" + objClient.getEmail());
            LOGGER.debug("register::::" + objClient.getPassword());
            System.out.println("branch" + branch.getBranchId());
            ModelAndView mav = new ModelAndView("user_clients_list.html");
            LOGGER.debug(" admin clients list  is displayed");
            String strEncPassword = Utilities.getEncryptSecurePassword(objClient.getPassword(), "GLAM");
            objClient.setPassword(strEncPassword);
            objClient.setIsActive("Y");
            objClient.setCreatedby(user.getUserId());
            objClient.setCreatedDate(LocalDateTime.now());
            objClient.setUpdatedDate(LocalDateTime.now());
            clientService.addClient(objClient);
            List<Client> clientList = clientService.getAllClients();
            List<Client> clientListByUser = clientList.stream().filter(obj -> obj.getUser().userId == user.getUserId())
                    .collect(Collectors.toList());
            mav.addObject("clientList", clientListByUser);
            return mav;
        } else {
            System.out.println("branch" + branch.getBranchId());
            LOGGER.debug("else block of validation:::: Use Another Mail");
            ModelAndView mav = new ModelAndView("user_client_pages_register.html");
            LOGGER.debug("Mail Id Already Exists");
            String s1 = "Mail Id Already Exists,Try With Another MailId";
            model.addAttribute("s1", s1);
            model.addAttribute("newClientDetails", new Client());
            return mav;
        }
    }
        
        @RequestMapping(value = "/activeClient/{id}", method = RequestMethod.GET)
        public String saveClientAfterUpdate(Model model, @ModelAttribute("Client") Client objClient,
                @PathVariable("id") int id) {
            Client objUserActive = clientService.getClientById(id);
            LOGGER.debug("client ActiveStatus is::" + objUserActive.getIsActive());
            if (objUserActive.getIsActive().equalsIgnoreCase("N")) {
                objUserActive.setIsActive("Y");
                objUserActive.setUpdatedDate(LocalDateTime.now());
                clientService.updateClient(objUserActive);
                List<Client> objClientSecond = clientService.getAllClients();
                model.addAttribute("clientList", objClientSecond);
                model.addAttribute("message", objUserActive.getClientName() + " is changed to Active ");
                return "admin_clients_list";
            } else {
                objUserActive.setIsActive("N");
                objUserActive.setUpdatedDate(LocalDateTime.now());
                clientService.updateClient(objUserActive);
                List<Client> objClientSecond = clientService.getAllClients();
                model.addAttribute("clientList", objClientSecond);
                model.addAttribute("red", objUserActive.getClientName() + " is changed to InActive ");
                return "user_clients_list.html";
            }
        }
        
        @GetMapping("/clientList/{userId}")
        public String getListOfClientsByUserId(@PathVariable("userId") int userId, Model model) {
            System.out.println("id:::" + userId);
            User user = userService.getUserById(userId);
            System.out.println("branchId:::" + user.getUserId());
            List<Client> objClient = clientService.getAllClients();
            List<Client> clientListByUser = objClient.stream().filter(obj -> obj.getUser().userId == user.getUserId())
                    .collect(Collectors.toList());
            model.addAttribute("branch", user);
            model.addAttribute("clientList", clientListByUser);
            System.out.println("branchId::::::::" + user.getUserId());
            return "user_clients_list.html";
        }
        @GetMapping("/employeeClientAssociation/{clientId}")
        public String employeeClientAssociation(Model model, @PathVariable("clientId") int clientId, Employee emp) {
            Client client = clientService.getClientById(clientId);
            List<Employee> employee = employeeService.getAllEmployees();
            List<Employee> objEmployees = employee.stream()
                    .filter(obj -> obj.getBranch().getBranchId() == client.getUser().getBranch().getBranchId())
                    .collect(Collectors.toList());
            model.addAttribute("listOfEmployees", objEmployees);
            model.addAttribute("client", client);
            return "user_add_eca";
        }
        @PostMapping(value = "/ecaList")
        public String getEmpCliAssPage(Model model, @ModelAttribute("eca") EmployeeClientAssociation eca,
                HttpSession session, @ModelAttribute("client") Client client,
                @ModelAttribute("listOfEmployees") Employee employee) {
            System.out.println("method invoked");
            System.out.println("Inside eca saving eca METHOD clientID::::::" + client.getClientId());
            System.out.println("Inside eca saving eca METHOD::::::Employeeid " + employee.getEmployeeId());
            EmployeeClientAssociation association = employeeClientAssociationService.findByEmployeeIdAndClientId(employee, client) ;
            if (association != null) {
                LOGGER.debug("else:::::::::::");
                List<Client> clientList = clientService.getAllClients();
                List<Employee> empList = employeeService.getAllEmployees();
                model.addAttribute("clientList", clientList);
                model.addAttribute("empList", empList);
                model.addAttribute("message",
                        employee.getEmployeeName() + " is already asscoicated with " + client.getClientName());
                return "user_add_eca.html";
            } else {
                LOGGER.debug("if::::::::::::::");
                eca.setClient(client);
                eca.setEmployee(employee);
                LOGGER.debug("eca::id" + eca.getEcaId());
                eca.setCreatedDate(LocalDateTime.now());
                eca.setUpdatedDate(LocalDateTime.now());
                eca.setCreatedby(client.getUser().getUserId());
                eca.setIsActive('Y');
                employeeClientAssociationService.addNewEca(eca);
                LOGGER.debug(eca);
                List<EmployeeClientAssociation> ecaList = employeeClientAssociationService
                        .getAllEmployeeClientAssociations();
                List<EmployeeClientAssociation> list = ecaList.stream()
                        .filter(obj -> obj.getClient().getClientId() == client.getClientId()).collect(Collectors.toList());
                model.addAttribute("ecaList", list);
                model.addAttribute("message",
                        employee.getEmployeeName() + " is asscoicated with " + client.getClientName());
                return "user_ecaList.html";
            }
        }
        
        @GetMapping("/ecaListAll")
        public String getJdList(Model model) {
            List<EmployeeClientAssociation> ecaList = employeeClientAssociationService.getAllEmployeeClientAssociations();
            model.addAttribute("ecaList", ecaList);
            return "user_ecaList.html";
        }
        
        
}
