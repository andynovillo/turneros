/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import entity.auth.SystemUser;

/**
 *
 * @author BV
 */
@Entity
@Table(name = "system_user_service_type", catalog = "htmc_as400", schema = "ticket")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemUserServiceType.findAll", query = "SELECT s FROM SystemUserServiceType s"),
		@NamedQuery(name = "SystemUserServiceType.findBySystemUserFk", query = "SELECT s FROM SystemUserServiceType s WHERE s.systemUserServiceTypePK.systemUserFk = :systemUserFk"),
		@NamedQuery(name = "SystemUserServiceType.findByServiceTypeFk", query = "SELECT s FROM SystemUserServiceType s WHERE s.systemUserServiceTypePK.serviceTypeFk = :serviceTypeFk"),
		@NamedQuery(name = "SystemUserServiceType.findByCanGenerate", query = "SELECT s FROM SystemUserServiceType s WHERE s.canGenerate = :canGenerate"),
		@NamedQuery(name = "SystemUserServiceType.findByCanCall", query = "SELECT s FROM SystemUserServiceType s WHERE s.canCall = :canCall"),
		@NamedQuery(name = "SystemUserServiceType.findByTableStatus", query = "SELECT s FROM SystemUserServiceType s WHERE s.tableStatus = :tableStatus") })
public class SystemUserServiceType implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected SystemUserServiceTypePK systemUserServiceTypePK;
	@Column(name = "can_generate")
	private Boolean canGenerate;
	@Column(name = "can_call")
	private Boolean canCall;
	@Column(name = "table_status")
	private Boolean tableStatus;
	@JoinColumn(name = "service_type_fk", referencedColumnName = "service_type_pk", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ServiceType serviceType;
	@JoinColumn(name = "system_user_fk", referencedColumnName = "id_system_user", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private SystemUser systemUser;
	@Transient
	private Boolean dirty;

	public SystemUserServiceType() {
	}

	public SystemUserServiceType(SystemUserServiceTypePK systemUserServiceTypePK) {
		this.systemUserServiceTypePK = systemUserServiceTypePK;
	}

	public SystemUserServiceType(int systemUserFk, int serviceTypeFk) {
		this.systemUserServiceTypePK = new SystemUserServiceTypePK(systemUserFk, serviceTypeFk);
	}

	public SystemUserServiceTypePK getSystemUserServiceTypePK() {
		return systemUserServiceTypePK;
	}

	public void setSystemUserServiceTypePK(SystemUserServiceTypePK systemUserServiceTypePK) {
		this.systemUserServiceTypePK = systemUserServiceTypePK;
	}

	public Boolean getCanGenerate() {
		return canGenerate;
	}

	public void setCanGenerate(Boolean canGenerate) {
		this.canGenerate = canGenerate;
	}

	public Boolean getCanCall() {
		return canCall;
	}

	public void setCanCall(Boolean canCall) {
		this.canCall = canCall;
	}

	public Boolean getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Boolean tableStatus) {
		this.tableStatus = tableStatus;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemUserServiceTypePK != null ? systemUserServiceTypePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SystemUserServiceType)) {
			return false;
		}
		SystemUserServiceType other = (SystemUserServiceType) object;
		if ((this.systemUserServiceTypePK == null && other.systemUserServiceTypePK != null)
				|| (this.systemUserServiceTypePK != null
						&& !this.systemUserServiceTypePK.equals(other.systemUserServiceTypePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.SystemUserServiceType[ systemUserServiceTypePK=" + systemUserServiceTypePK + " ]";
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public Boolean getDirty() {
		return dirty;
	}

	public void setDirty(Boolean dirty) {
		this.dirty = dirty;
	}

}
