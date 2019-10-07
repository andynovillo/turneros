/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.auth;

import java.util.List;

import javax.ejb.Local;

import entity.auth.SystemUser;

/**
 *
 * @author Bryan Valencia
 */
@Local
public interface SystemUserFacadeLocal {

	void create(SystemUser systemUser);

	void edit(SystemUser systemUser);

	void remove(SystemUser systemUser);

	SystemUser find(Object id);

	List<SystemUser> findAll();

	List<SystemUser> findRange(int[] range);

	int count();

	SystemUser findByUserAndPassword(String usuario, String pass);

	SystemUser fetchRolesByUser(Integer idSystemUser);

	SystemUser findByUserCode(String codSystemUser);

	SystemUser fetchInformationByUser(String usuario);

	SystemUser fetchServiceTypeByUser(Integer idSystemUser);

	List<SystemUser> findSystemUserByTicketService(Integer ticketServicePk);

	List<SystemUser> findSystemUserByServiceType(Integer serviceTypePk);

	List<SystemUser> findSystemUserByTicketServiceGroup(Integer idSystemUser);

	SystemUser fetchServiceTypePriorityByUser(Integer idSystemUser);

	SystemUser fetchServiceTypeByUserAndByAction(Integer idSystemUser, String action);

	SystemUser findByToken(String tkn);

	SystemUser fetchMngInfoForUser(Integer idSystemUser);

	String findFullNames(Integer idSystemUser);

	List<SystemUser> autoCompleteByUser(String query);

}
