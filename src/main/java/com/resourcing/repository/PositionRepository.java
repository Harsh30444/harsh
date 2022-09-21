package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
	@Query(value = "SELECT al FROM Position al WHERE al.deptName =?1 AND al.isActive='Y'")
	List<Position> findPositionByDepartment(String name);
	
	@Query("select a from Position a where lower(a.deptName) = lower(?1) and lower(a.postName)=lower(?2) ")
	Position findByPostionIgnoreCaseAndDepartmentIgnoreCase(String deptName, String postName);
	
}
