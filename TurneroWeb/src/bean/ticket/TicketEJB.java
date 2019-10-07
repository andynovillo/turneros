package bean.ticket;

import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import entity.auth.SystemUser;
import entity.ticket.AttentionModule;
import entity.ticket.ServiceType;
import entity.ticket.SystemUserTicket;
import entity.ticket.Ticket;
import entity.ticket.TicketPriority;
import sessionBean.pub.ParameterFacadeLocal;
import sessionBean.ticket.AreaFacadeLocal;
import sessionBean.ticket.ServiceTypeFacadeLocal;
import sessionBean.ticket.SystemUserTicketFacadeLocal;
import sessionBean.ticket.TicketFacadeLocal;
import sessionBean.ticket.TicketPriorityFacadeLocal;
import sessionBean.ticket.TicketServiceFacadeLocal;
import utility.MethodName;
import utility.Utility;

@Startup
@Singleton
@Lock(LockType.READ)
public class TicketEJB {

	/* Fields */

	/* Services */

	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;
	@Inject
	private AreaFacadeLocal areaFacadeLocal;
	@Inject
	private TicketServiceFacadeLocal ticketServiceFacadeLocal;
	@Inject
	private ServiceTypeFacadeLocal serviceTypeFacadeLocal;
	@Inject
	private TicketPriorityFacadeLocal ticketPriorityFacadeLocal;
	@Inject
	private TicketFacadeLocal ticketFacadeLocal;
	@Inject
	private SystemUserTicketFacadeLocal systemUserTicketFacadeLocal;

	@Resource
	private EJBContext context;

	/* Methods */

	@PostConstruct
	public void atStartup() {

	}

	@Lock(LockType.WRITE)
	public Ticket generateTicket(HashMap<String, Object> ticketData) {

		try {

			ServiceType serviceType = (ServiceType) ticketData.get("serviceType");
			String address = (String) ticketData.get("address");
			SystemUser systemUser = (SystemUser) ticketData.get("systemUser");

			Ticket ticket = null;

			Integer ticketNumber = 0;

			ticketNumber = ticketFacadeLocal.findNextTicketNumberByServiceType(serviceType.getServiceTypePk());

			StringBuilder label = new StringBuilder();

			if (serviceType.getTicketServiceFk().getAbbreviationPrinted())
				label.append(serviceType.getTicketServiceFk().getAbbreviation()).append("-");

			label.append(serviceType.getAbbreviation()).append("-").append(ticketNumber);

			ticket = new Ticket(label.toString());

			ticket.setServiceTypeFk(serviceType);

			ticketFacadeLocal.create(ticket);

			SystemUserTicket systemUserTicket = new SystemUserTicket();

			systemUserTicket.setAddress(address);
			systemUserTicket.setAction(parameterFacadeLocal.findByParameter("TICKET-ACTION-GENERATE").getValue());
			systemUserTicket.setDateTime(ticket.getGenerationDateTime());
			systemUserTicket.setTicketFk(ticket);
			systemUserTicket.setSystemUserFk(systemUser);

			systemUserTicketFacadeLocal.create(systemUserTicket);

			return ticket;

		} catch (Exception exception) {

			Utility.printError(this.getClass().getName(), MethodName.methodName(), "Error generating ticket!",
					exception);
			context.setRollbackOnly();
			return null;

		}

	}

	@Lock(LockType.WRITE)
	public Ticket findTicket(HashMap<String, Object> ticketData) {

		try {

			ServiceType serviceType = (ServiceType) ticketData.get("serviceType");
			SystemUser systemUser = (SystemUser) ticketData.get("systemUser");

			Ticket ticket = null;

			if (serviceType != null) {
				if (!serviceType.getScheduled()) {

					// find manual ticket
					ticket = ticketFacadeLocal.findNextTicketByServiceType(systemUser.getIdSystemUser(),
							serviceType.getServiceTypePk());

				} else {
					// TODO find scheduled ticket
				}
			} else {
				// find manual ticket

				TicketPriority ticketPriority = ticketPriorityFacadeLocal
						.findBySystemUser(systemUser.getIdSystemUser());

				if (ticketPriority != null) {
					ticket = ticketFacadeLocal.findNextTicketByUserAndByAlternativePriority(
							systemUser.getIdSystemUser(), ticketPriority.getServicePriority());
				} else {
					ticket = ticketFacadeLocal.findNextTicketByUserAndByPriority(systemUser.getIdSystemUser());
				}
				// TODO find scheduled ticket
			}

			if (ticket != null) {

				AttentionModule attentionModule = (AttentionModule) ticketData.get("attentionModule");
				String address = (String) ticketData.get("address");

				ticket.setAttentionModuleFk(attentionModule.getAttentionModulePk());
				ticket.setAttentionModule(attentionModule.getLabel());
				ticket.setCalled(true);
				ticket.setCallDateTime(Calendar.getInstance().getTime());
				ticket.setAttended(true);

				ticketFacadeLocal.edit(ticket);

				SystemUserTicket systemUserTicket = new SystemUserTicket();

				systemUserTicket.setAddress(address);
				systemUserTicket.setAction(parameterFacadeLocal.findByParameter("TICKET-ACTION-CALL").getValue());
				systemUserTicket.setDateTime(ticket.getCallDateTime());
				systemUserTicket.setTicketFk(ticket);
				systemUserTicket.setSystemUserFk(systemUser);

				systemUserTicketFacadeLocal.create(systemUserTicket);

			}

			return ticket;

		} catch (Exception exception) {

			Utility.printError(this.getClass().getName(), MethodName.methodName(), "Error finding ticket!", exception);
			context.setRollbackOnly();
			return null;

		}
	}

	public ParameterFacadeLocal getParameterFacadeLocal() {
		return parameterFacadeLocal;
	}

	public void setParameterFacadeLocal(ParameterFacadeLocal parameterFacadeLocal) {
		this.parameterFacadeLocal = parameterFacadeLocal;
	}

	public AreaFacadeLocal getAreaFacadeLocal() {
		return areaFacadeLocal;
	}

	public void setAreaFacadeLocal(AreaFacadeLocal areaFacadeLocal) {
		this.areaFacadeLocal = areaFacadeLocal;
	}

	public TicketServiceFacadeLocal getTicketServiceFacadeLocal() {
		return ticketServiceFacadeLocal;
	}

	public void setTicketServiceFacadeLocal(TicketServiceFacadeLocal ticketServiceFacadeLocal) {
		this.ticketServiceFacadeLocal = ticketServiceFacadeLocal;
	}

	public ServiceTypeFacadeLocal getServiceTypeFacadeLocal() {
		return serviceTypeFacadeLocal;
	}

	public void setServiceTypeFacadeLocal(ServiceTypeFacadeLocal serviceTypeFacadeLocal) {
		this.serviceTypeFacadeLocal = serviceTypeFacadeLocal;
	}

	public TicketFacadeLocal getTicketFacadeLocal() {
		return ticketFacadeLocal;
	}

	public void setTicketFacadeLocal(TicketFacadeLocal ticketFacadeLocal) {
		this.ticketFacadeLocal = ticketFacadeLocal;
	}

	public SystemUserTicketFacadeLocal getSystemUserTicketFacadeLocal() {
		return systemUserTicketFacadeLocal;
	}

	public void setSystemUserTicketFacadeLocal(SystemUserTicketFacadeLocal systemUserTicketFacadeLocal) {
		this.systemUserTicketFacadeLocal = systemUserTicketFacadeLocal;
	}

	public EJBContext getContext() {
		return context;
	}

	public void setContext(EJBContext context) {
		this.context = context;
	}

}
