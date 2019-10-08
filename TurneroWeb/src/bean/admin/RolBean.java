/*
To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.util.TreeUtils;

import entity.auth.Role;
import sessionBean.auth.RoleFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named("rolBean")
public class RolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String ROOT = "ROOT";

	private TreeNode root;
	private TreeNode selectedNode;
	private TreeNode[] selectedNodes;

	private Role rol;

	@EJB
	private RoleFacadeLocal tipoUsuarioFacadeLocal;

	private List<Role> roles;
	private List<Role> rolesFiltered;

	private boolean editMode;

	HashMap<String, Object> nodesMap;
	HashMap<String, Object> rolesMap;

	public RolBean() {

	}

	@PostConstruct
	public void inicio() {

		editMode = false;

		rol = new Role();

		roles = tipoUsuarioFacadeLocal.findAll();

		createCheckboxRoles();

	}

	public void createCheckboxRoles() {

		root = new DefaultTreeNode(RolBean.ROOT, null);

		nodesMap = new HashMap<>();

		rolesMap = new HashMap<>();

		for (Role rol : roles) {

			nodesMap.put(rol.getRole(), new DefaultTreeNode(rol.getRole(), root));

			rolesMap.put(rol.getRole(), rol.getIdPadre());

		}

		for (Map.Entry<String, Object> entry : nodesMap.entrySet()) {

			DefaultTreeNode node = (DefaultTreeNode) nodesMap.get(entry.getKey());

			String padre = (String) rolesMap.get(entry.getKey());

			if (!padre.equals(RolBean.ROOT)) {
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

	public void addRol() {

		String message = null;

		try {

			if (isEditMode()) {

				tipoUsuarioFacadeLocal.edit(rol);
				message = "El rol '" + rol.getRole() + "' ha sido editado";

				editMode = false;

			} else {

				rol.setRole(rol.getRole().toUpperCase());
				rol.setIdPadre(root.getData().toString());

				System.out.println(rol.getIdRole());

				message = "El rol '" + rol.getRole() + "' ya existe";

				if (tipoUsuarioFacadeLocal.findByRole(rol.getRole()) == null) {
					roles.add(0, rol);

					tipoUsuarioFacadeLocal.create(rol);
					message = "El rol '" + rol.getRole() + "' ha sido creado";
				}
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		FacesMessage mensaje = new FacesMessage(message, null);
		FacesContext.getCurrentInstance().addMessage(null, mensaje);

		rol = new Role();

		roles = tipoUsuarioFacadeLocal.findAll();

		JSFUtility.reset("form:rolSelected");

		createCheckboxRoles();

	}

	public static boolean findNode(final TreeNode selectedNode, final TreeNode parent) {
		if (selectedNode.getChildCount() > 0) {
			final Iterator<TreeNode> childs = selectedNode.getChildren().iterator();
			while (childs.hasNext()) {
				final TreeNode currentNode = childs.next();

				if (currentNode.equals(parent)) {
					return true;
				}

				if (currentNode.isLeaf()) {

				} else {
					findNode(currentNode, parent);
				}
			}
		}

		return false;

	}

	public void editRow(Role tipoUsuario) {

		JSFUtility.reset("form:rolSelected");

		rol = tipoUsuario;

		editMode = true;

	}

	public void onDragDrop(TreeDragDropEvent event) {
		TreeNode dragNode = event.getDragNode();
		TreeNode dropNode = event.getDropNode();

		try {

			Role tipoUsuario = tipoUsuarioFacadeLocal.findByRole(dragNode.getData().toString());
			tipoUsuario.setIdPadre(dropNode.getData().toString());
			tipoUsuarioFacadeLocal.edit(tipoUsuario);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Rol " + dragNode.getData() + " movido a " + dropNode.getData(), "");
		FacesContext.getCurrentInstance().addMessage(null, message);

		roles = tipoUsuarioFacadeLocal.findAll();
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public Role getRol() {
		return rol;
	}

	public void setRol(Role rol) {
		this.rol = rol;
	}

	public RoleFacadeLocal getTipoUsuarioFacadeLocal() {
		return tipoUsuarioFacadeLocal;
	}

	public void setTipoUsuarioFacadeLocal(RoleFacadeLocal tipoUsuarioFacadeLocal) {
		this.tipoUsuarioFacadeLocal = tipoUsuarioFacadeLocal;
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

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
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

}