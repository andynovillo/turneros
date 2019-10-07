/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.AttentionModule;
import entity.ticket.TicketService;

/**
 *
 * @author BV
 */
@Local
public interface TicketServiceFacadeLocal {

	void create(TicketService ticketService);

	void edit(TicketService ticketService);

	void remove(TicketService ticketService);

	TicketService find(Object id);

	List<TicketService> findAll();

	List<TicketService> findRange(int[] range);

	int count();

	List<TicketService> findByUser(Integer idSystemUser);

	TicketService fetchMonitorByTicketService(Integer ticketServicePk);

	List<AttentionModule> findAttentionModuleByTicketService(Integer valueOf);

}
