/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.poll;

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
 * @author Bryan Valencia
 */
@Entity
@Table(schema = "poll", name = "relative_type")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "RelativeType.findAll", query = "SELECT r FROM RelativeType r"),
		@NamedQuery(name = "RelativeType.findByRelativeTypePk", query = "SELECT r FROM RelativeType r WHERE r.relativeTypePk = :relativeTypePk"),
		@NamedQuery(name = "RelativeType.findByRelativeType", query = "SELECT r FROM RelativeType r WHERE r.relativeType = :relativeType") })
public class RelativeType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "relative_type_pk")
	private Integer relativeTypePk;
	@Size(max = 2147483647)
	@Column(name = "relative_type")
	private String relativeType;
	@OneToMany(mappedBy = "relativeTypeFk")
	private Set<Respondent> respondentSet;

	public RelativeType() {
	}

	public RelativeType(Integer relativeTypePk) {
		this.relativeTypePk = relativeTypePk;
	}

	public Integer getRelativeTypePk() {
		return relativeTypePk;
	}

	public void setRelativeTypePk(Integer relativeTypePk) {
		this.relativeTypePk = relativeTypePk;
	}

	public String getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(String relativeType) {
		this.relativeType = relativeType;
	}

	@XmlTransient
	public Set<Respondent> getRespondentSet() {
		return respondentSet;
	}

	public void setRespondentSet(Set<Respondent> respondentSet) {
		this.respondentSet = respondentSet;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (relativeTypePk != null ? relativeTypePk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof RelativeType)) {
			return false;
		}
		RelativeType other = (RelativeType) object;
		if ((this.relativeTypePk == null && other.relativeTypePk != null)
				|| (this.relativeTypePk != null && !this.relativeTypePk.equals(other.relativeTypePk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "pollEntities.RelativeType[ relativeTypePk=" + relativeTypePk + " ]";
	}

}
