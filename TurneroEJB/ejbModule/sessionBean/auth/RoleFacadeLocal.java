/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.auth;

import java.util.List;

import javax.ejb.Local;

import entity.auth.Role;

/**
 *
 * @author Bryan Valencia
 */
@Local
public interface RoleFacadeLocal {

	void create(Role role);

	void edit(Role role);

	void remove(Role role);

	Role find(Object id);

	List<Role> findAll();

	List<Role> findRange(int[] range);

	int count();

	Role fetchOptionsByRole(String role);

	Role findByRole(String role);

}
