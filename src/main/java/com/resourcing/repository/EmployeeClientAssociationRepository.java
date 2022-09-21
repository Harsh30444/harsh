package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Client;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;

@Repository
public interface EmployeeClientAssociationRepository extends JpaRepository<EmployeeClientAssociation, Integer> {

	@Query("select a from EmployeeClientAssociation a WHERE a.client=?1 AND isActive='Y'")
	public List<EmployeeClientAssociation> findEmpListByClient(Client client);

	@Query("select a from EmployeeClientAssociation a WHERE a.employee=?1 AND isActive='Y'")
	public List<EmployeeClientAssociation> findClientsByEmployeeId(Employee employee);
	
	@Query("select a from EmployeeClientAssociation a where a.employee = ?1 and a.client=?2 ")
	EmployeeClientAssociation findByEmployeeIdAndClientId(Employee employee, Client client); 

}
