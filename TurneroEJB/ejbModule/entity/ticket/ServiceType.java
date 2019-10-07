/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.ticket;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "service_type")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ServiceType.findAll", query = "SELECT s FROM ServiceType s"),
		@NamedQuery(name = "ServiceType.findAttentionModuleByServiceTypePk", query = "SELECT distinct s FROM ServiceType s left join fetch s.attentionModuleSet ams WHERE s.serviceTypePk = :serviceTypePk"),
		@NamedQuery(name = "ServiceType.findByServiceTypePk", query = "SELECT s FROM ServiceType s WHERE s.serviceTypePk = :serviceTypePk"),
		@NamedQuery(name = "ServiceType.findByLabel", query = "SELECT s FROM ServiceType s WHERE s.label = :label"),
		@NamedQuery(name = "ServiceType.findByStatus", query = "SELECT s FROM ServiceType s WHERE s.status = :status"),
		@NamedQuery(name = "ServiceType.findByTableOrder", query = "SELECT s FROM ServiceType s WHERE s.tableOrder = :tableOrder"),
		@NamedQuery(name = "ServiceType.findByPriority", query = "SELECT s FROM ServiceType s WHERE s.priority = :priority"),
		@NamedQuery(name = "ServiceType.findByAbbreviation", query = "SELECT s FROM ServiceType s WHERE s.abbreviation = :abbreviation"),
		@NamedQuery(name = "ServiceType.findByColorStyle", query = "SELECT s FROM ServiceType s WHERE s.colorStyle = :colorStyle"),
		@NamedQuery(name = "ServiceType.findByTicketService", query = "SELECT s FROM ServiceType s WHERE s.ticketServiceFk.ticketServicePk = :ticketServicePk and s.status = true order by s.tableOrder"),
		@NamedQuery(name = "ServiceType.fetchAttentionModuleByServiceType", query = "SELECT s FROM ServiceType s left join fetch s.attentionModuleSet ams WHERE s.serviceTypePk = :serviceTypePk"),
		@NamedQuery(name = "ServiceType.findServiceTypeByTicketServiceGroup", query = "select st from ServiceType st where st.ticketServiceFk.ticketServicePk in (SELECT distinct sts.serviceType.ticketServiceFk.ticketServicePk FROM SystemUser s join s.systemUserServiceTypeSet sts WHERE s.idSystemUser = :idSystemUser)"),
		@NamedQuery(name = "ServiceType.findByIconStyle", query = "SELECT s FROM ServiceType s WHERE s.iconStyle = :iconStyle") })
public class ServiceType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "service_type_pk")
	private Integer serviceTypePk;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "table_order")
	private Integer tableOrder;
	@Column(name = "priority")
	private Integer priority;
	@Size(max = 2147483647)
	@Column(name = "abbreviation")
	private String abbreviation;
	@Size(max = 2147483647)
	@Column(name = "color_style")
	private String colorStyle;
	@Size(max = 2147483647)
	@Column(name = "icon_style")
	private String iconStyle;
	@Column(name = "manually_called")
	private Boolean manuallyCalled;
	@Column(name = "manually_generated")
	private Boolean manuallyGenerated;
	@Column(name = "scheduled")
	private Boolean scheduled;
	@Column(name = "called_by_priority")
	private Boolean calledByPriority;
	@Column(name = "schedule_type")
	private String scheduleType;

	@JoinColumn(name = "ticket_service_fk", referencedColumnName = "ticket_service_pk")
	@ManyToOne
	private TicketService ticketServiceFk;
	@OneToMany(mappedBy = "serviceTypeFk")
	private Set<Ticket> ticketSet;
	// @ManyToMany(mappedBy = "serviceTypeSet")
	// private Set<SystemUser> systemUserSet;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceType")
	private Set<SystemUserServiceType> systemUserServiceTypeSet;
	@OrderBy("tableOrder")
	@ManyToMany
	@JoinTable(name = "ticket.service_type_attention_module", joinColumns = {
			@JoinColumn(name = "service_type_fk") }, inverseJoinColumns = { @JoinColumn(name = "attention_module_fk") })
	private Set<AttentionModule> attentionModuleSet;
	@Column(name = "ticket_copies")
	private Integer ticketCopies;

	public ServiceType() {
	}

	public ServiceType(Integer serviceTypePk) {
		this.serviceTypePk = serviceTypePk;
	}

	public Integer getServiceTypePk() {
		return serviceTypePk;
	}

	public void setServiceTypePk(Integer serviceTypePk) {
		this.serviceTypePk = serviceTypePk;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getColorStyle() {
		return colorStyle;
	}

	public void setColorStyle(String colorStyle) {
		this.colorStyle = colorStyle;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

	public TicketService getTicketServiceFk() {
		return ticketServiceFk;
	}

	public void setTicketServiceFk(TicketService ticketServiceFk) {
		this.ticketServiceFk = ticketServiceFk;
	}

	@XmlTransient
	public Set<Ticket> getTicketSet() {
		return ticketSet;
	}

	public void setTicketSet(Set<Ticket> ticketSet) {
		this.ticketSet = ticketSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (serviceTypePk != null ? serviceTypePk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ServiceType)) {
			return false;
		}
		ServiceType other = (ServiceType) object;
		if ((this.serviceTypePk == null && other.serviceTypePk != null)
				|| (this.serviceTypePk != null && !this.serviceTypePk.equals(other.serviceTypePk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.ServiceType[ serviceTypePk=" + serviceTypePk + " ]";
	}

	// public Set<SystemUser> getSystemUserSet() {
	// return systemUserSet;
	// }
	//
	// public void setSystemUserSet(Set<SystemUser> systemUserSet) {
	// this.systemUserSet = systemUserSet;
	// }

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public Set<AttentionModule> getAttentionModuleSet() {
		return attentionModuleSet;
	}

	public void setAttentionModuleSet(Set<AttentionModule> attentionModuleSet) {
		this.attentionModuleSet = attentionModuleSet;
	}

	public Boolean getCalledByPriority() {
		return calledByPriority;
	}

	public void setCalledByPriority(Boolean calledByPriority) {
		this.calledByPriority = calledByPriority;
	}

	public Integer getTicketCopies() {
		return ticketCopies;
	}

	public void setTicketCopies(Integer ticketCopies) {
		this.ticketCopies = ticketCopies;
	}

	public Set<SystemUserServiceType> getSystemUserServiceTypeSet() {
		return systemUserServiceTypeSet;
	}

	public void setSystemUserServiceTypeSet(Set<SystemUserServiceType> systemUserServiceTypeSet) {
		this.systemUserServiceTypeSet = systemUserServiceTypeSet;
	}

	public Boolean getManuallyCalled() {
		return manuallyCalled;
	}

	public void setManuallyCalled(Boolean manuallyCalled) {
		this.manuallyCalled = manuallyCalled;
	}

	public Boolean getManuallyGenerated() {
		return manuallyGenerated;
	}

	public void setManuallyGenerated(Boolean manuallyGenerated) {
		this.manuallyGenerated = manuallyGenerated;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

}
