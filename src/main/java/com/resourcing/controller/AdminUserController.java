//Author:Vineeth Kumar Mudham

package com.resourcing.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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

import com.resourcing.beans.AdminUser;
import com.resourcing.beans.Branch;
import com.resourcing.beans.Candidate;
import com.resourcing.beans.Client;
import com.resourcing.beans.Company;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.User;
import com.resourcing.service.AdminUserService;
import com.resourcing.service.BranchService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.ClientService;
import com.resourcing.service.CompanyService;
import com.resourcing.service.EmployeeClientAssociationService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.UserService;
import com.resourcing.utilities.Utilities;

@Controller
public class AdminUserController {

	static Logger LOGGER = Logger.getLogger(AdminUserController.class);

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	CompanyService companyService;
	@Autowired
	BranchService branchService;
	@Autowired
	private UserService userService;
	@Autowired
	CandidateService candidateService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private JobDescriptionService jobDescriptionService;
	@Autowired
	EmployeeClientAssociationService employeeClientAssociationService;

	// =========homepage job search================//

	@GetMapping("/resourcing")
	public String homePage(InterviewPanel interviewer, Model model, JobDescription jobDescription) {
		model.addAttribute("newInterviewerDetails", interviewer);
		List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
		List<Candidate> candidateList = candidateService.getAllCandidates();
		List<Client> clientList = clientService.getAllClients();
		List<Branch> objBranch = branchService.getAllBranchs();
		model.addAttribute("branchList", objBranch.size());
		model.addAttribute("jdList", jdList.size());
		model.addAttribute("candidateList", candidateList.size());
		model.addAttribute("clientList", clientList.size());
		model.addAttribute("objJd", jobDescription);
		return "homepage";
	}

	// To Get the JobDetails for an Logged-out Person
	@PostMapping("/jdListForUnknown")
	public String getJdListForUnknown(Model model,
			@Validated @ModelAttribute("objJd") JobDescription objJobDescription) {
		List<JobDescription> jdList = jobDescriptionService.getJobDescriptionListByLocationAndPosition(
				objJobDescription.getLocation(), objJobDescription.getPosition());
		LOGGER.debug("if condition:::::");
		LOGGER.debug("entered into getJdListForUnknown::::" + objJobDescription.getLocation());
		LOGGER.debug("entered into getJdListForUnknown::::" + objJobDescription.getPosition());
		model.addAttribute("jdList", jdList);
		LOGGER.debug("count::::::" + jdList.size());
		if (jdList.size() != 0) {
			model.addAttribute("jdList", jdList);
			return "admin-jobs-list-layout-full-page-map";
		} else {
			LOGGER.debug("else condition:::::::");
			List<JobDescription> jobs = jobDescriptionService.getAllJobDescriptions();
			List<Candidate> candidateList = candidateService.getAllCandidates();
			List<Client> clientList = clientService.getAllClients();
			model.addAttribute("jdList", jobs.size());
			model.addAttribute("candidateList", candidateList.size());
			model.addAttribute("clientList", clientList.size());
			model.addAttribute("objJd", objJobDescription);
			model.addAttribute("message",
					"No jobs found for " + objJobDescription.getLocation() + " and " + objJobDescription.getPosition());
			return "homepage";
		}

	}

	// =================homepage job search ended===========//

	// View loginPage with username and password in userLoginForm.html
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView adminUserRegisterPage(@Validated AdminUser newUser) {
		LOGGER.debug("entered into AdminUser/controller::::register method");
		ModelAndView mav = new ModelAndView("admin_user_register_form.html");
		mav.addObject("newUserDetails", newUser);
		return mav;
	}

	@PostMapping("/admin/saveUser")
	public ModelAndView registerEmployee(@Validated @ModelAttribute("newUserDetails") AdminUser objSaveAdmin,
			Model model) {
		if (!objSaveAdmin.getCaptcha().equals(objSaveAdmin.getUserCaptcha())) {
			model.addAttribute("message", "Please enter valid Captcha");
			ModelAndView mav = new ModelAndView("admin_user_register_form.html");
			return mav;
		}

		String strEncPassword = Utilities.getEncryptSecurePassword(objSaveAdmin.getPassword(), "RESOURCING");
		objSaveAdmin.setPassword(strEncPassword);
		LOGGER.debug("register::::" + objSaveAdmin.getEmailId());
		LOGGER.debug("register::::" + objSaveAdmin.getPassword());
		AdminUser objAdmin = adminUserService.findByEmailId(objSaveAdmin.getEmailId());
		if (objAdmin != null) {
			model.addAttribute("message", objSaveAdmin.getAdminName() + " EmailId already Exist!!");
			model.addAttribute("newUserDetails", objSaveAdmin);
			ModelAndView mav = new ModelAndView("admin_user_register_form.html");
			return mav;
		} else {
			objSaveAdmin.setCreatedDate(LocalDateTime.now());
			objSaveAdmin.setIsActive("Y");
			adminUserService.addAdminUser(objSaveAdmin);
			objSaveAdmin.setCreatedBy(objSaveAdmin.getAdminId());
			adminUserService.updateAdminUser(objSaveAdmin);
			model.addAttribute("message", objSaveAdmin.getAdminName() + " Registered sucessfuly!!");
			model.addAttribute("newUserDetails", objSaveAdmin);
			ModelAndView mav = new ModelAndView("admin_login");
			return mav;
		}
	}

