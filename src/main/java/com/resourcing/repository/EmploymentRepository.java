
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
import com.resourcing.beans.Employment;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Integer> {
	@Query(value = "SELECT a1 FROM Employment a1 WHERE a1.candidate =?1")
	List<Employment> findAllEmployments(Candidate candidateId);

}
