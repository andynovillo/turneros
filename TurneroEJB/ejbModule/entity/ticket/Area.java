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
@Table(schema = "ticket", name = "area")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a"),
		@NamedQuery(name = "Area.findByAreaPk", query = "SELECT a FROM Area a WHERE a.areaPk = :areaPk"),
		@NamedQuery(name = "Area.findByLabel", query = "SELECT a FROM Area a WHERE a.label = :label"),
		@NamedQuery(name = "Area.findByTableOrder", query = "SELECT a FROM Area a WHERE a.tableOrder = :tableOrder"),
		@NamedQuery(name = "Area.findByStatus", query = "SELECT a FROM Area a WHERE a.status = :status") })
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "area_pk")
	private Integer areaPk;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;
	@Column(name = "abbreviation")
	private String abbreviation;
	@Column(name = "table_order")
	private Integer tableOrder;
	@Column(name = "status")
	private Boolean status;
	@OneToMany(mappedBy = "areaFk")
	private Set<TicketService> ticketServiceSet;

	public Area() {
	}

	public Area(Integer areaPk) {
		this.areaPk = areaPk;
	}

	public Integer getAreaPk() {
		return areaPk;
	}

	public void setAreaPk(Integer areaPk) {
		this.areaPk = areaPk;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getTableOrder() {
		return tableOrder;
	}

	public void setTableOrder(Integer tableOrder) {
		this.tableOrder = tableOrder;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@XmlTransient
	public Set<TicketService> getTicketServiceSet() {
		return ticketServiceSet;
	}

	public void setTicketServiceSet(Set<TicketService> ticketServiceSet) {
		this.ticketServiceSet = ticketServiceSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (areaPk != null ? areaPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Area)) {
			return false;
		}
		Area other = (Area) object;
		if ((this.areaPk == null && other.areaPk != null)
				|| (this.areaPk != null && !this.areaPk.equals(other.areaPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.ticket.Area[ areaPk=" + areaPk + " ]";
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

}
