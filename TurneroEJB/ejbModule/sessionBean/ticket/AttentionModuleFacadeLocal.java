/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.AttentionModule;

/**
 *
 * @author BV
 */
@Local
public interface AttentionModuleFacadeLocal {

	void create(AttentionModule attentionModule);

	void edit(AttentionModule attentionModule);

	void remove(AttentionModule attentionModule);

	AttentionModule find(Object id);

	List<AttentionModule> findAll();

	List<AttentionModule> findRange(int[] range);

	int count();

	List<AttentionModule> findAttentionModuleByServiceTypePk(Integer serviceTypePk);

	List<AttentionModule> findAttentionModuleByUser(Integer idSystemUser);

	List<AttentionModule> findByDescription(String dependencyCode);

	List<AttentionModule> findLikeDescription(String string);

}
