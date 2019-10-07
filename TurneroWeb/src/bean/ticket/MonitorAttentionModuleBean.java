/*
 To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.ticket.AttentionModule;
import entity.ticket.Monitor;
import sessionBean.ticket.AttentionModuleFacadeLocal;
import sessionBean.ticket.MonitorFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named
@ViewScoped
public class MonitorAttentionModuleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private Monitor monitor;
	private AttentionModule attentionModule;

	private Boolean objectSelected;

	/* Lists */

	private List<Monitor> monitorList;
	private List<Monitor> monitorSelectedList;
	private List<AttentionModule> attentionModuleList;
	private List<AttentionModule> attentionModuleSelectedList;

	/* Services */

	@Inject
	private AttentionModuleFacadeLocal AttentionModuleFacadeLocal;
	@Inject
	private MonitorFacadeLocal monitorFacadeLocal;

	public MonitorAttentionModuleBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		objectSelected = false;

		try {

			monitorList = monitorFacadeLocal.findAll();
			attentionModuleList = AttentionModuleFacadeLocal.findAll();

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void fetchAttentionModuleByMonitor(Monitor object) {

		try {

			Monitor temporal = monitorFacadeLocal.fetchAttentionModuleByMonitor(object.getMonitorPk());

			if (temporal != null) {
				this.monitor = temporal;
				attentionModuleSelectedList = new ArrayList<>(temporal.getAttentionModuleSet());
				objectSelected = true;
			} else {
				objectSelected = false;
			}

		} catch (Exception exception) {

			System.out.println("Can't fetch attention by user!");

		}

	}

	public void save() {

		if (objectSelected) {

			try {

				monitor.setAttentionModuleSet(new HashSet<>(attentionModuleSelectedList));

				monitorFacadeLocal.edit(monitor);

				resetVariables();

				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar un monitor", null, FacesMessage.SEVERITY_WARN);

		}

	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public AttentionModule getAttentionModule() {
		return attentionModule;
	}

	public void setAttentionModule(AttentionModule attentionModule) {
		this.attentionModule = attentionModule;
	}

	public Boolean getObjectSelected() {
		return objectSelected;
	}

	public void setObjectSelected(Boolean objectSelected) {
		this.objectSelected = objectSelected;
	}

	public List<Monitor> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<Monitor> monitorList) {
		this.monitorList = monitorList;
	}

	public List<Monitor> getMonitorSelectedList() {
		return monitorSelectedList;
	}

	public void setMonitorSelectedList(List<Monitor> monitorSelectedList) {
		this.monitorSelectedList = monitorSelectedList;
	}

	public List<AttentionModule> getAttentionModuleList() {
		return attentionModuleList;
	}

	public void setAttentionModuleList(List<AttentionModule> attentionModuleList) {
		this.attentionModuleList = attentionModuleList;
	}

	public List<AttentionModule> getAttentionModuleSelectedList() {
		return attentionModuleSelectedList;
	}

	public void setAttentionModuleSelectedList(List<AttentionModule> attentionModuleSelectedList) {
		this.attentionModuleSelectedList = attentionModuleSelectedList;
	}

	public AttentionModuleFacadeLocal getAttentionModuleFacadeLocal() {
		return AttentionModuleFacadeLocal;
	}

	public void setAttentionModuleFacadeLocal(AttentionModuleFacadeLocal attentionModuleFacadeLocal) {
		AttentionModuleFacadeLocal = attentionModuleFacadeLocal;
	}

	public MonitorFacadeLocal getMonitorFacadeLocal() {
		return monitorFacadeLocal;
	}

	public void setMonitorFacadeLocal(MonitorFacadeLocal monitorFacadeLocal) {
		this.monitorFacadeLocal = monitorFacadeLocal;
	}

}
