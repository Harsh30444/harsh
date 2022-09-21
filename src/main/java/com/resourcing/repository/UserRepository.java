/* 
 * Author : Srivani Tudi */

package com.resourcing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Branch;
import com.resourcing.beans.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select a from User a WHERE lower(a.emailId)=lower(?1) AND a.password=?2")
	public User findByUserNameIgnoreCaseAndPassword(String emailId, String password);

	@Query("select a from User a WHERE lower(a.emailId)=lower(?1)")
	public User findByEmailId(String emailId);
	
	@Query("select a from User a WHERE  a.branch=?1")
	public User findByBranch(Branch branch);

}
