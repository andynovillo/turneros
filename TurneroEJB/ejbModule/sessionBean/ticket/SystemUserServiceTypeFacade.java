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

import entity.ticket.SystemUserServiceType;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class SystemUserServiceTypeFacade extends AbstractFacade<SystemUserServiceType>
		implements SystemUserServiceTypeFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public SystemUserServiceTypeFacade() {
		super(SystemUserServiceType.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUserServiceType> findSystemUserServiceTypeByUserAndByAction(List<Integer> ids, String action) {

		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(
					"ticket.find_system_user_service_type_by_user_and_by_action", SystemUserServiceType.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, ids.toString().replace("[", "").replaceAll("]", ""));
			storedProcedure.setParameter(2, action);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}

	}

}
