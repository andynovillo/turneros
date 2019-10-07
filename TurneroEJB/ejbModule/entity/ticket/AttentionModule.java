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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author BV
 */
@Entity
@Table(schema = "ticket", name = "attention_module")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "AttentionModule.findAll", query = "SELECT a FROM AttentionModule a"),
		@NamedQuery(name = "AttentionModule.findByAttentionModulePk", query = "SELECT a FROM AttentionModule a WHERE a.attentionModulePk = :attentionModulePk"),
		@NamedQuery(name = "AttentionModule.findByLabel", query = "SELECT a FROM AttentionModule a WHERE a.label = :label"),
		@NamedQuery(name = "AttentionModule.findByDescription", query = "SELECT a FROM AttentionModule a WHERE a.description = :description and a.status = true order by a.tableOrder"),
		@NamedQuery(name = "AttentionModule.findLikeDescription", query = "SELECT a FROM AttentionModule a WHERE a.description like concat('%',:description,'%') and a.status = true order by a.tableOrder"),
		@NamedQuery(name = "AttentionModule.findByStatus", query = "SELECT a FROM AttentionModule a WHERE a.status = :status"),
		@NamedQuery(name = "AttentionModule.findByTableOrder", query = "SELECT a FROM AttentionModule a WHERE a.tableOrder = :tableOrder") })
public class AttentionModule implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "attention_module_pk")
	private Integer attentionModulePk;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "table_order")
	private Integer tableOrder;
	@ManyToMany(mappedBy = "attentionModuleSet")
	private Set<ServiceType> serviceTypeSet;
	@ManyToMany(mappedBy = "attentionModuleSet")
	private Set<Monitor> monitorSet;
	@Size(max = 2147483647)
	@Column(name = "description")
	private String description;
	@Column(name = "extra_info")
	private String extraInfo;

	public AttentionModule() {
	}

	public AttentionModule(Integer attentionModulePk) {
		this.attentionModulePk = attentionModulePk;
	}

	public Integer getAttentionModulePk() {
		return attentionModulePk;
	}

	public void setAttentionModulePk(Integer attentionModulePk) {
		this.attentionModulePk = attentionModulePk;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (attentionModulePk != null ? attentionModulePk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AttentionModule)) {
			return false;
		}
		AttentionModule other = (AttentionModule) object;
		if ((this.attentionModulePk == null && other.attentionModulePk != null)
				|| (this.attentionModulePk != null && !this.attentionModulePk.equals(other.attentionModulePk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.AttentionModule[ attentionModulePk=" + attentionModulePk + " ]";
	}

	public Set<ServiceType> getServiceTypeSet() {
		return serviceTypeSet;
	}

	public void setServiceTypeSet(Set<ServiceType> serviceTypeSet) {
		this.serviceTypeSet = serviceTypeSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Set<Monitor> getMonitorSet() {
		return monitorSet;
	}

	public void setMonitorSet(Set<Monitor> monitorSet) {
		this.monitorSet = monitorSet;
	}

}
