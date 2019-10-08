/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.auth.Option;
import entity.auth.Role;
import sessionBean.auth.OptionFacadeLocal;
import sessionBean.auth.RoleFacadeLocal;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named("rolPermisoBean")
public class RolPermisoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Role rol;
	private Option permiso;

	@EJB
	private RoleFacadeLocal tipoUsuarioFacadeLocal;

	@EJB
	private OptionFacadeLocal permisoFacadeLocal;

	private List<Role> roles;
	private List<Role> rolesFiltered;

	private Set<Option> permisos;
	private Set<Option> permisosFiltered;

	private Set<Option> permisosSelected;

	private boolean rolSelected;

	public RolPermisoBean() {

	}

	@PostConstruct
	public void inicio() {

		rol = new Role();
		permiso = new Option();

		roles = tipoUsuarioFacadeLocal.findAll();
		permisos = new HashSet<>();
		permisos.addAll(permisoFacadeLocal.findAll());

	}

	public void buscarPermisos(Role tipoUsuario) {

		try {

			permisosSelected = new HashSet<>();

			this.rol = (Role) tipoUsuarioFacadeLocal.fetchOptionsByRole(tipoUsuario.getRole());

			if (this.rol != null) {

				permisosSelected.addAll(this.rol.getOptionSet());
			}

			this.rol = tipoUsuario;
			setRolSelected(true);

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public void guardar() {

		if (rolSelected) {

			this.rol.setOptionSet(permisosSelected);

			try {
				tipoUsuarioFacadeLocal.edit(this.rol);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
			}
		}

		setRolSelected(false);
		permisosSelected = new HashSet<>();
		rol = new Role();

	}

	public Role getRol() {
		return rol;
	}

	public void setRol(Role rol) {
		this.rol = rol;
	}

	public Option getPermiso() {
		return permiso;
	}

	public void setPermiso(Option permiso) {
		this.permiso = permiso;
	}

	public RoleFacadeLocal getTipoUsuarioFacadeLocal() {
		return tipoUsuarioFacadeLocal;
	}

	public void setTipoUsuarioFacadeLocal(RoleFacadeLocal tipoUsuarioFacadeLocal) {
		this.tipoUsuarioFacadeLocal = tipoUsuarioFacadeLocal;
	}

	public OptionFacadeLocal getPermisoFacadeLocal() {
		return permisoFacadeLocal;
	}

	public void setPermisoFacadeLocal(OptionFacadeLocal permisoFacadeLocal) {
		this.permisoFacadeLocal = permisoFacadeLocal;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRolesFiltered() {
		return rolesFiltered;
	}

	public void setRolesFiltered(List<Role> rolesFiltered) {
		this.rolesFiltered = rolesFiltered;
	}

	public Set<Option> getPermisos() {
		return permisos;
	}

	public void setPermisos(Set<Option> permisos) {
		this.permisos = permisos;
	}

	public Set<Option> getPermisosFiltered() {
		return permisosFiltered;
	}

	public void setPermisosFiltered(Set<Option> permisosFiltered) {
		this.permisosFiltered = permisosFiltered;
	}

	public Set<Option> getPermisosSelected() {
		return permisosSelected;
	}

	public void setPermisosSelected(Set<Option> permisosSelected) {
		this.permisosSelected = permisosSelected;
	}

	public boolean isRolSelected() {
		return rolSelected;
	}

	public void setRolSelected(boolean rolSelected) {
		this.rolSelected = rolSelected;
	}

}
