package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Position;

public interface PositionService {

	Position addPosition(Position details);

	Position getDataById(int id);

	List<Position> getAllPositions();

	Position getPositionById(int id);

	void deletePositionById(int id);

	public List<Position> findPositionByDepartment(String name);

	Position updatePosition(Position post);
	
	Position findByPostionIgnoreCaseAndDepartmentIgnoreCase(String deptName, String postName);

}
