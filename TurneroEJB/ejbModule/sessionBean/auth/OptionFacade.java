/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.auth;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import entity.auth.Option;
import entity.auth.SystemUser;
import sessionBean.AbstractFacade;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class OptionFacade extends AbstractFacade<Option> implements OptionFacadeLocal {
	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public OptionFacade() {
		super(Option.class);
	}

	@Override
	public Option findByOption(String permiso) {
		try {

			return (Option) entityManager.createNamedQuery("Option.findByOption").setParameter("option", permiso)
					.getSingleResult();

		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findOptions(SystemUser user) {

		try {

			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("auth.find_options");

			storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedureQuery.setParameter(1, user.getIdSystemUser());
			storedProcedureQuery.execute();

			return (List<Object[]>) storedProcedureQuery.getResultList();

		} catch (Exception exception) {
			return null;
		}

	}

}
