package com.resourcing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	
	@Query("select a from Department a WHERE lower(a.deptName)=lower(?1)")
	public Department findByDepartmentName(String deptName);

}