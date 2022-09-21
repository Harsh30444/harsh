
/* 
 *@Author : Srivani Tudi */

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Branch;
import com.resourcing.beans.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	@Query("select u from Employee u WHERE lower(u.emailId)=lower(?1) AND u.password=?2")
	public Employee findByUserNameIgnoreCaseAndPassword(String emailId, String password);

	@Query("select a from Employee a WHERE lower(a.emailId)=lower(?1)")
	public Employee findByEmailId(String emailId);

	@Query(value = "select a from Employee a WHERE a.isActive='Y'")
	public List<Employee> getByIsActive();

	@Query(value = "SELECT a1 FROM Employee a1 WHERE a1.branch=?1")
	List<Employee> getEmployeesListofThisbranch(Branch branchId);

	@Query(value = "select e from Employee e WHERE lower(e.employeeRole)=lower('RECRUITER') AND e.isActive='Y'")
	public List<Employee> getRecruitersListByActiveStatus();

	@Query(value = "select e from Employee e WHERE lower(e.employeeRole)=lower('RECRUITER') AND e.isActive='Y' AND e.branch=?1")
	public List<Employee> getActiveRecruitersListOfThisbranch(Branch branch);

	@Query(value = "select e from Employee e WHERE lower(e.employeeRole)=lower(?1) AND e.branch=?2")
	public List<Employee> getEmployeeListByRole(String role, Branch branchId);

	@Query("select e from Employee e")
	public List<Employee> allEmployeesList();
	
	
	

	
	

}
