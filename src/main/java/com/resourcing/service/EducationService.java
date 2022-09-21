/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Education;

public interface EducationService {
	void addEducation(Education education);

	List<Education> getAllEducations();

	List<Education> getAllEducations(Candidate candidateId);

	void updateEducation(Education education);

	void deleteEducationById(int educationId);

	Education getEducationById(int educationId);

}
