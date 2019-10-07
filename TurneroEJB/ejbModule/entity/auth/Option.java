/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.auth;

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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Bryan Valencia
 */
@Entity
@Table(name = "option", schema = "auth")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Option.findAll", query = "SELECT o FROM Option o"),
		@NamedQuery(name = "Option.findByIdOption", query = "SELECT o FROM Option o WHERE o.idOption = :idOption"),
		@NamedQuery(name = "Option.findByOption", query = "SELECT o FROM Option o WHERE o.option = :option") })
public class Option implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_option")
	private Integer idOption;
	@Size(max = 2147483647)
	@Column(name = "option")
	private String option;
	@Column(name = "pretty_url")
	private String prettyURL;
	@Column(name = "mapping_id")
	private String mappingID;
	@Column(name = "name")
	private String name;
	@Column(name = "table_order")
	private Integer tableOrder;
	@Column(name = "parent")
	private Integer parent;
	@Column(name = "is_file")
	private Boolean isFile;
	@Column(name = "is_visible")
	private Boolean isVisible;
	@ManyToMany(mappedBy = "optionSet")
	private Set<Role> roleSet;

	public Option() {
	}

	public Option(Integer idOption) {
		this.idOption = idOption;
	}

	public Integer getIdOption() {
		return idOption;
	}

	public void setIdOption(Integer idOption) {
		this.idOption = idOption;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@XmlTransient
	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idOption != null ? idOption.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Option)) {
			return false;
		}
		Option other = (Option) object;
		if ((this.idOption == null && other.idOption != null)
				|| (this.idOption != null && !this.idOption.equals(other.idOption))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "authEntities.Option[ idOption=" + idOption + " ]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTableOrder() {
		return tableOrder;
	}

	public void setTableOrder(Integer tableOrder) {
		this.tableOrder = tableOrder;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(Boolean isFile) {
		this.isFile = isFile;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getPrettyURL() {
		return prettyURL;
	}

	public void setPrettyURL(String prettyURL) {
		this.prettyURL = prettyURL;
	}

	public String getMappingID() {
		return mappingID;
	}

	public void setMappingID(String mappingID) {
		this.mappingID = mappingID;
	}

}
