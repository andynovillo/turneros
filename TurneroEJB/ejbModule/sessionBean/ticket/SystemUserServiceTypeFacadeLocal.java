/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.SystemUserServiceType;

/**
 *
 * @author BV
 */
@Local
public interface SystemUserServiceTypeFacadeLocal {

	void create(SystemUserServiceType systemUserServiceType);

	void edit(SystemUserServiceType systemUserServiceType);

	void remove(SystemUserServiceType systemUserServiceType);

	SystemUserServiceType find(Object id);

	List<SystemUserServiceType> findAll();

	List<SystemUserServiceType> findRange(int[] range);

	int count();

	List<SystemUserServiceType> findSystemUserServiceTypeByUserAndByAction(List<Integer> ids, String tckTodo);

}
