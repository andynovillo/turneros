/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.ticket.Monitor;
import sessionBean.AbstractFacade;

/**
 *
 * @author BV
 */
@Stateless
public class MonitorFacade extends AbstractFacade<Monitor> implements MonitorFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public MonitorFacade() {
		super(Monitor.class);
	}

	@Override
	public Monitor findByIP(String ip) {
		try {

			return (Monitor) entityManager.createNamedQuery("Monitor.findByAddress").setParameter("address", ip)
					.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public Monitor fetchAttentionModuleByMonitor(Integer monitorPk) {
		try {

			return (Monitor) entityManager.createNamedQuery("Monitor.fetchAttentionModuleByMonitor")
					.setParameter("monitorPk", monitorPk).getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Monitor> findMonitorGroupByAttModule(Integer attentionModulePk) {
		try {

			return (List<Monitor>) entityManager.createNamedQuery("Monitor.findMonitorGroupByAttModule")
					.setParameter("attentionModulePk", attentionModulePk).getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

}
