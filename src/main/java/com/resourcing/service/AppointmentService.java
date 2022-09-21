package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Appointment;

public interface AppointmentService {
	// Appointment Table
	List<Appointment> getAllAppointment();

	void saveAppointment(Appointment appointment);

	Appointment getAppointmentById(long appointmentId);

	Appointment addAppointment(Appointment appointment);

	void updateAppointment(Appointment appointment);

	void deleteAppointmentById(long appointmentId);

}