	// View loginPage with username and password in userLoginForm.html
	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public ModelAndView adminLoginPage(AdminUser loginUser) {
		loginUser.setIsActive("Y");
		LOGGER.debug("entered into user/controller:::: login method");
		ModelAndView mav = new ModelAndView("admin_login");
		mav.addObject("newUserDetails", loginUser);
		return mav;
	}

	@PostMapping("/admin/validateUser")
	public ModelAndView validatemethod(AdminUser objValidateAdmin, HttpServletRequest request, Model model) {
		LOGGER.debug("entered into validation method");
		String strEncPassword = Utilities.getEncryptSecurePassword(objValidateAdmin.getPassword(), "RESOURCING");
		objValidateAdmin.setPassword(strEncPassword);
		AdminUser objAdminFirst = adminUserService.findByUserNameIgnoreCaseAndPassword(objValidateAdmin.getEmailId(),
				strEncPassword);
		if (objAdminFirst != null) {
			HttpSession session = request.getSession();
			ModelAndView mav = new ModelAndView("admin_user_dashboard");
			session.setAttribute("adminId", objAdminFirst.getAdminId());
			session.setAttribute("adminUserName", objAdminFirst.getAdminName());
			model.addAttribute("company", new Company());
			model.addAttribute("", objAdminFirst);
			List<Candidate> candidateList = candidateService.getAllCandidates();
			List<Client> clientList = clientService.getAllClients();
			List<Employee> empList = employeeService.allEmployeesList();
			List<Branch> branchList = branchService.getAllBranchs();
			model.addAttribute("candidateList", candidateList.size());
			model.addAttribute("clientList", clientList.size());
			model.addAttribute("employeeList", empList.size());
			model.addAttribute("branchList", branchList.size());
			LOGGER.info("admin dashboard  page is displayed");
			return mav;
		} else {
			LOGGER.debug("else block of validation:::: Use valid details");
			ModelAndView mav = new ModelAndView("admin_login");
			String s1 = "Invalid Credentials";
			mav.addObject("ErrorMessage", s1);
			mav.addObject("newUserDetails", new AdminUser());

			return mav;
		}
	}

	@GetMapping("/admin/deleteAdmin/{id}")
	public String deleteAdmin(@PathVariable(value = "id") int id, AdminUser adminUser, Model model) {

		AdminUser objAdminDelete = adminUserService.getAdminUserById(id);
		objAdminDelete.setIsActive("N");
		LOGGER.debug("company delete" + objAdminDelete.getAdminId());
		LOGGER.debug("branches with this companyId");
		adminUserService.updateAdminUser(objAdminDelete);
		model.addAttribute("newUserDetails", adminUser);
		return "admin_login";
	}

	@GetMapping("/admin/dashboard/{id}")
	public String dashboard(@PathVariable(value = "id") int id, AdminUser adminUser, Model model) {
		AdminUser objAdminDashboard = adminUserService.getAdminUserById(id);
		List<Candidate> candidateList = candidateService.getAllCandidates();
		List<Client> clientList = clientService.getAllClients();
		List<Employee> empList = employeeService.allEmployeesList();
		List<Branch> branchList = branchService.getAllBranchs();
		model.addAttribute("candidateList", candidateList.size());
		model.addAttribute("clientList", clientList.size());
		model.addAttribute("employeeList", empList.size());
		model.addAttribute("branchList", branchList.size());
		model.addAttribute("newUserDetails", objAdminDashboard);
		return "admin_user_dashboard";
	}

//=============================================================basic validation completeed=============================================				

