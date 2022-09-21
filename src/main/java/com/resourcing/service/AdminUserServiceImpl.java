package com.resourcing.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.resourcing.beans.AdminUser;
import com.resourcing.repository.AdminUserRepository;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	AdminUserRepository adminUserRepository;

	@Override
	public void deleteAdminUserById(int adminUserId) {
		try {
			adminUserRepository.deleteById(adminUserId);
		} catch (DataAccessException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public void saveAdminUser(AdminUser adminUser) {
		this.adminUserRepository.save(adminUser);

	}

	@Override
	public AdminUser getAdminUserById(int adminUserId) {
		Optional<AdminUser> optional = adminUserRepository.findById(adminUserId);
		AdminUser AdminUser = null;
		if (optional.isPresent()) {
			AdminUser = optional.get();
		} else {
			throw new RuntimeException("repository:::: AdminUser not found for id:::" + adminUserId);
		}

		return AdminUser;

	}

	@Override
	public AdminUser updateAdminUser(AdminUser adminUser) {
		return adminUserRepository.save(adminUser);
	}

	@Override
	public void addAdminUser(AdminUser adminUser) {
		this.adminUserRepository.save(adminUser);

	}

	@Override
	public AdminUser findByUserNameIgnoreCaseAndPassword(String adminMail, String adminPassword) {
		return adminUserRepository.findByAdminNameIgnoreCaseAndPassword(adminMail, adminPassword);
	}

	@Override
	public AdminUser findByEmailId(String emailId) {
		return adminUserRepository.findByEmailId(emailId);
	}

}
