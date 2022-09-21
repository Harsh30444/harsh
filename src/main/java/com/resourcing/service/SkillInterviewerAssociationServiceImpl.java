//*author: HarshKashyap

package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.SkillInterviewerAssosiation;
import com.resourcing.repository.SkillInterviewerAssociationRepository;
@Service
public class SkillInterviewerAssociationServiceImpl implements SkillInterviewerAssociationService {
    
	@Autowired
	SkillInterviewerAssociationRepository siaRepository;
	
	@Override
	public List<SkillInterviewerAssosiation> getAllSkillInterviewerAssociations() {
		// TODO Auto-generated method stub
		return siaRepository.findAll();
	}

	@Override
	public void addNewSia(SkillInterviewerAssosiation skillInterviewerAssociation) {
		// TODO Auto-generated method stub
		siaRepository.save(skillInterviewerAssociation);
	}

	

	
	@Override
	public List<SkillInterviewerAssosiation> findSkillListByInterviewerId(int interviewerId) {
		// TODO Auto-generated method stub
		return siaRepository.findSkillListByInterviewerId(interviewerId);
	}

	@Override
	public List<SkillInterviewerAssosiation> findBySkill(int skillId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SkillInterviewerAssosiation> findInterviewerBySkillId(int skillId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SkillInterviewerAssosiation findBySkillIdAndInterviewerId(int interviewerId, int skillId) {
		// TODO Auto-generated method stub
		return siaRepository.checkPairOfSkillAndInterviewer(interviewerId, skillId);
	}

	@Override
	public void updateSia(SkillInterviewerAssosiation sia) {
		// TODO Auto-generated method stub
		siaRepository.save(sia);
	}

	@Override
	public void deleteSkillInterviewerAssociationBySkillId(int skillId) {
		// TODO Auto-generated method stub
		this.siaRepository.deleteById(skillId);
	}

	@Override
	public SkillInterviewerAssosiation getAssociationById(int associationId) {
		// TODO Auto-generated method stub
		return siaRepository.findById(associationId).get();
	}

	@Override
	public SkillInterviewerAssosiation updateSkillAssociation(SkillInterviewerAssosiation soa) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
