package bean.ticket;

import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import AS400.AS400Bean;
import bean.generic.AS400GenericBean;
import entity.auth.SystemUser;
import entity.surgery.Patient;
import entity.ticket.Area;
import entity.ticket.AttentionModule;
import entity.ticket.Monitor;
import entity.ticket.ServiceType;
import entity.ticket.SystemUserServiceType;
import entity.ticket.Ticket;
import entity.ticket.TicketPriority;
import entity.ticket.TicketService;
import sessionBean.auth.SystemUserFacadeLocal;
import sessionBean.pub.ParameterFacadeLocal;
import sessionBean.ticket.AttentionModuleFacadeLocal;
import sessionBean.ticket.MonitorFacadeLocal;
import sessionBean.ticket.ServiceTypeFacadeLocal;
import sessionBean.ticket.SystemUserServiceTypeFacadeLocal;
import sessionBean.ticket.TicketFacadeLocal;
import sessionBean.ticket.TicketPriorityFacadeLocal;
import utilities.AppointmentWrapper;
import utilities.ColumnModel;
import utilities.GenericPrinterSocket;
import utilities.ResultData;
import utility.MethodName;
import utility.Utility;
import web.utility.JSFUtility;
import web.utility.JSFUtility.DialogType;

/**
 *
 * @author BryanV
 */
