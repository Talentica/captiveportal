/**
 * 
 */
package com.talentica.sdn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.persistence.repositories.UserRepository;
import com.talentica.sdn.service.base.UserService;

/**
 * @author NarenderK
 *
 */
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public User createUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public List<User> findUserDetails() {
		return userRepo.findUserDetails();
	}

	@Override
	public User findUserByMacAddress(String macAddress) {
		return userRepo.findByMacAddress(macAddress);
	}
	
	@Override
	public List<String> findRegisteredMacs() {
		return userRepo.findRegisteredMacs();
	}

}
