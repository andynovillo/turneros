/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author BV
 */
@Embeddable
public class SystemUserServiceTypePK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(name = "system_user_fk", nullable = false)
	private int systemUserFk;
	@Basic(optional = false)
	@NotNull
	@Column(name = "service_type_fk", nullable = false)
	private int serviceTypeFk;

	public SystemUserServiceTypePK() {
	}

	public SystemUserServiceTypePK(int systemUserFk, int serviceTypeFk) {
		this.systemUserFk = systemUserFk;
		this.serviceTypeFk = serviceTypeFk;
	}

	public int getSystemUserFk() {
		return systemUserFk;
	}

	public void setSystemUserFk(int systemUserFk) {
		this.systemUserFk = systemUserFk;
	}

	public int getServiceTypeFk() {
		return serviceTypeFk;
	}

	public void setServiceTypeFk(int serviceTypeFk) {
		this.serviceTypeFk = serviceTypeFk;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += systemUserFk;
		hash += serviceTypeFk;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SystemUserServiceTypePK)) {
			return false;
		}
		SystemUserServiceTypePK other = (SystemUserServiceTypePK) object;
		if (this.systemUserFk != other.systemUserFk) {
			return false;
		}
		if (this.serviceTypeFk != other.serviceTypeFk) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.SystemUserServiceTypePK[ systemUserFk=" + systemUserFk + ", serviceTypeFk="
				+ serviceTypeFk + " ]";
	}

}
