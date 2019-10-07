/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.cdi.ViewScoped;

import entity.auth.SystemUser;
import entity.ticket.ServiceType;
import entity.ticket.SystemUserServiceType;
import sessionBean.auth.SystemUserFacadeLocal;
import sessionBean.ticket.ServiceTypeFacadeLocal;
import sessionBean.ticket.SystemUserServiceTypeFacadeLocal;
import utility.Constant;
import utility.Role;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named
@ViewScoped
public class SystemUserServiceTypeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private ServiceType serviceType;
	private SystemUser systemUser;

	private Boolean systemUserSelected;
	private Boolean serviceTypeSelected;
	private Boolean objLoaded;
	private Boolean objCombined;

	/* Lists */

	private List<ServiceType> serviceTypeList;
	private List<ServiceType> serviceTypeSelectedList;
	private List<SystemUser> systemUserList;
	private List<SystemUser> systemUserSelectedList;

	private List<SystemUserServiceType> systemUserServiceTypeSelectedList;

	/* Services */

	@Inject
	private ServiceTypeFacadeLocal serviceTypeFacadeLocal;
	@Inject
	private SystemUserFacadeLocal systemUserFacadeLocal;
	@Inject
	private SystemUserServiceTypeFacadeLocal systemUserServiceTypeFacadeLocal;

	public SystemUserServiceTypeBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void reset() {

		resetVariables();
		JSFUtility.reset("form");

	}

	@SuppressWarnings("unchecked")
	public void resetVariables() {

		systemUserSelected = false;

		systemUserSelectedList = null;
		serviceTypeSelectedList = null;
		systemUserServiceTypeSelectedList = null;

		objLoaded = objCombined = false;

		try {

			List<Integer> rolesInSession = (List<Integer>) JSFUtility
					.getAttributeFromSession(JSFUtility.ROLES_IN_SESSION);

			if (rolesInSession.contains(Role.ADMINISTRATOR)) {
				systemUserList = systemUserFacadeLocal.findAll();
				serviceTypeList = serviceTypeFacadeLocal.findAll();
			} else {
				SystemUser systemUser = (SystemUser) JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION);
				systemUserList = systemUserFacadeLocal.findSystemUserByTicketServiceGroup(systemUser.getIdSystemUser());
				serviceTypeList = serviceTypeFacadeLocal
						.findServiceTypeByTicketServiceGroup(systemUser.getIdSystemUser());
			}

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void loadInfo() {

		if (!CollectionUtils.isEmpty(systemUserSelectedList)) {

			List<Integer> ids = new ArrayList<>();

			systemUserSelectedList.stream().forEach(obj -> {
				ids.add(obj.getIdSystemUser());
			});

			List<SystemUserServiceType> systemUserServiceTypeList = systemUserServiceTypeFacadeLocal
					.findSystemUserServiceTypeByUserAndByAction(ids, Constant.TCK_TODO);

			if (systemUserServiceTypeList != null)
				systemUserServiceTypeSelectedList = new ArrayList<>(systemUserServiceTypeList);
			else
				systemUserServiceTypeSelectedList = new ArrayList<>();

			objLoaded = true;
			systemUserSelected = true;

		} else {

			JSFUtility.addMessage("Debe seleccionar al menos un usuario", 2);

			objLoaded = false;
			systemUserSelected = false;

		}

	}

	public void combineInfo() {

		if (systemUserSelected) {

			if (CollectionUtils.isEmpty(serviceTypeSelectedList)) {

				JSFUtility.addMessage("Debe seleccionar al menos un servicio", 2);
				return;

			}

			HashMap<UserServiceKey, SystemUserServiceType> userServiceMap = new HashMap<>();

			SystemUserServiceType temporal = null;

			for (SystemUserServiceType userService : systemUserServiceTypeSelectedList) {

				userServiceMap.put(new UserServiceKey(userService.getSystemUser().getIdSystemUser(),
						userService.getServiceType().getServiceTypePk()), userService);

			}

			Integer mainPk = null;
			Integer altPk = null;
			UserServiceKey key = null;

			for (SystemUser systemUser : systemUserSelectedList) {

				for (ServiceType serviceType : serviceTypeSelectedList) {

					mainPk = systemUser.getIdSystemUser();
					altPk = serviceType.getServiceTypePk();

					key = new UserServiceKey(mainPk, altPk);

					if (userServiceMap.get(key) == null) {
						temporal = new SystemUserServiceType(mainPk, altPk);
						temporal.setSystemUser(systemUser);
						temporal.setServiceType(serviceType);
						temporal.setTableStatus(true);
						temporal.setDirty(false);
						systemUserServiceTypeSelectedList.add(temporal);
					}

				}

			}

			objCombined = true;

		} else {

			objCombined = false;

			JSFUtility.addMessage("Debe seleccionar al menos un usuario", 2);

		}

	}

	public void save() {

		if (systemUserSelected) {

			try {

				if (!CollectionUtils.isEmpty(systemUserServiceTypeSelectedList)) {

					for (SystemUserServiceType userService : systemUserServiceTypeSelectedList) {

						if (userService.getSystemUserServiceTypePK() == null) {
							systemUserServiceTypeFacadeLocal.create(userService);
						} else {
							if (userService.getDirty() != null && userService.getDirty()) {
								systemUserServiceTypeFacadeLocal.edit(userService);
							}
						}

					}

				}

				resetVariables();

				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar al menos un usuario", 2);

		}

	}

	final static class UserServiceKey {

		private int user;
		private int service;

		public UserServiceKey() {

		}

		public UserServiceKey(int user, int service) {

			this.user = user;
			this.service = service;
		}

		public int getUser() {
			return user;
		}

		public void setUser(int user) {
			this.user = user;
		}

		public int getService() {
			return service;
		}

		public void setService(int service) {
			this.service = service;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + service;
			result = prime * result + user;
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
			UserServiceKey other = (UserServiceKey) obj;
			if (service != other.service)
				return false;
			if (user != other.user)
				return false;
			return true;
		}

	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
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

	public List<ServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<ServiceType> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public List<SystemUser> getSystemUserList() {
		return systemUserList;
	}

	public void setSystemUserList(List<SystemUser> systemUserList) {
		this.systemUserList = systemUserList;
	}

	public List<SystemUserServiceType> getsystemUserServiceTypeSelectedList() {
		return systemUserServiceTypeSelectedList;
	}

	public void setsystemUserServiceTypeSelectedList(List<SystemUserServiceType> systemUserServiceTypeSelectedList) {
		this.systemUserServiceTypeSelectedList = systemUserServiceTypeSelectedList;
	}

	public ServiceTypeFacadeLocal getServiceTypeFacadeLocal() {
		return serviceTypeFacadeLocal;
	}

	public void setServiceTypeFacadeLocal(ServiceTypeFacadeLocal serviceTypeFacadeLocal) {
		this.serviceTypeFacadeLocal = serviceTypeFacadeLocal;
	}

	public SystemUserFacadeLocal getSystemUserFacadeLocal() {
		return systemUserFacadeLocal;
	}

	public void setSystemUserFacadeLocal(SystemUserFacadeLocal systemUserFacadeLocal) {
		this.systemUserFacadeLocal = systemUserFacadeLocal;
	}

	public List<ServiceType> getServiceTypeSelectedList() {
		return serviceTypeSelectedList;
	}

	public void setServiceTypeSelectedList(List<ServiceType> serviceTypeSelectedList) {
		this.serviceTypeSelectedList = serviceTypeSelectedList;
	}

	public List<SystemUser> getSystemUserSelectedList() {
		return systemUserSelectedList;
	}

	public void setSystemUserSelectedList(List<SystemUser> systemUserSelectedList) {
		this.systemUserSelectedList = systemUserSelectedList;
	}

	public List<SystemUserServiceType> getSystemUserServiceTypeSelectedList() {
		return systemUserServiceTypeSelectedList;
	}

	public void setSystemUserServiceTypeSelectedList(List<SystemUserServiceType> systemUserServiceTypeSelectedList) {
		this.systemUserServiceTypeSelectedList = systemUserServiceTypeSelectedList;
	}

	public Boolean getServiceTypeSelected() {
		return serviceTypeSelected;
	}

	public void setServiceTypeSelected(Boolean serviceTypeSelected) {
		this.serviceTypeSelected = serviceTypeSelected;
	}

	public Boolean getObjLoaded() {
		return objLoaded;
	}

	public void setObjLoaded(Boolean objLoaded) {
		this.objLoaded = objLoaded;
	}

	public Boolean getObjCombined() {
		return objCombined;
	}

	public void setObjCombined(Boolean objCombined) {
		this.objCombined = objCombined;
	}

	public SystemUserServiceTypeFacadeLocal getSystemUserServiceTypeFacadeLocal() {
		return systemUserServiceTypeFacadeLocal;
	}

	public void setSystemUserServiceTypeFacadeLocal(SystemUserServiceTypeFacadeLocal systemUserServiceTypeFacadeLocal) {
		this.systemUserServiceTypeFacadeLocal = systemUserServiceTypeFacadeLocal;
	}

}
