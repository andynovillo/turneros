/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.poll;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bryan Valencia
 */
@Entity
@Table(schema = "poll", name = "respondent")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Respondent.findAll", query = "SELECT r FROM Respondent r"),
		@NamedQuery(name = "Respondent.findByPersonPk", query = "SELECT r FROM Respondent r WHERE r.personPk = :personPk"),
		@NamedQuery(name = "Respondent.findByCode", query = "SELECT r FROM Respondent r WHERE r.code = :code"),
		@NamedQuery(name = "Respondent.findByFullName", query = "SELECT r FROM Respondent r WHERE r.fullName = :fullName"),
		@NamedQuery(name = "Respondent.findByClinicHistory", query = "SELECT r FROM Respondent r WHERE r.clinicHistory = :clinicHistory"),
		@NamedQuery(name = "Respondent.findByPhone", query = "SELECT r FROM Respondent r WHERE r.phone = :phone"),
		@NamedQuery(name = "Respondent.findByMail", query = "SELECT r FROM Respondent r WHERE r.mail = :mail"),
		@NamedQuery(name = "Respondent.findByInsurance", query = "SELECT r FROM Respondent r WHERE r.insurance = :insurance") })
public class Respondent implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "person_pk")
	private Integer personPk;
	@Size(max = 2147483647)
	@Column(name = "code")
	private String code;
	@Size(max = 2147483647)
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "age")
	private Integer age;
	@Size(max = 2147483647)
	@Column(name = "clinic_history")
	private String clinicHistory;
	// @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
	// message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the
	// field contains phone or fax number consider using this annotation to
	// enforce field validation
	@Size(max = 2147483647)
	@Column(name = "phone")
	private String phone;
	@Column(name = "alt_phone")
	private String altPhone;
	@Size(max = 2147483647)
	@Column(name = "mail")
	private String mail;
	@Size(max = 2147483647)
	@Column(name = "insurance")
	private String insurance;
	@Column(name = "birthday")
	private Date birthday;
	@Column(name = "gender")
	private String gender;
	@Column(name = "address")
	private String address;
	@Column(name = "birthplace")
	private String birthplace;
	@Column(name = "profession")
	private String profession;
	@Column(name = "marital_status")
	private String maritalStatus;
	@JoinColumn(name = "relative_type_fk", referencedColumnName = "relative_type_pk")
	@ManyToOne
	private RelativeType relativeTypeFk;

	public Respondent() {
	}

	public Respondent(Integer personPk) {
		this.personPk = personPk;
	}

	public Integer getPersonPk() {
		return personPk;
	}

	public void setPersonPk(Integer personPk) {
		this.personPk = personPk;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getClinicHistory() {
		return clinicHistory;
	}

	public void setClinicHistory(String clinicHistory) {
		this.clinicHistory = clinicHistory;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public RelativeType getRelativeTypeFk() {
		return relativeTypeFk;
	}

	public void setRelativeTypeFk(RelativeType relativeTypeFk) {
		this.relativeTypeFk = relativeTypeFk;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAltPhone() {
		return altPhone;
	}

	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (personPk != null ? personPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Respondent)) {
			return false;
		}
		Respondent other = (Respondent) object;
		if ((this.personPk == null && other.personPk != null)
				|| (this.personPk != null && !this.personPk.equals(other.personPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "pollEntities.Respondent[ personPk=" + personPk + " ]";
	}

}
