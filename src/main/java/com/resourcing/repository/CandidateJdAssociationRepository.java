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

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.JobDescription;

@Repository
public interface CandidateJdAssociationRepository extends JpaRepository<CandidateJdAssociation, Integer> {

	@Query("select a from CandidateJdAssociation a WHERE a.candidate=?1")
	public List<CandidateJdAssociation> findByCandidate(Candidate candidateId);

	@Query("select a from CandidateJdAssociation a WHERE a.jobDescription=?1")
	public List<CandidateJdAssociation> findCandidatesByJdId(JobDescription jdId);
	
	@Query("select a from CandidateJdAssociation a WHERE a.jobDescription=?1")
	CandidateJdAssociation findByJobDescription(JobDescription jd);

	@Query("select a from CandidateJdAssociation a where a.candidate=?1")
	public List<CandidateJdAssociation> appliedJobsByCandidate(Candidate candidateId);

	@Query("select a from CandidateJdAssociation a where a.candidate=?1")
	public CandidateJdAssociation appliedJobsByCandidateList(Candidate candidateId);

	@Query("select a from CandidateJdAssociation a where a.candidate = ?1 and a.jobDescription=?2 ")
	CandidateJdAssociation findByCandidateIdAndJdId(Candidate candidateId, JobDescription jdId);
	
	@Query("select a from CandidateJdAssociation a where a.candidate = ?1 and a.jobDescription=?2 ")
	List<CandidateJdAssociation> listOfCandidateJdAssociation(Candidate candidate, JobDescription jd);

}
