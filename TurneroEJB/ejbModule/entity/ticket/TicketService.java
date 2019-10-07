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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "ticket_service")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TicketService.findAll", query = "SELECT t FROM TicketService t"),
		@NamedQuery(name = "TicketService.findByTicketServicePk", query = "SELECT t FROM TicketService t WHERE t.ticketServicePk = :ticketServicePk"),
		@NamedQuery(name = "TicketService.findByLabel", query = "SELECT t FROM TicketService t WHERE t.label = :label"),
		@NamedQuery(name = "TicketService.findByTableOrder", query = "SELECT t FROM TicketService t WHERE t.tableOrder = :tableOrder"),
		@NamedQuery(name = "TicketService.findByStatus", query = "SELECT t FROM TicketService t WHERE t.status = :status"),
		@NamedQuery(name = "TicketService.fetchMonitorByTicketService", query = "SELECT distinct t FROM TicketService t left join fetch t.monitorSet ms WHERE t.ticketServicePk = :ticketServicePk"),
		@NamedQuery(name = "TicketService.findByAbbreviation", query = "SELECT t FROM TicketService t WHERE t.abbreviation = :abbreviation") })
public class TicketService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ticket_service_pk")
	private Integer ticketServicePk;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@Size(max = 2147483647)
	@Column(name = "table_order")
	private String tableOrder;
	@Column(name = "status")
	private Boolean status;

	@Column(name = "abbreviation_printed")
	private Boolean abbreviationPrinted;
	@Size(max = 2147483647)
	@Column(name = "abbreviation")
	private String abbreviation;
	@OneToMany(mappedBy = "ticketServiceFk")
	private Set<ServiceType> serviceTypeSet;
	@OneToMany(mappedBy = "ticketServiceFk")
	private Set<TicketPriority> ticketPrioritySet;
	@JoinColumn(name = "area_fk", referencedColumnName = "area_pk")
	@ManyToOne
	private Area areaFk;
	@ManyToMany
	@JoinTable(name = "ticket.ticket_service_monitor", joinColumns = {
			@JoinColumn(name = "ticket_service_fk") }, inverseJoinColumns = { @JoinColumn(name = "monitor_fk") })
	private Set<Monitor> monitorSet;

	public TicketService() {
	}

	public TicketService(Integer ticketServicePk) {
		this.ticketServicePk = ticketServicePk;
	}

	public Integer getTicketServicePk() {
		return ticketServicePk;
	}

	public void setTicketServicePk(Integer ticketServicePk) {
		this.ticketServicePk = ticketServicePk;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTableOrder() {
		return tableOrder;
	}

	public void setTableOrder(String tableOrder) {
		this.tableOrder = tableOrder;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@XmlTransient
	public Set<ServiceType> getServiceTypeSet() {
		return serviceTypeSet;
	}

	public void setServiceTypeSet(Set<ServiceType> serviceTypeSet) {
		this.serviceTypeSet = serviceTypeSet;
	}

	public Area getAreaFk() {
		return areaFk;
	}

	public void setAreaFk(Area areaFk) {
		this.areaFk = areaFk;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ticketServicePk != null ? ticketServicePk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TicketService)) {
			return false;
		}
		TicketService other = (TicketService) object;
		if ((this.ticketServicePk == null && other.ticketServicePk != null)
				|| (this.ticketServicePk != null && !this.ticketServicePk.equals(other.ticketServicePk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.TicketService[ ticketServicePk=" + ticketServicePk + " ]";
	}

	public Set<Monitor> getMonitorSet() {
		return monitorSet;
	}

	public void setMonitorSet(Set<Monitor> monitorSet) {
		this.monitorSet = monitorSet;
	}

	public Boolean getAbbreviationPrinted() {
		return abbreviationPrinted;
	}

	public void setAbbreviationPrinted(Boolean abbreviationPrinted) {
		this.abbreviationPrinted = abbreviationPrinted;
	}

	public Set<TicketPriority> getTicketPrioritySet() {
		return ticketPrioritySet;
	}

	public void setTicketPrioritySet(Set<TicketPriority> ticketPrioritySet) {
		this.ticketPrioritySet = ticketPrioritySet;
	}

}
