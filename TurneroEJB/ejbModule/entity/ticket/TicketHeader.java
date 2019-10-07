/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BV
 */
@Entity
@Table(name = "ticket_header", catalog = "htmc_as400", schema = "ticket")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TicketHeader.findAll", query = "SELECT t FROM TicketHeader t"),
		@NamedQuery(name = "TicketHeader.findByTicketHeaderPk", query = "SELECT t FROM TicketHeader t WHERE t.ticketHeaderPk = :ticketHeaderPk"),
		@NamedQuery(name = "TicketHeader.findByServiceTypeFk", query = "SELECT t FROM TicketHeader t WHERE t.serviceTypeFk = :serviceTypeFk"),
		@NamedQuery(name = "TicketHeader.findByPatientFk", query = "SELECT t FROM TicketHeader t WHERE t.patientFk = :patientFk"),
		@NamedQuery(name = "TicketHeader.findByAssignedUserFk", query = "SELECT t FROM TicketHeader t WHERE t.assignedUserFk = :assignedUserFk"),
		@NamedQuery(name = "TicketHeader.findByAssignedModuleFk", query = "SELECT t FROM TicketHeader t WHERE t.assignedModuleFk = :assignedModuleFk"),
		@NamedQuery(name = "TicketHeader.findByAppointments", query = "SELECT t FROM TicketHeader t WHERE t.appointments = :appointments"),
		@NamedQuery(name = "TicketHeader.findByCreationUserFk", query = "SELECT t FROM TicketHeader t WHERE t.creationUserFk = :creationUserFk"),
		@NamedQuery(name = "TicketHeader.findByCreationDateTime", query = "SELECT t FROM TicketHeader t WHERE t.creationDateTime = :creationDateTime"),
		@NamedQuery(name = "TicketHeader.findByEditionUserFk", query = "SELECT t FROM TicketHeader t WHERE t.editionUserFk = :editionUserFk"),
		@NamedQuery(name = "TicketHeader.findByEditionDateTime", query = "SELECT t FROM TicketHeader t WHERE t.editionDateTime = :editionDateTime"),
		@NamedQuery(name = "TicketHeader.findByTableStatus", query = "SELECT t FROM TicketHeader t WHERE t.tableStatus = :tableStatus"),
		@NamedQuery(name = "TicketHeader.findByTableOrder", query = "SELECT t FROM TicketHeader t WHERE t.tableOrder = :tableOrder") })
public class TicketHeader implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ticket_header_pk")
	private Integer ticketHeaderPk;
	@Column(name = "service_type_fk")
	private Integer serviceTypeFk;
	@Column(name = "patient_fk")
	private Integer patientFk;
	@Column(name = "assigned_user_fk")
	private Integer assignedUserFk;
	@Column(name = "assigned_module_fk")
	private Integer assignedModuleFk;
	@Column(name = "appointments")
	private Integer appointments;
	@Column(name = "creation_user_fk")
	private Integer creationUserFk;
	@Column(name = "creation_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDateTime;
	@Column(name = "creation_address")
	private String creationAddress;
	@Column(name = "edition_user_fk")
	private Integer editionUserFk;
	@Column(name = "edition_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editionDateTime;
	@Column(name = "edition_address")
	private String editionAddress;
	@Column(name = "extra_info")
	private String extraInfo;
	@Column(name = "table_status")
	private Boolean tableStatus;
	@Column(name = "table_order")
	private Integer tableOrder;

	public TicketHeader() {
	}

	public TicketHeader(Integer ticketHeaderPk) {
		this.ticketHeaderPk = ticketHeaderPk;
	}

	public Integer getTicketHeaderPk() {
		return ticketHeaderPk;
	}

	public void setTicketHeaderPk(Integer ticketHeaderPk) {
		this.ticketHeaderPk = ticketHeaderPk;
	}

	public Integer getServiceTypeFk() {
		return serviceTypeFk;
	}

	public void setServiceTypeFk(Integer serviceTypeFk) {
		this.serviceTypeFk = serviceTypeFk;
	}

	public Integer getPatientFk() {
		return patientFk;
	}

	public void setPatientFk(Integer patientFk) {
		this.patientFk = patientFk;
	}

	public Integer getAssignedUserFk() {
		return assignedUserFk;
	}

	public void setAssignedUserFk(Integer assignedUserFk) {
		this.assignedUserFk = assignedUserFk;
	}

	public Integer getAssignedModuleFk() {
		return assignedModuleFk;
	}

	public void setAssignedModuleFk(Integer assignedModuleFk) {
		this.assignedModuleFk = assignedModuleFk;
	}

	public Integer getAppointments() {
		return appointments;
	}

	public void setAppointments(Integer appointments) {
		this.appointments = appointments;
	}

	public Integer getCreationUserFk() {
		return creationUserFk;
	}

	public void setCreationUserFk(Integer creationUserFk) {
		this.creationUserFk = creationUserFk;
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Integer getEditionUserFk() {
		return editionUserFk;
	}

	public void setEditionUserFk(Integer editionUserFk) {
		this.editionUserFk = editionUserFk;
	}

	public Date getEditionDateTime() {
		return editionDateTime;
	}

	public void setEditionDateTime(Date editionDateTime) {
		this.editionDateTime = editionDateTime;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
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

	public String getCreationAddress() {
		return creationAddress;
	}

	public void setCreationAddress(String creationAddress) {
		this.creationAddress = creationAddress;
	}

	public String getEditionAddress() {
		return editionAddress;
	}

	public void setEditionAddress(String editionAddress) {
		this.editionAddress = editionAddress;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ticketHeaderPk != null ? ticketHeaderPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TicketHeader)) {
			return false;
		}
		TicketHeader other = (TicketHeader) object;
		if ((this.ticketHeaderPk == null && other.ticketHeaderPk != null)
				|| (this.ticketHeaderPk != null && !this.ticketHeaderPk.equals(other.ticketHeaderPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.TicketHeader[ ticketHeaderPk=" + ticketHeaderPk + " ]";
	}

}
