
package com.resourcing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Department;
import com.resourcing.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public Department addDepartment(Department name) {

		return departmentRepository.save(name);
	}

	@Override
	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}

	@Override
	public List<Department> getAllDepartmentById(int id) {
		return null;
	}

	@Override
	public List<Department> getAllDepartmentById(Iterable<Integer> id) {
		return null;
	}

	@Override
	public Department getDepartmentById(int id) {
		return departmentRepository.getById(id);
	}

	@Override
	public void deleteCategoryById(int id) {
		departmentRepository.deleteById(id);
	}

	@Override
	public Iterable<Department> findAll() {
		return departmentRepository.findAll();
	}

	@Override
	public Department find(int id) {
		return departmentRepository.findById(id).get();
	}

	@Override
	public Department findByDepartmentName(String deptName) {
		return departmentRepository.findByDepartmentName(deptName);
	}
}
