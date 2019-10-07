/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.Monitor;

/**
 *
 * @author BV
 */
@Local
public interface MonitorFacadeLocal {

	void create(Monitor monitor);

	void edit(Monitor monitor);

	void remove(Monitor monitor);

	Monitor find(Object id);

	List<Monitor> findAll();

	List<Monitor> findRange(int[] range);

	int count();

	Monitor findByIP(String ip);

	Monitor fetchAttentionModuleByMonitor(Integer monitorPk);

	List<Monitor> findMonitorGroupByAttModule(Integer attentionModulePk);

}
