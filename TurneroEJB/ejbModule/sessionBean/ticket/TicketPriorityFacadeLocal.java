/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.TicketPriority;

/**
 *
 * @author BV
 */
@Local
public interface TicketPriorityFacadeLocal {

	void create(TicketPriority ticketPriority);

	void edit(TicketPriority ticketPriority);

	void remove(TicketPriority ticketPriority);

	TicketPriority find(Object id);

	List<TicketPriority> findAll();

	List<TicketPriority> findRange(int[] range);

	int count();

	TicketPriority findBySystemUser(Integer idSystemUser);

	List<TicketPriority> findServiceTypePriorityByTicketServiceGroup(Integer idSystemUser);

}
