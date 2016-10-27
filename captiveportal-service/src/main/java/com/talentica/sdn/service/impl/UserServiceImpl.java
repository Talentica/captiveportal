/**
 * 
 */
package com.talentica.sdn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.persistence.entities.UserRole;
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

	@Override
	public User findUserByIpAddress(String ipAddress) {
		return userRepo.findByIpAddress(ipAddress);
	}

	@Override
	public void setUserActivatedByMac(String mac) {
		userRepo.setUserActivatedByMac(mac);
	}

	@Override
	public List<User> findUserByIpAddressAndActivated(String ipAddress, boolean activated) {
		return userRepo.findByIpAddressAndActivated(ipAddress, activated);
	}

	@Override
	public void setUserRoleByMac(UserRole userRole, String mac) {
		userRepo.setUserRoleByMac(userRole, mac);
		
	}

	@Override
	public List<User> deleteUser(long userId) {
		return userRepo.deleteById(userId);	
	}

}
