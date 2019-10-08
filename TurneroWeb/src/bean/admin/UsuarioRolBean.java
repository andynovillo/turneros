/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import entity.auth.Role;
import entity.auth.SystemUser;
import sessionBean.auth.RoleFacadeLocal;
import sessionBean.auth.SystemUserFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named("usuarioRolBean")
@ViewScoped
public class UsuarioRolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean usuarioSelected;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	private Role role;
	private SystemUser systemUser;

	private List<Role> roleList;
	private List<Role> roleSelectedList;

	private List<SystemUser> systemUserList;
	private List<SystemUser> systemUserSelectedList;

	private List<Role> parentRoleList;

	private HashMap<String, Object> nodesMap;
	private HashMap<String, Object> rolesMap;
	private HashMap<String, Role> roleMap;

	@Inject
	private RoleFacadeLocal roleFacadeLocal;
	@Inject
	private SystemUserFacadeLocal systemUserFacadeLocal;

	public UsuarioRolBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void reset() {

		resetVariables();
		JSFUtility.reset("form");

	}

	public void resetVariables() {

		role = new Role();
		systemUser = new SystemUser();

		try {

			parentRoleList = new ArrayList<>();
			roleList = roleFacadeLocal.findAll();
			systemUserList = systemUserFacadeLocal.findAll();

			fillHashMap();
			createCheckboxRoles();

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void fillHashMap() {

		if (!CollectionUtils.isEmpty(roleList)) {
			roleMap = new HashMap<String, Role>();
			for (Role rol : roleList) {
				roleMap.put(rol.getRole(), rol);
			}
		}

	}

	public void createCheckboxRoles() {

		root = new DefaultTreeNode("ROOT", null);

		nodesMap = new HashMap<>();
		rolesMap = new HashMap<>();

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

	}

	public void resetPanelRoles() {
		parentRoleList = new ArrayList<>();

		for (Map.Entry<String, Object> entry : nodesMap.entrySet()) {

			DefaultTreeNode node = (DefaultTreeNode) nodesMap.get(entry.getKey());

			node.setSelected(false);
			node.setExpanded(false);

		}
	}

	public void save() {

		if (!CollectionUtils.isEmpty(systemUserSelectedList)) {

			HashSet<TreeNode> set = new HashSet<>();

			for (TreeNode treeNode : selectedNodes) {
				set.add(treeNode);
				set.add(treeNode.getParent());
			}

			set.remove(root);

			Set<Role> lista = new HashSet<>();

			Iterator<TreeNode> iterator = set.iterator();
			while (iterator.hasNext()) {
				TreeNode node = iterator.next();
				lista.add(roleMap.get(node.getData().toString()));

			}

			try {

				for (SystemUser obj : systemUserSelectedList) {

					obj = systemUserFacadeLocal.find(obj.getIdSystemUser());
					obj.setRoleSet(lista);
					systemUserFacadeLocal.edit(obj);

				}

				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
				resetPanelRoles();

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar al menos un usuario", 2);

		}

	}

	public boolean isUsuarioSelected() {
		return usuarioSelected;
	}

	public void setUsuarioSelected(boolean usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Role> getRoleSelectedList() {
		return roleSelectedList;
	}

	public void setRoleSelectedList(List<Role> roleSelectedList) {
		this.roleSelectedList = roleSelectedList;
	}

	public List<SystemUser> getSystemUserList() {
		return systemUserList;
	}

	public void setSystemUserList(List<SystemUser> systemUserList) {
		this.systemUserList = systemUserList;
	}

	public List<SystemUser> getSystemUserSelectedList() {
		return systemUserSelectedList;
	}

	public void setSystemUserSelectedList(List<SystemUser> systemUserSelectedList) {
		this.systemUserSelectedList = systemUserSelectedList;
	}

	public List<Role> getParentRoleList() {
		return parentRoleList;
	}

	public void setParentRoleList(List<Role> parentRoleList) {
		this.parentRoleList = parentRoleList;
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

	public SystemUserFacadeLocal getSystemUserFacadeLocal() {
		return systemUserFacadeLocal;
	}

	public void setSystemUserFacadeLocal(SystemUserFacadeLocal systemUserFacadeLocal) {
		this.systemUserFacadeLocal = systemUserFacadeLocal;
	}

}
