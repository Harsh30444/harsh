
package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Department;

public interface DepartmentService {

	Department addDepartment(Department name);

	List<Department> getAllDepartment();

	List<Department> getAllDepartmentById(int id);

	List<Department> getAllDepartmentById(Iterable<Integer> id);

	Department getDepartmentById(int id);

	void deleteCategoryById(int id);

	public Iterable<Department> findAll();

	public Department find(int id);
	
	public Department findByDepartmentName(String deptName);
}
