/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;

import entity.auth.SystemUser;
import entity.ticket.Ticket;
import sessionBean.AbstractFacade;
import sessionBean.pub.ParameterFacadeLocal;
import utility.Constant;

/**
 *
 * @author BV
 */
@Stateless
public class TicketFacade extends AbstractFacade<Ticket> implements TicketFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public TicketFacade() {
		super(Ticket.class);
	}

	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;

	@Resource
	private EJBContext context;

	@Override
	public Ticket findNextTicketByServiceType(Integer systemUserPk, Integer serviceTypePk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_by_service_type", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);
			storedProcedure.setParameter(2, serviceTypePk);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findNextTicketByUserAndByPriority(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_by_user_and_by_priority", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findNextTicketByUserAndByAlternativePriority(Integer systemUserPk, String priority) {

		if (priority == null || priority.isEmpty())
			return null;

		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(
					"ticket.find_next_ticket_by_user_and_by_alternative_priority", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);
			storedProcedure.setParameter(2, priority);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findWaitingTicketGroupedListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.waiting_ticket_grouped_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCalledTicketListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.called_ticket_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCalledTicketListByTicketService(String address) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.called_ticket_list_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, address);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findWaitingTicketListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.waiting_ticket_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Integer findNextTicketNumberByServiceType(Integer serviceTypePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_number_by_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, serviceTypePk);

			storedProcedure.execute();

			return (Integer) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findAvailableTicket(Integer service, Integer attModOrUser, Date date, String evalType) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_available_ticket");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, service);
			storedProcedure.setParameter(2, attModOrUser);
			storedProcedure.setParameter(3, date);
			storedProcedure.setParameter(4, evalType != null ? evalType : Constant.TCK_AG_SALA);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public SystemUser findTicketUser(Integer pk, String type) {

		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("ticket.find_ticket_user",
					SystemUser.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, pk);
			storedProcedure.setParameter(2, type);
			storedProcedure.execute();

			return (SystemUser) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> findGeneratedTicket(String code, Integer ticketServicePk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_generated_ticket", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, ticketServicePk);
			storedProcedure.execute();

			return (List<Ticket>) storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findTicketProduction(Date startDate, Date endDate, Integer ticketServicePk,
			String actionParameter, String queryParameter) {

		try {

			String query = parameterFacadeLocal.findByParameter("QRY-TR-P-" + queryParameter).getValue();

			return entityManager.createNativeQuery(query).setParameter(1, startDate, TemporalType.DATE)
					.setParameter(2, endDate, TemporalType.DATE).setParameter(3, ticketServicePk)
					.setParameter(4, actionParameter).getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findLastCalledTicketNonFinishedByUser(Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_last_called_ticket_non_finished", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, idSystemUser);
			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findTicketDetailByTicketService(Date from, Date to, Integer ticketServicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.ticket_detail_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, from);
			storedProcedure.setParameter(2, to);
			storedProcedure.setParameter(3, ticketServicePk);
			storedProcedure.execute();

			return (List<Object[]>) storedProcedure.getResultList();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}
	}

}
