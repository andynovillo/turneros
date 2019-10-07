/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.poll;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.poll.Respondent;
import sessionBean.AbstractFacade;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class RespondentFacade extends AbstractFacade<Respondent> implements RespondentFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public RespondentFacade() {
		super(Respondent.class);
	}

	@Override
	public Respondent findByCode(String code) {

		try {

			return (Respondent) entityManager.createNamedQuery("Respondent.findByCode").setParameter("code", code)
					.getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Respondent upsert(Respondent respondent) {

		Respondent temporalPatient = findByCode(respondent.getCode());

		if (temporalPatient == null) {

			respondent.setPersonPk(null);
			entityManager.persist(respondent);

		} else {

			respondent.setPersonPk(temporalPatient.getPersonPk());
			entityManager.merge(respondent);

		}

		return respondent;

	}

}
