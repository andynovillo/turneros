/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.auth;

import java.util.List;

import javax.ejb.Local;

import entity.auth.Option;
import entity.auth.SystemUser;

/**
 *
 * @author Bryan Valencia
 */
@Local
public interface OptionFacadeLocal {

	void create(Option option);

	void edit(Option option);

	void remove(Option option);

	Option find(Object id);

	List<Option> findAll();

	List<Option> findRange(int[] range);

	int count();

	Option findByOption(String permiso);

	List<Object[]> findOptions(SystemUser systemUser);

}
