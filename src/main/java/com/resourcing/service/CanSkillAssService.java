/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.CandidateSkillAssociation;

public interface CanSkillAssService {

	List<CandidateSkillAssociation> getAllCandidateSkillAssociations();

	void addNewCsa(CandidateSkillAssociation csa);

	void updateCandidateSkill(CandidateSkillAssociation candidate);

	void deleteCandidateSkillAssociationBySkillId(int skillId);

	CandidateSkillAssociation getAssociationById(int skillId);

	List<CandidateSkillAssociation> findSkillListByCandidateId(int candidateId);

	List<CandidateSkillAssociation> findCandidateListBySkillId(int skillId);

	CandidateSkillAssociation updateEca(CandidateSkillAssociation csa);

	public List<CandidateSkillAssociation> findEmpListByClient(int id);

	public List<CandidateSkillAssociation> findClientsByEmployeeId(int employeeId);

	public CandidateSkillAssociation findBySkillIdAndCanId(int candidateId, int skillId);

}
