package bean.ticket;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import entity.auth.SystemUser;
import sessionBean.ticket.TicketFacadeLocal;
import utilities.ColumnModel;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class StatisticTicketBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	/* Lists */

	private List<Object[]> objectList;
	private List<ColumnModel> columnModelList;

	/* Services */

	@Inject
	private TicketFacadeLocal ticketFacadeLocal;

	public StatisticTicketBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		objectList = null;

		try {

		} catch (Exception exception) {

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void findWaitingTicketListByUserService() {

		try {

			columnModelList = Utility.getColumnModelList("C-TE");

			SystemUser systemUser = (SystemUser) JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION);

			objectList = ticketFacadeLocal.findWaitingTicketListBySystemUserServiceType(systemUser.getIdSystemUser());

			if (objectList != null && !objectList.isEmpty()) {
				JSFUtility.addMessage(JSFUtility.DATA_FOUND, 1);
			} else {
				JSFUtility.addMessage(JSFUtility.DATA_NOT_FOUND, 2);
			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public List<Object[]> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object[]> objectList) {
		this.objectList = objectList;
	}

	public List<ColumnModel> getColumnModelList() {
		return columnModelList;
	}

	public void setColumnModelList(List<ColumnModel> columnModelList) {
		this.columnModelList = columnModelList;
	}

	public TicketFacadeLocal getTicketFacadeLocal() {
		return ticketFacadeLocal;
	}

	public void setTicketFacadeLocal(TicketFacadeLocal ticketFacadeLocal) {
		this.ticketFacadeLocal = ticketFacadeLocal;
	}

}
