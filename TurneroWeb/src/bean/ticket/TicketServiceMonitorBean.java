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
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.ticket.Monitor;
import entity.ticket.TicketService;
import sessionBean.ticket.MonitorFacadeLocal;
import sessionBean.ticket.TicketServiceFacadeLocal;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@Named
@ViewScoped
public class TicketServiceMonitorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private TicketService ticketService;
	private Monitor monitor;

	private Boolean objectSelected;

	/* Lists */

	private List<Monitor> monitorList;
	private List<Monitor> monitorSelectedList;
	private List<TicketService> ticketServiceList;

	/* Services */

	@Inject
	private TicketServiceFacadeLocal ticketServiceFacadeLocal;
	@Inject
	private MonitorFacadeLocal monitorFacadeLocal;

	public TicketServiceMonitorBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		objectSelected = false;

		try {

			monitorList = monitorFacadeLocal.findAll();
			ticketServiceList = ticketServiceFacadeLocal.findAll();

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void fetchMonitorByTicketService(TicketService object) {

		try {

			TicketService temporal = ticketServiceFacadeLocal.fetchMonitorByTicketService(object.getTicketServicePk());

			if (temporal != null) {
				this.ticketService = temporal;
				monitorSelectedList = new ArrayList<>(temporal.getMonitorSet());
				objectSelected = true;
			} else {
				objectSelected = false;
			}

		} catch (Exception exception) {

			System.out.println("Can't fetch services by user!");

		}

	}

	public void save() {

		if (objectSelected) {

			try {

				ticketService.setMonitorSet(new HashSet<>(monitorSelectedList));

				ticketServiceFacadeLocal.edit(ticketService);

				resetVariables();

				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

			} catch (Exception exception) {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} else {

			JSFUtility.addMessage("Debe seleccionar un servicio", 2);

		}

	}

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
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

	public List<TicketService> getTicketServiceList() {
		return ticketServiceList;
	}

	public void setTicketServiceList(List<TicketService> ticketServiceList) {
		this.ticketServiceList = ticketServiceList;
	}

	public TicketServiceFacadeLocal getTicketServiceFacadeLocal() {
		return ticketServiceFacadeLocal;
	}

	public void setTicketServiceFacadeLocal(TicketServiceFacadeLocal ticketServiceFacadeLocal) {
		this.ticketServiceFacadeLocal = ticketServiceFacadeLocal;
	}

	public MonitorFacadeLocal getMonitorFacadeLocal() {
		return monitorFacadeLocal;
	}

	public void setMonitorFacadeLocal(MonitorFacadeLocal monitorFacadeLocal) {
		this.monitorFacadeLocal = monitorFacadeLocal;
	}

}
