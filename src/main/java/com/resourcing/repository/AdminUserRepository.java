//Auther: Vineeth kumar Mudham
package com.resourcing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.AdminUser;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {

	@Query("select u from adminUser u WHERE lower(u.emailId)=lower(?1) AND u.password=?2")
	public AdminUser findByAdminNameIgnoreCaseAndPassword(String adminMail, String adminPassword);

	@Query("select u from adminUser u WHERE lower(u.emailId)=lower(?1)")
	public AdminUser findByEmailId(String emailId);

}