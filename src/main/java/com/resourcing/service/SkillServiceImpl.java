/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Skill;
import com.resourcing.repository.SkillRepository;

@Service
public class SkillServiceImpl implements SkillService {

	@Autowired
	SkillRepository skillRepository;

	@Override
	public List<Skill> getAllSkills() {
		return skillRepository.findAll();
	}

	@Override
	public Skill getSkillBySkillName(String skillName) {
		return skillRepository.getSkillBySkillName(skillName);
	}

	@Override
	public Skill getSkillBySkillId(int skillId) {
		return skillRepository.getById(skillId);
	}

}
