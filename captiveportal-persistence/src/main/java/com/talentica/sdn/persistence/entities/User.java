/**
 * 
 */
package com.talentica.sdn.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author NarenderK
 *
 */

@SuppressWarnings("serial")
@Entity
public class User extends AbstractAuditableEntity {

	@NotNull
	@Column(unique = true)
	private String macAddress;
	
	private boolean activated;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_role", referencedColumnName = "id", nullable = true)
	private UserRole userRole;

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	@Override
	public String toString() {
		return "User [macAddress=" + macAddress + ", activated=" + activated + ", userRole=" + userRole + "]";
	}

}
