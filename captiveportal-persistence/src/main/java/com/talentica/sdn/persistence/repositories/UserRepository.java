/**
 * 
 */
package com.talentica.sdn.persistence.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.persistence.entities.UserRole;

/**
 * @author NarenderK
 *
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("select u from User u")
	public List<User> findUserDetails();
	
	@Query("select u.macAddress from User u")
	public List<String> findRegisteredMacs();
	
	public User findByMacAddress(String macAddress);

	public User findByIpAddress(String ipAddress);
	
	@Modifying
	@Query("update User u set u.activated = 1 where u.macAddress = ?1")
	public void setUserActivatedByMac(String mac);

	public List<User> findByIpAddressAndActivated(String ipAddress, boolean activated);
	
	@Modifying
	@Query("update User u set u.userRole = ?1 where u.macAddress = ?2")
	public void setUserRoleByMac(UserRole userRole, String mac);
	
}