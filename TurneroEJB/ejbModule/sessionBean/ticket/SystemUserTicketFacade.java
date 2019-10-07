/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.ticket.SystemUserTicket;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class SystemUserTicketFacade extends AbstractFacade<SystemUserTicket> implements SystemUserTicketFacadeLocal {

    @PersistenceContext(unitName = "EntityGeneratorPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public SystemUserTicketFacade() {
        super(SystemUserTicket.class);
    }
    
}
