/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.Client;
import com.resourcing.beans.Doc;
import com.resourcing.beans.Employee;
import com.resourcing.beans.JobDescription;
import com.resourcing.beans.Schedule;
import com.resourcing.repository.CandidateRepository;
import com.resourcing.service.CandidateJdAssociationService;
import com.resourcing.service.CandidateService;
import com.resourcing.service.DocStorageService;
import com.resourcing.service.EducationService;
import com.resourcing.service.EmailSenderService;
import com.resourcing.service.EmployeeService;
import com.resourcing.service.JobDescriptionService;
import com.resourcing.service.ScheduleService;
import com.resourcing.utilities.Utilities;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

	static Logger LOGGER = Logger.getLogger(CandidateController.class);

	@Autowired
	CandidateService candidateService;

	@Autowired
	EducationService educationService;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	JobDescriptionService jobDescriptionService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	private DocStorageService docStorageService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CandidateJdAssociationService candidateJdAssociationService;

	@GetMapping("/homePage")
	public String getHomePage(Model model, JobDescription objJobDescription) {
		LOGGER.debug("entered into getHomePage::::opening Page of Welcome");
		model.addAttribute("objJd", objJobDescription);
		return "candidate_index_logged_out";
	}

//  To Get Job Descriptions List	
	@GetMapping("/jdList")
	public String getJdList(Model model, Client client) {
		List<JobDescription> jdList = jobDescriptionService.getAllJobDescriptions();
		model.addAttribute("objJobDescription", jdList);
		model.addAttribute("clientDetails", client);
		return "candidate_client_dashboard_jobList";
	}

	// get the list of candidates
	@GetMapping("/candidateList")
	public String getAllCandidates(Model model) {
		LOGGER.debug("inside getAllCandidates this will get the all Candidates:::");
		List<Candidate> candidateList = candidateService.getAllCandidates();
		model.addAttribute("candidateList", candidateList);
		return "candidate_list";
	}

	// Insert candidate record
	@GetMapping(value = "/saveCandidate")
	public String saveCandidate(Model model, @ModelAttribute("candidate") Candidate candidate) {
		Candidate candidateObj = candidateService.findByEmail(candidate.getEmail());
		if (candidateObj == null) {
			String strEncPassword = Utilities.getEncryptSecurePassword(candidate.getPassword(), "GLAM");
			candidate.setPassword(strEncPassword); // while saving the candidate set this to encrypt pwd.
			candidateService.addCandidate(candidate);
			candidate.setCreatedDate(LocalDateTime.now());
			LOGGER.debug("created date:::" + candidate.getCreatedDate());
			LOGGER.debug("created_by:::" + candidate.getCandidateId());
			// after adding the account we get the candidateId that's why we are updating
			// the created by here..
			candidate.setCreatedBy(candidate.getCandidateId());
			candidate.setIsActive('Y');
			candidateService.updateCandidate(candidate);
			LOGGER.debug("name of the first:::" + candidate.getFirstName());
			model.addAttribute("message", "registered successfully!!");
			model.addAttribute("candidate", candidate);
			return "candidate_pages_login";
		} else {
			model.addAttribute("message",
					candidate.getEmail() + "  is already exists!! please try with another mail!!");
			model.addAttribute("candidate", candidate);
			return "candidate_pages_register";
		}
	}

	// candidate form will appear to enter his/her details
	@GetMapping("")
	public String newCandidate(Model model, HttpServletRequest request, Candidate candidate,
			final HttpServletResponse response, HttpSession session) {
		LOGGER.debug("add candidate::::");
		final StringBuffer url1 = request.getRequestURL();
		HttpSession sessionOne = request.getSession();
		sessionOne.setAttribute("url", url1);
		LOGGER.debug("url1::::::" + url1);
		model.addAttribute("candidate", candidate);
		return "candidate_pages_register";
	}

	// verification of mail if it is valid or not
	@RequestMapping("/verification")
	public String validatemail(Model model, Candidate candidate, final HttpServletRequest request,
			final HttpServletResponse response, HttpSession session) throws MessagingException {
		Candidate candidateObj = candidateService.findByEmail(candidate.getEmail());
		if (candidateObj != null) {
			model.addAttribute("message",
					candidate.getEmail() + "  is already exists!! please try with another mail!!");
			model.addAttribute("candidate", candidate);
			return "candidate_pages_register";
		} else {
			final StringBuffer uri = request.getRequestURL();
			LOGGER.debug(uri);
			model.addAttribute("Emailid", candidate.getEmail());
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			LOGGER.debug(":::::::::::::::::2" + candidate.getEmail());
			mailMessage.setTo(candidate.getEmail());
			LOGGER.debug("::::::::::::::::: 3" + candidate.getEmail());
			mailMessage.setSubject("Complete Registration!");
			LOGGER.debug("::::::::::::::::: 4" + candidate.getEmail());
			mailMessage.setFrom("t.srivani488@gmail.com");
			LOGGER.debug("::::::::::::::::: 5" + candidate.getEmail());
			mailMessage.setText("To confirm your account, please click here : " + session.getAttribute("url")
					+ "/saveCandidate?email=" + candidate.getEmail() + "&password=" + candidate.getPassword());
			LOGGER.debug("::::::::::::::::: 6" + session.getAttribute("url") + "saveCandidate?email="
					+ candidate.getEmail() + "&password=" + candidate.getPassword());
			candidateService.sendSimpleEmail(candidate.getEmail(),
					"To confirm your account, please click here : " + session.getAttribute("url")
							+ "/saveCandidate?email=" + candidate.getEmail() + "&password=" + candidate.getPassword(),
					"Mailid Verification");
			LOGGER.debug("::::::::::::::::: 7" + candidate.getEmail());
			model.addAttribute("Emailid", candidate.getEmail());
			LOGGER.debug("::::::::::::::::: 8" + candidate.getEmail());
			return "successfulRegistration";
		}
	}

	@GetMapping("/dashboardSettings/{id}")
	public String dashboardSettingsPage(Model model, @PathVariable int id) {
		Candidate candidate = candidateService.getCandidateById(id);
		model.addAttribute("candidate", candidate);
		return "candidate_dashboard_settings";
	}

	@RequestMapping(value = "/updateCandidateDetails/{id}", method = RequestMethod.POST)
	public String updateCandidateDetails(Model model, @ModelAttribute("candidate") Candidate candidateObj,
			@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
		Candidate existCandidate = candidateService.getCandidateById(id);
		LOGGER.debug("is active status:::" + existCandidate.getIsActive());
		candidateObj.setUpdatedBy(existCandidate.getCandidateId());
		candidateObj.setUpdatedDate(LocalDateTime.now());
		candidateObj.setCreatedDate(existCandidate.getCreatedDate());
		candidateObj.setCreatedBy(existCandidate.getCandidateId());
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(candidateObj.getDateOfBirth(), currentDate);
		candidateObj.setAge(period.getYears() + " years " + period.getMonths() + " months " + period.getDays() + " days");
		if (file.getOriginalFilename() == "") {
			candidateObj.setImage(existCandidate.getImage());
		} else {
			candidateObj.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
		}
		Candidate candidate = candidateRepository.save(candidateObj);
		model.addAttribute("candidateObj", candidate);
		model.addAttribute("message", "candidate details are updates");
		return "candidate_dashboard_settings";
	}


	// Delete Candidate record http://localhost:8080/candidate/deleteCandidate/2
	@GetMapping("/deleteCandidate/{id}")
	public String deleteCandidate(Model model, @PathVariable int id, HttpSession session) {
		LOGGER.debug("inside deleteCandidate id:::" + id);
		Candidate candidate = candidateService.getCandidateById(id);
		candidate.setIsActive('N');
		candidateService.updateCandidate(candidate);
		LOGGER.debug("candidated updated successfully");
		LOGGER.debug("id value is:::" + id);
		return "redirect:/candidate";
	}
				
	// candidate can login here
	@GetMapping("/login")
	public String login(Model model, @ModelAttribute("candidate") Candidate candidate) {
		model.addAttribute("candidate", candidate);
		return "candidate_pages_login";
	}

	@RequestMapping(value = "/checkCredentials", method = RequestMethod.POST)
	public String checkCredentials(Model model, @ModelAttribute("candidate") Candidate candidate,
			HttpServletRequest request) {
		LOGGER.debug("getCaptcha:::::" + candidate.getCaptcha());
		LOGGER.debug("getUserCaptcha::::::" + candidate.getUserCaptcha());
		if (!candidate.getCaptcha().equals(candidate.getUserCaptcha())) {
			model.addAttribute("message", "Please enter valid Captcha");
			return "candidate_pages_login";
		}
		String strEncPassword = Utilities.getEncryptSecurePassword(candidate.getPassword(), "GLAM");
		Candidate objCandidate = candidateService.getCandidate(candidate.getEmail(), strEncPassword);
		LOGGER.debug("email:::::" + candidate.getEmail());
		LOGGER.debug("password:::::" + strEncPassword);
		if (objCandidate != null) {
			if (objCandidate.getIsActive() == 'Y') {
				LOGGER.debug("login successfully");
				LOGGER.debug("credential login user::::" + objCandidate.getEmail());
				HttpSession session = request.getSession();
				LOGGER.debug("session created:::::");
				session.setAttribute("candidateObj", objCandidate);
				LOGGER.debug("session created:::::::::::::::::::::::::::::::::");
				LOGGER.debug("session working:::::");
				LOGGER.debug("objschedule::::::");
				List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
				List<CandidateJdAssociation> candidateJobList = list.stream().filter(obj -> obj.getCandidate().getCandidateId()== objCandidate.getCandidateId()).
						collect(Collectors.toList());
				List<CandidateJdAssociation> selected = candidateJobList.stream().filter(obj ->obj.getStatus().equals("SELECTED")).collect(Collectors.toList());
				List<CandidateJdAssociation> rejected = candidateJobList.stream().filter(obj ->obj.getStatus().equals("REJECTED")).collect(Collectors.toList());
				List<CandidateJdAssociation> pending = candidateJobList.stream().filter(obj ->obj.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
				List<CandidateJdAssociation> scheduleJobs = candidateJobList.stream().filter(obj -> obj.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
				List<CandidateJdAssociation> notScheduleJobs = candidateJobList.stream().filter(obj -> obj.getStatus().equals("NOT SCHEDULED")).collect(Collectors.toList());
				List<Schedule> allSchedules = scheduleService.getAllSchedules();
				List<Schedule> mySchedules = allSchedules.stream().filter(obj -> obj.getCandidate().getCandidateId()==objCandidate.getCandidateId()).collect(Collectors.toList());
				List<Schedule> todaySchedule= mySchedules.stream().filter(today ->today.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());
				List<Schedule> upcomingSchedule= mySchedules.stream().filter(upcoming ->upcoming.getDate().isAfter(LocalDate.now())).collect(Collectors.toList());
				model.addAttribute("todaySchedule", todaySchedule.size());
				model.addAttribute("upcomingSchedule", upcomingSchedule.size());
				model.addAttribute("selectedJobs", selected.size());
				model.addAttribute("pendingJobs", pending.size());
				model.addAttribute("rejectedJobs", rejected.size());
				model.addAttribute("appliedJobs", candidateJobList.size());
				model.addAttribute("notScheduledJobs", notScheduleJobs.size());
				model.addAttribute("scheduledJobs", scheduleJobs.size());
				model.addAttribute("candidate", objCandidate);
				return "candidate_dashboard";
			} else {
				model.addAttribute("candidate", candidate);
				model.addAttribute("invalid", "your account is not here.. pls register..");
				return "candidate_pages_login";
			}
		} else {
			LOGGER.debug("invalid credentials");
			String invalid = "invalid Credentials";
			model.addAttribute("invalid", invalid);
			model.addAttribute("candidate", new Candidate());
			return "candidate_pages_login";
		}
	}

	@GetMapping("/dashboard/{id}")
	public String dashboard(Model model, @PathVariable int id) {
		Candidate candidate = candidateService.getCandidateById(id);
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> candidateJobList = list.stream().filter(obj -> obj.getCandidate().getCandidateId()== candidate.getCandidateId()).
				collect(Collectors.toList());
		List<CandidateJdAssociation> selected = candidateJobList.stream().filter(obj ->obj.getStatus().equals("SELECTED")).collect(Collectors.toList());
		List<CandidateJdAssociation> rejected = candidateJobList.stream().filter(obj ->obj.getStatus().equals("REJECTED")).collect(Collectors.toList());
		List<CandidateJdAssociation> pending = candidateJobList.stream().filter(obj ->obj.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
		List<CandidateJdAssociation> scheduleJobs = candidateJobList.stream().filter(obj -> obj.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
		List<CandidateJdAssociation> notScheduleJobs = candidateJobList.stream().filter(obj -> obj.getStatus().equals("NOT SCHEDULED")).collect(Collectors.toList());
		List<Schedule> allSchedules = scheduleService.getAllSchedules();
		List<Schedule> mySchedules = allSchedules.stream().filter(obj -> obj.getCandidate().getCandidateId()==id).collect(Collectors.toList());
		List<Schedule> todaySchedule= mySchedules.stream().filter(today ->today.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());
		List<Schedule> upcomingSchedule= mySchedules.stream().filter(upcoming ->upcoming.getDate().isAfter(LocalDate.now())).collect(Collectors.toList());
		model.addAttribute("todaySchedule", todaySchedule.size());
		model.addAttribute("upcomingSchedule", upcomingSchedule.size());
		model.addAttribute("selectedJobs", selected.size());
		model.addAttribute("pendingJobs", pending.size());
		model.addAttribute("rejectedJobs", rejected.size());
		model.addAttribute("appliedJobs", candidateJobList.size());
		model.addAttribute("notScheduledJobs", notScheduleJobs.size());
		model.addAttribute("scheduledJobs", scheduleJobs.size());
		model.addAttribute("candidate", candidate);
		return "candidate_dashboard";
	}

	// ==========================forgot password and reset======================//
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView candidateForgotPasswordPage(HttpSession session, Candidate newCandidate) {
		LOGGER.debug("entered into candidate/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("candidate_forgot_password");
		mav.addObject("newCandidateDetails", newCandidate);
		return mav;
	}

	@PostMapping(value = "/validateEmail")
	public String checkMailId(HttpSession session, Model model, Candidate tempCandidate) {
		LOGGER.debug("entered into Candidate/controller::::check EmailId existing or not");
		LOGGER.debug("UI given mail Id:" + tempCandidate.getEmail());
		Candidate existCandidate = candidateService.findByEmail(tempCandidate.getEmail());
		if (existCandidate != null) {
			model.addAttribute("newCandidateDetails", tempCandidate);
			return "candidate_reset_password";
		} else {
			model.addAttribute("newCandidateDetails", tempCandidate);
			model.addAttribute("message", "email doesn't exist!!!");
			return "candidate_forgot_password";
		}
	}

	@RequestMapping(value = "/updateCandidatePassword", method = RequestMethod.POST)
	public ModelAndView updateCandidatePassword(Model model,
			@ModelAttribute("newCandidateDetails") Candidate tempCandidate, HttpSession session) {
		Candidate existCandidate = candidateService.findByEmail(tempCandidate.getEmail());
		LOGGER.debug(" in update method employee created date::" + existCandidate.getCreatedDate());
		LOGGER.debug("in update method pUser:: name " + existCandidate.getFirstName());
		existCandidate.setUpdatedBy(existCandidate.getCandidateId());
		existCandidate.setUpdatedDate(LocalDateTime.now());
		LOGGER.debug("new password is::::" + existCandidate.getPassword());
		String strEncPassword = Utilities.getEncryptSecurePassword(tempCandidate.getPassword(), "GLAM");
		tempCandidate.setPassword(strEncPassword);
		existCandidate.setPassword(tempCandidate.getPassword());
		candidateService.updateCandidate(existCandidate);
		LOGGER.debug("password is updated sucessfully:::" + existCandidate.getPassword());
		ModelAndView mav = new ModelAndView("candidate_pages_login");
		LOGGER.debug("login page is displayed");
		mav.addObject("candidate", existCandidate);
		mav.addObject("message", "your password is upadated!!");
		return mav;
	}// ==========================forgot password and reset======================//

	// candidate logout method
	@GetMapping("/logout")
	public String logout(Model model,HttpSession session, Candidate logoutCandidate, HttpServletRequest request) {
		session.setAttribute("candidateObj", null);
		LOGGER.debug("session:::"+session.getAttribute("candidateObj"));
		return "redirect:/resourcing";
	}


// uploading files =============================//
// =============================================//

	@GetMapping("/doc/{candidateId}")
	public String listOfCandidateDocs(Model model, @PathVariable int candidateId) {
		LOGGER.debug("candidateId in list method:::::" + candidateId);
		Candidate candidate = candidateService.getCandidateById(candidateId);
		List<Doc> docs = docStorageService.getAllFilesById(candidate.getCandidateId());
		Comparator<Doc> docName = Comparator.comparing(Doc::getDocName);
		List<Doc> sortedDocs = docs.stream().sorted(docName).collect(Collectors.toList());
		model.addAttribute("docs", sortedDocs);
		return "candidate_doc";
	}

	@PostMapping("/uploadFiles/{candidateId}")
	public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, Doc docObj,
			@PathVariable int candidateId) throws IOException {
		for (MultipartFile file : files) {
			LOGGER.debug("candidateId in upload method::;" + candidateId);
			docObj.setCandidateId(candidateId);
			docStorageService.saveFile(file, docObj);
			LOGGER.debug("uploaded successfully::::::");
		}
		return "redirect:/candidate/doc/" + candidateId;
	}

	// download the document by file id
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) throws Exception {
		Doc doc = docStorageService.getFileById(fileId);
		LOGGER.debug(":::::::::::");
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + doc.getDocName() + "\"")
				.body(new ByteArrayResource(doc.getData()));
	}

	// delete a document by id
	@GetMapping("/deleteFile/{candidateId}/{fileId}")
	public String deleteFile(@PathVariable int candidateId, @PathVariable int fileId, Doc doc) {
		LOGGER.debug("fileid:::::" + fileId);
		docStorageService.deleteFileById(fileId);
		LOGGER.debug("file deleted:::::");
		return "redirect:/candidate/doc/" + candidateId;
	}

	//get Feedback of interview
	@GetMapping("/feedbackOfInterview/{id}")
	public String scheduleList(Model model, @PathVariable int id) {
		LOGGER.debug("method invoked:::::::::");
		Candidate candidateObj = candidateService.getCandidateById(id);
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(candidateObj);
		LOGGER.debug("size:::::" + objSchedule.size());
		model.addAttribute("scheduled JObs", objSchedule.size());
		model.addAttribute("scheduleList", objSchedule);
		return "candidate_interview_feedbacklist";
	}

	@GetMapping("/interviewSchedules/{id}")
	public String feedbackList(Model model, @PathVariable int id) {
		LOGGER.debug("method invoked:::::::::");
		Candidate candidateObj = candidateService.getCandidateById(id);
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(candidateObj);
		model.addAttribute("scheduleList", objSchedule);
		if (candidateObj.getEmployeeObj() == null) {
			model.addAttribute("message", "Sorry! No recruiter viewed Your Profile");
			return "candidate_interview_schedulelist";
		} else {
			int employeeId = candidateObj.getEmployeeObj().getEmployeeId();
			LOGGER.debug("contact recuiter::Id::" + employeeId);
			Employee recruiter = employeeService.getEmployeeById(employeeId);
			model.addAttribute("recruiter", recruiter);
			model.addAttribute("TableName", "Interview schedules and Feedback List");
			return "candidate_interview_schedulelist";
		}
	}

	@GetMapping("/contactRecruiter/{id}")
	public String contactRecruiter(Model model, @PathVariable int id) {
		LOGGER.debug("method invoked:::::::::");
		Candidate candidate = candidateService.getCandidateById(id);
		int employeeId = candidate.getEmployeeObj().getEmployeeId();
		Employee recruiter = employeeService.getEmployeeById(employeeId);
		model.addAttribute("recruiter", recruiter);
		return "candidate_recruiter_details";
	}
	
	//=========methods for dashboard having all the details about the Schedule..===========//
	
	@GetMapping("/selectedJobs/{candidateId}")
	public String statusOfTheInterview(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> schedule = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		System.out.println("check::"+schedule.size());
		List<CandidateJdAssociation> selected = schedule.stream().filter(result ->  result.getStatus().equals("SELECTED")).collect(Collectors.toList());
		model.addAttribute("TableName"," Congratulations!! you are selected for "+ selected.size() +" JOBs ");
		model.addAttribute("result", selected);
		return "candidate_jd_status";
	}
	
	@GetMapping("/rejectedJobs/{candidateId}")
	public String candidateRejectedJobs(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> schedule = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		System.out.println("check::"+schedule.size());
		List<CandidateJdAssociation> selected = schedule.stream().filter(result -> result.getStatus().equals("REJECTED")).collect(Collectors.toList());
		model.addAttribute("TableName",  " you are rejected for "+ selected.size() +" JOBs ");
		model.addAttribute("result", selected);
		return "candidate_jd_status";
	}
	
	@GetMapping("/pendingJobs/{candidateId}")
	public String candidatePendingJobs(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> schedule = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		System.out.println("check::"+schedule.size());
		List<CandidateJdAssociation> selected = schedule.stream().filter(result -> result.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
		model.addAttribute("TableName",   selected.size() + " JOBs are in pending state");
		model.addAttribute("result", selected);
		return "candidate_jd_status";
	}
	
	@GetMapping("/scheduledJobs/{candidateId}")
	public String scheduledJobs(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> candidateList = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		List<CandidateJdAssociation> scheduled = candidateList.stream().filter(result -> result.getStatus().equals("SCHEDULED")).collect(Collectors.toList());
		model.addAttribute("TableName",   scheduled.size() +" JOBs are scheduled for you!!");
		model.addAttribute("association", scheduled);
		return "candidate_jd_status";
	}
	
	@GetMapping("/notScheduledJobs/{candidateId}")
	public String notScheduledJobs(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> candidateList = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		List<CandidateJdAssociation> scheduled = candidateList.stream().filter(result -> result.getStatus().equals("NOT SCHEDULED")).collect(Collectors.toList());
		model.addAttribute("TableName",   scheduled.size() +" JOBs are need to schedule for you!!");
		model.addAttribute("association", scheduled);
		return "candidate_jd_status";
	}
	
	@GetMapping("/detailViewOfSchedule/{candidateId}/{jdId}")
	public String detaileViewOfSchedule(@PathVariable int candidateId,@PathVariable int jdId,Model model) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		JobDescription jobDescription = jobDescriptionService.getJobDescriptionById(jdId);
		List<Schedule> schedule = scheduleService.listOfCandidateAndJdAssociation(candidate, jobDescription);
		model.addAttribute("schedule", schedule);
		model.addAttribute("TableName",   "Feedback of all your Interview Phases for Job "+jobDescription.getPosition().toUpperCase());
		return "candidate_jd_feedback";
	}
	
	@GetMapping("/appliedJobs/{candidateId}")
	public String appliedJobs(@PathVariable int candidateId,Model model) {
		List<CandidateJdAssociation> list = candidateJdAssociationService.getAllCandidateJdAssociations();
		List<CandidateJdAssociation> jdList = list.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		model.addAttribute("TableName",   "Feedback Of all your "+jdList.size() +"JOBs");
		model.addAttribute("appliedJobs", jdList);
		return "candidate_jd_status";
	}
	
	@GetMapping("/todaysSchedule/{candidateId}")
	public String todaySchedule(Model model,@PathVariable int candidateId) {
		List<Schedule> allSchedules = scheduleService.getAllSchedules();
		List<Schedule> mySchedules = allSchedules.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		List<Schedule> todaySchedule= mySchedules.stream().filter(list ->list.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());
		model.addAttribute("schedule", todaySchedule);
		model.addAttribute("TableName", "TODAY SCHEDULES");
		return "candidate_today_schedule";
	}
	
	@GetMapping("/upcomingSchedule/{candidateId}")
	public String upcomingSchedule(Model model,@PathVariable int candidateId) {
		List<Schedule> allSchedules = scheduleService.getAllSchedules();
		List<Schedule> mySchedules = allSchedules.stream().filter(obj -> obj.getCandidate().getCandidateId()==candidateId).collect(Collectors.toList());
		List<Schedule> upcomingSchedule= mySchedules.stream().filter(list ->list.getDate().isAfter(LocalDate.now())).toList();
		System.out.println("today schedule count::"+upcomingSchedule.size());
		model.addAttribute("schedule", upcomingSchedule);
		model.addAttribute("TableName", "UPCOMING SCHEDULES");
		return "candidate_today_schedule";
	}
	
	//==================	Dashboard  Closed		=================================//
	

}
