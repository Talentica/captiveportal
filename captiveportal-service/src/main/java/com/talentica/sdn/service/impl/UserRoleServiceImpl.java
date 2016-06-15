/**
 * 
 */
package com.talentica.sdn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentica.sdn.persistence.entities.UserRole;
import com.talentica.sdn.persistence.repositories.UserRoleRepository;
import com.talentica.sdn.service.base.UserRoleService;

/**
 * @author NarenderK
 *
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired
	private UserRoleRepository userRoleRepo;

	@Override
	public UserRole findUserRole(String userRole) {
		return userRoleRepo.findByRole(userRole);
	}

}
