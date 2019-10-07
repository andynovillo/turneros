/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.ticket.TicketPriority;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class TicketPriorityFacade extends AbstractFacade<TicketPriority> implements TicketPriorityFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public TicketPriorityFacade() {
		super(TicketPriority.class);
	}

	@Override
	public TicketPriority findBySystemUser(Integer idSystemUser) {

		try {
			return (TicketPriority) entityManager.createNamedQuery("TicketPriority.findBySystemUser")
					.setParameter("idSystemUser", idSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TicketPriority> findServiceTypePriorityByTicketServiceGroup(Integer idSystemUser) {
		try {
			return entityManager.createNamedQuery("TicketPriority.findTicketPriorityByTicketServiceGroup")
					.setParameter("idSystemUser", idSystemUser).getResultList();
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

}
