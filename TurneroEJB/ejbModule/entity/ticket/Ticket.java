/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import entity.surgery.Patient;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "ticket")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
		@NamedQuery(name = "Ticket.findByTicketPk", query = "SELECT t FROM Ticket t WHERE t.ticketPk = :ticketPk"),
		@NamedQuery(name = "Ticket.findByTicket", query = "SELECT t FROM Ticket t WHERE t.ticket = :ticket"),
		@NamedQuery(name = "Ticket.findByGenerationDateTime", query = "SELECT t FROM Ticket t WHERE t.generationDateTime = :generationDateTime"),
		@NamedQuery(name = "Ticket.findByCallDateTime", query = "SELECT t FROM Ticket t WHERE t.callDateTime = :callDateTime"),
		@NamedQuery(name = "Ticket.findByEndDateTime", query = "SELECT t FROM Ticket t WHERE t.endDateTime = :endDateTime"),
		@NamedQuery(name = "Ticket.findByCalled", query = "SELECT t FROM Ticket t WHERE t.called = :called"),
		@NamedQuery(name = "Ticket.findByAttended", query = "SELECT t FROM Ticket t WHERE t.attended = :attended"),
		@NamedQuery(name = "Ticket.findByEnded", query = "SELECT t FROM Ticket t WHERE t.ended = :ended"),
		@NamedQuery(name = "Ticket.findByPatientFk", query = "SELECT t FROM Ticket t WHERE t.patientFk = :patientFk") })
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ticket_pk")
	private Integer ticketPk;
	@Size(max = 2147483647)
	@Column(name = "ticket")
	private String ticket;
	@Column(name = "generation_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date generationDateTime;
	@Column(name = "call_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date callDateTime;
	@Column(name = "end_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateTime;
	@Column(name = "called")
	private Boolean called;
	@Column(name = "attended")
	private Boolean attended;
	@Column(name = "ended")
	private Boolean ended;
	@JoinColumn(name = "patient_fk", referencedColumnName = "patient_pk")
	@ManyToOne
	private Patient patientFk;
	@JoinColumn(name = "service_type_fk", referencedColumnName = "service_type_pk")
	@ManyToOne
	private ServiceType serviceTypeFk;
	@OneToMany(mappedBy = "ticketFk")
	private Set<SystemUserTicket> systemUserTicketSet;
	@Column(name = "attention_module_fk")
	private Integer attentionModuleFk;
	@Column(name = "attention_module")
	private String attentionModule;
	@Column(name = "scheduling_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date schedulingDateTime;
	@Column(name = "scheduling_start_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date schedulingStartDateTime;
	@Column(name = "scheduling_end_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date schedulingEndDateTime;
	@Column(name = "additional_data")
	private String additionalData;
	@Column(name = "extra_info")
	private String extraInfo;
	@Column(name = "table_status")
	private Boolean tableStatus;
	@Column(name = "generated")
	private Boolean generated;
	@Column(name = "person_fk")
	private Integer personFk;

	public Ticket() {
		tableStatus = true;
	}

	public Ticket(String ticket) {

		this.ticket = ticket;
		generated = true;
		called = false;
		attended = false;
		ended = false;
		generationDateTime = Calendar.getInstance().getTime();
		callDateTime = null;
		endDateTime = null;
		schedulingDateTime = null;
		schedulingStartDateTime = null;
		schedulingEndDateTime = null;
		tableStatus = true;

	}

	public Ticket(Integer ticketPk) {
		this.ticketPk = ticketPk;
	}

	public Integer getTicketPk() {
		return ticketPk;
	}

	public void setTicketPk(Integer ticketPk) {
		this.ticketPk = ticketPk;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getGenerationDateTime() {
		return generationDateTime;
	}

	public void setGenerationDateTime(Date generationDateTime) {
		this.generationDateTime = generationDateTime;
	}

	public Date getCallDateTime() {
		return callDateTime;
	}

	public void setCallDateTime(Date callDateTime) {
		this.callDateTime = callDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Boolean getCalled() {
		return called;
	}

	public void setCalled(Boolean called) {
		this.called = called;
	}

	public Boolean getAttended() {
		return attended;
	}

	public void setAttended(Boolean attended) {
		this.attended = attended;
	}

	public Boolean getEnded() {
		return ended;
	}

	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

	public ServiceType getServiceTypeFk() {
		return serviceTypeFk;
	}

	public void setServiceTypeFk(ServiceType serviceTypeFk) {
		this.serviceTypeFk = serviceTypeFk;
	}

	@XmlTransient
	public Set<SystemUserTicket> getSystemUserTicketSet() {
		return systemUserTicketSet;
	}

	public void setSystemUserTicketSet(Set<SystemUserTicket> systemUserTicketSet) {
		this.systemUserTicketSet = systemUserTicketSet;
	}

	public Patient getPatientFk() {
		return patientFk;
	}

	public void setPatientFk(Patient patientFk) {
		this.patientFk = patientFk;
	}

	public String getAttentionModule() {
		return attentionModule;
	}

	public void setAttentionModule(String attentionModule) {
		this.attentionModule = attentionModule;
	}

	public Date getSchedulingStartDateTime() {
		return schedulingStartDateTime;
	}

	public void setSchedulingStartDateTime(Date schedulingStartDateTime) {
		this.schedulingStartDateTime = schedulingStartDateTime;
	}

	public Date getSchedulingEndDateTime() {
		return schedulingEndDateTime;
	}

	public void setSchedulingEndDateTime(Date schedulingEndDateTime) {
		this.schedulingEndDateTime = schedulingEndDateTime;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public Boolean getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Boolean tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Date getSchedulingDateTime() {
		return schedulingDateTime;
	}

	public void setSchedulingDateTime(Date schedulingDateTime) {
		this.schedulingDateTime = schedulingDateTime;
	}

	public Boolean getGenerated() {
		return generated;
	}

	public void setGenerated(Boolean generated) {
		this.generated = generated;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Integer getAttentionModuleFk() {
		return attentionModuleFk;
	}

	public void setAttentionModuleFk(Integer attentionModuleFk) {
		this.attentionModuleFk = attentionModuleFk;
	}

	public Integer getPersonFk() {
		return personFk;
	}

	public void setPersonFk(Integer personFk) {
		this.personFk = personFk;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ticketPk != null ? ticketPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Ticket)) {
			return false;
		}
		Ticket other = (Ticket) object;
		if ((this.ticketPk == null && other.ticketPk != null)
				|| (this.ticketPk != null && !this.ticketPk.equals(other.ticketPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.Ticket[ ticketPk=" + ticketPk + " ]";
	}

}
