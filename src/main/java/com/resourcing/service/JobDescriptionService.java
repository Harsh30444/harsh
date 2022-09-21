package com.resourcing.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.resourcing.beans.Client;
import com.resourcing.beans.JobDescription;

public interface JobDescriptionService {

	JobDescription addJobDescription(JobDescription jd);

	JobDescription getJobDescriptionById(int jdId);

	void updateJobDescription(JobDescription jd);

	void deleteJobDescriptionById(int jdId);

	List<JobDescription> getAllJobDescriptions();

	List<JobDescription> findAllJobDescriptionByClient(Client Client);

	List<JobDescription> findAllActiveJobDescriptionByClient(Client client);
	
	List<JobDescription> findInActiveJobDescriptionByClient(Client clientId);

	List<JobDescription> getJdListOfThisDepartment(JobDescription department);

	List<JobDescription> getJobDescriptionListByLocationAndPosition(String location, String position);

	List<JobDescription> findByKeyword(@Param("keyword") String keyword);

}
