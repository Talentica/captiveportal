/**
 * 
 */
package com.talentica.sdn.service.base;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.persistence.entities.UserRole;

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
	public User findUserByIpAddress(String ipAddress);
	public void setUserActivatedByMac(String mac);
	public List<User> findUserByIpAddressAndActivated(String ipAddress, boolean activated);
	public void setUserRoleByMac(UserRole userRole, String mac);
	public List<User> deleteUser(long parseLong);
}