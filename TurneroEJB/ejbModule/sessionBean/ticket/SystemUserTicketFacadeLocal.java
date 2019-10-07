/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.SystemUserTicket;

/**
 *
 * @author BV
 */
@Local
public interface SystemUserTicketFacadeLocal {

    void create(SystemUserTicket systemUserTicket);

    void edit(SystemUserTicket systemUserTicket);

    void remove(SystemUserTicket systemUserTicket);

    SystemUserTicket find(Object id);

    List<SystemUserTicket> findAll();

    List<SystemUserTicket> findRange(int[] range);

    int count();
    
}
