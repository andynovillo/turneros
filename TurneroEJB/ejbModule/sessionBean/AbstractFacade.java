/*
 * To change this license header, choose License Headers in Project Properooties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.wsdl.OperationType;

/**
 *
 * @author Bryan
 */
public abstract class AbstractFacade<T> {
	private Class<T> entityClass;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public void create(T entity) {
		getEntityManager().persist(entity);
	}

	public void edit(T entity) {
		getEntityManager().merge(entity);
	}

	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
		criteriaQuery.select(criteriaQuery.from(entityClass));
		return getEntityManager().createQuery(criteriaQuery).getResultList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllActive() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), true));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllInactive() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), false));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllActiveOrderedAsc() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), true));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("tableOrder")));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllActiveOrderedDsc() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), true));
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("tableOrder")));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllInactiveOrderedAsc() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), false));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("tableOrder")));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllInactiveOrderedDsc() {

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<OperationType> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("tableStatus"), false));
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("tableOrder")));
		return getEntityManager().createQuery(criteriaQuery).getResultList();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
		criteriaQuery.select(criteriaQuery.from(entityClass));
		javax.persistence.Query query = getEntityManager().createQuery(criteriaQuery);
		query.setMaxResults(range[1] - range[0] + 1);
		query.setFirstResult(range[0]);
		return query.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int count() {
		javax.persistence.criteria.CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(getEntityManager().getCriteriaBuilder().count(root));
		javax.persistence.Query query = getEntityManager().createQuery(criteriaQuery);
		return ((Long) query.getSingleResult()).intValue();
	}

}
