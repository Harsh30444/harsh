//Auther: Vineeth kumar Mudham
package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Branch;
import com.resourcing.repository.BranchRepository;

@Service
public class BranchServiceImpl implements BranchService {
	@Autowired
	private BranchRepository branchRepository;

	@Override
	public List<Branch> getAllBranchs() {
		return branchRepository.findAll();
	}

	@Override
	public void deleteBranchById(int id) {
		try {
			branchRepository.deleteById(id);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	@Override
	public void saveBranch(Branch branch) {
		branchRepository.save(branch);
	}

	@Override
	public Branch getBranchById(int id) {
		Optional<Branch> optional = branchRepository.findById(id);
		Branch objBranch = null;
		if (optional.isPresent()) {
			objBranch = optional.get();
		} else {
			throw new RuntimeException("repository:::: branch  not found for id:::" + id);
		}

		return objBranch;
	}

	@Override
	public Branch updateBranch(Branch branch) {
		return branchRepository.save(branch);
	}

	@Override
	public void addBranch(Branch branch) {
		branchRepository.save(branch);
	}

	@Override
	public List<Branch> getByIsActive() {
		return branchRepository.getByIsActive();
	}

	@Override
	public List<String> getBranchNamesByActiveStatus() {
		return branchRepository.getBranchNamesByActiveStatus();
	}

	@Override
	public Branch findByBranchName(String branchName) {
		// TODO Auto-generated method stub
		return branchRepository.findByBranchName(branchName);
	}

	
	@Override
	public Branch getEmailId(String emailId) {
		return branchRepository.findByEmail(emailId);
	}

}