	// ==========================forgot password and reset======================//
	@RequestMapping(value = "/admin/forgotPassword", method = RequestMethod.GET)
	public ModelAndView UserforgotPasswordPage(AdminUser newUser) {
		LOGGER.debug("entered into user/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("admin_forgot_password");
		mav.addObject("newUserDetails", newUser);
		return mav;
	}

	@PostMapping(value = "/admin/validteEmailId")
	public String checkMailId(Model model, AdminUser objAdminThird) {
		LOGGER.debug("entered into user/controller::::check EmailId existing or not");
		LOGGER.debug("UI given mail Id:" + objAdminThird.getEmailId());
		AdminUser objAdminSecond = adminUserService.findByEmailId(objAdminThird.getEmailId());
		if (objAdminSecond != null) {
			model.addAttribute("newUserDetails", objAdminThird);
			return "admin_reset_password";
		} else {
			model.addAttribute("newUserDetails", objAdminThird);
			model.addAttribute("message", "email doesn't exist!!!");
			return "admin_forgot_password";
		}

	}

	@RequestMapping(value = "/admin/updateUserPassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("newUserDetails") AdminUser objUpdatePassword) {
		AdminUser objUpdateAdmin = adminUserService.findByEmailId(objUpdatePassword.getEmailId());
		LOGGER.debug(" in update method employee created date::" + objUpdateAdmin.getCreatedDate());
		LOGGER.debug("in update method objUpdateAdmin:: name " + objUpdateAdmin.getAdminName());

		String strEncPassword = Utilities.getEncryptSecurePassword(objUpdatePassword.getPassword(), "RESOURCING");
		objUpdatePassword.setPassword(strEncPassword);

		objUpdateAdmin.setUpdatedBy(objUpdateAdmin.getAdminName());
		objUpdateAdmin.setPassword(objUpdatePassword.getPassword());
		objUpdateAdmin.setUpdatedDate(LocalDateTime.now());
		adminUserService.updateAdminUser(objUpdateAdmin);
		LOGGER.debug("password is updated sucessfully" + objUpdatePassword.getPassword());
		ModelAndView mav = new ModelAndView("admin_login");
		LOGGER.debug("login page is displayed");
		String s1 = "password is updated sucessfully";
		mav.addObject("password", s1);
		mav.addObject("newUserDetails", objUpdateAdmin);
		return mav;
	}

	// =================================================================================================================
	// Company
	/* Company list for Admin */

	@GetMapping("/admin/CompanyList")
	public String getListOfCompanies(Model model) {
		List<Company> company = companyService.getAllCompanyies();
		model.addAttribute("listCompany", company);
		return "admin_company_list";
	}

	/* Add company */
	@GetMapping("/admin/addCompanyForm")
	public String addCompanyForm(Model model) {
		model.addAttribute("company", new Company());
		return "admin_add_company_form";
	}

	@PostMapping("/admin/saveCompany")
	public String saveCompany(Model model, @ModelAttribute("company") Company objCompany) {
		LOGGER.debug("condition::::::::");
		LOGGER.debug("checking:::::::");
		LOGGER.debug("abobe if condition::::::::");
		if (companyService.findByCompanyName(objCompany.getCompanyName()) == null) {
			LOGGER.debug("if condition:::::::");
			objCompany.setIsActive("Y");
			objCompany.setCreatedDate(LocalDateTime.now());
			companyService.saveCompany(objCompany);
			List<Company> companyObj = companyService.getAllCompanyies();
			model.addAttribute("listCompany", companyObj);
			model.addAttribute("message", "company registered successfully");
			return "admin_company_list";
		} else {
			model.addAttribute("exist", "this company name is already existed!! please change the company Name!!");
			model.addAttribute("company", objCompany);
			return "admin_add_company_form";
		}
	}

	@GetMapping("/admin/showFormForUpdate/{id}")
	public String showFormForCompanyUpdate(@PathVariable(value = "id") int id, Model model, Company objCompany) {
		Company company = companyService.getCompanyById(id);
		company.setUpdatedDate(LocalDateTime.now());
		LOGGER.debug("updated date::::::::" + LocalDateTime.now());
		companyService.updateCompany(company);
		LOGGER.debug("aaaaaaaaaaaa" + LocalDateTime.now());
		model.addAttribute("company", company);
		return "update_company_form";
	}

	@PostMapping("/admin/updateCompany")
	public String updateCompany(Model model, @ModelAttribute("company") Company objCompany) {
		objCompany.setUpdatedDate(LocalDateTime.now());
		LOGGER.debug(LocalDateTime.now());
		companyService.updateCompany(objCompany);
		List<Company> companyObj = companyService.getAllCompanyies();
		model.addAttribute("listCompany", companyObj);
		model.addAttribute("message", objCompany.getCompanyName() + "  company details are updated successfully");
		return "admin_company_list";
	}

	@RequestMapping(value = "/admin/activeCompany/{id}", method = RequestMethod.GET)
	public String saveCompanyAfterUpdate(Model model, @ModelAttribute("company") Company objCompany,
			@PathVariable("id") int id) {
		Company objUserActive = companyService.getCompanyById(id);
		LOGGER.debug("employee ActiveStatus is::" + objUserActive.getIsActive());
		if (objUserActive.getIsActive().equalsIgnoreCase("N")) {
			objUserActive.setIsActive("Y");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			companyService.updateCompany(objUserActive);
			List<Company> companyObj = companyService.getAllCompanyies();
			model.addAttribute("listCompany", companyObj);
			model.addAttribute("message", objUserActive.getCompanyName() + " is changed to Active ");
			return "admin_company_list";
		} else {
			objUserActive.setIsActive("N");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			companyService.updateCompany(objUserActive);
			List<Company> companyObj = companyService.getAllCompanyies();
			model.addAttribute("listCompany", companyObj);
			model.addAttribute("red", objUserActive.getCompanyName() + " is changed to inActive ");
			return "admin_company_list";
		}

	}

	@RequestMapping(value = "/admin/companyProfile/{id}", method = RequestMethod.GET)
	public String companyProfile(Model model, @Validated @PathVariable("id") int id,
			@Validated @PathVariable("id") int branchId) {
		Company objCompanyBrowse = companyService.getCompanyById(id);
		List<Company> objCompany = companyService.getAllCompanyies();
		model.addAttribute("listCompany", objCompany);
		model.addAttribute("listCompany", objCompanyBrowse);
		return "company_profile.html";
	}

	// =================================================================

	// ==========Branch==================

	@GetMapping("/admin/BranchList")
	public String listTheAllBranches(Model model, Company company) {
		List<Branch> branch = branchService.getAllBranchs();
		model.addAttribute("listBranches", branch);
		return "branch_list";
	}

	/*
	 * @GetMapping("/admin/EmployeeClientAssociation/{id}") public String
	 * employeeClientAssociation(Model model, @PathVariable("id") int id) { Client
	 * objClient = clientService.getClientById(id); model.addAttribute("clientList",
	 * objClient); return null;
	 * 
	 * }
	 */

	@GetMapping("/admin/showNewBranchForm/{id}")
	public String showNewBranchForm(Model model, @PathVariable("id") int id, Branch branchObj) {
		Company objCompany = companyService.getCompanyById(id);
		LOGGER.debug("before add branch Form::" + objCompany.getCompanyId());
		LOGGER.debug("before add branch Form::" + objCompany.getCompanyName());
		model.addAttribute("company", objCompany);
		model.addAttribute("branch", branchObj);
		return "add_branch_form";
	}

	@PostMapping("/admin/saveBranch")
	public String saveBranch(@ModelAttribute("branch") Branch branch, Model model,
			@ModelAttribute("company") Company company) {
		Company objCompany = companyService.getCompanyById(company.getCompanyId());
		Branch branchObj = branchService.getEmailId(branch.getEmailId());
		if (branchObj == null) {
			System.out.println("before Save::" + company.getCompanyId());
			LOGGER.debug("before Save::" + company.getCompanyId());
			LOGGER.debug("before Save::" + objCompany.getCompanyName());
			branch.setIsActive("Y");
			branch.setCreatedDate(LocalDateTime.now());
			branch.setCompany(objCompany);
			branch.getBranchId();
			branchService.addBranch(branch);
			List<Branch> objBranch = branchService.getAllBranchs();
			List<Branch> objBranchSecond = objBranch.stream()
					.filter(obj -> obj.getCompany().getCompanyId() == branch.getCompany().getCompanyId())
					.collect(Collectors.toList());
			model.addAttribute("listBranches", objBranchSecond);
			model.addAttribute("message", branch.getBranchName() + "    successfully registered in"
					+ objCompany.getCompanyName() + " company");
			return "branch_list.html";
		} else {
			System.out.println("before add branch Form::" + objCompany.getCompanyId());
			LOGGER.debug("before add branch Form::" + objCompany.getCompanyName());
			model.addAttribute("company", objCompany);
			model.addAttribute("branch", branch);
			model.addAttribute("message", branch.getEmailId() + " is already registed!! try with another one");
			return "add_branch_form";
		}
	}

	@GetMapping("/admin/showNewBranchFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") int id, Model model,
			@ModelAttribute("CompanyId") Company objCompany) {
		objCompany.getCompanyId();
		LOGGER.debug("compaany id:::" + objCompany.getCompanyId());
		Branch branch = branchService.getBranchById(id);
		LOGGER.debug("branch id " + branch.getBranchId());
		model.addAttribute("branch", branch);
		LOGGER.debug(branch.getBranchId());
		branchService.updateBranch(branch);
		return "update_branch_form";
	}

