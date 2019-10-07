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

import entity.ticket.AttentionModule;
import entity.ticket.ServiceType;
import sessionBean.ticket.AttentionModuleFacadeLocal;
import sessionBean.ticket.ServiceTypeFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named
@ViewScoped
public class ServiceTypeAttentionModuleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private ServiceType serviceType;
	private AttentionModule attentionModule;

	private Boolean objectSelected;

	/* Lists */

	private List<AttentionModule> attentionModuleList;
	private List<AttentionModule> attentionModuleSelectedList;
	private List<ServiceType> serviceTypeList;

	/* Services */

	@Inject
	private ServiceTypeFacadeLocal serviceTypeFacadeLocal;
	@Inject
	private AttentionModuleFacadeLocal attentionModuleFacadeLocal;

	public ServiceTypeAttentionModuleBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		objectSelected = false;

		try {

			attentionModuleList = attentionModuleFacadeLocal.findAll();
			serviceTypeList = serviceTypeFacadeLocal.findAll();

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void fetchAttentionModuleByServiceType(ServiceType object) {

		try {

			ServiceType temporal = serviceTypeFacadeLocal.fetchAttentionModuleByServiceType(object.getServiceTypePk());

			if (temporal != null) {
				this.serviceType = temporal;
				attentionModuleSelectedList = new ArrayList<>(temporal.getAttentionModuleSet());
				objectSelected = true;
			} else {
				objectSelected = false;
			}

		} catch (Exception exception) {

			System.out.println("Can't fetch modules by service!");

		}

	}

	public void save() {

		if (objectSelected) {

			try {

				serviceType.setAttentionModuleSet(new HashSet<>(attentionModuleSelectedList));

				serviceTypeFacadeLocal.edit(serviceType);

				resetVariables();

				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar un servicio", 2);

		}

	}

	public void setAttentionModuleFacadeLocal(AttentionModuleFacadeLocal monitorFacadeLocal) {
		this.attentionModuleFacadeLocal = monitorFacadeLocal;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public AttentionModule getAttentionModule() {
		return attentionModule;
	}

	public void setAttentionModule(AttentionModule attentionModule) {
		this.attentionModule = attentionModule;
	}

	public Boolean getObjectSelected() {
		return objectSelected;
	}

	public void setObjectSelected(Boolean objectSelected) {
		this.objectSelected = objectSelected;
	}

	public List<AttentionModule> getAttentionModuleList() {
		return attentionModuleList;
	}

	public void setAttentionModuleList(List<AttentionModule> attentionModuleList) {
		this.attentionModuleList = attentionModuleList;
	}

	public List<AttentionModule> getAttentionModuleSelectedList() {
		return attentionModuleSelectedList;
	}

	public void setAttentionModuleSelectedList(List<AttentionModule> attentionModuleSelectedList) {
		this.attentionModuleSelectedList = attentionModuleSelectedList;
	}

	public List<ServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<ServiceType> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public ServiceTypeFacadeLocal getServiceTypeFacadeLocal() {
		return serviceTypeFacadeLocal;
	}

	public void setServiceTypeFacadeLocal(ServiceTypeFacadeLocal serviceTypeFacadeLocal) {
		this.serviceTypeFacadeLocal = serviceTypeFacadeLocal;
	}

	public AttentionModuleFacadeLocal getAttentionModuleFacadeLocal() {
		return attentionModuleFacadeLocal;
	}

}
