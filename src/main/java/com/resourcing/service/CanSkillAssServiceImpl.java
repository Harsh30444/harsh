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

import com.resourcing.beans.CandidateSkillAssociation;
import com.resourcing.repository.CanSkillAssRepository;

@Service
public class CanSkillAssServiceImpl implements CanSkillAssService {

	@Autowired
	CanSkillAssRepository canSkillAssRepository;

	@Override
	public List<CandidateSkillAssociation> getAllCandidateSkillAssociations() {
		return canSkillAssRepository.findAll();
	}

	@Override
	public void addNewCsa(CandidateSkillAssociation csa) {
		canSkillAssRepository.save(csa);
	}

	@Override
	public List<CandidateSkillAssociation> findSkillListByCandidateId(int candidateId) {
		return canSkillAssRepository.findSkillListByCanId(candidateId);
	}

	@Override
	public CandidateSkillAssociation updateEca(CandidateSkillAssociation csa) {
		return null;
	}

	@Override
	public List<CandidateSkillAssociation> findEmpListByClient(int id) {
		return null;
	}

	@Override
	public List<CandidateSkillAssociation> findClientsByEmployeeId(int employeeId) {
		return null;
	}

	@Override
	public CandidateSkillAssociation findBySkillIdAndCanId(int candidateId, int skillId) {
		return canSkillAssRepository.checkPairOfSkillAndCandidate(candidateId, skillId);
	}

	@Override
	public void updateCandidateSkill(CandidateSkillAssociation candidate) {
		canSkillAssRepository.save(candidate);
	}

	@Override
	public void deleteCandidateSkillAssociationBySkillId(int skillId) {
		this.canSkillAssRepository.deleteById(skillId);
	}

	@Override
	public CandidateSkillAssociation getAssociationById(int associationId) {
		return canSkillAssRepository.findById(associationId).get();
	}

	@Override
	public List<CandidateSkillAssociation> findCandidateListBySkillId(int skillId) {
		return canSkillAssRepository.findCandidateListBySkillId(skillId);
	}
}
