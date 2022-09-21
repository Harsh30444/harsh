/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.Education;
import com.resourcing.repository.EducationRepository;

@Service
public class EducationServiceImpl implements EducationService {
	
	@Autowired
	private EducationRepository educationRepository;

	@Override
	public void addEducation(Education education) {
		this.educationRepository.save(education);

	}

	@Override
	public List<Education> getAllEducations() {
		return educationRepository.findAll();
	}

	@Override
	public List<Education> getAllEducations(Candidate candidateId) {

		return educationRepository.findAllEducations(candidateId);
	}

	@Override
	public Education getEducationById(int educationId) {
		return educationRepository.findById(educationId).get();
	}

	@Override
	public void updateEducation(Education education) {
		educationRepository.save(education);

	}

	@Override
	public void deleteEducationById(int educationId) {
		try {
			this.educationRepository.deleteById(educationId);

		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

}
