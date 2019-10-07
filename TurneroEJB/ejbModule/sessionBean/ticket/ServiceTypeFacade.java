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

import entity.ticket.ServiceType;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class ServiceTypeFacade extends AbstractFacade<ServiceType> implements ServiceTypeFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public ServiceTypeFacade() {
		super(ServiceType.class);
	}

	@Override
	public ServiceType fetchAttentionModuleByServiceType(Integer serviceTypePk) {
		try {
			return (ServiceType) entityManager.createNamedQuery("ServiceType.fetchAttentionModuleByServiceType")
					.setParameter("serviceTypePk", serviceTypePk).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceType> findServiceTypeByTicketServiceGroup(Integer idSystemUser) {
		try {
			return entityManager.createNamedQuery("ServiceType.findServiceTypeByTicketServiceGroup")
					.setParameter("idSystemUser", idSystemUser).getResultList();
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceType> findByTicketService(int pk) {
		try {
			return entityManager.createNamedQuery("ServiceType.findByTicketService").setParameter("ticketServicePk", pk)
					.getResultList();
		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceType> findServiceTypeByKiosk(String ip) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_service_type_by_kiosk", ServiceType.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, ip);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

}
