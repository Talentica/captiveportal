/**
 * 
 */
package com.talentica.sdn.service.base;

import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.UserRole;

/**
 * @author NarenderK
 *
 */
@Service
public interface UserRoleService {
	
	public UserRole findUserRole(String userRole);
}
