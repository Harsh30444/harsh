//*author: HarshKashyap

package com.resourcing.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.validation.Valid;

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

import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.InterviewerAvailability;
import com.resourcing.beans.Schedule;
import com.resourcing.repository.AppointmentRepository;
import com.resourcing.repository.InterviewerAvailabilityRepository;
import com.resourcing.service.AppointmentService;
import com.resourcing.service.CandidateService;
//import com.resourcing.dao.ImageDao;
import com.resourcing.service.InterviewPanelService;
import com.resourcing.service.InterviewerAvailabilityService;
import com.resourcing.service.ScheduleService;
import com.resourcing.utilities.Utilities;

@Controller

public class InterviewPanelController {

	static Logger LOGGER = Logger.getLogger(InterviewPanelController.class);

	@Autowired
	private InterviewPanelService interviewerService;
	@Autowired
	private InterviewerAvailabilityService interService;
	@Autowired
	InterviewerAvailabilityRepository interRepository;
	@Autowired
	AppointmentRepository appointmentRepository;
	@Autowired
	AppointmentService appointmentService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	CandidateService candidateService;

	// display home page

	@GetMapping("/resouce")
	public String firstPage() {
		LOGGER.info("displaying home page");
		return "homepage";
	}

	// AddUserMoethod creates an object for User pojo class
	@GetMapping("/inter")
	public ModelAndView addInterviewermethod() {
		ModelAndView mav = new ModelAndView("interviewerRegistration");
		InterviewPanel newInterviewer = new InterviewPanel();
		mav.addObject("newInterviewerDetails", newInterviewer);
		InterviewPanel oldInterviewer = new InterviewPanel();
		mav.addObject("InterviewerLoginDetails", oldInterviewer);
		return mav;
	}

	@GetMapping("/saveInterviewer")
	public ModelAndView saveUser(@ModelAttribute("newInterviewerDetails") InterviewPanel interviewer, Model model,
			HttpServletRequest request) {
		if (!interviewer.getCaptcha().equals(interviewer.getUserCaptcha())) {
			model.addAttribute("message", "Please enter valid Captcha");
			ModelAndView mvcc = new ModelAndView("interviewerRegistration");
			return mvcc;
		}
		String strEncPassword = Utilities.getEncryptSecurePassword(interviewer.getPassword(), "GLAM");
		interviewer.setPassword(strEncPassword);
		InterviewPanel inter = interviewerService.findByEmailId(interviewer.getInterviewerMail());

		if (inter != null) {
			String s1 = "emailId already exists!!";
			model.addAttribute("s1", s1);

			model.addAttribute("newInterviewerDetails", interviewer);
			ModelAndView mvc = new ModelAndView("interviewerRegistration");
			return mvc;
		} else {

			interviewer.setIsActive("yes");
			interviewer.setCreatedDate(LocalDateTime.now());
			interviewerService.addInterviewer(interviewer);

			interviewer.setCreatedby(interviewer.getInterviewerId());
			LOGGER.debug("aaaaaa" + interviewer.getInterviewerId());
			interviewerService.updateInterviewer(interviewer);
			LOGGER.info("name of the first:::" + interviewer.getInterviewerName());
			model.addAttribute("message", interviewer.getInterviewerName() + " registered successfully!!");

			model.addAttribute("newInterviewerDetails", interviewer);
			ModelAndView mac = new ModelAndView("interviewerLogin");
			return mac;
		}
	}

	@GetMapping("/login")
	public ModelAndView getloginPage() {
		LOGGER.info("entered into user/controller:::: login method");
		ModelAndView mav = new ModelAndView("interviewerlogin");
		InterviewPanel newInterviewer = new InterviewPanel();
		mav.addObject("InterviewerDetails", newInterviewer);
		return mav;
	}

