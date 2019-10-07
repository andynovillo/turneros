/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.auth;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.auth.Role;
import sessionBean.AbstractFacade;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class RoleFacade extends AbstractFacade<Role> implements RoleFacadeLocal {
	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public RoleFacade() {
		super(Role.class);
	}

	@Override
	public Role fetchOptionsByRole(String role) {
		try {
			return (Role) entityManager
					.createQuery("SELECT distinct tipoUsuario FROM Role tipoUsuario "
							+ "join fetch tipoUsuario.optionSet permisos WHERE tipoUsuario.role = :role")
					.setParameter("role", role).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public Role findByRole(String role) {
		try {
			return (Role) entityManager.createNamedQuery("Role.findByRole").setParameter("role", role)
					.getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

}
