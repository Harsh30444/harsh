package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Client;
import com.resourcing.beans.Employee;
import com.resourcing.beans.EmployeeClientAssociation;
import com.resourcing.repository.EmployeeClientAssociationRepository;

@Service
public class EmployeeClientAssociationServiceImpl implements EmployeeClientAssociationService {

	@Autowired
	EmployeeClientAssociationRepository employeeClientAssociationRepository;

	@Override
	public List<EmployeeClientAssociation> getAllEmployeeClientAssociations() {
		return employeeClientAssociationRepository.findAll();
	}

	@Override
	public void addNewEca(EmployeeClientAssociation employeeClientAssociation) {
		employeeClientAssociationRepository.save(employeeClientAssociation);
	}

	@Override
	public EmployeeClientAssociation findEcaById(int ecaId) {
		Optional<EmployeeClientAssociation> optional = employeeClientAssociationRepository.findById(ecaId);
		EmployeeClientAssociation employeeClientAssociation = null;
		if (optional.isPresent()) {
			employeeClientAssociation = optional.get();
		} else {
			throw new RuntimeException("repository:::: EmployeeClientAssociation not found for id:::" + ecaId);
		}

		return employeeClientAssociation;
	}

	@Override
	public EmployeeClientAssociation updateEca(EmployeeClientAssociation eca) {
		return employeeClientAssociationRepository.save(eca);
	}

	@Override
	public List<EmployeeClientAssociation> findEmpListByClient(Client client) {
		return employeeClientAssociationRepository.findEmpListByClient(client);
	}

	@Override
	public List<EmployeeClientAssociation> findClientsByEmployeeId(Employee employee) {
		return employeeClientAssociationRepository.findClientsByEmployeeId(employee);
	}

	@Override
	public EmployeeClientAssociation findByEmployeeIdAndClientId(Employee employee, Client client) {
		return employeeClientAssociationRepository.findByEmployeeIdAndClientId(employee, client);
	}

}
