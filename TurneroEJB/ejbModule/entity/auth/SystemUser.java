/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.auth;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import entity.ticket.SystemUserServiceType;
import entity.ticket.SystemUserTicket;
import entity.ticket.TicketPriority;

/**
 *
 * @author Bryan Valencia
 */
@Entity
@Table(name = "system_user", schema = "auth")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemUser.findAll", query = "SELECT s FROM SystemUser s"),
		@NamedQuery(name = "SystemUser.findFullNames", query = "SELECT s.nombresCompletos FROM SystemUser s WHERE s.idSystemUser = :idSystemUser"),
		@NamedQuery(name = "SystemUser.findByPass", query = "SELECT s FROM SystemUser s WHERE s.pass = :pass"),
		@NamedQuery(name = "SystemUser.findByIdSystemUser", query = "SELECT s FROM SystemUser s WHERE s.idSystemUser = :idSystemUser"),
		@NamedQuery(name = "SystemUser.findByCodSystemUser", query = "SELECT s FROM SystemUser s WHERE s.codSystemUser = :codSystemUser"),
		@NamedQuery(name = "SystemUser.findByEstado", query = "SELECT s FROM SystemUser s WHERE s.estado = :estado"),
		@NamedQuery(name = "SystemUser.findUser", query = "SELECT s FROM SystemUser s WHERE s.codSystemUser = :codSystemUser and s.pass = :pass"),
		@NamedQuery(name = "SystemUser.findByNombresCompletos", query = "SELECT s FROM SystemUser s WHERE s.nombresCompletos = :nombresCompletos"),
		@NamedQuery(name = "SystemUser.findUsersByProcess", query = "SELECT DISTINCT s FROM SystemUser s join fetch s.processInstanceSet p where p.processInstancePk = :processInstance"),
		@NamedQuery(name = "SystemUser.findServiceTypeByUser", query = "SELECT DISTINCT s FROM SystemUser s left join fetch s.systemUserServiceTypeSet suts where s.idSystemUser = :systemUserPk and (suts.tableStatus = true or suts.tableStatus is null)"),
		@NamedQuery(name = "SystemUser.findCanGenerateServiceTypeByUser", query = "SELECT DISTINCT s FROM SystemUser s left join fetch s.systemUserServiceTypeSet suts where s.idSystemUser = :systemUserPk and suts.tableStatus = true and suts.canGenerate = true"),
		@NamedQuery(name = "SystemUser.findCanCallServiceTypeByUser", query = "SELECT DISTINCT s FROM SystemUser s left join fetch s.systemUserServiceTypeSet suts where s.idSystemUser = :systemUserPk and suts.tableStatus = true and suts.canCall = true"),
		@NamedQuery(name = "SystemUser.findSystemUserByTicketServiceGroup", query = "select distinct su from SystemUser su join su.systemUserServiceTypeSet susts where susts.serviceType.ticketServiceFk.ticketServicePk in (SELECT distinct sts.serviceType.ticketServiceFk.ticketServicePk FROM SystemUser s join s.systemUserServiceTypeSet sts WHERE s.idSystemUser = :idSystemUser)"),
		@NamedQuery(name = "SystemUser.fetchServiceTypePriorityByUser", query = "select distinct su from SystemUser su LEFT join fetch su.ticketPrioritySet tps where su.idSystemUser = :idSystemUser"),
		@NamedQuery(name = "SystemUser.findByRolActivo", query = "SELECT s FROM SystemUser s WHERE s.rolActivo = :rolActivo"),
		@NamedQuery(name = "SystemUser.fetchMngInfoForUser", query = "SELECT s FROM SystemUser s left join fetch s.dependencySet left join fetch s.roleSet WHERE s.idSystemUser = :idSystemUser"),
		@NamedQuery(name = "SystemUser.findByToken", query = "SELECT s FROM SystemUser s WHERE s.tokenText = :tkn") })
