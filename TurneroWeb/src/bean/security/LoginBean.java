/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import entity.auth.Role;
import entity.auth.SystemUser;
import filter.LoginFilter;
import sessionBean.auth.OptionFacadeLocal;
import sessionBean.auth.SystemUserFacadeLocal;
import web.utility.JSFUtility;
import web.utility.WebCipher;

/**
 *
 * @author BryanV
 */
@RequestScoped
@Named("loginBean")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private SystemUser systemUser;

	@Inject
	private SystemUserFacadeLocal systemUserFacadeLocal;
	@Inject
	private OptionFacadeLocal optionFacadeLocal;

	public LoginBean() {
	}

	@PostConstruct
	public void atStart() {
		this.systemUser = new SystemUser();
	}

	public void logIn() {

		try {

			boolean redirected = false;

			SystemUser userLogin = systemUserFacadeLocal.findByUserAndPassword(this.systemUser.getCodSystemUser(),
					WebCipher.getStringMessageDigest(this.systemUser.getPass(), WebCipher.MD5));

			if (userLogin != null) {

				if (userLogin.getEstado()) {

					invalidateSession();
					HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(true);

					userLogin = systemUserFacadeLocal.fetchInformationByUser(userLogin.getCodSystemUser());

					if (userLogin != null) {

						String startPage = userLogin.getStartPage();

						if (startPage != null) {

							session.setAttribute("userInSession", userLogin);
							session.setAttribute("tiposDeUsuario", getRoles(userLogin));
							session.setAttribute("rolesInSession", getRoles(userLogin));
							session.setAttribute("pagesInSession", getPages(userLogin));
							session.setAttribute("menuInSession",
									new Gson().toJson(optionFacadeLocal.findOptions(userLogin)));

							redirected = redirect(startPage);

						} else {
							JSFUtility.addMessage("Su usuario no tiene página inicial", 2);
						}

					} else {
						JSFUtility.addMessage("Su usuario no tiene roles o permisos", 2);
					}

				} else {
					JSFUtility.addMessage("Su usuario se encuentra inactivo", 2);
				}

			} else {

				JSFUtility.keepMessages();
				JSFUtility.addMessage("Credenciales incorrectas", 2);

				redirected = redirect(LoginFilter.indexPath);

			}

			if (!redirected)
				invalidateSession();

		} catch (Exception exception) {

			System.out.println("Can't login!");
			System.out.println("Exception: " + exception.getMessage());

		}

		systemUser = new SystemUser();

	}

	public String logOut() {

		try {

			invalidateSession();
			return LoginFilter.INDEX;

		} catch (Exception exception) {
			System.out.println("Can't logout!");
			System.out.println("Exception: " + exception.getMessage());
			return LoginFilter.E500;
		}

	}

	public boolean redirect(String path) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + path);
			return true;
		} catch (IOException exception) {
			System.out.println("Can't redirect to page!");
			return false;

		}
	}

	public void invalidateSession() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	public List<Integer> getRoles(SystemUser user) {

		List<Integer> roles = new ArrayList<>();

		user.getRoleSet().stream().forEach((object) -> {
			roles.add(object.getIdRole());
		});

		return roles;

	}

	public Set<String> getPages(SystemUser user) {

		Set<String> pagesInSession = new HashSet<>();
		for (Role role : user.getRoleSet()) {

			role.getOptionSet().stream().forEach((option) -> {
				pagesInSession.add(option.getOption());
			});

		}

		return pagesInSession;

	}

	public SystemUser getUser() {
		return systemUser;
	}

	public void setUser(SystemUser user) {
		this.systemUser = user;
	}

	public SystemUserFacadeLocal getSystemUserFacadeLocal() {
		return systemUserFacadeLocal;
	}

	public void setSystemUserFacadeLocal(SystemUserFacadeLocal systemUserFacadeLocal) {
		this.systemUserFacadeLocal = systemUserFacadeLocal;
	}

	public OptionFacadeLocal getOptionFacadeLocal() {
		return optionFacadeLocal;
	}

	public void setOptionFacadeLocal(OptionFacadeLocal optionFacadeLocal) {
		this.optionFacadeLocal = optionFacadeLocal;
	}

}
