/* 
 * Author : Srivani Tudi */

package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.resourcing.beans.Branch;
import com.resourcing.beans.Employee;
import com.resourcing.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(int employeeId) {
		Optional<Employee> optional = employeeRepository.findById(employeeId);
		Employee employee = null;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			throw new RuntimeException("repository:::: employee not found for id:::" + employeeId);
		}

		return employee;
	}

	@Override
	public void deleteEmployeeById(int employeeId) {
		try {
			employeeRepository.deleteById(employeeId);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	@Override
	public void saveEmployee(Employee employee) {
		this.employeeRepository.save(employee);

	}

	@Override
	public void addNewEmployee(Employee newEmployee) {
		this.employeeRepository.save(newEmployee);

	}

	@Override
	public Employee findByUserNameIgnoreCaseAndPassword(String employeeName, String password) {
		return employeeRepository.findByUserNameIgnoreCaseAndPassword(employeeName, password);
	}

	@Override
	public Employee updateEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee findByEmailId(String emailId) {
		return employeeRepository.findByEmailId(emailId);
	}

	@Override
	public List<Employee> getByIsActive() {
		return employeeRepository.getByIsActive();
	}

	

	@Override
	public List<Employee> getEmployeesListofThisbranch(Branch branch) {
		return employeeRepository.getEmployeesListofThisbranch(branch);
	}

	@Override
	public List<Employee> getRecruitersListByActiveStatus() {
		return employeeRepository.getRecruitersListByActiveStatus();
	}

	@Override
	public List<Employee> getEmployeeListByRole(String role, Branch branch) {
		return employeeRepository.getEmployeeListByRole(role, branch);
	}

	@Override
	public List<Employee> allEmployeesList() {
		return employeeRepository.allEmployeesList();
	}

	@Override
	public List<Employee> getActiveRecruitersListOfThisbranch(Branch branch) {
		return employeeRepository.getActiveRecruitersListOfThisbranch(branch);
	}

}
