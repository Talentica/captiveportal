/**
 * 
 */
package com.talentica.sdn.persistence.entities;

import javax.persistence.Entity;


/**
 * @author NarenderK
 *
 */
@SuppressWarnings("serial")
@Entity
public class UserRole extends AbstractAuditableEntity {

	private String role;

	public UserRole() {
	}

	public UserRole(User user, String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}

