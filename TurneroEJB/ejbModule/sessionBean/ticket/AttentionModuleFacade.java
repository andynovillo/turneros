/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import entity.ticket.AttentionModule;
import entity.ticket.ServiceType;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class AttentionModuleFacade extends AbstractFacade<AttentionModule> implements AttentionModuleFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public AttentionModuleFacade() {
		super(AttentionModule.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttentionModule> findAttentionModuleByUser(Integer systemUserPk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_attention_module_by_user", AttentionModule.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public List<AttentionModule> findAttentionModuleByServiceTypePk(Integer serviceTypePk) {
		try {

			ServiceType temporal = (ServiceType) entityManager
					.createNamedQuery("ServiceType.findAttentionModuleByServiceTypePk")
					.setParameter("serviceTypePk", serviceTypePk).getSingleResult();

			return new ArrayList<>(temporal.getAttentionModuleSet());

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttentionModule> findByDescription(String dependencyCode) {
		try {

			return (List<AttentionModule>) entityManager.createNamedQuery("AttentionModule.findByDescription")
					.setParameter("description", dependencyCode).getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttentionModule> findLikeDescription(String string) {
		try {

			return (List<AttentionModule>) entityManager.createNamedQuery("AttentionModule.findLikeDescription")
					.setParameter("description", string).getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

}