	@PostMapping("/admin/updateBranch")
	public String updateBranch(@ModelAttribute("branch") Branch branch, Model model) {
		Branch objBranch = branchService.getBranchById(branch.getBranchId());
		LOGGER.debug("before update::" + branch.getCompany().getCompanyId());
		branch.setCompany(objBranch.getCompany());
		branch.setCreatedBy(objBranch.getCreatedBy());
		branch.setUpdatedDate(LocalDateTime.now());
		branchService.updateBranch(branch);
		LOGGER.debug(branch.branchId);
		LOGGER.debug(branch.branchName);
		LOGGER.debug("branch updates successfully");
		List<Branch> branchObj = branchService.getAllBranchs();
		List<Branch> objBranchOne = branchObj.stream()
				.filter(obj -> obj.getCompany().getCompanyId() == branch.getCompany().getCompanyId())
				.collect(Collectors.toList());
		model.addAttribute("listBranches", objBranchOne);
		model.addAttribute("message", branch.getBranchName() + "   details are updated successfully");
		return "branch_list";
	}

	@RequestMapping(value = "/admin/activeBranch/{id}", method = RequestMethod.GET)
	public String saveBranchAfterUpdate(Model model, @ModelAttribute("branch") Branch objBranch,
			@PathVariable("id") int id) {
		Branch objUserActive = branchService.getBranchById(id);
		LOGGER.debug("employee ActiveStatus is::" + objUserActive.getIsActive());
		if (objUserActive.getIsActive().equalsIgnoreCase("N")) {
			objUserActive.setIsActive("Y");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			branchService.updateBranch(objUserActive);
			List<Branch> branch = branchService.getAllBranchs();
			List<Branch> branchs = branch.stream()
					.filter(obj -> obj.getCompany().getCompanyId() == objUserActive.getCompany().getCompanyId())
					.collect(Collectors.toList());
			model.addAttribute("listBranches", branchs);
			model.addAttribute("message", objUserActive.getBranchName() + " is changed to Active ");
			return "branch_list";
		} else {
			objUserActive.setIsActive("N");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			branchService.updateBranch(objUserActive);
			List<Branch> branch = branchService.getAllBranchs();
			List<Branch> branchs = branch.stream()
					.filter(obj -> obj.getCompany().getCompanyId() == objUserActive.getCompany().getCompanyId())
					.collect(Collectors.toList());
			model.addAttribute("listBranches", branchs);
			model.addAttribute("red", objUserActive.getBranchName() + " is changed to InActive ");
			return "branch_list";
		}
	}