@ViewScoped
@Named
public class TicketBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int port = 9999;
	private static final String app = "Laboratory";

	/* Fields */

	private Patient patient;
	private Ticket ticket;
	private Socket clientSocket = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;
	private Boolean printed;
	private ServiceType serviceType;
	private TicketPriority ticketPriority;
	private Area area;
	private AttentionModule attentionModule;
	private String fontSize;
	private String htmlStyle;
	private String videoList;
	private Boolean videoEnabled;
	private Boolean ticketEnabled;
	private Integer updateInterval;
	private Date minDate;
	private SystemUser systemUser;
	private String dates;
	private String times;
	private Boolean compactView;
	private Date date;
	private Date startDate;
	private Date endDate;
	private String schedule;
	private TicketService ticketService;
	private Date scheduledDate;
	private Date scheduledTime;
	private ResultData resultData;
	private String action;
	private String address;
	private String code;

	/* Lists */

	private List<Ticket> ticketList;
	private List<Area> areaList;
	private List<ServiceType> serviceTypeList;
	private HashMap<TicketService, List<ServiceType>> serviceTypeByAreaMap;
	private HashMap<String, Object> data;
	private List<SystemUser> systemUserByServiceTypeList;
	private List<AttentionModule> attentionModuleList;
	private List<ServiceType> serviceTypeByUserList;
	private List<Object[]> calledTicketListByService;
	private List<Object[]> calledTicketListByArea;
	private List<Object[]> waitingTicketList;
	private List<Object[]> objectList;
	private List<ColumnModel> columnModelList;
	private List<AppointmentWrapper> appointmentWrapperList;

	/* Services */

	@Inject
	private SystemUserFacadeLocal systemUserFacadeLocal;
	@Inject
	private SystemUserServiceTypeFacadeLocal systemUserServiceTypeFacadeLocal;
	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;
	@Inject
	private TicketFacadeLocal ticketFacadeLocal;
	@Inject
	private ServiceTypeFacadeLocal serviceTypeFacadeLocal;
	@Inject
	private AttentionModuleFacadeLocal attentionModuleFacadeLocal;
	@Inject
	private MonitorFacadeLocal monitorFacadeLocal;
	@Inject
	private AS400GenericBean as400GenericBean;
	@Inject
	private AS400Bean as400Bean;
	@Inject
	private TicketPriorityFacadeLocal ticketPriorityFacadeLocal;
	@Inject
	private TicketEJB ticketEJB;

	/*
	 * Methods
	 */

	public TicketBean() {

	}

	@PostConstruct
	public void startUp() {

		resetVariables();

	}

	public void resetVariables() {

		startDate = endDate = date = Calendar.getInstance().getTime();
		Calendar calendar = Calendar.getInstance();
		minDate = calendar.getTime();

		printed = false;
		compactView = false;

		resultData = new ResultData();
		patient = new Patient();
		ticket = new Ticket();
		serviceType = new ServiceType();
		area = new Area();
		attentionModule = new AttentionModule();

		serviceTypeByUserList = new ArrayList<>();
		serviceTypeByAreaMap = new HashMap<>();
		appointmentWrapperList = null;
		ticketList = null;
		objectList = null;

		try {

			address = JSFUtility.getIP();
			htmlStyle = parameterFacadeLocal.findByParameter("TICKET-HTML-STYLE").getValue();
			htmlStyle = htmlStyle.replaceAll("(\r\n|\n)", "");
			updateInterval = Integer.valueOf(parameterFacadeLocal.findByParameter("TICKET-UPDATE-INTERVAL").getValue());
			videoList = parameterFacadeLocal.findByParameter("TV-VIDEO-LIST").getValue();

		} catch (Exception exception) {

			updateInterval = 8;

			System.out.println("Can't initialize variables in " + this.getClass().getName());

		}

	}

	public void resetForm() {

		resetVariables();
		as400GenericBean.resetVariables();
		JSFUtility.reset("form");

	}

	public void initializeDataByUser(String action) {

		this.action = action;

		SystemUser systemUser = (SystemUser) JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION);

		fetchServiceTypeByUser(systemUser, action);

		defineModuleByServiceType();

		waitingTicketList = ticketFacadeLocal
				.findWaitingTicketGroupedListBySystemUserServiceType(systemUser.getIdSystemUser());

		calledTicketListByService = ticketFacadeLocal
				.findCalledTicketListBySystemUserServiceType(systemUser.getIdSystemUser());

		ticketPriority = ticketPriorityFacadeLocal.findBySystemUser(JSFUtility.getUserInSession().getIdSystemUser());

		ticket = ticketFacadeLocal.findLastCalledTicketNonFinishedByUser(systemUser.getIdSystemUser());

		if (ticket != null) {

		} else {
			ticket = new Ticket();
		}

	}

	public void initializeDataByArea() {

		try {

			initializeDataByTV();

			String ip = JSFUtility.getIP();

			calledTicketListByArea = ticketFacadeLocal.findCalledTicketListByTicketService(ip);

			videoList = parameterFacadeLocal.findByParameter("TV-VIDEO-LIST").getValue();

		} catch (Exception exception) {

			fontSize = "1em";

			System.err.println("Can't execute: initializeDataByArea!");

		}

	}

	public void initializeDataByKiosk() {

		try {

			String ip = JSFUtility.getIP();

			serviceTypeList = serviceTypeFacadeLocal.findServiceTypeByKiosk(ip);

		} catch (Exception exception) {

			System.err.println("Can't execute: initializeDataByKiosk!");

		}

	}

	public void initializeDataByTV() {

		try {

			String ip = JSFUtility.getIP();

			Monitor monitor = monitorFacadeLocal.findByIP(ip);

			if (monitor != null && monitor.getFontSize() != null)
				fontSize = monitor.getFontSize();
			else
				fontSize = "1em";

			if (monitor != null && monitor.getEnableVideo() != null) {
				videoEnabled = monitor.getEnableVideo();
			} else {
				videoEnabled = false;
			}

			if (monitor != null && monitor.getEnableTicket() != null) {
				ticketEnabled = monitor.getEnableTicket();
			} else {
				ticketEnabled = false;
			}

		} catch (Exception exception) {

			fontSize = "1em";

			videoEnabled = false;

			ticketEnabled = false;

			System.err.println("Can't execute: initializeDataByTV!");

		}

	}

	public void defineModuleByServiceType() {

		try {

			if (serviceType == null || serviceType.getServiceTypePk() == null) {

				SystemUser systemUser = (SystemUser) JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION);

				attentionModuleList = attentionModuleFacadeLocal
						.findAttentionModuleByUser(systemUser.getIdSystemUser());

			} else {

				attentionModuleList = attentionModuleFacadeLocal
						.findAttentionModuleByServiceTypePk(serviceType.getServiceTypePk());

			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void defineSystemUserByServiceType() {

		try {

			systemUserByServiceTypeList = systemUserFacadeLocal
					.findSystemUserByServiceType(serviceType.getServiceTypePk());

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void fetchServiceTypeByUser(SystemUser systemUser, String action) {

		try {

			List<Integer> ids = new ArrayList<>();
			ids.add(systemUser.getIdSystemUser());

			List<SystemUserServiceType> systemUserServiceTypeList = systemUserServiceTypeFacadeLocal
					.findSystemUserServiceTypeByUserAndByAction(ids, action);

			if (systemUserServiceTypeList != null) {

				systemUser.setSystemUserServiceTypeSet(new LinkedHashSet<>(systemUserServiceTypeList));

				createServiceTypeByAreaMap(systemUser);

				ticketService = serviceTypeByAreaMap.entrySet().iterator().next().getKey();

				showServiceTypeByArea();

			} else {

				JSFUtility.addMessage("No tiene servicios asignados", 2);

			}

		} catch (Exception exception) {

			Utility.printError(this.getClass().getName(), MethodName.methodName(), "Can't fetch service types by user!",
					exception);

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void fetchInfoByServiceType() {

		defineModuleByServiceType();
		defineSystemUserByServiceType();

		systemUser = JSFUtility.getUserInSession();
		// dates = "{}";
		resultData = new ResultData();

	}

	public void createServiceTypeByAreaMap(SystemUser systemUser) {

		serviceTypeByAreaMap = new HashMap<>();

		serviceTypeByUserList = new ArrayList<>();

		for (SystemUserServiceType object : systemUser.getSystemUserServiceTypeSet()) {

			List<ServiceType> temporalList = serviceTypeByAreaMap.get(object.getServiceType().getTicketServiceFk());

			if (temporalList == null) {

				temporalList = new ArrayList<>();

				serviceTypeByAreaMap.put(object.getServiceType().getTicketServiceFk(), temporalList);

			}

			temporalList.add(object.getServiceType());

			serviceTypeByUserList.add(object.getServiceType());

		}

	}

	public void showServiceTypeByArea() {

		serviceTypeList = serviceTypeByAreaMap.get(ticketService);

	}

	public void printTestTicket() {

		data = new HashMap<>();

		data.put("turno", "PRUEBA");
		data.put("dateTime", Calendar.getInstance().getTime());
		data.put("ip", JSFUtility.getIP());
		data.put("app", app);
		data.put("copies", 1);
		data.put("printerName", parameterFacadeLocal.findByParameter("TICKET-PRINTER").getValue());
		data.put("logStatus", Boolean.valueOf(parameterFacadeLocal.findByParameter("LOG-PRINTER").getValue()));

		sendTicket(data);

	}

	public void printTicket(ServiceType serviceType) {

		try {

			HashMap<String, Object> ticketData = new HashMap<>();

			ticketData.put("serviceType", serviceType);
			ticketData.put("systemUser", JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION));
			ticketData.put("address", JSFUtility.getIP());
			ticketData.put("scheduledPatient", false);

			ticket = ticketEJB.generateTicket(ticketData);

			if (ticket != null) {

				data = new HashMap<>();

				data.put("turno", ticket.getTicket());
				data.put("dateTime", ticket.getGenerationDateTime());
				data.put("ip", JSFUtility.getIP());
				data.put("app", app);
				data.put("copies", serviceType.getTicketCopies());
				data.put("printerName", parameterFacadeLocal.findByParameter("TICKET-PRINTER").getValue());
				data.put("logStatus", Boolean.valueOf(parameterFacadeLocal.findByParameter("LOG-PRINTER").getValue()));

				sendTicket(data);

			} else {

				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);

			}

		} catch (Exception exception) {

			exception.printStackTrace();

			System.out.println("Exception: " + exception.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public void sendTicket(HashMap<String, Object> data) {

		printed = false;

		try {

			Boolean logStatus = data.get("logStatus") != null ? (Boolean) data.get("logStatus") : false;

			clientSocket = new Socket(data.get("ip").toString(), port);

			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectOutputStream.writeObject(data);

			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			HashMap<String, Object> result = (HashMap<String, Object>) objectInputStream.readObject();

			if (logStatus)
				System.out.println(result.get("text"));

			printed = (Boolean) result.get("printed");

			if (objectInputStream != null) {
				objectInputStream.close();
			}
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}

		} catch (IOException | ClassNotFoundException exception) {
			System.out.println("Can't send data to print from " + data.get("ip").toString());
			System.out.println("Exception: " + exception.getMessage());
		} finally {
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException exception) {
				System.out.println("Can't close connection from " + data.get("ip").toString());
				System.out.println("Exception: " + exception.getMessage());
			}
		}

		PrimeFaces.current().ajax().update("form:printer-msg-dlg-bdy");

		if (!printed) {
			showPrinterErrorMessage();
			System.out.println(String.format("msg=Ticket not printed! ticket=%s ip=%s", ticket.getTicket(),
					data.get("ip").toString()));
		} else {
			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
		}

	}

	public void showPrinterErrorMessage() {

		JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_PRINTING, 2);
		JSFUtility.dialog("printer-msg-dlg", JSFUtility.OPEN, DialogType.BS);

	}

	public void printGenericTestTicket() {

		try {

			HashMap<String, Object> data = new HashMap<>();

			data = new HashMap<>();

			data.put("ip", JSFUtility.getIP());
			data.put("copies", 1);
			data.put("printerName", parameterFacadeLocal.findByParameter("TICKET-PRINTER").getValue());
			data.put("paperWidth", 222.0);
			data.put("paperHeight", 500.0);
			data.put("dataMap", generateTestTicket());
			data.put("ticket", "PRUEBA");
			data.put("logStatus", Boolean.valueOf(parameterFacadeLocal.findByParameter("LOG-PRINTER").getValue()));

			GenericPrinterSocket genericPrinterSocket = new GenericPrinterSocket();
			genericPrinterSocket.sendTicket(data);

			if (genericPrinterSocket.getPrinted()) {
				JSFUtility.addMessage(JSFUtility.SUCCESSFUL_PRINTING, 1);
			} else {
				System.out.println(
						String.format("msg=Ticket not printed! ticket=%s ip=%s", "PRUEBA", data.get("ip").toString()));
				JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_PRINTING, 2);
			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);
			System.out.println("Exception: " + exception.getMessage());

		}

	}

	public HashMap<String, Object> generateTestTicket() {

		HashMap<String, Object> dataMap = new HashMap<>();

		int yOffSet = 90;

		try {

			JSONArray jsonArray = new JSONArray();
			JSONObject jsonElement = null;

			jsonElement = new JSONObject();
			jsonElement.put("text", "PRUEBA");
			jsonElement.put("xPos", "0");
			jsonElement.put("yPos", "0");
			jsonElement.put("fontFamily", "TimesRoman");
			jsonElement.put("fontStyle", Font.BOLD);
			jsonElement.put("fontSize", "42");
			jsonArray.put(jsonElement);

			/*
			 * Footer
			 */

			jsonElement = new JSONObject();
			jsonElement.put("text", "-------------------------------------");
			jsonElement.put("xPos", "0");
			jsonElement.put("yPos", "0");
			jsonElement.put("fontFamily", "TimesRoman");
			jsonElement.put("fontStyle", Font.PLAIN);
			jsonElement.put("fontSize", "11");
			jsonArray.put(jsonElement);

			jsonElement = new JSONObject();
			jsonElement.put("text", "Por favor espere su turno");
			jsonElement.put("xPos", "0");
			jsonElement.put("yPos", "0");
			jsonElement.put("fontFamily", "TimesRoman");
			jsonElement.put("fontStyle", Font.PLAIN);
			jsonElement.put("fontSize", "9");
			jsonArray.put(jsonElement);

			Date now = Calendar.getInstance().getTime();
			String date = new SimpleDateFormat("yyyy/MM/dd").format(now);
			String time = new SimpleDateFormat("HH:mm").format(now);
			String dateTime = date + " " + time;

			jsonElement = new JSONObject();
			jsonElement.put("text", "Emitido: " + dateTime);
			jsonElement.put("xPos", "0");
			jsonElement.put("yPos", "0");
			jsonElement.put("fontFamily", "TimesRoman");
			jsonElement.put("fontStyle", Font.PLAIN);
			jsonElement.put("fontSize", "9");
			jsonArray.put(jsonElement);

			/*
			 * End of footer
			 */

			JSONObject data = new JSONObject();
			data.put("data", jsonArray);

			dataMap.put("yOffSet", yOffSet);
			dataMap.put("objectData", data.toString());

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return dataMap;

	}

	public void findNextTicket() {

		endTicket();

		HashMap<String, Object> ticketData = new HashMap<>();

		ticketData.put("serviceType", serviceType);
		ticketData.put("attentionModule", attentionModule);
		ticketData.put("systemUser", JSFUtility.getAttributeFromSession(JSFUtility.USER_IN_SESSION));
		ticketData.put("address", JSFUtility.getIP());

		Ticket temporalTicket = ticketEJB.findTicket(ticketData);

		if (temporalTicket != null) {

			ticket = temporalTicket;

		} else {

			JSFUtility.addMessage("No hay turnos en cola", 2);

		}

		initializeDataByUser(this.action);

	}

	public void reCallTicket() {

		if (ticket != null) {

			String data = ticket.getAdditionalData();

			JSONObject json = null;
			JSONArray array = null;

			if (data != null && !data.isEmpty()) {

				try {
					json = new JSONObject(data);
					array = json.getJSONArray("calls");
					array.put(ticket.getCallDateTime());
					json.put("calls", array);
				} catch (JSONException exception) {
					System.out.println("Can't parse json! Exception: " + exception.getMessage());
				}

			} else {
				json = new JSONObject();
				array = new JSONArray();
				try {
					array.put(ticket.getCallDateTime());
					json.put("calls", array);
				} catch (JSONException exception) {
					System.out.println("Can't parse json! Exception: " + exception.getMessage());
				}

			}

			ticket.setCallDateTime(Calendar.getInstance().getTime());
			ticket.setAdditionalData(json.toString());
			ticketFacadeLocal.edit(ticket);
			initializeDataByUser(this.action);

		}

	}

	public void endTicket() {

		if (ticket != null && ticket.getTicketPk() != null) {

			ticket.setEndDateTime(Calendar.getInstance().getTime());
			ticket.setEnded(true);

			ticketFacadeLocal.edit(ticket);

			ticket = new Ticket();

		}

	}

	public void saveScheduledTicket() {

		patient = as400GenericBean.getPatient();

		if (!as400GenericBean.getPatientFound()) {
			JSFUtility.addMessage(JSFUtility.PATIENT_DATA_NOT_FOUND, 2);
			return;
		}

		if (appointmentWrapperList == null || appointmentWrapperList.isEmpty()) {
			JSFUtility.addMessage("No se han asignado horarios", 2);
			return;
		}

		HashMap<String, Object> data = new HashMap<>();

		data.put("patient", patient);
		data.put("attentionModule", attentionModule);
		data.put("systemUser", JSFUtility.getUserInSession());
		data.put("assignedUser", systemUser);
		data.put("serviceType", serviceType);
		data.put("address", JSFUtility.getIP());
		data.put("appointmentWrapperList", appointmentWrapperList);

		resultData = ticketFacadeLocal.saveScheduledTicket(data);

		if (resultData.getStatus()) {
			resetForm();
			initializeDataByUser(action);
			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);
		} else {
			JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 2);
		}

	}

	public void inactivateTicket(Integer id) {

		try {

			ticket = ticketFacadeLocal.find(id);
			ticket.setTableStatus(false);
			ticketFacadeLocal.edit(ticket);

			JSFUtility.addMessage(JSFUtility.SUCCESSFUL_TRANSACTION, 1);

		} catch (Exception exception) {
			JSFUtility.addMessage(JSFUtility.UNSUCCESSFUL_TRANSACTION, 1);
			Utility.printErrorMessage("Method: inactivateTicket!", exception);
		}

	}

	public void updateTableStatus(Integer id, Boolean value) {

		try {

			ticket = ticketFacadeLocal.find(id);
			ticket.setTableStatus(value);
			ticketFacadeLocal.edit(ticket);

		} catch (Exception exception) {
			Utility.printErrorMessage("Method: updateTableStatus!", exception);
		}

	}

	public void updateAttendedStatus(Integer id, Boolean value) {

		try {

			ticket = ticketFacadeLocal.find(id);
			ticket.setAttended(value);
			ticketFacadeLocal.edit(ticket);

		} catch (Exception exception) {
			Utility.printErrorMessage("Method: updateAttendedStatus!", exception);
		}

	}

	public void findScheduledTicketByPatient() {

		try {

			ticketList = ticketFacadeLocal.findScheduledTicketByPatient(patient.getCode());

			if (ticketList != null && !ticketList.isEmpty()) {

				JSFUtility.addMessage(JSFUtility.DATA_FOUND, 1);

			} else {

				JSFUtility.addMessage(JSFUtility.DATA_NOT_FOUND, 2);

			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	public void findTicketTraceByCodeAndByTicketService() {

		try {

			Integer ticketServicePk = Integer.valueOf(Utility.getProperty("T-SP-EME"));

			columnModelList = Utility.getColumnModelList("C-ER-TRZ");

			objectList = ticketFacadeLocal.findTicketTraceByCodeAndByTicketService(code, ticketServicePk);

			if (!CollectionUtils.isEmpty(objectList)) {
				JSFUtility.info(JSFUtility.DATA_FOUND);
			} else {
				JSFUtility.warn(JSFUtility.DATA_NOT_FOUND);
			}

		} catch (Exception exception) {

			JSFUtility.addMessage(JSFUtility.SOMETHING_WENT_WRONG, 3);

		}

	}

	/*
	 * Getters and setters
	 */

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	public SystemUserFacadeLocal getSystemUserFacadeLocal() {
		return systemUserFacadeLocal;
	}

	public void setSystemUserFacadeLocal(SystemUserFacadeLocal systemUserFacadeLocal) {
		this.systemUserFacadeLocal = systemUserFacadeLocal;
	}

	public List<ServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<ServiceType> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public HashMap<TicketService, List<ServiceType>> getServiceTypeByAreaMap() {
		return serviceTypeByAreaMap;
	}

	public void setServiceTypeByAreaMap(HashMap<TicketService, List<ServiceType>> serviceTypeByAreaMap) {
		this.serviceTypeByAreaMap = serviceTypeByAreaMap;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
		this.objectOutputStream = objectOutputStream;
	}

	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	public void setObjectInputStream(ObjectInputStream objectInputStream) {
		this.objectInputStream = objectInputStream;
	}

	public Boolean getPrinted() {
		return printed;
	}

	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
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

	public TicketEJB getTicketEJB() {
		return ticketEJB;
	}

	public void setTicketEJB(TicketEJB ticketEJB) {
		this.ticketEJB = ticketEJB;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<ServiceType> getServiceTypeByUserList() {
		return serviceTypeByUserList;
	}

	public void setServiceTypeByUserList(List<ServiceType> serviceTypeByUserList) {
		this.serviceTypeByUserList = serviceTypeByUserList;
	}

	public AttentionModule getAttentionModule() {
		return attentionModule;
	}

	public void setAttentionModule(AttentionModule attentionModule) {
		this.attentionModule = attentionModule;
	}

	public List<Object[]> getCalledTicketListByService() {
		return calledTicketListByService;
	}

	public void setCalledTicketListByService(List<Object[]> calledTicketListByService) {
		this.calledTicketListByService = calledTicketListByService;
	}

	public List<Object[]> getCalledTicketListByArea() {
		return calledTicketListByArea;
	}

	public void setCalledTicketListByArea(List<Object[]> calledTicketListByArea) {
		this.calledTicketListByArea = calledTicketListByArea;
	}

	public List<Object[]> getWaitingTicketList() {
		return waitingTicketList;
	}

	public void setWaitingTicketList(List<Object[]> waitingTicketList) {
		this.waitingTicketList = waitingTicketList;
	}

	public List<AttentionModule> getAttentionModuleList() {
		return attentionModuleList;
	}

	public void setAttentionModuleList(List<AttentionModule> attentionModuleList) {
		this.attentionModuleList = attentionModuleList;
	}

	public ServiceTypeFacadeLocal getServiceTypeFacadeLocal() {
		return serviceTypeFacadeLocal;
	}

	public void setServiceTypeFacadeLocal(ServiceTypeFacadeLocal serviceTypeFacadeLocal) {
		this.serviceTypeFacadeLocal = serviceTypeFacadeLocal;
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

	public AS400Bean getAs400Bean() {
		return as400Bean;
	}

	public void setAs400Bean(AS400Bean as400Bean) {
		this.as400Bean = as400Bean;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(Integer updateInterval) {
		this.updateInterval = updateInterval;
	}

	public AttentionModuleFacadeLocal getAttentionModuleFacadeLocal() {
		return attentionModuleFacadeLocal;
	}

	public void setAttentionModuleFacadeLocal(AttentionModuleFacadeLocal attentionModuleFacadeLocal) {
		this.attentionModuleFacadeLocal = attentionModuleFacadeLocal;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public List<SystemUser> getSystemUserByServiceTypeList() {
		return systemUserByServiceTypeList;
	}

	public void setSystemUserByServiceTypeList(List<SystemUser> systemUserByServiceTypeList) {
		this.systemUserByServiceTypeList = systemUserByServiceTypeList;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public Boolean getCompactView() {
		return compactView;
	}

	public void setCompactView(Boolean compactView) {
		this.compactView = compactView;
	}

	public AS400GenericBean getAs400GenericBean() {
		return as400GenericBean;
	}

	public void setAs400GenericBean(AS400GenericBean as400GenericBean) {
		this.as400GenericBean = as400GenericBean;
	}

	public String getHtmlStyle() {
		return htmlStyle;
	}

	public void setHtmlStyle(String htmlStyle) {
		this.htmlStyle = htmlStyle;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Ticket> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<Ticket> ticketList) {
		this.ticketList = ticketList;
	}

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public ResultData getResultData() {
		return resultData;
	}

	public void setResultData(ResultData resultData) {
		this.resultData = resultData;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public MonitorFacadeLocal getMonitorFacadeLocal() {
		return monitorFacadeLocal;
	}

	public void setMonitorFacadeLocal(MonitorFacadeLocal monitorFacadeLocal) {
		this.monitorFacadeLocal = monitorFacadeLocal;
	}

	public TicketPriority getTicketPriority() {
		return ticketPriority;
	}

	public void setTicketPriority(TicketPriority ticketPriority) {
		this.ticketPriority = ticketPriority;
	}

	public SystemUserServiceTypeFacadeLocal getSystemUserServiceTypeFacadeLocal() {
		return systemUserServiceTypeFacadeLocal;
	}

	public void setSystemUserServiceTypeFacadeLocal(SystemUserServiceTypeFacadeLocal systemUserServiceTypeFacadeLocal) {
		this.systemUserServiceTypeFacadeLocal = systemUserServiceTypeFacadeLocal;
	}

	public TicketPriorityFacadeLocal getTicketPriorityFacadeLocal() {
		return ticketPriorityFacadeLocal;
	}

	public void setTicketPriorityFacadeLocal(TicketPriorityFacadeLocal ticketPriorityFacadeLocal) {
		this.ticketPriorityFacadeLocal = ticketPriorityFacadeLocal;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public List<AppointmentWrapper> getAppointmentWrapperList() {
		return appointmentWrapperList;
	}

	public void setAppointmentWrapperList(List<AppointmentWrapper> appointmentWrapperList) {
		this.appointmentWrapperList = appointmentWrapperList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getVideoList() {
		return videoList;
	}

	public void setVideoList(String videoList) {
		this.videoList = videoList;
	}

	public Boolean getVideoEnabled() {
		return videoEnabled;
	}

	public void setVideoEnabled(Boolean videoEnabled) {
		this.videoEnabled = videoEnabled;
	}

	public Boolean getTicketEnabled() {
		return ticketEnabled;
	}

	public void setTicketEnabled(Boolean ticketEnabled) {
		this.ticketEnabled = ticketEnabled;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
