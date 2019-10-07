package bean.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

import entity.ticket.TicketService;
import sessionBean.pub.ParameterFacadeLocal;
import sessionBean.ticket.TicketFacadeLocal;
import utilities.ColumnModel;
import utilities.ListToExcel;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class TicketStatisticBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Fields */

	private Date startDate;
	private Date endDate;
	private String action;

	private TicketService ticketService;

	/* Lists */

	private List<Object[]> objectList;
	private List<ColumnModel> columnModelList;

	/* Services */

	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;
	@Inject
	private TicketFacadeLocal ticketFacadeLocal;

	/*
	 * Methods
	 */

	public TicketStatisticBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		ticketService = new TicketService();

		columnModelList = new ArrayList<>();

		try {

		} catch (Exception exception) {

		}

	}

	public void resetForm() {

		resetVariables();
		JSFUtility.reset("form");

	}

	public void findTicketProduction() {

		try {

			String actionParameter = parameterFacadeLocal.findByParameter(action).getValue();
			String queryParameter = ticketService.getAbbreviation();

			columnModelList = Utility.getColumnModelList("COL-TR-P-" + queryParameter);
			objectList = ticketFacadeLocal.findTicketProduction(startDate, endDate, ticketService.getTicketServicePk(),
					actionParameter, queryParameter);

			if (!CollectionUtils.isEmpty(objectList)) {
				JSFUtility.addMessage(JSFUtility.DATA_FOUND, 1);
			} else {
				JSFUtility.addMessage(JSFUtility.DATA_NOT_FOUND, 2);
			}

		} catch (Exception exception) {
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);
		}

	}

	public void exportTicketProduction() {

		if (columnModelList != null)
			ListToExcel.generateExcel(objectList, columnModelList, "Producción", "Producción.xlsx",
					JSFUtility.getFullNamesFromUserInSession());

	}

	public void prepareTicketProduction() {

		findTicketProduction();
		exportTicketProduction();

	}

	public void onDateSelect(SelectEvent event) {
		Integer month = Integer.parseInt(event.getComponent().getAttributes().get("month").toString());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) event.getObject());
		calendar.add(Calendar.MONTH, month);
		endDate = calendar.getTime();
	}

	public void findTicketDetailByTicketService() {

		try {

			columnModelList = Utility.getColumnModelList("COL-TR-DT");
			objectList = ticketFacadeLocal.findTicketDetailByTicketService(startDate, endDate,
					ticketService.getTicketServicePk());

			if (!CollectionUtils.isEmpty(objectList)) {
				JSFUtility.addMessage(JSFUtility.DATA_FOUND, 1);
			} else {
				JSFUtility.addMessage(JSFUtility.DATA_NOT_FOUND, 2);
			}

		} catch (Exception exception) {
			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);
		}

	}

	public void exportTicketDetail() {

		if (columnModelList != null)
			ListToExcel.generateExcel(objectList, columnModelList, "Detalle", "Detalle de turnos.xlsx",
					JSFUtility.getFullNamesFromUserInSession());

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public ParameterFacadeLocal getParameterFacadeLocal() {
		return parameterFacadeLocal;
	}

	public void setParameterFacadeLocal(ParameterFacadeLocal parameterFacadeLocal) {
		this.parameterFacadeLocal = parameterFacadeLocal;
	}

	public TicketFacadeLocal getTicketFacadeLocal() {
		return ticketFacadeLocal;
	}

	public void setTicketFacadeLocal(TicketFacadeLocal ticketFacadeLocal) {
		this.ticketFacadeLocal = ticketFacadeLocal;
	}

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

}
