/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Employment;

public interface EmploymentService {

	void addEmployment(Employment employment);

	List<Employment> getAllEmployments();

	List<Employment> getAllEmployments(Candidate candidateId);

	void updateEmployment(Employment employment);

	void deleteEmploymentById(int employmentId);

	Employment getEmploymentById(int employmentId);

}