	@GetMapping("admin/branchList/{companyId}")
	public String branchListByCompanyId(@PathVariable int companyId, Model model) {
		List<Branch> branch = branchService.getAllBranchs();
		List<Branch> branchList = branch.stream().filter(list -> list.getCompany().getCompanyId() == companyId)
				.collect(Collectors.toList());
		model.addAttribute("listBranches", branchList);
		return "branch_list";
	}

	@GetMapping("/admin/browseBranches")
	public String browseBranches(Model model) {
		List<Branch> objBranchBrowse = branchService.getAllBranchs();
		model.addAttribute("listBranches", objBranchBrowse);
		return "browse_branches.html";
	}

	@RequestMapping(value = "/admin/branchProfile/{id}", method = RequestMethod.GET)
	public String branchProfile(Model model, @Validated @PathVariable("id") int id) {
		Branch objBranchBrowse = branchService.getBranchById(id);
		model.addAttribute("listBranches", objBranchBrowse);
		return "branch_profile.html";
	}

	// ==============================================================

	// Candidate

	/* Candidate list for Admin */
	@GetMapping("/admin/candidateList")
	public String getAllCandidates(Model model) {
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<Candidate> candidateList = candidateService.getAllCandidates();
		model.addAttribute("candidateList", candidateList);
		return "admin_user_candidatelist";
	}

	// =========================================================================================================
	/* Employees list for Admin */
	@GetMapping(value = "/admin/EmployeesList")
	public String getAllEmployees(Model model, @ModelAttribute("Branch") Branch branch,
			@ModelAttribute("company") Company company) {
		LOGGER.debug("inside getAllEmployees this will get the all employees:::");
		List<Employee> empList = employeeService.getAllEmployees();
		model.addAttribute("listOfEmployees", empList);
		model.addAttribute("branch", branch);
		model.addAttribute("company", company);
		return "admin_user_employeelist";
	}

	// ======================================================================================================================================================
	// User Details
	@GetMapping("/admin/usersList")
	public String getListOfUsers(Model model) {
		List<User> objUser = userService.getAllUsers();
		model.addAttribute("listUsers", objUser);
		List<Branch> branchList = branchService.getByIsActive();
		model.addAttribute("branchList", branchList);
		return "admin_users_list";
	}