	@PostMapping("/validateInterviewer")
	public ModelAndView validateUsermethod(InterviewPanel tinterviewer, ModelAndView mav, HttpServletRequest request,
			Object String, InterviewPanel interviewerProfile) {
		LOGGER.info("entered into validation method");
		String strEncPassword = Utilities.getEncryptSecurePassword(tinterviewer.getPassword(), "GLAM");
		tinterviewer.setPassword(strEncPassword);
		InterviewPanel pInterviewer = interviewerService.findByInterviewerMailIgnoreCaseAndPassword(tinterviewer.getInterviewerMail(), tinterviewer.getPassword());
		LOGGER.info("validation method done");
		if (pInterviewer != null) {
			HttpSession session = request.getSession();
			session.setAttribute("interviewerId", pInterviewer.getInterviewerId());
			LOGGER.debug("id:::::" + pInterviewer.getInterviewerId());
			LOGGER.debug("interviewer session is created");
			session.setAttribute("name", pInterviewer.getInterviewerName());
			LOGGER.info("controller validation method username is:" + pInterviewer.getInterviewerMail());
			  ModelAndView mac = new ModelAndView("interviewerDashboard");
			  mac.addObject("interviewerProfile", pInterviewer);
			  List<Schedule> allCandidates = scheduleService.ScheduleList(pInterviewer);
			  List<Schedule> selCandidates = allCandidates.stream().filter(list -> list.getFeedback()!= null && list.getFeedback().equalsIgnoreCase("selected") ).toList();
			  List<Schedule> rejCand = allCandidates.stream().filter(rej -> rej.getFeedback()!=null && rej.getFeedback().equalsIgnoreCase("rejected")).toList();
			  List<Schedule> notAttended=allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("NOTATTENDED")).toList();
				List<Schedule> allSchedules = scheduleService.ScheduleList(pInterviewer);
				List<Schedule> todaysSchedule= allSchedules.stream().filter(list ->list.getDate().equals(LocalDate.now())).collect(Collectors.toList());
				List<Schedule> pendingCandidate=allCandidates.stream().filter(list -> list.getFeedback().equalsIgnoreCase("PENDING")).toList();
				
			  mac.addObject("selCandidates", selCandidates.size());
			  mac.addObject("rejCand", rejCand.size());
			  mac.addObject("allCandidates", allCandidates.size());
			  mac.addObject("notAttended", notAttended.size());
				mac.addObject("pending", pendingCandidate.size());
				mac.addObject("todays", todaysSchedule.size());
				mac.addObject("allSchedule", allSchedules.size());
			return mac;
		} else {
			LOGGER.info("else block ");
			LOGGER.info("else block of validation:::: Interviewer doesnt exist you can register");
			ModelAndView mac = new ModelAndView("interviewerlogin");
			String s1 = "Invalid Credentials";
			mac.addObject("s1", s1);
			mac.addObject("newInterviewerDetails", interviewerProfile);
			return mac;
		}
	}
	


 

	@GetMapping("/dashboard.html/{id}")
	public String dashboardPage(@PathVariable int id, Model model) {
		InterviewPanel inter = interviewerService.getInterviewerById(id);
		model.addAttribute("inter", inter);
		model.addAttribute("interviewerAvailabilityLis", inter);
		List<Schedule> allCandidates = scheduleService.ScheduleList(inter);
		List<Schedule> selCandidates = allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("selected")).toList();
		List<Schedule> rejCand = allCandidates.stream().filter(rej -> rej.getFeedback()!= null && rej.getFeedback().equalsIgnoreCase("rejected")).toList();
		List<Schedule> notAttended=allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("NOTATTENDED")).toList();
		List<Schedule> allSchedules = scheduleService.ScheduleList(inter);
		List<Schedule> todaysSchedule= allSchedules.stream().filter(list ->list.getDate().equals(LocalDate.now())).collect(Collectors.toList());
		List<Schedule> pendingCandidate=allCandidates.stream().filter(list -> list.getFeedback().equalsIgnoreCase("PENDING")).toList();
		System.out.println("pending count:::"+ pendingCandidate.size());
		System.out.println("allSchedule count:"+  allSchedules.size());
		model.addAttribute("selCandidates", selCandidates.size());
		model.addAttribute("rejCand", rejCand.size());
		model.addAttribute("allCandidates", allCandidates.size());
		model.addAttribute("notAttended", notAttended.size());
		model.addAttribute("pending", pendingCandidate.size());
		model.addAttribute("todays", todaysSchedule.size());
		model.addAttribute("allSchedule", allSchedules.size());



		return "interviewerDashboard";
	}
	
	@GetMapping("/selCandidates/{id}")
	public String selectedCandidates(Model model,@PathVariable int id,InterviewPanel inter) {
		System.out.println("invoked");
		InterviewPanel interviewer=interviewerService.getInterviewerById(id);
		System.out.println("get the id:" + id);
		List<Schedule> allCandidates = scheduleService.ScheduleList(interviewer);
		List<Schedule> selCandidates = allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("selected")).toList();
		model.addAttribute("selCandidates", selCandidates);
		model.addAttribute("allCandidates", allCandidates);
		System.out.println("cvjkkjhgfdfgh");
		return "selCandidates";
	}
	@GetMapping("/rejCandidates/{id}")
	public String rejectedCandidates(Model model,@PathVariable int id) {
		System.out.println("invoked");
		InterviewPanel interviewer=interviewerService.getInterviewerById(id);
		System.out.println("get the id:" + id);
		List<Schedule> allCandidates = scheduleService.ScheduleList(interviewer);
		List<Schedule> selCandidates = allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("selected")).toList();

		List<Schedule> rejCand = allCandidates.stream().filter(rej -> rej.getFeedback()!= null && rej.getFeedback().equalsIgnoreCase("rejected")).toList();
		model.addAttribute("rejCand", rejCand);
		model.addAttribute("selCandidates", selCandidates);

		System.out.println("cvjkkjhgfdfgh");
		return "rejCandidates";
	}
	@GetMapping("/notAttended/{id}")
	public String notAttendedCandidates(Model model,@PathVariable int id) {
		System.out.println("invoked");
		InterviewPanel interviewer=interviewerService.getInterviewerById(id);
		System.out.println("get the id:" + id);
		List<Schedule> allCandidates = scheduleService.ScheduleList(interviewer);
		List<Schedule> selCandidates = allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("selected")).toList();
		List<Schedule> rejCand = allCandidates.stream().filter(rej -> rej.getFeedback()!= null && rej.getFeedback().equalsIgnoreCase("rejected")).toList();
		List<Schedule> notAttended=allCandidates.stream().filter(list -> list.getFeedback()!=null && list.getFeedback().equalsIgnoreCase("NOT ATTENDED")).toList();
	    System.out.println("count::"+notAttended.size());
		model.addAttribute("selCandidates", selCandidates);
		model.addAttribute("rejCand", rejCand);
		model.addAttribute("allCandidates", allCandidates);
		model.addAttribute("notAttended", notAttended);
		System.out.println("cvjkkjhgfdfgh");
		return "notAttendedCandidates";
	}
	@GetMapping("/pending/{id}")
	public String pendingCandidates(Model model,@PathVariable int id) {
		System.out.println(" pending/{id} ::invoked");
		InterviewPanel interviewer=interviewerService.getInterviewerById(id);
		System.out.println("get the inter id:" + id);
		List<Schedule> allCandidates = scheduleService.FeedbackList((InterviewPanel) interviewer);
		List<Schedule> pendingCandidate=allCandidates.stream().filter(list -> list.getFeedback().equalsIgnoreCase("pending")).toList();

		System.out.println("pending count:::"+ pendingCandidate.size());

		model.addAttribute("pending",  pendingCandidate.size());
		model.addAttribute("pending", pendingCandidate);
		System.out.println("pending list diplayed");
		return "pendingCandidates";
	}
	@GetMapping("/todaysSchedule/{id}")
	public String todaysSchedule(Model model,@PathVariable int id) {
		InterviewPanel interviewer=interviewerService.getInterviewerById(id);
		System.out.println("get the id:" + id);
		List<Schedule> allSchedules = scheduleService.ScheduleList(interviewer);
		List<Schedule> todaysSchedule= allSchedules.stream().filter(list ->list.getDate().equals(LocalDate.now())).collect(Collectors.toList());
		System.out.println("today schedule count::"+todaysSchedule.size());
		model.addAttribute("today", todaysSchedule);
		return "interviewerTodaysSchedule";
	}
	@GetMapping("/allSchedule/{id}")
	public String allSchedule(Model model,@PathVariable int id) {
		InterviewPanel inter=interviewerService.getInterviewerById(id);
		System.out.println("get the id:" + id);
		List<Schedule> allSchedules = scheduleService.ScheduleList(inter);
		System.out.println("all schedule count:::"+allSchedules.size());
		model.addAttribute("allSchedules", allSchedules);
		model.addAttribute("allSchedule", allSchedules.size());
		return"interviewerAllSchedule";
	}


	@GetMapping("/validateInter/{id}")
	public String getSettingPage(Model model, @PathVariable("id") int interviewerId, InterviewPanel intervi) {
		LOGGER.info("setting is working");
		InterviewPanel interv = interviewerService.getInterviewerById(interviewerId);
		model.addAttribute("interviewerProfile", interv);
		return "interviewerDashboard-settings";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(Model model, @ModelAttribute("interviewerProfile") InterviewPanel interviewerProfile) {

		InterviewPanel interviewers = interviewerService.getInterviewerById(interviewerProfile.getInterviewerId());
		LOGGER.info("local date valuesctaking::::");
		LOGGER.info("name of the first::: interviewer" + interviewerProfile.getInterviewerName());
		LOGGER.info("name of the first::: interviewers" + interviewers.getInterviewerId());
		interviewerService.addInterviewer(interviewerProfile);
		interviewerProfile.setUpdatedby(interviewerProfile.getInterviewerId());
		LOGGER.info("name of the first:::" + interviewerProfile.getInterviewerName());
		model.addAttribute("candidate", interviewerProfile);
		model.addAttribute("interviewerName", interviewerProfile.getInterviewerName());
		interviewerService.saveInterviewer(interviewerProfile);
		return "interviewerDashboard";
	}

	
	@RequestMapping(value = "/index-logged-out.html", method = RequestMethod.GET)
	public String userlogout(InterviewPanel newUser, Model model, HttpSession session) {
		LOGGER.info("logout method in controller activated");
	
		session.setAttribute("interviewerName", null);
		model.addAttribute("interviewerName", null);
		LOGGER.info("interviewername after session after sessio.invalidate():" + newUser.getInterviewerMail());

		return "redirect:/resourcing";

	}

	@RequestMapping(value = "/validateInter/index-logged-out.html", method = RequestMethod.GET)
	public String uselogout(InterviewPanel newUser, Model model, HttpSession session) {
		LOGGER.info("logout method in controller activated");
		
		session.setAttribute("interviewerName", null);
		model.addAttribute("interviewerName", null);
		LOGGER.info("interviewername after session after sessio.invalidate():" + newUser.getInterviewerMail());

		return "redirect:/resourcing";

	}

	@RequestMapping(value = "/availabilityList/index-logged-out.html", method = RequestMethod.GET)
	public String useelogout(InterviewPanel newUser, Model model, HttpSession session) {
		LOGGER.info("logout method in controller activated");
	
		session.setAttribute("interviewerName", null);
		model.addAttribute("interviewerName", null);
		LOGGER.info("interviewername after session after sessio.invalidate():" + newUser.getInterviewerMail());

		return "redirect:/resourcing";

	}
	

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView UserforgotPasswordPage(InterviewPanel newUser) {
		LOGGER.info("entered into user/controller::::forgot paswword method");
		ModelAndView mav = new ModelAndView("interviewerforgotPassword");
		mav.addObject("newUserDetails", newUser);
		return mav;
	}

	@PostMapping(value = "/validteEmailId")
	public String checkMailId(Model model, InterviewPanel tempUser) {
		LOGGER.info("entered into user/controller::::check EmailId existing or not");
		LOGGER.info("UI given mail Id:" + tempUser.getInterviewerMail());
		InterviewPanel pUser = interviewerService.findByEmailId(tempUser.getInterviewerMail());
		if (pUser != null) {
			model.addAttribute("newUserDetails", tempUser);
			return "interviewerResetPassword";
		} else {
			model.addAttribute("newUserDetails", tempUser);
			model.addAttribute("message", "email doesn't exist!!!");
			return "interviewerforgotPassword";
		}

	}

	@RequestMapping(value = "/updateUserPassword", method = RequestMethod.POST)
	public ModelAndView updateUserPassword(Model model, @ModelAttribute("newUserDetails") InterviewPanel tempUser) {
		String strEncPassword = Utilities.getEncryptSecurePassword(tempUser.getPassword(), "GLAM");
		tempUser.setPassword(strEncPassword);

		InterviewPanel pUser = interviewerService.findByEmailId(tempUser.interviewerMail);

		LOGGER.info(" in update method created date::" + pUser.getCreatedDate());

		LOGGER.info("in update method pUser:: name " + pUser.getInterviewerId());
		pUser.setUpdatedby(pUser.getInterviewerId());

		pUser.setPassword(tempUser.getPassword());
		interviewerService.updateInterviewer(pUser);
		LOGGER.info("password is updated sucessfully");

		ModelAndView mav = new ModelAndView("interviewerlogin");
		LOGGER.info("login page is displayed");
		String s1 = "Your Password Is Updated";
		mav.addObject("s1", s1);
		mav.addObject("newUserDetails", pUser);

		return mav;
	}

	@RequestMapping(value = "/availability.html", method = RequestMethod.GET)
	public String availability(Model model, @ModelAttribute("inter") InterviewerAvailability inter) {
		model.addAttribute("inter", inter);
		return "interviewerAvailability";
	}

	@PostMapping(value = "/dashboard-availability")
	public String saveAvailability(Model model, @ModelAttribute(value = "inter") InterviewerAvailability interval,
			@ModelAttribute("objInter") InterviewPanel intro) {

		LOGGER.info("schedule page is displayed");

		interval.setInterviewerAvailabilityId((int) Math.random());
		interService.saveInterviewerAvailability(interval);
		model.addAttribute("inter", interval);
		LOGGER.debug("from::::" + interval.getFromDate());
		return "redirect:/jam";
	}

	@GetMapping("/jam")
	public String showingpage(Model model, @ModelAttribute(value = "inter") InterviewerAvailability inter) {
		LOGGER.debug(inter.getFromDate());
		model.addAttribute("inter", inter);
		return "interviewerDashboard";
	}

	@GetMapping("/availabilityList/{interviewerId}")
	public String getAvailabilityOfOneInterviewer(Model model, @PathVariable("interviewerId") int interviewerId) {
		LOGGER.debug("this will get the inteviewers availability schedule:::" + interviewerId);
		InterviewPanel interviewer = interviewerService.getInterviewerById(interviewerId);
		LOGGER.debug("finding the interviewer_id value:::" + interviewer.getInterviewerId());
		List<InterviewerAvailability> interviewerAvailabilityList = interService
				.getAllInterviewerAvailabilities(interviewer);
		model.addAttribute("interviewer", interviewer.getInterviewerId());
		model.addAttribute("Name", interviewer.getInterviewerName());
		model.addAttribute("interviewerAvailabilityLis", interviewerAvailabilityList);
		return "interviewerAvailabilityList";
	}

	@GetMapping("/availability/{interviewerId}")
	public String getAvailabilityOfInterviewer(Model model, @PathVariable("interviewerId") int interviewerId) {
		LOGGER.debug("this will get the inteviewers availability schedule:::" + interviewerId);
		InterviewPanel interviewer = interviewerService.getInterviewerById(interviewerId);
		LOGGER.debug("finding the interviewer_id value:::" + interviewer.getInterviewerId());
		List<InterviewerAvailability> interviewerAvailabilityList = interService
				.getAllInterviewerAvailabilities(interviewer);
		model.addAttribute("interviewer", interviewer.getInterviewerId());
		model.addAttribute("Name", interviewer.getInterviewerName());
		model.addAttribute("interviewerAvailabilityLis", interviewerAvailabilityList);
		return "listInterviewerAvailability";
	}

	@GetMapping("/updateAvailability/{interviewerId}/{interviewerAvailabilityId}")
	public String showFormForUpdate(Model model, @PathVariable(value = "interviewerId") int interviewerId,
			@PathVariable(value = "interviewerAvailabilityId") int interviewerAvailabilityId) {
		InterviewerAvailability interviewerAvailability = interService
				.getInterviewerAvailabilityById(interviewerAvailabilityId);
		LOGGER.debug("interviewerAvailabilityId is:::" + interviewerAvailability.getInterviewerAvailabilityId());
		InterviewPanel interviewer = interviewerService.getInterviewerById(interviewerId);
		LOGGER.debug("interviewer id is:::" + interviewer.getInterviewerId());
		model.addAttribute("interviewer", interviewer.getInterviewerId());
		model.addAttribute("Name", interviewer.getInterviewerName());
		model.addAttribute("interviewerAvailabilityList", interviewerAvailability);
		LOGGER.debug("*****************");
		return "updateAvailability";
	}

	@PostMapping("/updateAvailabilities")
	public String updateAvailabilityDetails(Model model, HttpSession session,
			InterviewerAvailability interviewerAvailability, InterviewPanel interviewer) {
		int id = (int) session.getAttribute("interviewerId");

		LOGGER.debug("??????????");
		InterviewerAvailability objInterviewerAvailability = interService
				.getInterviewerAvailabilityById(interviewerAvailability.getInterviewerAvailabilityId());
		LOGGER.debug("date" + objInterviewerAvailability.getFromDate());
		objInterviewerAvailability.setFromDate(interviewerAvailability.getFromDate());
		objInterviewerAvailability.setToDate(interviewerAvailability.getToDate());
		interService.updateInterviewerAvailability(objInterviewerAvailability);
		LOGGER.debug("id::");
		List<InterviewerAvailability> interviewerAvailability2 = interService.getAllInterviewerAvailabilities();
		model.addAttribute("interviewerAvailabilityLis", interviewerAvailability);
		return "redirect:/availabilityList/" + id;
	}

	@GetMapping("/List")
	public String getAvailabilityOfInterviewer(Model model,HttpSession session,
			@ModelAttribute("interviewerAvailabilityLis") InterviewerAvailability interviewerAvailability,
			@ModelAttribute("interviewerAvailabilityLis") InterviewPanel interviewPanel) {
		int id = (int) session.getAttribute("interviewerId");
		InterviewPanel panel = interviewerService.getInterviewerById(id);
		LOGGER.debug("avilabe::::" + interviewPanel.getInterviewerMail());
		List<InterviewerAvailability> interviewerAvailabilityList = interService.getAllInterviewerAvailabilities();
		List<InterviewPanel> interviewerList = interviewerService.getAllInterviewers();
		List<InterviewerAvailability> availableList = interService.getAllInterviewerAvailabilities(panel);
		model.addAttribute("interviewerAvailabilityLis", availableList);
		return "interAvailabilityList";
	}
	

	@GetMapping("/interList")
	public String getListOfInterviewer(Model model,
			@ModelAttribute("interviewerAvailabilityLis") InterviewPanel interviewPanel,
			@ModelAttribute("interviewerAvail") InterviewerAvailability interviewer) {

		List<InterviewPanel> interviewerList = interviewerService.getAllInterviewers();
		List<InterviewerAvailability> avilabiltyList = interService.getAllInterviewerAvailabilities();

		model.addAttribute("Ilist", interviewerList);
		model.addAttribute("Iavail", interviewer);
		return "interviewerList";
	}

	@GetMapping("/interviewer/dashboard")
	public String dashboard(Model model, @PathVariable int id) {
		InterviewPanel data = interviewerService.getInterviewerById(id);
		model.addAttribute("candidate", data);
		return "interviewerDashboard";
	}

	

	

// for feedback	
	@GetMapping("/feedbackList/{id}")
	public String listOfSchedules(Model model, @PathVariable int id, HttpSession session) {
		InterviewPanel interviewer = interviewerService.getInterviewerById(id);

		// InterviewPanel session = (Employee) session.getAttribute("employeeObj");
		List<Schedule> objSchedule = scheduleService.FeedbackList(interviewer);
		model.addAttribute("scheduleList", objSchedule);
		// model.addAttribute("interviewer", interviewer);

		return "interviewerFeedback";
	}

	@PostMapping("/saveFeedback/{id}")
	public String listOffeedback(Model model, @PathVariable int id, @ModelAttribute("schedule") Schedule schedule,
			@ModelAttribute("scheduleList") Schedule scheduleObj, HttpSession session) {
		LOGGER.debug("schedule feedback::" + schedule);
		Schedule scheduleExist = scheduleService.getScheduleById(id);
		LOGGER.debug("selected schedule::" + scheduleExist.getFeedback());
		LOGGER.debug("selected schedule::" + scheduleExist);
		scheduleExist.setFeedback("PENDING");
		scheduleExist.setComments(schedule.getComments());
		scheduleExist.setFeedback(schedule.getFeedback());
		scheduleService.updateSchedule(scheduleExist);
		// update is done
		// to get final feedbackList

		return "redirect:/feedbackList/" + scheduleExist.getInterviewPanel().getInterviewerId();
	}

	@GetMapping("/finalFeedbackList/{id}")
	public String feedbackList(Model model, @PathVariable int id, HttpSession session) {
		InterviewPanel interviewer = interviewerService.getInterviewerById(id);
		List<Schedule> objSchedule = scheduleService.finalFeedbackList(interviewer);
		model.addAttribute("scheduleList", objSchedule);
		return "interviewer_feedback_list_dup";
	}
}
