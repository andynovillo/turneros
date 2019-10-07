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

import entity.ticket.Monitor;
import sessionBean.ticket.MonitorFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class MonitorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Monitor object;

	private Boolean editMode;

	private List<Monitor> objectList;

	@Inject
	private MonitorFacadeLocal monitorFacadeLocal;

	public MonitorBean() {

	}

	@PostConstruct
	public void atStart() {

		resetVariables();

	}

	public void resetVariables() {

		editMode = false;

		object = new Monitor();

		objectList = monitorFacadeLocal.findAll();

	}

	public void resetForm() {

		resetVariables();
		JSFUtility.reset("form");

	}

	public void saveObject() {

		try {

			if (editMode) {

				monitorFacadeLocal.edit(object);
				editMode = false;

			} else {

				monitorFacadeLocal.create(object);

			}

			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

		} catch (Exception exception) {

			System.out.println("Exception: " + exception.getMessage());
			JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 3);
		}

		resetVariables();

	}

	public void selectObject(Monitor object) {

		this.object = object;
		editMode = true;

	}

	public Monitor getObject() {
		return object;
	}

	public void setObject(Monitor object) {
		this.object = object;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public List<Monitor> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Monitor> objectList) {
		this.objectList = objectList;
	}

	public MonitorFacadeLocal getMonitorFacadeLocal() {
		return monitorFacadeLocal;
	}

	public void setMonitorFacadeLocal(MonitorFacadeLocal monitorFacadeLocal) {
		this.monitorFacadeLocal = monitorFacadeLocal;
	}

}
