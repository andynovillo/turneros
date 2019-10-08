/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.TreeUtils;

import entity.auth.Role;
import entity.auth.SystemUser;
import sessionBean.auth.RoleFacadeLocal;
import sessionBean.auth.SystemUserFacadeLocal;
import sessionBean.pub.ParameterFacadeLocal;
import utility.Constant;
import utility.Utility;
import web.utility.JSFUtility;
import web.utility.WebCipher;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named("usuarioBean")
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String actualPassword;
	private String newPassword;
	private String newMatchPassword;

	private SystemUser usuario;

	private Boolean editMode;
	private Boolean dataChanged;

	private List<SystemUser> usuarios;
	private Set<Role> roleSet;
	private TreeNode root;
	private TreeNode[] selectedNodes;

	private HashMap<String, Object> nodesMap;
	private HashMap<String, Object> rolesMap;
	private HashMap<String, Role> roleMap;

	@Inject
	private SystemUserFacadeLocal usuarioFacadeLocal;
	@Inject
	private RoleFacadeLocal roleFacadeLocal;
	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;

	public UsuarioBean() {

	}

	@PostConstruct
	public void atStart() {

		editMode = false;
		usuario = new SystemUser();
		usuario.setEstado(true);
		selectedNodes = new TreeNode[] {};
		resetPanelRoles();

	}

	public void resetForm() {
		JSFUtility.reset("form");
		atStart();
	}

	public void findUsers() {

		usuarios = usuarioFacadeLocal.findAll();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void findDataForUsers() {

		try {
			roleSet = new HashSet(roleFacadeLocal.findAll());
			usuarios = usuarioFacadeLocal.findAll();
			fillHashMap();
			createCheckboxRoles();
		} catch (Exception exception) {
			exception.printStackTrace();
			System.err.println("Can't find data for users!");
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);
		}

	}

	public void removeUser(SystemUser usuario) {

		usuarios.remove(usuario);

		try {
			if (usuarioFacadeLocal.find(usuario.getIdSystemUser()) != null) {
				usuario.setEstado(false);
				usuarioFacadeLocal.edit(usuario);
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

	}

	public void addUser() {

		String message = null;

		try {

			HashSet<TreeNode> set = new HashSet<>();

			if (selectedNodes != null && selectedNodes.length != 0) {
				for (TreeNode treeNode : selectedNodes) {
					set.add(treeNode);
					set.add(treeNode.getParent());
				}
				set.remove(root);
			}

			Set<Role> lista = new HashSet<Role>();

			Iterator<TreeNode> iterator = set.iterator();
			while (iterator.hasNext()) {
				TreeNode node = iterator.next();
				lista.add(roleMap.get(node.getData().toString()));

			}

			this.usuario.setRoleSet(lista);

			if (isEditMode()) {

				usuarioFacadeLocal.edit(usuario);
				message = "El usuario '" + usuario.getCodSystemUser() + "' ha sido editado";

				editMode = false;

			} else {

				message = "El usuario '" + usuario.getCodSystemUser() + "' ya existe";

				if (usuarioFacadeLocal.findByUserCode(usuario.getCodSystemUser()) == null) {
					usuario.setStartPage(Constant.PAGE_HOME);
					usuario.setPass(WebCipher.getStringMessageDigest(usuario.getCodSystemUser(), WebCipher.MD5));
					usuario.setLastPassword(usuario.getPass());
					usuario.setLastPasswordChangeDateTime(null);
					usuarioFacadeLocal.create(usuario);
					message = "El usuario '" + usuario.getCodSystemUser() + "' ha sido creado";
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();

		}

		FacesMessage mensaje = new FacesMessage(message, null);
		FacesContext.getCurrentInstance().addMessage(null, mensaje);

		usuario = new SystemUser();

		findDataForUsers();

		JSFUtility.reset("form:usuarioSelected");

	}

	public void editRow(SystemUser usuario) {

		try {

			this.usuario = usuario;
			fetchMngInfoForUser();

		} catch (Exception exception) {

			System.out.println("Can't edit row! " + exception.getMessage());
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void resetPassword(SystemUser usuario) {

		try {

			usuario = usuarioFacadeLocal.find(usuario.getIdSystemUser());
			usuario.setPass(WebCipher.getStringMessageDigest(usuario.getCodSystemUser(), "MD5"));
			usuario.setLastPassword(usuario.getPass());
			usuario.setLastPasswordChangeDateTime(null);
			usuarioFacadeLocal.edit(usuario);
			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

		} catch (Exception exception) {

			System.out.println("Can't reset password! " + exception.getMessage());
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void resetPasswordByMail() {

		try {

			usuario = usuarioFacadeLocal.findByUserCode(usuario.getCodSystemUser());

			if (usuario != null) {

				if (usuario.getMail() != null && usuario.getOtherMail() != null) {

					Boolean defaultHost = Boolean
							.valueOf(parameterFacadeLocal.findByParameter("MAIL-DEFAULT-HOST").getValue());

					String username;
					String password;

					if (defaultHost) {
						username = parameterFacadeLocal.findByParameter("HTMC-MAIL-USER").getValue();
						password = parameterFacadeLocal.findByParameter("HTMC-MAIL-PASS").getValue();
					} else {
						username = parameterFacadeLocal.findByParameter("GMAIL-MAIL-USER").getValue();
						password = parameterFacadeLocal.findByParameter("GMAIL-MAIL-PASS").getValue();
					}

//					Mail mail = new Mail(username, password);
//					mail.setDefaultHost(defaultHost);
//					mail.setInlineMode(false);
//
//					String url = parameterFacadeLocal.findByParameter("URL-VRF-TKN").getValue();
//					String token = UUID.randomUUID().toString().replace("-", "");
//					String bodyTxt = "Por favor, de click en la siguiente dirección para proceder con el reseteo de su cuenta: <br/><br/>"
//							+ url + token;
//
//					boolean result = mail.sendMail(usuario.getMail() + "," + usuario.getOtherMail(),
//							"Reseteo de contraseña", bodyTxt, "htmc-reportes@htmc.gob.ec", "Reseteo de contraseña", "",
//							new String[] {});
//
//					if (result) {
//						usuario.setTokenText(token);
//						usuario.setTokenDateTime(Calendar.getInstance().getTime());
//						usuarioFacadeLocal.edit(usuario);
//						dataChanged = true;
//						JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
//					} else {
//						dataChanged = false;
//						JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);
//					}

				} else {

					dataChanged = false;
					JSFUtility.addMessage("Su cuenta no tiene registrado un correo", 2);

				}

			} else {

				dataChanged = false;
				JSFUtility.addMessage(JSFUtility.DATA_NOT_FOUND, 2);

			}

		} catch (Exception exception) {

			dataChanged = false;
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

		atStart();

	}

	public void checkToken(String tkn) {

		try {

			usuario = usuarioFacadeLocal.findByToken(tkn);

			if (usuario != null) {
				usuario.setPass(WebCipher.getStringMessageDigest(usuario.getCodSystemUser(), "MD5"));
				usuario.setLastPassword(usuario.getPass());
				usuario.setLastPasswordChangeDateTime(null);
				usuario.setTokenText(null);
				usuario.setTokenDateTime(null);
				usuarioFacadeLocal.edit(usuario);
				dataChanged = true;
			} else {
				dataChanged = false;
			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void changePassword() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		SystemUser userInSession = new SystemUser();
		userInSession = (SystemUser) session.getAttribute("userInSession");

		if (userInSession != null) {

			String actualPasswordMD5 = WebCipher.getStringMessageDigest(actualPassword, "MD5");
			String newPasswordMD5 = WebCipher.getStringMessageDigest(newPassword, "MD5");

			if (actualPasswordMD5.equals(userInSession.getPass())) {
				if (newPasswordMD5.equals(userInSession.getLastPassword())) {
					JSFUtility.addMessage("No puede usar la última contraseña asignada", 3);
					return;
				}
				if (newPassword.length() < Constant.PASSWORD_MIN_LENGTH) {
					JSFUtility.addMessage("No cumple la longitud mínima", 3);
					return;
				}
				if (!newPassword.matches("(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*)")) {
					JSFUtility.addMessage("No cumple el nivel de complejidad", 3);
					return;
				}
				if (!newPassword.equals(newMatchPassword)) {
					JSFUtility.addMessage("No coinciden las contraseñas", 3);
					return;
				}
				userInSession.setPass(newPasswordMD5);
				try {
					userInSession.setLastPasswordChangeDateTime(Calendar.getInstance().getTime());
					userInSession.setLastPassword(userInSession.getPass());
					usuarioFacadeLocal.edit(userInSession);
					dataChanged = true;
					JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
				} catch (Exception exception) {
					dataChanged = false;
					System.out.println("Exception: " + exception.getMessage());
				}
			} else {
				dataChanged = false;
				JSFUtility.addMessage("La contraseña actual no es correcta", 3);

			}

		} else {
			JSFUtility.addMessage("Inicie sesión para cambiar su contraseña", 2);
		}

	}

	public void updateUserInfo() {

		try {

			String mail = usuario.getMail();
			String otherMail = usuario.getOtherMail();

			if (mail.equals(otherMail)) {
				dataChanged = false;
				JSFUtility.addMessage("Los correos no deben ser iguales", 2);
				return;
			}

			usuario = usuarioFacadeLocal.find(JSFUtility.getUserInSession().getIdSystemUser());
			usuario.setMail(mail);
			usuario.setOtherMail(otherMail);
			usuarioFacadeLocal.edit(usuario);
			dataChanged = true;
			JSFUtility.getUserInSession().setMail(mail);
			JSFUtility.getUserInSession().setOtherMail(otherMail);
			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

		} catch (Exception exception) {

			dataChanged = false;
			System.out.println("Can't update mail! " + exception.getMessage());
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void resetPanelRoles() {

		if (nodesMap != null) {
			for (Map.Entry<String, Object> entry : nodesMap.entrySet()) {

				DefaultTreeNode node = (DefaultTreeNode) nodesMap.get(entry.getKey());

				node.setSelected(false);
				node.setExpanded(false);

			}
		}
	}

	public void fillHashMap() {

		roleMap = new HashMap<String, Role>();
		for (Role rol : roleSet) {
			roleMap.put(rol.getRole(), rol);
		}

	}

	public void createCheckboxRoles() {

		root = new DefaultTreeNode("ROOT", null);

		nodesMap = new HashMap<>();

		rolesMap = new HashMap<>();

		List<Role> roleList = new ArrayList<>(roleSet);

		for (Role rol : roleList) {

			nodesMap.put(rol.getRole(), new DefaultTreeNode(rol.getRole(), root));

			rolesMap.put(rol.getRole(), rol.getIdPadre());

		}

		for (Map.Entry<String, Object> entry : nodesMap.entrySet()) {

			DefaultTreeNode node = (DefaultTreeNode) nodesMap.get(entry.getKey());

			String padre = (String) rolesMap.get(entry.getKey());

			if (!padre.equals("ROOT")) {
				DefaultTreeNode parent = (DefaultTreeNode) node.getParent();
				List<org.primefaces.model.TreeNode> children = new ArrayList<TreeNode>();

				children.addAll(parent.getChildren());

				parent.getChildren().clear();
				children.remove(node);

				parent.setChildren(children);
				((DefaultTreeNode) nodesMap.get(padre)).getChildren().add(node);

			}

		}

		TreeUtils.sortNode(root, new Comparator<DefaultTreeNode>() {
			public int compare(DefaultTreeNode anObj, DefaultTreeNode otherObj) {

				return (((String) anObj.getData()).compareTo((String) otherObj.getData()));

			};
		});

	}

	public void fetchMngInfoForUser() {

		try {

			usuario = usuarioFacadeLocal.fetchMngInfoForUser(usuario.getIdSystemUser());

			resetPanelRoles();
			if (usuario != null) {

				roleSet = usuario.getRoleSet();
				selectedNodes = new DefaultTreeNode[roleSet.size()];

				int index = 0;

				for (Role tipoUsuario : roleSet) {

					DefaultTreeNode node = (DefaultTreeNode) nodesMap.get(tipoUsuario.getRole());
					selectedNodes[index++] = node;
					node.setSelected(true);
					node.setExpanded(true);

				}

				editMode = true;

			} else {

				editMode = false;

			}

		} catch (Exception exception) {

			editMode = false;
			Utility.printErrorMessage("fetchMngInfoForUser()", exception);

		}
	}

	public SystemUserFacadeLocal getUsuarioFacadeLocal() {
		return usuarioFacadeLocal;
	}

	public void setUsuarioFacadeLocal(SystemUserFacadeLocal usuarioFacadeLocal) {
		this.usuarioFacadeLocal = usuarioFacadeLocal;
	}

	public SystemUser getUsuario() {
		return usuario;
	}

	public void setUsuario(SystemUser usuario) {
		this.usuario = usuario;
	}

	public List<SystemUser> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SystemUser> usuarios) {
		this.usuarios = usuarios;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getActualPassword() {
		return actualPassword;
	}

	public void setActualPassword(String actualPassword) {
		this.actualPassword = actualPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewMatchPassword() {
		return newMatchPassword;
	}

	public void setNewMatchPassword(String newMatchPassword) {
		this.newMatchPassword = newMatchPassword;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public HashMap<String, Object> getNodesMap() {
		return nodesMap;
	}

	public void setNodesMap(HashMap<String, Object> nodesMap) {
		this.nodesMap = nodesMap;
	}

	public HashMap<String, Object> getRolesMap() {
		return rolesMap;
	}

	public void setRolesMap(HashMap<String, Object> rolesMap) {
		this.rolesMap = rolesMap;
	}

	public HashMap<String, Role> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(HashMap<String, Role> roleMap) {
		this.roleMap = roleMap;
	}

	public RoleFacadeLocal getRoleFacadeLocal() {
		return roleFacadeLocal;
	}

	public void setRoleFacadeLocal(RoleFacadeLocal roleFacadeLocal) {
		this.roleFacadeLocal = roleFacadeLocal;
	}

	public Boolean getDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(Boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	public ParameterFacadeLocal getParameterFacadeLocal() {
		return parameterFacadeLocal;
	}

	public void setParameterFacadeLocal(ParameterFacadeLocal parameterFacadeLocal) {
		this.parameterFacadeLocal = parameterFacadeLocal;
	}

}
