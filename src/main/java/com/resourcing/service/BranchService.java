//Auther: Vineeth kumar Mudham
package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Branch;

public interface BranchService {

	List<Branch> getAllBranchs();

	void deleteBranchById(int id);

	void saveBranch(Branch branch);

	Branch getBranchById(int id);
	
	Branch getEmailId(String emailId);

	Branch updateBranch(Branch branch);

	void addBranch(Branch branch);

	public List<Branch> getByIsActive();

	public List<String> getBranchNamesByActiveStatus();

	Branch findByBranchName(String branchName);

}
