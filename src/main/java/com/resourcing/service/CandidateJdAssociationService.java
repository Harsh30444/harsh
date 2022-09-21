/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.JobDescription;

public interface CandidateJdAssociationService {

	List<CandidateJdAssociation> getAllCandidateJdAssociations();

	void addNewCandidateJdAssociation(CandidateJdAssociation candidateJdAssociation);

	CandidateJdAssociation findCandidateJdAssociationById(int candidateJdAssociationId);

	void updateCandidateJdAssociation(CandidateJdAssociation candidateJdAssociation);

	public List<CandidateJdAssociation> findByCandidate(Candidate candidateId);

//	public List<CandidateJdAssociation> findByJd(JobDescription jdId);

	public List<CandidateJdAssociation> findCandidatesByJdId(JobDescription jdId);

//	public List<CandidateJdAssociation> findJdByCandidateId(Candidate candidateId);

//	public List<Integer> getJobdIds();

	public CandidateJdAssociation findJdByCandidateIdList(Candidate candidateId);
	
	CandidateJdAssociation findByJobDescription(JobDescription jd);

	public CandidateJdAssociation findByCandidateIdAndJdId(Candidate candidateId, JobDescription jdId);
	
	List<CandidateJdAssociation> listOfCandidateJdAssociation(Candidate candidate, JobDescription jd);

}
