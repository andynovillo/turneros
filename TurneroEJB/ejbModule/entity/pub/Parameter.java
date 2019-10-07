/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.pub;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bryan Valencia
 */
@Entity
@Table(name = "parameter")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Parameter.findAll", query = "SELECT p FROM Parameter p order by p.resourcePk"),
		@NamedQuery(name = "Parameter.findByParam", query = "SELECT p FROM Parameter p WHERE p.param = :param"),
		@NamedQuery(name = "Parameter.findByValue", query = "SELECT p FROM Parameter p WHERE p.value = :value"),
		@NamedQuery(name = "Parameter.findByLabel", query = "SELECT p FROM Parameter p WHERE p.label = :label"),
		@NamedQuery(name = "Parameter.findByResourcePk", query = "SELECT p FROM Parameter p WHERE p.resourcePk = :resourcePk") })
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "resource_pk")
	private Integer resourcePk;
	@Size(max = 2147483647)
	@Column(name = "param")
	private String param;
	@Size(max = 2147483647)
	@Column(name = "value")
	private String value;
	@Size(max = 2147483647)
	@Column(name = "label")
	private String label;

	public Parameter() {
	}

	public Parameter(Integer resourcePk) {
		this.resourcePk = resourcePk;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getResourcePk() {
		return resourcePk;
	}

	public void setResourcePk(Integer resourcePk) {
		this.resourcePk = resourcePk;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (resourcePk != null ? resourcePk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Parameter)) {
			return false;
		}
		Parameter other = (Parameter) object;
		if ((this.resourcePk == null && other.resourcePk != null)
				|| (this.resourcePk != null && !this.resourcePk.equals(other.resourcePk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "publicEntities.Parameter[ resourcePk=" + resourcePk + " ]";
	}

}
