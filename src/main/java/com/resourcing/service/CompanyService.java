//Auther: Vineeth kumar Mudham
package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Company;

public interface CompanyService {

	Company getCompanyById(int id);

	void saveCompany(Company company);

	Company updateCompany(Company company);

	void deleteCompanyById(int id);

	List<Company> getAllCompanyies();

	void addCompany(Company company);

	public List<Company> getByIsActive();
	
	Company findByCompanyName(String companyName);
}
