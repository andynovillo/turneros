/*
To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.auth.Option;
import sessionBean.auth.OptionFacadeLocal;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named("permisoBean")
public class PermisoBean implements Serializable, Converter {

	private static final long serialVersionUID = 1L;

	@EJB
	private OptionFacadeLocal permisoFacadeLocal;

	private List<Option> permisos;

	private List<Option> permisosFiltered;

	private Option permiso;

	private boolean editMode;

	public PermisoBean() {

	}

	@PostConstruct
	public void inicio() {

		permiso = new Option();

		editMode = false;

		permisos = permisoFacadeLocal.findAll();

	}

	public void removePermiso(Option permiso) {

		permisos.remove(permiso);

		try {
			if (permisoFacadeLocal.find(permiso.getIdOption()) != null) {
				permisoFacadeLocal.edit(permiso);
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

	}

	public void editRow(Option permiso) {

		this.permiso = permiso;
		editMode = true;

	}

	public void addPermiso() {

		String message = null;

		try {

			if (isEditMode()) {

				permisoFacadeLocal.edit(permiso);
				message = "El permiso '" + permiso.getOption() + "' ha sido editado";

				editMode = false;

			} else {

				permiso.setOption(permiso.getOption());

				message = "El permiso '" + permiso.getOption() + "' ya existe";

				if (permisoFacadeLocal.findByOption(permiso.getOption()) == null) {
					permisos.add(0, permiso);
					permisoFacadeLocal.create(permiso);
					message = "El permiso '" + permiso.getOption() + "' ha sido creado";
				}
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		FacesMessage mensaje = new FacesMessage(message, null);
		FacesContext.getCurrentInstance().addMessage(null, mensaje);

		permiso = new Option();

		permisos = permisoFacadeLocal.findAll();

	}

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {

		try {

			Option permiso = new Option();

			permiso = permisoFacadeLocal.find(Integer.parseInt(string));

			return permiso;

		} catch (Exception exception) {
			System.out.print("Error getting permiso as object: " + exception.getMessage());
			return null;

		}

	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {

		if (object != null) {
			try {
				if (object instanceof Option) {
					Option permiso = (Option) object;
					return permiso.getIdOption().toString();
				} else
					return null;
			} catch (Exception exception) {
				System.out.print("Error getting permiso as string: " + exception.getMessage());
				return null;
			}
		}

		return null;

	}

	public OptionFacadeLocal getPermisoFacadeLocal() {
		return permisoFacadeLocal;
	}

	public void setPermisoFacadeLocal(OptionFacadeLocal permisoFacadeLocal) {
		this.permisoFacadeLocal = permisoFacadeLocal;
	}

	public List<Option> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Option> permisos) {
		this.permisos = permisos;
	}

	public List<Option> getPermisosFiltered() {
		return permisosFiltered;
	}

	public void setPermisosFiltered(List<Option> permisosFiltered) {
		this.permisosFiltered = permisosFiltered;
	}

	public Option getPermiso() {
		return permiso;
	}

	public void setPermiso(Option permiso) {
		this.permiso = permiso;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}