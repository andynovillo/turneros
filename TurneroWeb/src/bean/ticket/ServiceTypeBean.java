/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.ticket;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.ticket.ServiceType;
import sessionBean.ticket.ServiceTypeFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class ServiceTypeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ServiceType object;

	private Boolean editMode;

	private List<ServiceType> objectList;

	@Inject
	private ServiceTypeFacadeLocal objectFacadeLocal;

	public ServiceTypeBean() {

	}

	@PostConstruct
	public void atStart() {

		resetVariables();

	}

	public void resetVariables() {

		editMode = false;

		object = new ServiceType();

		objectList = objectFacadeLocal.findAll();

	}

	public void resetForm() {

		resetVariables();
		JSFUtility.reset("form");

	}

	public void saveObject() {

		try {

			if (editMode) {

				objectFacadeLocal.edit(object);
				editMode = false;

			} else {

				objectFacadeLocal.create(object);

			}

			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

		} catch (Exception exception) {

			System.out.println("Exception: " + exception.getMessage());
			JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 3);
		}

		resetVariables();

	}

	public void selectObject(ServiceType object) {

		this.object = object;
		editMode = true;

	}

	public ServiceType getObject() {
		return object;
	}

	public void setObject(ServiceType object) {
		this.object = object;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public List<ServiceType> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<ServiceType> objectList) {
		this.objectList = objectList;
	}

	public ServiceTypeFacadeLocal getServiceTypeFacadeLocal() {
		return objectFacadeLocal;
	}

	public void setServiceTypeFacadeLocal(ServiceTypeFacadeLocal objectFacadeLocal) {
		this.objectFacadeLocal = objectFacadeLocal;
	}

}