public class SystemUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_system_user")
	private Integer idSystemUser;
	@Size(max = 2147483647)
	@Column(name = "cod_system_user")
	private String codSystemUser;
	@Size(max = 2147483647)
	@Column(name = "pass")
	private String pass;
	@Size(max = 2147483647)
	@Column(name = "nombres_completos")
	private String nombresCompletos;
	@Column(name = "estado")
	private Boolean estado;
	@Size(max = 2147483647)
	@Column(name = "rol_activo")
	private String rolActivo;
	@Column(name = "start_page")
	private String startPage;
	@Column(name = "last_password")
	private String lastPassword;
	@Column(name = "last_password_change_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastPasswordChangeDateTime;
	@Column(name = "mail")
	private String mail;
	@Column(name = "other_mail")
	private String otherMail;
	@Column(name = "token_text")
	private String tokenText;
	@Column(name = "token_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tokenDateTime;
	@Column(name = "extra_info")
	private String extraInfo;

	/*
	 * Roles
	 */
	@ManyToMany
	@JoinTable(name = "auth.system_user_role", joinColumns = {
			@JoinColumn(name = "id_system_user") }, inverseJoinColumns = { @JoinColumn(name = "id_role") })
	private Set<Role> roleSet;

	/*
	 * Turnero
	 */

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "systemUser")
	private Set<SystemUserServiceType> systemUserServiceTypeSet;
	@OrderBy("tableOrder")
	@ManyToMany
	@JoinTable(name = "ticket.system_user_ticket_priority", joinColumns = {
			@JoinColumn(name = "system_user_fk") }, inverseJoinColumns = { @JoinColumn(name = "ticket_priority_fk") })
	private Set<TicketPriority> ticketPrioritySet;
	@OneToMany(mappedBy = "systemUserFk")
	private Set<SystemUserTicket> systemUserTicketSet;

	public SystemUser() {
	}

	public SystemUser(Integer idSystemUser, String codSystemUser, String nombresCompletos, String mail,
			Boolean estado) {
		this.idSystemUser = idSystemUser;
		this.codSystemUser = codSystemUser;
		this.nombresCompletos = nombresCompletos;
		this.estado = estado;
		this.mail = mail;
	}

	public SystemUser(Integer idSystemUser) {
		this.idSystemUser = idSystemUser;
	}

	public Integer getIdSystemUser() {
		return idSystemUser;
	}

	public void setIdSystemUser(Integer idSystemUser) {
		this.idSystemUser = idSystemUser;
	}

	public String getCodSystemUser() {
		return codSystemUser;
	}

	public void setCodSystemUser(String codSystemUser) {
		this.codSystemUser = codSystemUser;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNombresCompletos() {
		return nombresCompletos;
	}

	public void setNombresCompletos(String nombresCompletos) {
		this.nombresCompletos = nombresCompletos;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getRolActivo() {
		return rolActivo;
	}

	public void setRolActivo(String rolActivo) {
		this.rolActivo = rolActivo;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getLastPassword() {
		return lastPassword;
	}

	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}

	public Date getLastPasswordChangeDateTime() {
		return lastPasswordChangeDateTime;
	}

	public void setLastPasswordChangeDateTime(Date lastPasswordChangeDateTime) {
		this.lastPasswordChangeDateTime = lastPasswordChangeDateTime;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getOtherMail() {
		return otherMail;
	}

	public void setOtherMail(String otherMail) {
		this.otherMail = otherMail;
	}

	public String getTokenText() {
		return tokenText;
	}

	public void setTokenText(String tokenText) {
		this.tokenText = tokenText;
	}

	public Date getTokenDateTime() {
		return tokenDateTime;
	}

	public void setTokenDateTime(Date tokenDateTime) {
		this.tokenDateTime = tokenDateTime;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public Set<SystemUserServiceType> getSystemUserServiceTypeSet() {
		return systemUserServiceTypeSet;
	}

	public void setSystemUserServiceTypeSet(Set<SystemUserServiceType> systemUserServiceTypeSet) {
		this.systemUserServiceTypeSet = systemUserServiceTypeSet;
	}

	public Set<TicketPriority> getTicketPrioritySet() {
		return ticketPrioritySet;
	}

	public void setTicketPrioritySet(Set<TicketPriority> ticketPrioritySet) {
		this.ticketPrioritySet = ticketPrioritySet;
	}

	public Set<SystemUserTicket> getSystemUserTicketSet() {
		return systemUserTicketSet;
	}

	public void setSystemUserTicketSet(Set<SystemUserTicket> systemUserTicketSet) {
		this.systemUserTicketSet = systemUserTicketSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSystemUser == null) ? 0 : idSystemUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemUser other = (SystemUser) obj;
		if (idSystemUser == null) {
			if (other.idSystemUser != null)
				return false;
		} else if (!idSystemUser.equals(other.idSystemUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemUser [idSystemUser=" + idSystemUser + "]";
	}

}
