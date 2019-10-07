/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

import authEntities.SystemUser;
import entity.ticket.Ticket;
import utilities.ResultData;

/**
 *
 * @author BV
 */
@Local
public interface TicketFacadeLocal {

	void create(Ticket ticket);

	void edit(Ticket ticket);

	void remove(Ticket ticket);

	Ticket find(Object id);

	List<Ticket> findAll();

	List<Ticket> findRange(int[] range);

	int count();

	Ticket findNextTicketByServiceType(Integer systemUserPk, Integer serviceTypePk);

	Ticket findNextTicketByUserAndByPriority(Integer systemUserPk);

	Ticket findNextTicketByUserAndByAlternativePriority(Integer systemUserPk, String priority);

	List<Object[]> findWaitingTicketGroupedListBySystemUserServiceType(Integer systemUserPk);

	List<Object[]> findCalledTicketListBySystemUserServiceType(Integer systemUserPk);

	List<Object[]> findCalledTicketListByTicketService(String address);

	List<Object[]> findWaitingTicketListBySystemUserServiceType(Integer idSystemUser);

	ResultData saveScheduledTicket(HashMap<String, Object> data);

	List<Object[]> findScheduledTicketBySystemUser(Date startDate, Date endDate, Integer idSystemUser);

	List<Object[]> findScheduledTicketByServiceType(Date startDate, Date endDate, Integer idSystemUser);

	List<Ticket> findScheduledTicketByPatientAndService(String code, Integer userPk);

	List<Object[]> findMonitorInfoScheduledTicketByServiceType(String ip);

	List<Ticket> findScheduledTicketByPatient(String code);

	Integer findNextTicketNumberByServiceType(Integer serviceTypePk);

	List<Object[]> findAvailableTicket(Integer serviceTypePk, Integer idSystemUser, Date date, String evalType);

	ResultData checkTicketAvailability(Integer service, Integer attentionModule, Date date, String schedule,
			String evalType);

	ResultData checkTicketCollision(String string);

	ResultData checkTicketOrder(String orders);

	/* Imagenología */

	List<Object[]> findImgScheduledTicketByPatientAndService(String code, Integer userPk);

	Object[] findImgScheduledTicket(Integer ticketPk, String code, Integer idSystemUser);

	/* Fisiatría */

	List<Object[]> findPhyScheduledTicketByPatientAndService(String code, Integer servicePk);

	List<Object[]> findPhyScheduledTicketByServiceType(Date startDate, Date endDate, Integer servicePk);

	List<Object[]> findPhyScheduledTicketBySystemUser(Date startDate, Date endDate, Integer idSystemUser);

	List<Object[]> findPhyClosestAvailableApptInfo(String string);

	SystemUser findTicketUser(Integer pk, String type);

	List<Ticket> findGeneratedTicket(String code, Integer ticketServicePk);

	List<Object[]> findTicketProduction(Date startDate, Date endDate, Integer ticketServicePk, String actionParameter,
			String queryParameter);

	Ticket findLastCalledTicketNonFinishedByUser(Integer idSystemUser);

	List<Object[]> findTicketDetailByTicketService(Date from, Date to, Integer ticketServicePk);

	ResultData saveTicketHeader(HashMap<String, Object> data);

	ResultData saveTicketDetail(HashMap<String, Object> data);

	List<Object[]> findTicketHeaderByCode(String code);

	/* Lab. Genética y Molecular */

	List<Object[]> findLabGMScheduledTicketByPatientAndService(String code, Integer userPk);

	Object[] findLabGMScheduledTicket(Integer ticketPk, String code, Integer idSystemUser);

	/* Traza */

	List<Object[]> findTicketTraceByCodeAndByTicketService(String code, Integer ticketServicePk);

}
