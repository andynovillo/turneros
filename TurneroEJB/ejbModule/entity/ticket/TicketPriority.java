/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import entity.auth.SystemUser;

/**
 *
 * @author BV
 */
@Entity
@Table(name = "ticket_priority", catalog = "htmc_as400", schema = "ticket")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TicketPriority.findAll", query = "SELECT s FROM TicketPriority s"),
		@NamedQuery(name = "TicketPriority.findByticketPriorityPk", query = "SELECT s FROM TicketPriority s WHERE s.ticketPriorityPk = :ticketPriorityPk"),
		@NamedQuery(name = "TicketPriority.findByServicePriority", query = "SELECT s FROM TicketPriority s WHERE s.servicePriority = :servicePriority"),
		@NamedQuery(name = "TicketPriority.findByTableStatus", query = "SELECT s FROM TicketPriority s WHERE s.tableStatus = :tableStatus"),
		@NamedQuery(name = "TicketPriority.findByTableOrder", query = "SELECT s FROM TicketPriority s WHERE s.tableOrder = :tableOrder"),
		@NamedQuery(name = "TicketPriority.findBySystemUser", query = "SELECT distinct s FROM TicketPriority s join fetch s.systemUserSet sus WHERE sus.idSystemUser = :idSystemUser"),
		@NamedQuery(name = "TicketPriority.findTicketPriorityByTicketServiceGroup", query = "SELECT tp FROM TicketPriority tp where tp.ticketServiceFk.ticketServicePk in (select distinct sts.serviceType.ticketServiceFk.ticketServicePk from SystemUser su join su.systemUserServiceTypeSet sts where su.idSystemUser = :idSystemUser)"),
		@NamedQuery(name = "TicketPriority.findByLabel", query = "SELECT s FROM TicketPriority s WHERE s.label = :label") })
public class TicketPriority implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ticket_priority_pk")
	private Integer ticketPriorityPk;
	@Size(max = 2147483647)
	@Column(name = "service_priority")
	private String servicePriority;
	@Column(name = "table_status")
	private Boolean tableStatus;
	@Column(name = "table_order")
	private Integer tableOrder;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@ManyToMany(mappedBy = "ticketPrioritySet")
	private Set<SystemUser> systemUserSet;
	@JoinColumn(name = "ticket_service_fk", referencedColumnName = "ticket_service_pk")
	@ManyToOne
	private TicketService ticketServiceFk;

	public TicketPriority() {
	}

	public TicketPriority(Integer ticketPriorityPk) {
		this.ticketPriorityPk = ticketPriorityPk;
	}

	public Integer getTicketPriorityPk() {
		return ticketPriorityPk;
	}

	public void setTicketPriorityPk(Integer ticketPriorityPk) {
		this.ticketPriorityPk = ticketPriorityPk;
	}

	public TicketService getTicketServiceFk() {
		return ticketServiceFk;
	}

	public void setTicketServiceFk(TicketService ticketServiceFk) {
		this.ticketServiceFk = ticketServiceFk;
	}

	public String getServicePriority() {
		return servicePriority;
	}

	public void setServicePriority(String servicePriority) {
		this.servicePriority = servicePriority;
	}

	public Boolean getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Boolean tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Integer getTableOrder() {
		return tableOrder;
	}

	public void setTableOrder(Integer tableOrder) {
		this.tableOrder = tableOrder;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ticketPriorityPk != null ? ticketPriorityPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TicketPriority)) {
			return false;
		}
		TicketPriority other = (TicketPriority) object;
		if ((this.ticketPriorityPk == null && other.ticketPriorityPk != null)
				|| (this.ticketPriorityPk != null && !this.ticketPriorityPk.equals(other.ticketPriorityPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.TicketPriority[ ticketPriorityPk=" + ticketPriorityPk + " ]";
	}

	public Set<SystemUser> getSystemUserSet() {
		return systemUserSet;
	}

	public void setSystemUserSet(Set<SystemUser> systemUserSet) {
		this.systemUserSet = systemUserSet;
	}

}