	@GetMapping("/admin/usersList/{branchId}")
	public String getListofUsersByBranchId(@PathVariable("branchId") int branchId, Model model) {
		Branch branch = branchService.getBranchById(branchId);
		List<User> objUser = userService.getAllUsers();
		List<User> userobj = objUser.stream().filter(obj -> obj.getBranch().branchId == branch.getBranchId())
				.collect(Collectors.toList());
		model.addAttribute("listUsers", userobj);
		model.addAttribute("branch", branch);

		return "admin_users_list";

	}

	@RequestMapping(value = "/admin/register/{id}", method = RequestMethod.GET)
	public String addUserRegisterPage(@Validated @PathVariable("id") int id, AdminUser newEmployee, Model model,
			User newUser, Branch objBranch) {
		LOGGER.debug("before add user Form=====::" + objBranch.getBranchId());
		Branch objBranchSecond = branchService.getBranchById(id);
		LOGGER.debug("after add user Form::" + objBranchSecond.getBranchId());
		System.out.println("in register company name" + objBranchSecond.getCompany().getCompanyName());
		LOGGER.debug("entered into admin/controller::::register method  " + id);
		model.addAttribute("newUserDetails", newUser);
		model.addAttribute("branch", objBranchSecond);
		model.addAttribute("company", objBranchSecond.getCompany().getCompanyName());
		return "admin_users_register_page.html";
	}

	@PostMapping("/admin/saveUsers")
	public ModelAndView registerUser(@ModelAttribute("listUsers") User objUser, Model model,
			@ModelAttribute("branch") Branch objBranch) {
		User userObj = userService.findByEmailId(objUser.getEmailId());
		String strEncPassword = Utilities.getEncryptSecurePassword(objUser.getPassword(), "RESOURCING");
		objUser.setPassword(strEncPassword);
		LOGGER.debug("register::::" + objUser.getEmailId());
		LOGGER.debug("register::::" + objUser.getPassword());
		Branch objBranchSecond = branchService.getBranchById(objBranch.getBranchId());
		User check = userService.findByBranchId(objBranchSecond);
		if (check == null) {
			if (userObj != null) {
				ModelAndView mav1 = new ModelAndView("admin_users_register_page.html");
				mav1.addObject("message", "emailID already exist");
				// mav1.addObject("message", "already admin added");
				mav1.addObject("newUserDetails", objUser);
				return mav1;
			} else {
				LOGGER.debug("before Save::" + objBranch.getBranchName());
				ModelAndView mav2 = new ModelAndView("admin_users_list");
				mav2.addObject("message", "your registration is sucessful");
				mav2.addObject("newUserDetails", objUser);
				objUser.setCreatedDate(LocalDateTime.now());
				objUser.setUpdatedDate(LocalDateTime.now());
				objUser.setIsActive("Y");
				objUser.setBranch(objBranchSecond);
				userService.addUser(objUser);
				LOGGER.debug("user Added sucessfully");
				List<User> objUserOne = userService.getAllUsers();
				List<User> userobj = objUserOne.stream()
						.filter(obj -> obj.getBranch().branchId == objBranch.getBranchId())
						.collect(Collectors.toList());
				model.addAttribute("listUsers", userobj);

				return mav2;
			}
		} else {
			ModelAndView mavObj = new ModelAndView("branch_list");
			List<Branch> branch = branchService.getAllBranchs();
			mavObj.addObject("listBranches", branch);
			mavObj.addObject("message", "User Already Created For " + objBranchSecond.getBranchName());
			return mavObj;
		}
	}

	@GetMapping("/admin/showFormForUserUpdate/{id}")
	public String showFormForUserUpdate(@PathVariable(value = "id") int id, Model model) {
		User objUserUpdate = userService.getUserById(id);
		LOGGER.info("branch id for beforeUser updating" + objUserUpdate.getUserId());
		LOGGER.debug("updated date::::::::" + LocalDateTime.now());
		model.addAttribute("listUsers", objUserUpdate);
		model.addAttribute("branch", objUserUpdate.getBranch());
		List<Branch> branchList = branchService.getByIsActive();
		model.addAttribute("branchList", branchList);
		return "admin_users_update";
	}

	@PostMapping("/admin/updateUser")
	public String updateUser(@ModelAttribute("listUsers") User objUser, @ModelAttribute("branch") Branch branch,
			Model model) {
		User objUserUpdate = userService.getUserById(objUser.getUserId());
		objUserUpdate.setUserName(objUser.getUserName());
		objUserUpdate.setUserRole(objUser.getUserRole());
		objUserUpdate.setEmailId(objUser.getEmailId());
		objUserUpdate.setUpdatedBy(objUser.getUpdatedBy());
		objUserUpdate.setBranch(branch);
		LOGGER.debug("aaaaaa:::" + objUser.getUserId());
		LOGGER.debug("aaaaaa:::" + branch.getBranchId());
		LOGGER.info("aaaaaa:::" + objUser.getUserId());
		objUserUpdate.setUpdatedDate(LocalDateTime.now());
		userService.updateUser(objUserUpdate);
		List<User> objUsersecond = userService.getAllUsers();
		model.addAttribute("listUsers", objUsersecond);
		List<Branch> branchList = branchService.getByIsActive();
		model.addAttribute("branchList", branchList);
		model.addAttribute("message", objUser.getUserName() + "   details are updated successfully");

		return "admin_users_list";
	}

