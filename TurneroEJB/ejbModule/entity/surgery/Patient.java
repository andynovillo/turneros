/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.surgery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bryan Valencia
 */
@Entity
@Table(schema = "surgery", name = "patient")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p"),
		@NamedQuery(name = "Patient.findByPatientPk", query = "SELECT p FROM Patient p WHERE p.patientPk = :patientPk"),
		@NamedQuery(name = "Patient.findByPatient", query = "SELECT p FROM Patient p WHERE p.patient = :patient"),
		@NamedQuery(name = "Patient.findByCode", query = "SELECT p FROM Patient p WHERE p.code = :code"),
		@NamedQuery(name = "Patient.findByClinicHistory", query = "SELECT p FROM Patient p WHERE p.clinicHistory = :clinicHistory") })
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "patient_pk")
	private Integer patientPk;
	@Size(max = 2147483647)
	@Column(name = "patient")
	private String patient;
	@Size(max = 2147483647)
	@Column(name = "code")
	private String code;
	@Size(max = 2147483647)
	@Column(name = "clinic_history")
	private String clinicHistory;
	@Column(name = "age")
	private Integer age;
	@Column(name = "insurance")
	private String insurance;
	@Column(name = "address")
	private String address;
	@Column(name = "phone")
	private String phone;
	@Column(name = "gender")
	private String gender;
	@Temporal(TemporalType.DATE)
	@Column(name = "birthday")
	private Date birthday;

	@Column(name = "province")
	private String province;
	@Column(name = "canton")
	private String canton;
	@Column(name = "parish")
	private String parish;

	public Patient() {
	}

	public Patient(Integer patientPk) {
		this.patientPk = patientPk;
	}

	public Integer getPatientPk() {
		return patientPk;
	}

	public void setPatientPk(Integer patientPk) {
		this.patientPk = patientPk;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClinicHistory() {
		return clinicHistory;
	}

	public void setClinicHistory(String clinicHistory) {
		this.clinicHistory = clinicHistory;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCanton() {
		return canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getParish() {
		return parish;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (patientPk != null ? patientPk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Patient)) {
			return false;
		}
		Patient other = (Patient) object;
		if ((this.patientPk == null && other.patientPk != null)
				|| (this.patientPk != null && !this.patientPk.equals(other.patientPk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Patient[ patientPk=" + patientPk + " ]";
	}

}
