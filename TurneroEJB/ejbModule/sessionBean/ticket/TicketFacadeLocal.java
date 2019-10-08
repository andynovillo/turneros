/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import entity.auth.SystemUser;
import entity.ticket.Ticket;

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

	Integer findNextTicketNumberByServiceType(Integer serviceTypePk);

	List<Object[]> findAvailableTicket(Integer serviceTypePk, Integer idSystemUser, Date date, String evalType);

	SystemUser findTicketUser(Integer pk, String type);

	List<Ticket> findGeneratedTicket(String code, Integer ticketServicePk);

	List<Object[]> findTicketProduction(Date startDate, Date endDate, Integer ticketServicePk, String actionParameter,
			String queryParameter);

	Ticket findLastCalledTicketNonFinishedByUser(Integer idSystemUser);

	List<Object[]> findTicketDetailByTicketService(Date from, Date to, Integer ticketServicePk);

}
