package com.resourcing.service;

import com.resourcing.beans.AdminUser;

public interface AdminUserService {

	void deleteAdminUserById(int adminUserId);

	void saveAdminUser(AdminUser adminUser);

	AdminUser getAdminUserById(int id);

	AdminUser updateAdminUser(AdminUser adminUser);

	void addAdminUser(AdminUser adminUser);

	AdminUser findByEmailId(String emailId);

	AdminUser findByUserNameIgnoreCaseAndPassword(String adminUserMail, String adminUserPassword);

}
