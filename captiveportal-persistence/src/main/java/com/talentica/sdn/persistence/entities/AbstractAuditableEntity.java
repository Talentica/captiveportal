/**
 * 
 */
package com.talentica.sdn.persistence.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author NarenderK
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractAuditableEntity extends AbstractPersistable<Long> {

	@Version
	@Column(name = "version")
	protected Long version;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonProperty("createdDate")
	protected DateTime createdDate;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonProperty("lastModifiedDate")
	protected DateTime lastModifiedDate;

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the createdDate
	 */
	public DateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate
	 *            the lastModifiedDate to set
	 */
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return "AbstractAuditableEntity [version=" + version + ", createdDate="
				+ createdDate + ", lastModifiedDate=" + lastModifiedDate + "]";
	}

}
