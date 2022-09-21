package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Client;
import com.resourcing.beans.JobDescription;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Integer> {
	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE a1.client=?1")
	List<JobDescription> findAllJobDescriptionByClient(Client clientId);

	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE a1.department=?1")
	List<JobDescription> getJdListOfThisDepartment(JobDescription department);

	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE a1.jdId=?1")
	JobDescription getJobDescriptionById(int jdId);

	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE lower(a1.location)=lower(?1) AND lower(a1.position)=lower(?2)")
	List<JobDescription> getJobDescriptionListByLocationAndPosition(String location, String position);

	@Query(value = "select * from JobDescription s where s.location like %:keyword% or s.department like %:keyword% or s.lpa", nativeQuery = true)
	List<JobDescription> findByKeyword(@Param("keyword") String keyword);

	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE a1.client=?1 AND a1.isActive='Y'")
	List<JobDescription> findAllActiveJobDescriptionByClient(Client clientId);
	
	@Query(value = "SELECT a1 FROM JobDescription a1 WHERE a1.client=?1 AND a1.isActive='N'")
	List<JobDescription> findInActiveJobDescriptionByClient(Client clientId);
	
	

}
