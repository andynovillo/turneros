/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBean.ticket;

import java.util.List;

import javax.ejb.Local;

import entity.ticket.ServiceType;

/**
 *
 * @author BV
 */
@Local
public interface ServiceTypeFacadeLocal {

	void create(ServiceType serviceType);

	void edit(ServiceType serviceType);

	void remove(ServiceType serviceType);

	ServiceType find(Object id);

	List<ServiceType> findAll();

	List<ServiceType> findRange(int[] range);

	int count();

	ServiceType fetchAttentionModuleByServiceType(Integer serviceTypePk);

	List<ServiceType> findServiceTypeByTicketServiceGroup(Integer idSystemUser);

	List<ServiceType> findByTicketService(int i);

	List<ServiceType> findServiceTypeByKiosk(String ip);

}
