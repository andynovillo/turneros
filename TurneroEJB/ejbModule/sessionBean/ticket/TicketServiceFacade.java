/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import entity.ticket.AttentionModule;
import entity.ticket.TicketService;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class TicketServiceFacade extends AbstractFacade<TicketService> implements TicketServiceFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public TicketServiceFacade() {
		super(TicketService.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TicketService> findByUser(Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_ticket_service_by_user", TicketService.class);
			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, idSystemUser);
			storedProcedure.execute();
			return storedProcedure.getResultList();

		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public TicketService fetchMonitorByTicketService(Integer ticketServicePk) {
		try {
			return (TicketService) entityManager.createNamedQuery("TicketService.fetchMonitorByTicketService")
					.setParameter("ticketServicePk", ticketServicePk).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttentionModule> findAttentionModuleByTicketService(Integer ticketServicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(
					"ticket.find_attention_module_by_ticket_service", AttentionModule.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, ticketServicePk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {
			return null;
		}

	}

}
