/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Skill;

public interface SkillService {

	List<Skill> getAllSkills();

	Skill getSkillBySkillName(String skillName);

	Skill getSkillBySkillId(int skillId);

}
