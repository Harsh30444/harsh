package com.resourcing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Client;
import com.resourcing.beans.JobDescription;
import com.resourcing.repository.JobDescriptionRepository;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService {
	@Autowired
	private JobDescriptionRepository repository;

	@Override
	public JobDescription addJobDescription(JobDescription jd) {
		return repository.save(jd);
	}

	@Override
	public JobDescription getJobDescriptionById(int jdId) {
		return repository.findById(jdId).get();
	}

	@Override
	public void updateJobDescription(JobDescription jd) {
		repository.save(jd);

	}

	@Override
	public void deleteJobDescriptionById(int jdId) {
		try {
			repository.deleteById(jdId);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	@Override
	public List<JobDescription> getAllJobDescriptions() {
		return repository.findAll();
	}

//	@Override
//	public List<JobDescription> findAllJobDescriptionByClient(int ClientId) {
//		return repository.findAllJobDescriptionByClient(ClientId);
//	}

	@Override
	public List<JobDescription> getJdListOfThisDepartment(JobDescription department) {

		return repository.getJdListOfThisDepartment(department);
	}

	@Override
	public List<JobDescription> getJobDescriptionListByLocationAndPosition(String location, String position) {
		return repository.getJobDescriptionListByLocationAndPosition(location, position);
	}

	@Override
	public List<JobDescription> findByKeyword(String keyword) {
		return repository.findByKeyword(keyword);
	}

	@Override
	public List<JobDescription> findAllJobDescriptionByClient(Client Client) {
		return repository.findAllJobDescriptionByClient(Client);
	}

	@Override
	public List<JobDescription> findAllActiveJobDescriptionByClient(Client client) {
		return repository.findAllActiveJobDescriptionByClient(client);
	}

	@Override
	public List<JobDescription> findInActiveJobDescriptionByClient(Client clientId) {
		return repository.findInActiveJobDescriptionByClient(clientId);
	}

//	@Override
//	public List<JobDescription> findAllActiveJobDescriptionByClient(int ClientId) {
//		return repository.findAllActiveJobDescriptionByClient(null);
//	}

//	@Override
//	public List<JobDescription> findAllActiveJobDescriptionByClient(Client clientId) {
//		return repository.findAllActiveJobDescriptionByClient(clientId);
//	}

}
