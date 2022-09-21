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

import com.resourcing.beans.Candidate;
import com.resourcing.beans.CandidateJdAssociation;
import com.resourcing.beans.JobDescription;
import com.resourcing.repository.CandidateJdAssociationRepository;

@Service
public class CandidateJdAssociationServiceImpl implements CandidateJdAssociationService {

	@Autowired
	CandidateJdAssociationRepository candidateJdAssociationRepository;

	@Override
	public List<CandidateJdAssociation> getAllCandidateJdAssociations() {
		return candidateJdAssociationRepository.findAll();
	}

	@Override
	public void addNewCandidateJdAssociation(CandidateJdAssociation candidateJdAssociation) {
		candidateJdAssociationRepository.save(candidateJdAssociation);
	}

	@Override
	public CandidateJdAssociation findCandidateJdAssociationById(int candidateJdAssociationId) {
		return candidateJdAssociationRepository.findById(candidateJdAssociationId).get();
	}

	@Override
	public void updateCandidateJdAssociation(CandidateJdAssociation candidateJdAssociation) {
		candidateJdAssociationRepository.save(candidateJdAssociation);
	}

	@Override
	public List<CandidateJdAssociation> findByCandidate(Candidate candidateId) {
		return candidateJdAssociationRepository.findByCandidate(candidateId);
	}


	@Override
	public List<CandidateJdAssociation> findCandidatesByJdId(JobDescription jdId) {
		return candidateJdAssociationRepository.findCandidatesByJdId(jdId);
	}


	@Override
	public CandidateJdAssociation findJdByCandidateIdList(Candidate candidateId) {
		return candidateJdAssociationRepository.appliedJobsByCandidateList(candidateId);
	}

	@Override
	public CandidateJdAssociation findByCandidateIdAndJdId(Candidate candidateId, JobDescription jdId) {
		return candidateJdAssociationRepository.findByCandidateIdAndJdId(candidateId, jdId);
	}

	@Override
	public List<CandidateJdAssociation> listOfCandidateJdAssociation(Candidate candidate, JobDescription jd) {
		return candidateJdAssociationRepository.listOfCandidateJdAssociation(candidate, jd);
	}

	@Override
	public CandidateJdAssociation findByJobDescription(JobDescription jd) {
		return candidateJdAssociationRepository.findByJobDescription(jd);
	}

}
