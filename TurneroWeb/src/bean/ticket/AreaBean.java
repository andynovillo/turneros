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

import entity.ticket.Area;
import sessionBean.ticket.AreaFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class AreaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Area object;

	private Boolean editMode;

	private List<Area> objectList;

	@Inject
	private AreaFacadeLocal objectFacadeLocal;

	public AreaBean() {

	}

	@PostConstruct
	public void atStart() {

		resetVariables();

	}

	public void resetVariables() {

		editMode = false;

		object = new Area();

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

	public void selectObject(Area object) {

		this.object = object;
		editMode = true;

	}

	public Area getObject() {
		return object;
	}

	public void setObject(Area object) {
		this.object = object;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public List<Area> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Area> objectList) {
		this.objectList = objectList;
	}

	public AreaFacadeLocal getAreaFacadeLocal() {
		return objectFacadeLocal;
	}

	public void setAreaFacadeLocal(AreaFacadeLocal objectFacadeLocal) {
		this.objectFacadeLocal = objectFacadeLocal;
	}

}
