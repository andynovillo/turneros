/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.pub;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.pub.Parameter;
import sessionBean.AbstractFacade;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class ParameterFacade extends AbstractFacade<Parameter> implements ParameterFacadeLocal {
	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public ParameterFacade() {
		super(Parameter.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Parameter> findAll() {
		try {

			return (List<Parameter>) entityManager.createNamedQuery("Parameter.findAll").getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Parameter findByParameter(String parameter) {
		try {

			return (Parameter) entityManager.createNamedQuery("Parameter.findByParam").setParameter("param", parameter)
					.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public String getValueByParameter(String string) {
		try {

			Parameter parameter = (Parameter) entityManager.createNamedQuery("Parameter.findByParam")
					.setParameter("param", string).getSingleResult();

			if (parameter != null) {
				return parameter.getValue();
			} else {
				return null;
			}

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public void setValueByParameter(String string, String result) {

		try {

			Parameter parameter = findByParameter(string);

			if (parameter != null) {

				parameter.setValue(result);
				entityManager.merge(parameter);

			} else {

				System.err.println(String.format("Error: Parameter not found, can't define value %s to parameter %s!",
						result, string));

			}

		} catch (Exception exception) {

			System.err.println(String.format("Error: Can't define value %s to parameter %s! Exception: %s!", result,
					string, exception.getMessage()));

		}

	}

}
