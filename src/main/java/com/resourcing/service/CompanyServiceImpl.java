//Auther: Vineeth kumar Mudham
package com.resourcing.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Company;
import com.resourcing.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Company getCompanyById(int id) {

		Optional<Company> optional = companyRepository.findById(id);
		Company company = null;
		if (optional.isPresent()) {
			company = optional.get();
		} else {
			throw new RuntimeException("repository:::: company not found for id:::" + company);
		}

		return company;
	}

	@Override
	public void saveCompany(Company company) {

		companyRepository.save(company);
	}

	@Override
	public Company updateCompany(Company company) {
		return companyRepository.save(company);
	}

	@Override
	public void deleteCompanyById(int id) {

		try {
			companyRepository.deleteById(id);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public List<Company> getAllCompanyies() {
		return companyRepository.findAll();
	}

	@Override
	public void addCompany(Company company) {
		companyRepository.save(company);
	}

	@Override
	public List<Company> getByIsActive() {
		return companyRepository.getByIsActive();
	}

	@Override
	public Company findByCompanyName(String companyName) {
		return companyRepository.findByCompanyName(companyName);
	}

}
