/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.pub.Parameter;
import sessionBean.pub.ParameterFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class ParameterBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Parameter object;

	private Boolean editMode;

	private List<Parameter> objectList;

	@Inject
	private ParameterFacadeLocal objectFacadeLocal;

	public ParameterBean() {

	}

	@PostConstruct
	public void atStart() {

		resetVariables();

	}

	public void resetVariables() {

		editMode = false;

		object = new Parameter();

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

	public void selectObject(Parameter object) {

		this.object = object;
		editMode = true;

	}

	public Parameter getObject() {
		return object;
	}

	public void setObject(Parameter object) {
		this.object = object;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public List<Parameter> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Parameter> objectList) {
		this.objectList = objectList;
	}

	public ParameterFacadeLocal getParameterFacadeLocal() {
		return objectFacadeLocal;
	}

	public void setParameterFacadeLocal(ParameterFacadeLocal objectFacadeLocal) {
		this.objectFacadeLocal = objectFacadeLocal;
	}

}
