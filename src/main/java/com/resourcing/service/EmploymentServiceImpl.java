
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
import com.resourcing.beans.Employment;
import com.resourcing.repository.EmploymentRepository;

@Service
public class EmploymentServiceImpl implements EmploymentService {

	@Autowired
	private EmploymentRepository employmentRepository;

	@Override
	public void addEmployment(Employment employment) {
		employmentRepository.save(employment);
	}

	@Override
	public List<Employment> getAllEmployments() {
		return employmentRepository.findAll();
	}

	@Override
	public List<Employment> getAllEmployments(Candidate candidateId) {
		return employmentRepository.findAllEmployments(candidateId);
	}

	@Override
	public void updateEmployment(Employment employment) {
		employmentRepository.save(employment);
	}

	@Override
	public void deleteEmploymentById(int employmentId) {
		try {
			this.employmentRepository.deleteById(employmentId);

		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public Employment getEmploymentById(int employmentId) {
		return employmentRepository.findById(employmentId).get();
	}

}
