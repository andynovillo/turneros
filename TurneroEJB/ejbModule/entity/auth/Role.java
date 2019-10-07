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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "role", schema = "auth")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
		@NamedQuery(name = "Role.findByIdRole", query = "SELECT r FROM Role r WHERE r.idRole = :idRole"),
		@NamedQuery(name = "Role.findByRole", query = "SELECT r FROM Role r WHERE r.role = :role"),
		@NamedQuery(name = "Role.findByStartPage", query = "SELECT r FROM Role r WHERE r.startPage = :startPage"),
		@NamedQuery(name = "Role.findByIdPadre", query = "SELECT r FROM Role r WHERE r.idPadre = :idPadre"),
		@NamedQuery(name = "Role.findByDescription", query = "SELECT r FROM Role r WHERE r.description = :description") })
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	@Size(max = 2147483647)
	@Column(name = "role")
	private String role;
	@Size(max = 2147483647)
	@Column(name = "start_page")
	private String startPage;
	@Size(max = 2147483647)
	@Column(name = "id_padre")
	private String idPadre;
	@Size(max = 2147483647)
	@Column(name = "description")
	private String description;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_role")
	private Integer idRole;
	@ManyToMany
	@JoinTable(name = "auth.role_option", joinColumns = { @JoinColumn(name = "id_role") }, inverseJoinColumns = {
			@JoinColumn(name = "id_option") })
	private Set<Option> optionSet;
	@ManyToMany(mappedBy = "roleSet")
	private Set<SystemUser> systemUserSet;

	public Role() {
	}

	public Role(Integer idRole) {
		this.idRole = idRole;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdRole() {
		return idRole;
	}

	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}

	@XmlTransient
	public Set<Option> getOptionSet() {
		return optionSet;
	}

	public void setOptionSet(Set<Option> optionSet) {
		this.optionSet = optionSet;
	}

	@XmlTransient
	public Set<SystemUser> getSystemUserSet() {
		return systemUserSet;
	}

	public void setSystemUserSet(Set<SystemUser> systemUserSet) {
		this.systemUserSet = systemUserSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRole != null ? idRole.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
		if ((this.idRole == null && other.idRole != null)
				|| (this.idRole != null && !this.idRole.equals(other.idRole))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "authEntities.Role[ idRole=" + idRole + " ]";
	}

}
