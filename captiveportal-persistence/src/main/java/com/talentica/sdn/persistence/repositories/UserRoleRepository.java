/**
 * 
 */
package com.talentica.sdn.persistence.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentica.sdn.persistence.entities.UserRole;

/**
 * @author NarenderK
 *
 */
@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
	
	public UserRole findByRole(String userRole);

}
