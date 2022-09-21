/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Employee;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

	@Query("select a from Candidate a where lower(a.email) = lower(?1) and a.password=?2 ")
	Candidate findByEmailIgnoreCaseAndPassword(String email, String password);

	@Query("select a from Candidate a WHERE lower(a.email)=lower(?1)")
	public Candidate findByEmail(String email);

	@Query(value = "SELECT a1 FROM Candidate a1 WHERE a1.employeeObj=null")
	List<Candidate> newCandidatesList();

	@Query(value = "SELECT a1 FROM Candidate a1 WHERE a1.employeeObj IS NOT NULL")
	List<Candidate> getassignedCandidateList();

	@Query(value = "SELECT a1 FROM Candidate a1 WHERE a1.employeeObj=?1")
	List<Candidate> getCandidateListOfRecruiter(Employee employee);
	
//	@Query(value = "SELECT a1 FROM Candidate a1 WHERE a.candidateId=?1 AND a1.employeeObj=?2")
//	Employee getRecruiterDetails(int candidateId,int employeeId);
}
