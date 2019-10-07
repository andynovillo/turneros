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

import entity.auth.SystemUser;
import sessionBean.AbstractFacade;
import utility.Constant;

/**
 *
 * @author Bryan Valencia
 */
@Stateless
public class SystemUserFacade extends AbstractFacade<SystemUser> implements SystemUserFacadeLocal {
	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public SystemUserFacade() {
		super(SystemUser.class);
	}

	@Override
	public SystemUser findByUserAndPassword(String usuario, String pass) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.findUser")
					.setParameter("codSystemUser", usuario).setParameter("pass", pass).getSingleResult();
		} catch (Exception exception) {

			return null;
		}
	}

	@Override
	public SystemUser fetchRolesByUser(Integer idSystemUser) {
		try {
			return (SystemUser) entityManager
					.createQuery("SELECT distinct su FROM SystemUser su left join fetch su.roleSet rs "
							+ "WHERE su.idSystemUser = :idSystemUser")
					.setParameter("idSystemUser", idSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public SystemUser fetchInformationByUser(String usuario) {
		try {
			return (SystemUser) entityManager.createQuery(
					"SELECT distinct usuario FROM SystemUser usuario left join fetch usuario.roleSet roles "
							+ "left join fetch roles.optionSet permisos WHERE usuario.codSystemUser = :codSystemUser")
					.setParameter("codSystemUser", usuario).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public SystemUser findByUserCode(String codSystemUser) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.findByCodSystemUser")
					.setParameter("codSystemUser", codSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public SystemUser fetchServiceTypeByUser(Integer systemUserPk) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.findServiceTypeByUser")
					.setParameter("systemUserPk", systemUserPk).getSingleResult();
		} catch (Exception exception) {

			return null;
		}
	}

	@Override
	public SystemUser fetchServiceTypeByUserAndByAction(Integer systemUserPk, String action) {
		try {
			if (action.equals(Constant.TCK_GENERAR)) {
				return (SystemUser) entityManager.createNamedQuery("SystemUser.findCanGenerateServiceTypeByUser")
						.setParameter("systemUserPk", systemUserPk).getSingleResult();
			} else if (action.equals(Constant.TCK_LLAMAR)) {
				return (SystemUser) entityManager.createNamedQuery("SystemUser.findCanCallServiceTypeByUser")
						.setParameter("systemUserPk", systemUserPk).getSingleResult();
			} else {
				return fetchServiceTypeByUser(systemUserPk);
			}

		} catch (Exception exception) {

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> findAll() {
		try {
			return entityManager.createQuery(
					"select new SystemUser(s.idSystemUser,s.codSystemUser,s.nombresCompletos,s.mail,s.estado) from SystemUser s order by s.idSystemUser")
					.getResultList();
		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> findSystemUserByTicketService(Integer ticketServicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_system_user_by_ticket_service", SystemUser.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, ticketServicePk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> findSystemUserByServiceType(Integer serviceTypePk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_system_user_by_service_type", SystemUser.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, serviceTypePk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> findSystemUserByTicketServiceGroup(Integer idSystemUser) {
		try {
			return entityManager.createNamedQuery("SystemUser.findSystemUserByTicketServiceGroup")
					.setParameter("idSystemUser", idSystemUser).getResultList();
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	@Override
	public SystemUser fetchServiceTypePriorityByUser(Integer idSystemUser) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.fetchServiceTypePriorityByUser")
					.setParameter("idSystemUser", idSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public SystemUser findByToken(String tkn) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.findByToken").setParameter("tkn", tkn)
					.getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public SystemUser fetchMngInfoForUser(Integer idSystemUser) {
		try {
			return (SystemUser) entityManager.createNamedQuery("SystemUser.fetchMngInfoForUser")
					.setParameter("idSystemUser", idSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public String findFullNames(Integer idSystemUser) {
		try {
			return (String) entityManager.createNamedQuery("SystemUser.findFullNames")
					.setParameter("idSystemUser", idSystemUser).getSingleResult();
		} catch (Exception exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemUser> autoCompleteByUser(String query) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("auth.auto_complete_by_user", SystemUser.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, query);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

}
