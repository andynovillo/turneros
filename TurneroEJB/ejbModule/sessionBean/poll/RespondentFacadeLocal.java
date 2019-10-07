/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.poll;

import java.util.List;

import javax.ejb.Local;

import entity.poll.Respondent;

/**
 *
 * @author Bryan Valencia
 */
@Local
public interface RespondentFacadeLocal {

	void create(Respondent respondent);

	void edit(Respondent respondent);

	void remove(Respondent respondent);

	Respondent find(Object id);

	List<Respondent> findAll();

	List<Respondent> findRange(int[] range);

	int count();

	Respondent findByCode(String code);

	Respondent upsert(Respondent respondent);

}