	@GetMapping("/admin/deleteUsers/{id}")
	public String deleteUsers(@PathVariable(value = "id") int id) {
		User objUser = userService.getUserById(id);
		objUser.setIsActive("N");
		userService.updateUser(objUser);
		return "redirect:/admin/usersList";
	}

	@RequestMapping(value = "/admin/activeUser/{id}", method = RequestMethod.GET)
	public String saveUserAfterUpdate(Model model, @ModelAttribute("listUsers") User objUser,
			@PathVariable("id") int id) {

		User objUserActive = userService.getUserById(id);
		LOGGER.debug("employee ActiveStatus is::" + objUserActive.getIsActive());
		if (objUserActive.getIsActive().equalsIgnoreCase("N")) {
			objUserActive.setIsActive("Y");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			userService.updateUser(objUserActive);
			List<User> objUsersecond = userService.getAllUsers();
			model.addAttribute("listUsers", objUsersecond);
			List<Branch> branchList = branchService.getByIsActive();
			model.addAttribute("branchList", branchList);
			model.addAttribute("message", objUserActive.getUserName() + " is changed to Active ");

			return "admin_users_list";

		} else {
			objUserActive.setIsActive("N");
			objUserActive.setUpdatedDate(LocalDateTime.now());
			userService.updateUser(objUserActive);
			List<User> objUsersecond = userService.getAllUsers();
			model.addAttribute("listUsers", objUsersecond);
			List<Branch> branchList = branchService.getByIsActive();
			model.addAttribute("branchList", branchList);
			model.addAttribute("red", objUserActive.getUserName() + " is changed to InActive ");
			return "admin_users_list";
		}
	}

	// ========================================================================================================

	// Client Information
	@GetMapping("/admin/clientList")
	public String getListOfClients(Model model) {
		List<Client> objClient = clientService.getAllClients();
		model.addAttribute("clientList", objClient);
		return "admin_clients_list";
	}


	@RequestMapping(value = "/admin/clientregister/{branchId}", method = RequestMethod.GET)
	public ModelAndView clientRegisterPage(@PathVariable("branchId") int branchId, Client newClient) {
		LOGGER.debug("entered into user/controller::::register method");
		ModelAndView mav = new ModelAndView("admin_client_pages_register.html");
		Branch branch = branchService.getBranchById(branchId);
		mav.addObject("newClientDetails", newClient);
		mav.addObject("branch", branch);
		return mav;
	}

	/*
	 * @PostMapping("/admin/saveClient") public ModelAndView
	 * registerClient(@ModelAttribute("newClientDetails") Client objClient, Model
	 * model,
	 * 
	 * @ModelAttribute("branch") Branch branch) { System.out.println("branch" +
	 * branch.getBranchId()); if
	 * (!objClient.getCaptcha().equals(objClient.getClientCaptcha())) {
	 * model.addAttribute("ClientMessage", "Please enter valid Captcha");
	 * ModelAndView mav = new ModelAndView("admin_client_pages_register.html");
	 * return mav; } Client objClientSecond =
	 * clientService.findByEmail(objClient.getEmail()); if (objClientSecond == null)
	 * { LOGGER.debug("register::::" + objClient.getEmail());
	 * LOGGER.debug("register::::" + objClient.getPassword());
	 * System.out.println("branch" + branch.getBranchId()); ModelAndView mav = new
	 * ModelAndView("admin_clients_list");
	 * LOGGER.debug(" admin clients list  is displayed"); String strEncPassword =
	 * Utilities.getEncryptSecurePassword(objClient.getPassword(), "GLAM");
	 * objClient.setPassword(strEncPassword); objClient.setIsActive("Y");
	 * objClient.setCreatedDate(LocalDateTime.now());
	 * objClient.setUpdatedDate(LocalDateTime.now());
	 * clientService.addClient(objClient); List<Client> clientList =
	 * clientService.getAllClients(); List<Client> clientListByBranch =
	 * clientList.stream() .filter(obj -> obj.getBranch().branchId ==
	 * branch.getBranchId()).collect(Collectors.toList());
	 * mav.addObject("clientList", clientListByBranch); return mav; } else {
	 * System.out.println("branch" + branch.getBranchId());
	 * LOGGER.debug("else block of validation:::: Use Another Mail"); ModelAndView
	 * mav = new ModelAndView("admin_client_pages_register.html");
	 * LOGGER.debug("Mail Id Already Exists"); String s1 =
	 * "Mail Id Already Exists,Try With Another MailId"; model.addAttribute("s1",
	 * s1); model.addAttribute("newClientDetails", new Client()); return mav; } }
	 */

