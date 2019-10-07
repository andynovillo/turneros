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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import entity.auth.SystemUser;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "system_user_ticket")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemUserTicket.findAll", query = "SELECT s FROM SystemUserTicket s"),
		@NamedQuery(name = "SystemUserTicket.findBySystemUserTicketPk", query = "SELECT s FROM SystemUserTicket s WHERE s.systemUserTicketPk = :systemUserTicketPk"),
		@NamedQuery(name = "SystemUserTicket.findByAction", query = "SELECT s FROM SystemUserTicket s WHERE s.action = :action"),
		@NamedQuery(name = "SystemUserTicket.findByDateTime", query = "SELECT s FROM SystemUserTicket s WHERE s.dateTime = :dateTime"),
		@NamedQuery(name = "SystemUserTicket.findByAddress", query = "SELECT s FROM SystemUserTicket s WHERE s.address = :address") })
public class SystemUserTicket implements Serializable {

	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "system_user_fk", referencedColumnName = "id_system_user")
	@ManyToOne
	private SystemUser systemUserFk;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "system_user_ticket_pk")
	private Integer systemUserTicketPk;
	@Size(max = 2147483647)
	@Column(name = "action")
	private String action;
	@Column(name = "date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	@Size(max = 2147483647)
	@Column(name = "address")
	private String address;
	@JoinColumn(name = "ticket_fk", referencedColumnName = "ticket_pk")
	@ManyToOne
	private Ticket ticketFk;

	public SystemUserTicket() {
	}

	public SystemUserTicket(Integer systemUserTicketPk) {
		this.systemUserTicketPk = systemUserTicketPk;
	}

	public SystemUser getSystemUserFk() {
		return systemUserFk;
	}

	public void setSystemUserFk(SystemUser systemUserFk) {
		this.systemUserFk = systemUserFk;
	}

	public Integer getSystemUserTicketPk() {
		return systemUserTicketPk;
	}

	public void setSystemUserTicketPk(Integer systemUserTicketPk) {
		this.systemUserTicketPk = systemUserTicketPk;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Ticket getTicketFk() {
		return ticketFk;
	}

	public void setTicketFk(Ticket ticketFk) {
		this.ticketFk = ticketFk;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemUserTicketPk != null ? systemUserTicketPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SystemUserTicket)) {
			return false;
		}
		SystemUserTicket other = (SystemUserTicket) object;
		if ((this.systemUserTicketPk == null && other.systemUserTicketPk != null)
				|| (this.systemUserTicketPk != null && !this.systemUserTicketPk.equals(other.systemUserTicketPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.SystemUserTicket[ systemUserTicketPk=" + systemUserTicketPk + " ]";
	}

}
