/**
 * 
 */
package com.talentica.sdn.service.base;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.User;

/**
 * @author NarenderK
 *
 */
@Service
public interface UserService {
	public User createUser(User user);	
	public List<User> findUserDetails();
	public User findUserByMacAddress(String macAddress);
	List<String> findRegisteredMacs();
}