	@GetMapping("/admin/deleteClient/{id}")
	public String deleteClientById(@PathVariable(value = "id") Integer id) {
		LOGGER.debug(id);
		Client objClient = clientService.getClientById(id);
		objClient.setIsActive("N");
		clientService.updateClient(objClient);
		return "redirect:/admin/clientList";
	}

	@RequestMapping(value = "/admin/activeClient/{id}", method = RequestMethod.GET)
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
			return "admin_clients_list";

		}
	}

	@GetMapping(value = "/admin/addEca")
	public String getEmpCliAssAddingPage(Model model, @ModelAttribute("eca") EmployeeClientAssociation eca) {
		LOGGER.debug("formcstarted:::::::");
		List<Client> clientList = clientService.getAllClients();
		List<Employee> empList = employeeService.getAllEmployees();
		model.addAttribute("clientList", clientList);
		model.addAttribute("empList", empList);
		LOGGER.debug("form:::::::::");
		return "admin_add_eca";
	}

	/*
	 * @GetMapping("/admin/employeeClientAssociation/{clientId}") public String
	 * employeeClientAssociation(Model model, @PathVariable("clientId") int
	 * clientId, Employee emp) { Client client =
	 * clientService.getClientById(clientId); List<Employee> employee =
	 * employeeService.getAllEmployees(); List<Employee> objEmployees =
	 * employee.stream() .filter(obj -> obj.getBranch().getBranchId() ==
	 * client.getBranch().getBranchId()) .collect(Collectors.toList());
	 * model.addAttribute("listOfEmployees", objEmployees);
	 * model.addAttribute("client", client); return "admin_add_eca"; }
	 */

	@PostMapping(value = "/admin/ecaList")
	public String getEmpCliAssPage(Model model, @ModelAttribute("eca") EmployeeClientAssociation eca,
			HttpSession session, @ModelAttribute("client") Client client,
			@ModelAttribute("listOfEmployees") Employee employee) {
		System.out.println("method invoked");
		System.out.println("Inside eca saving eca METHOD clientID::::::" + client.getClientId());
		System.out.println("Inside eca saving eca METHOD::::::Employeeid " + employee.getEmployeeId());
		if (employeeClientAssociationService.findByEmployeeIdAndClientId(employee, client) != null) {
			LOGGER.debug("else:::::::::::");
			List<Client> clientList = clientService.getAllClients();
			List<Employee> empList = employeeService.getAllEmployees();
			model.addAttribute("clientList", clientList);
			model.addAttribute("empList", empList);
			model.addAttribute("message",
					employee.getEmployeeName() + " is already asscoicated with " + client.getClientName());
			return "admin_add_eca";
		} else {
			LOGGER.debug("if::::::::::::::");
			int adminId = (int) session.getAttribute("adminId");
			eca.setClient(client);
			eca.setEmployee(employee);
			LOGGER.debug("eca::id" + eca.getEcaId());
			eca.setCreatedDate(LocalDateTime.now());
			eca.setUpdatedDate(LocalDateTime.now());
			eca.setCreatedby(adminId);
			eca.setUpdatedby(adminId);
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
			return "admin_ecaList";
		}
	}

	@GetMapping("/admin/ecaListAll")
	public String getJdList(Model model) {
		List<EmployeeClientAssociation> ecaList = employeeClientAssociationService.getAllEmployeeClientAssociations();
		model.addAttribute("ecaList", ecaList);
		return "admin_ecaList";
	}

	@GetMapping("/map")
	public String messageLocation(Model model) {
		List<Company> objCompany = companyService.getAllCompanyies();
		model.addAttribute("listCompany", objCompany);
		StringBuffer obj = new StringBuffer();
		int i = 1;
		for (Company a : objCompany) {
			obj.append("[\"" + a.getCompanyName() + "\"," + a.getLatitude() + "," + a.getLongitude() + "," + i + "],");
			i++;
		}
		LOGGER.debug(obj);
		model.addAttribute("listCompany", obj.toString());
		return "map_locations.html";
	}

	@GetMapping("/admin/logout")
	public String logout(HttpServletRequest req, Model model, AdminUser logoutUser, HttpSession session) {
		session.setAttribute("adminId", null);
		session.setAttribute("adminUserName", null);
		System.out.println(logoutUser.getAdminId());
		return "redirect:/resourcing";
	}

}
