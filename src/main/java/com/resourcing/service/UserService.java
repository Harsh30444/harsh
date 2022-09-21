/* 
 * Author : Srivani Tudi */

package com.resourcing.service;

import java.util.List;

import com.resourcing.beans.Branch;
import com.resourcing.beans.User;

public interface UserService {

	List<User> getAllUsers();

	void deleteUserById(int userId);

	User updateUser(User userObj);

	void addUser(User adminUserObj);

	User findByUserNameIgnoreCaseAndPassword(String emailId, String password);

	User findByEmailId(String emailId);

	User getUserById(int userId);
	
	User findByBranchId(Branch branch);

}
