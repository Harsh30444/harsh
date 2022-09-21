
/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.resourcing.beans.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	@Query(value = "SELECT al FROM Skill al WHERE lower(al.skillName) =lower(?1)")
	List<Skill> findPositionByDepartment(String name);

	@Query(value = "SELECT al FROM Skill al WHERE lower(al.skillName) =lower(?1)")
	Skill getSkillBySkillName(String skillName);

}
