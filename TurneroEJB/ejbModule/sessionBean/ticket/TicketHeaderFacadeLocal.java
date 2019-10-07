/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.TicketHeader;

/**
 *
 * @author BV
 */
@Local
public interface TicketHeaderFacadeLocal {

    void create(TicketHeader ticketHeader);

    void edit(TicketHeader ticketHeader);

    void remove(TicketHeader ticketHeader);

    TicketHeader find(Object id);

    List<TicketHeader> findAll();

    List<TicketHeader> findRange(int[] range);

    int count();
    
}
