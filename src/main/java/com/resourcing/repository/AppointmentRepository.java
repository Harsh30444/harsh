package com.resourcing.repository;


	import java.util.List;

	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.stereotype.Repository;

import com.resourcing.beans.Appointment;

	

	@Repository
	public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

		@Query(value = "select al from Appointment al where al.interviewPanel.interviewerId=?1 and al.date=?2")
		List<Appointment> appointmentListByInterviewerid(int interviewerId, String date);

		@Query(value = "SELECT appointment_id, created_by, isactive,   update_by, interviewer_id,  date,\r\n"
				+ " FROM public.appointment where interviewer_id=?1 and date between ?2 and ?3", nativeQuery = true)
		List<Appointment> interviewerAppointmentList(int interviewerId, String from, String to);

		@Query(value = "select al from Appointment al where al.interviewPanel.interviewerId=?1 and date between ?2 and ?3")
		List<Appointment> applointmentListBetweenDatesByInterviewerId(int interviewerid, String from, String to);
		
//		@Query(value="select al from Appointment al where al.patient.patientId=?1")
//		Appointment getallapointmentkinks(int patientid);
		
		

	}



