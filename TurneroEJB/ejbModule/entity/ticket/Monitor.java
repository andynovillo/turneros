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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "monitor")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Monitor.findAll", query = "SELECT m FROM Monitor m"),
		@NamedQuery(name = "Monitor.findByMonitorPk", query = "SELECT m FROM Monitor m WHERE m.monitorPk = :monitorPk"),
		@NamedQuery(name = "Monitor.findByLabel", query = "SELECT m FROM Monitor m WHERE m.label = :label"),
		@NamedQuery(name = "Monitor.findByAddress", query = "SELECT m FROM Monitor m WHERE m.address = :address"),
		@NamedQuery(name = "Monitor.findByStatus", query = "SELECT m FROM Monitor m WHERE m.status = :status"),
		@NamedQuery(name = "Monitor.fetchAttentionModuleByMonitor", query = "SELECT distinct m from Monitor m left join fetch m.attentionModuleSet ams where m.monitorPk = :monitorPk"),
		@NamedQuery(name = "Monitor.findMonitorGroupByAttModule", query = "SELECT distinct m from Monitor m left join fetch m.attentionModuleSet ams where ams.attentionModulePk = :attentionModulePk"),
		@NamedQuery(name = "Monitor.findByTableOrder", query = "SELECT m FROM Monitor m WHERE m.tableOrder = :tableOrder") })
public class Monitor implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "monitor_pk")
	private Integer monitorPk;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@Size(max = 2147483647)
	@Column(name = "address")
	private String address;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "table_order")
	private Integer tableOrder;
	@ManyToMany(mappedBy = "monitorSet")
	private Set<TicketService> ticketServiceSet;
	@Column(name = "font_size")
	private String fontSize;
	@Column(name = "enable_video")
	private Boolean enableVideo;
	@Column(name = "enable_ticket")
	private Boolean enableTicket;
	@OrderBy("tableOrder")
	@ManyToMany
	@JoinTable(name = "ticket.monitor_attention_module", joinColumns = {
			@JoinColumn(name = "monitor_fk") }, inverseJoinColumns = { @JoinColumn(name = "attention_module_fk") })
	private Set<AttentionModule> attentionModuleSet;

	public Monitor() {
	}

	public Monitor(Integer monitorPk) {
		this.monitorPk = monitorPk;
	}

	public Integer getMonitorPk() {
		return monitorPk;
	}

	public void setMonitorPk(Integer monitorPk) {
		this.monitorPk = monitorPk;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getTableOrder() {
		return tableOrder;
	}

	public void setTableOrder(Integer tableOrder) {
		this.tableOrder = tableOrder;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (monitorPk != null ? monitorPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Monitor)) {
			return false;
		}
		Monitor other = (Monitor) object;
		if ((this.monitorPk == null && other.monitorPk != null)
				|| (this.monitorPk != null && !this.monitorPk.equals(other.monitorPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.Monitor[ monitorPk=" + monitorPk + " ]";
	}

	public Set<TicketService> getTicketServiceSet() {
		return ticketServiceSet;
	}

	public void setTicketServiceSet(Set<TicketService> ticketServiceSet) {
		this.ticketServiceSet = ticketServiceSet;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public Set<AttentionModule> getAttentionModuleSet() {
		return attentionModuleSet;
	}

	public void setAttentionModuleSet(Set<AttentionModule> attentionModuleSet) {
		this.attentionModuleSet = attentionModuleSet;
	}

	public Boolean getEnableVideo() {
		return enableVideo;
	}

	public void setEnableVideo(Boolean enableVideo) {
		this.enableVideo = enableVideo;
	}

	public Boolean getEnableTicket() {
		return enableTicket;
	}

	public void setEnableTicket(Boolean enableTicket) {
		this.enableTicket = enableTicket;
	}

}
