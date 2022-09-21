package com.resourcing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.resourcing.beans.Appointment;
import com.resourcing.repository.AppointmentRepository;
import com.resourcing.service.AppointmentService;
import com.resourcing.service.InterviewPanelService;

@Controller
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;
	@Autowired
	InterviewPanelService interviewerService;

	@Autowired
	AppointmentRepository appointmentRepository;

	static Logger log = Logger.getLogger(AppointmentController.class.getClass());

	@GetMapping(value = "/appointmentlist")
	public String appointmentlist(Model model) {
		List<Appointment> appointment = appointmentService.getAllAppointment();
		log.info("===========Appointment List=============");
		log.info("Appointment : " + appointment);
		List<Appointment> list = new ArrayList<>();
		for (Appointment app : appointment) {
			if (app.getIsactive() == 'Y' || app.getIsactive() == 'y') {
				list.add(app);
			}
		}
		model.addAttribute("appointmentlist", list);
		return "pappointmentlist";
	}

	

	@GetMapping(value = "/appointmentlist/{id}")
	public String pAppointmentlist(Model model, @PathVariable("id") int interviewerId, HttpServletRequest request) {
		List<Appointment> appointment = appointmentService.getAllAppointment();
		log.info("===========Appointment List=============");
		log.info("Appointment : " + appointment);
		List<Appointment> list = new ArrayList<>();
		for (Appointment app : appointment) {
			if (app.getIsactive() != 'N' && app.getIsactive() != 'n') {
				list.add(app);
			}
		}
		//HttpSession session = request.getSession();
		//model.addAttribute("appointmentlist", list);
		//model.addAttribute("doctorid", interviewerId);
		//model.addAttribute("meetingLink", "");
		return "pappointmentlist";
	}

	@GetMapping(value = "/deleteappointment/{did}/{aid}/{from}/{to}")
	public String deleteAppointment(@PathVariable("id") int interviewerId, @PathVariable("aid") long aid,
			@PathVariable("from") String from, @PathVariable("to") String to) {
		Appointment inactive = appointmentService.getAppointmentById(aid);
		inactive.setIsactive('N');
		appointmentService.updateAppointment(inactive);
		return "redirect:/getallappointments/" + interviewerId + "?from=" + from + "&to=" + to;
	}


	@RequestMapping(value = "/saveappointment/{id}", method = RequestMethod.POST)
	public String addPatientAppointment(Model model, @PathVariable("id") int interviewerId, Appointment appointment) {
		appointment.setIsactive('Y');
		appointmentService.updateAppointment(appointment);
		appointmentRepository.save(appointment);
		log.info("Inserting :" + appointment);
		log.info("=========Patient appointment Inserted=======");
		return "redirect://appointmentlist" + interviewerId;
	}

	
}
