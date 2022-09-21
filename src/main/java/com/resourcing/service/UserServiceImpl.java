/* 
 * Author : Srivani Tudi */

package com.resourcing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.Branch;
import com.resourcing.beans.User;
import com.resourcing.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public void deleteUserById(int adminUserId) {
		try {
			userRepository.deleteById(adminUserId);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public User getUserById(int adminUserId) {
		Optional<User> optional = userRepository.findById(adminUserId);
		User AdminUser = null;
		if (optional.isPresent()) {
			AdminUser = optional.get();
		} else {
			throw new RuntimeException("repository:::: AdminUser not found for id:::" + adminUserId);
		}
		return AdminUser;
	}

	@Override
	public User updateUser(User adminUser) {
		return userRepository.save(adminUser);
	}

	@Override
	public void addUser(User adminUser) {
		this.userRepository.save(adminUser);

	}

	@Override
	public User findByUserNameIgnoreCaseAndPassword(String adminMail, String password) {
		return userRepository.findByUserNameIgnoreCaseAndPassword(adminMail, password);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findByEmailId(String emailId) {
		
		return userRepository.findByEmailId(emailId);
	}

	@Override
	public User findByBranchId(Branch branch) {
		return userRepository.findByBranch(branch);
	}
}
