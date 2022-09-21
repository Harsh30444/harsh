/* 
 * Author : Srivani Tudi */

package com.resourcing.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.resourcing.beans.Branch;
import com.resourcing.beans.Employee;

public interface EmployeeService {
	List<Employee> getAllEmployees();

	void deleteEmployeeById(int id);

	void saveEmployee(Employee employee);

	Employee getEmployeeById(int id);

	Employee updateEmployee(Employee employee);

	void addNewEmployee(Employee employee);

	Employee findByUserNameIgnoreCaseAndPassword(String employeeName, String password);

	Employee findByEmailId(String emailId);

	public List<Employee> getByIsActive();

	public List<Employee> getEmployeesListofThisbranch(Branch branch);


	public List<Employee> getRecruitersListByActiveStatus();

	public List<Employee> getEmployeeListByRole(String role, Branch branch);

	public List<Employee> allEmployeesList();

	public List<Employee> getActiveRecruitersListOfThisbranch(Branch branch);

//	List<Employee> getEmployeesListofThisbranch(int branchId);

}
