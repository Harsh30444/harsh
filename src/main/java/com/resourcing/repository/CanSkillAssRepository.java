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
import org.springframework.stereotype.Repository;

import com.resourcing.beans.CandidateSkillAssociation;

@Repository
public interface CanSkillAssRepository extends JpaRepository<CandidateSkillAssociation, Integer> {

	@Query("select a from CandidateSkillAssociation a WHERE a.candidateId=?1")
	public List<CandidateSkillAssociation> findSkillListByCanId(int candidateId);

	@Query("select a from CandidateSkillAssociation a WHERE a.candidateId=?1 AND a.skillId=?2")
	public CandidateSkillAssociation checkPairOfSkillAndCandidate(int candidateId, int skillId);

	@Query("select a from CandidateSkillAssociation a WHERE a.skillId=?1")
	public List<CandidateSkillAssociation> findCandidateListBySkillId(int skillId);

}
