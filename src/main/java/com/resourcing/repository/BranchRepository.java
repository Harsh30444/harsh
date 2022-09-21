//Auther: Vineeth kumar Mudham

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Branch;
import com.resourcing.beans.Company;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

	@Query(value = "select a from Branch a WHERE a.isActive='Y'")
	public List<Branch> getByIsActive();

	@Query(value = "select branchName from Branch WHERE isActive='Y'")
	public List<String> getBranchNamesByActiveStatus();
	
	@Query("select a from Branch a where a.branchName=?1")
	public Branch findByBranchName(String branchName);
	
	@Query("select a from Branch a where a.emailId=?1")
	public Branch findByEmail(String emailId);


}
