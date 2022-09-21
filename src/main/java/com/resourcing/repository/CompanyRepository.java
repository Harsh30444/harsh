//Auther: Vineeth kumar Mudham
package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	@Query(value = "select a from Company a WHERE a.isActive='Y'")
	public List<Company> getByIsActive();
	
	@Query("select a from Company a where a.companyName=?1")
	public Company findByCompanyName(String companyName);

}
