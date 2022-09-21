package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.resourcing.beans.Client;
import com.resourcing.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientRepository clientRepository;

	@Override
	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	@Override
	public Client getClientById(int clientId) {
		Optional<Client> optional = clientRepository.findById(clientId);
		Client client = null;
		if (optional.isPresent()) {
			client = optional.get();
		} else {
			throw new RuntimeException("repository:::: employee not found for id:::" + clientId);
		}

		return client;
	}

	@Override
	public void deleteClientById(int Id) {
		try {
			clientRepository.deleteById(Id);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	@Override
	public void saveClient(Client client) {
		this.clientRepository.save(client);

	}

	@Override
	public void addClient(@Validated Client client) {
		this.clientRepository.save(client);

	}

	@Override
	public Client findByUserNameIgnoreCaseAndPassword(String employeeName, String password) {
		return clientRepository.findByUserNameIgnoreCaseAndPassword(employeeName, password);
	}

	@Override
	public Client updateClient(Client client) {
		return clientRepository.save(client);
	}

	@Override
	public Client findByEmail(String email) {
		return clientRepository.findByEmail(email);
	}

}
