/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.auth.SystemUser;
import entity.ticket.TicketPriority;
import sessionBean.auth.SystemUserFacadeLocal;
import sessionBean.ticket.TicketPriorityFacadeLocal;
import utility.Role;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named
@ViewScoped
public class SystemUserServiceTypePriorityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private TicketPriority ticketPriority;
	private SystemUser systemUser;

	private Boolean systemUserSelected;

	/* Lists */

	private List<SystemUser> systemUserList;
	private List<TicketPriority> serviceTypePriorityList;
	private List<TicketPriority> serviceTypePrioritySelectedList;

	/* Services */

	@Inject
	private TicketPriorityFacadeLocal ticketPriorityFacadeLocal;
	@Inject
	private SystemUserFacadeLocal systemUserFacadeLocal;

	public SystemUserServiceTypePriorityBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	@SuppressWarnings("unchecked")
	public void resetVariables() {

		systemUserSelected = false;

		try {

			List<Integer> rolesInSession = (List<Integer>) JSFUtility
					.getAttributeFromSession(JSFUtility.ROLES_IN_SESSION);

			if (rolesInSession.contains(Role.ADMINISTRATOR)) {
				systemUserList = systemUserFacadeLocal.findAll();
				serviceTypePriorityList = ticketPriorityFacadeLocal.findAll();
			} else {
				SystemUser systemUser = (SystemUser) JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION);
				systemUserList = systemUserFacadeLocal.findSystemUserByTicketServiceGroup(systemUser.getIdSystemUser());
				serviceTypePriorityList = ticketPriorityFacadeLocal
						.findServiceTypePriorityByTicketServiceGroup(systemUser.getIdSystemUser());
			}

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void fetchServiceTypePriorityByUser(SystemUser systemUser) {

		try {

			SystemUser temporal = systemUserFacadeLocal.fetchServiceTypePriorityByUser(systemUser.getIdSystemUser());

			if (temporal != null) {
				this.systemUser = temporal;
				serviceTypePrioritySelectedList = new ArrayList<>(temporal.getTicketPrioritySet());
				systemUserSelected = true;
			} else {
				systemUserSelected = false;
			}

		} catch (Exception exception) {

			System.err.println("Method: fetchServiceTypePriorityByUser!");

		}

	}

	public void save() {

		if (systemUserSelected) {

			try {

				if (serviceTypePrioritySelectedList != null && serviceTypePrioritySelectedList.size() <= 1) {

					systemUser.setTicketPrioritySet(new HashSet<>(serviceTypePrioritySelectedList));

					systemUserFacadeLocal.edit(systemUser);

					resetVariables();

					JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
				} else {
					JSFUtility.addMessage("Máximo una prioridad de llamado a la vez", 2);
				}

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar un usuario", 2);

		}

	}

	public TicketPriority getServiceTypePriority() {
		return ticketPriority;
	}

	public void setServiceTypePriority(TicketPriority ticketPriority) {
		this.ticketPriority = ticketPriority;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public Boolean getSystemUserSelected() {
		return systemUserSelected;
	}

	public void setSystemUserSelected(Boolean systemUserSelected) {
		this.systemUserSelected = systemUserSelected;
	}

	public List<TicketPriority> getServiceTypePriorityList() {
		return serviceTypePriorityList;
	}

	public void setServiceTypePriorityList(List<TicketPriority> serviceTypePriorityList) {
		this.serviceTypePriorityList = serviceTypePriorityList;
	}

	public List<SystemUser> getSystemUserList() {
		return systemUserList;
	}

	public void setSystemUserList(List<SystemUser> systemUserList) {
		this.systemUserList = systemUserList;
	}

	public List<TicketPriority> getServiceTypePrioritySelectedList() {
		return serviceTypePrioritySelectedList;
	}

	public void setServiceTypePrioritySelectedList(List<TicketPriority> serviceTypePrioritySelectedList) {
		this.serviceTypePrioritySelectedList = serviceTypePrioritySelectedList;
	}

	public TicketPriorityFacadeLocal getServiceTypePriorityFacadeLocal() {
		return ticketPriorityFacadeLocal;
	}

	public void setServiceTypePriorityFacadeLocal(TicketPriorityFacadeLocal ticketPriorityFacadeLocal) {
		this.ticketPriorityFacadeLocal = ticketPriorityFacadeLocal;
	}

	public SystemUserFacadeLocal getSystemUserFacadeLocal() {
		return systemUserFacadeLocal;
	}

	public void setSystemUserFacadeLocal(SystemUserFacadeLocal systemUserFacadeLocal) {
		this.systemUserFacadeLocal = systemUserFacadeLocal;
	}

}
