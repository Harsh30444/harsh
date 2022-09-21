package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Client;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;

public interface EmployeeClientAssociationService {

	List<EmployeeClientAssociation> getAllEmployeeClientAssociations();

	void addNewEca(EmployeeClientAssociation employeeClientAssociation);

	EmployeeClientAssociation findEcaById(int ecaId);

	EmployeeClientAssociation updateEca(EmployeeClientAssociation eca);

	public List<EmployeeClientAssociation> findEmpListByClient(Client client);

	public List<EmployeeClientAssociation> findClientsByEmployeeId(Employee employee);
	
	EmployeeClientAssociation findByEmployeeIdAndClientId(Employee employee, Client client);

}
