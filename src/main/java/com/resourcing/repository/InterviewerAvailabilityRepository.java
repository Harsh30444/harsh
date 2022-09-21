//*author: HarshKashyap

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.InterviewPanel;
import com.resourcing.beans.InterviewerAvailability;

@Repository
public interface InterviewerAvailabilityRepository extends JpaRepository<InterviewerAvailability, Integer> {

	@Query(value = "SELECT a1 FROM InterviewerAvailability a1 WHERE a1.interviewPanel=?1")
	List<InterviewerAvailability> findAllInterviewerAvailability(InterviewPanel interviewerId);
	
	@Query("select al from InterviewerAvailability al where al.interviewPanel.interviewerId=?1")
	public List<InterviewerAvailability> availableInterviewer(int interviewerId);

}
