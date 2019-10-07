/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;

import org.json.JSONObject;

import authEntities.SystemUser;
import entity.ticket.AttentionModule;
import entity.ticket.ServiceType;
import entity.ticket.SystemUserTicket;
import entity.ticket.Ticket;
import entity.ticket.TicketHeader;
import pollEntities.Respondent;
import surgeryEntities.Patient;
import utilities.AppointmentWrapper;
import utilities.Constant;
import utilities.ErrorTypeConstant;
import utilities.ResultData;
import utilities.Utility;

/**
 *
 * @author BV
 */
@Stateless
public class TicketFacade extends AbstractFacade<Ticket> implements TicketFacadeLocal {

	@PersistenceContext(unitName = "EntityGeneratorPU")
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public TicketFacade() {
		super(Ticket.class);
	}

	@Inject
	private ParameterFacadeLocal parameterFacadeLocal;
	@Inject
	private PatientFacadeLocal patientFacadeLocal;
	@Inject
	private RespondentFacadeLocal respondentFacadeLocal;

	@Resource
	private EJBContext context;

	@Override
	public Ticket findNextTicketByServiceType(Integer systemUserPk, Integer serviceTypePk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_by_service_type", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);
			storedProcedure.setParameter(2, serviceTypePk);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findNextTicketByUserAndByPriority(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_by_user_and_by_priority", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findNextTicketByUserAndByAlternativePriority(Integer systemUserPk, String priority) {

		if (priority == null || priority.isEmpty())
			return null;

		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(
					"ticket.find_next_ticket_by_user_and_by_alternative_priority", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);
			storedProcedure.setParameter(2, priority);

			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findWaitingTicketGroupedListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.waiting_ticket_grouped_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCalledTicketListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.called_ticket_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCalledTicketListByTicketService(String address) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.called_ticket_list_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, address);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findWaitingTicketListBySystemUserServiceType(Integer systemUserPk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.waiting_ticket_list_by_system_user_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, systemUserPk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData saveScheduledTicket(HashMap<String, Object> data) {

		ResultData resultData = new ResultData();

		try {

			Patient patient = (Patient) data.get("patient");
			Respondent respondent = (Respondent) data.get("respondent");
			Boolean patientType = (Boolean) data.get("patientType");
			SystemUser systemUser = (SystemUser) data.get("systemUser");
			SystemUser assignedUser = (SystemUser) data.get("assignedUser");
			AttentionModule attentionModule = (AttentionModule) data.get("attentionModule");
			ServiceType serviceType = (ServiceType) data.get("serviceType");
			String address = (String) data.get("address");
			List<AppointmentWrapper> appoinmentWrapperList = (List<AppointmentWrapper>) data
					.get("appointmentWrapperList");

			if (patientType != null) {

				if (patientType) {

					patient = patientFacadeLocal.upsert(patient);

				} else {

					respondent = respondentFacadeLocal.upsert(respondent);

				}

			} else {

				System.err
						.println(String.format("msg=Person type not defined! serviceType=%s", serviceType.getLabel()));
				context.setRollbackOnly();
				resultData.setStatus(false);
				return resultData;

			}

			JSONObject json = null;

			for (AppointmentWrapper appointmentWrapper : appoinmentWrapperList) {

				String[] schedules = appointmentWrapper.getTimeRange().split(" - ");
				LocalDateTime dateTime = null;
				Date startDateTime = null;
				Date endDateTime = null;

				dateTime = LocalDateTime.of(Utility.toLocalDate(appointmentWrapper.getAppointment()),
						LocalTime.parse(schedules[0]));
				startDateTime = Utility.toDate(dateTime);
				dateTime = LocalDateTime.of(Utility.toLocalDate(appointmentWrapper.getAppointment()),
						LocalTime.parse(schedules[1]));
				endDateTime = Utility.toDate(dateTime);

				try {

					json = new JSONObject();
					json.put("startDateTime", startDateTime);
					json.put("endDateTime", endDateTime);
					if (patientType) {

						json.put("code", patient.getCode());

					} else {

						json.put("code", respondent.getCode());

					}

				} catch (Exception exception) {
					System.err.println("Can't parse data for ticket collision!");
				}

				resultData = checkTicketCollision(json.toString());
				if (!resultData.getStatus()) {
					context.setRollbackOnly();
					return resultData;
				}

				String evalType = serviceType.getScheduleType();

				if (evalType != null) {
					if (evalType.equals(Constant.TCK_AG_USUARIO)) {
						resultData = checkTicketAvailability(serviceType.getServiceTypePk(),
								assignedUser.getIdSystemUser(), appointmentWrapper.getAppointment(),
								Constant.TCK_AG_USUARIO, appointmentWrapper.getTimeRange());
					} else if (evalType.equals(Constant.TCK_AG_SALA)) {
						resultData = checkTicketAvailability(serviceType.getServiceTypePk(),
								attentionModule.getAttentionModulePk(), appointmentWrapper.getAppointment(),
								Constant.TCK_AG_SALA, appointmentWrapper.getTimeRange());
					}
					if (!resultData.getStatus()) {
						context.setRollbackOnly();
						return resultData;
					}
				} else {
					System.err.println("Evaluation type not defined!");
					context.setRollbackOnly();
					resultData.setStatus(false);
					return resultData;
				}

				Ticket ticket = new Ticket();

				if (patientType) {
					ticket.setPatientFk(patient);
				} else {
					ticket.setPersonFk(respondent.getPersonPk());
				}
				ticket.setCalled(false);
				ticket.setAttended(false);
				ticket.setEnded(false);
				ticket.setGenerated(false);
				ticket.setGenerationDateTime(Calendar.getInstance().getTime());
				ticket.setServiceTypeFk(serviceType);
				ticket.setAttentionModuleFk(attentionModule.getAttentionModulePk());
				ticket.setAttentionModule(attentionModule.getLabel());
				ticket.setSchedulingDateTime(ticket.getGenerationDateTime());
				ticket.setSchedulingStartDateTime(startDateTime);
				ticket.setSchedulingEndDateTime(endDateTime);

				if (assignedUser != null) {
					try {

						if (ticket.getAdditionalData() != null)
							json = new JSONObject(ticket.getAdditionalData());
						else
							json = new JSONObject();
						json.put("assignedUserID", assignedUser.getIdSystemUser());
						json.put("assignedUserName", assignedUser.getNombresCompletos());
						ticket.setAdditionalData(json.toString());

					} catch (Exception exception) {
						System.err.println("Can't parse data for assigned user!");
					}
				}

				String orders = (String) data.get("orders");
				if (orders != null) {

					try {

						json = new JSONObject();
						json.put("serviceTypePk", serviceType.getServiceTypePk());
						json.put("orders", orders);

					} catch (Exception exception) {
						System.err.println("Can't parse data for order collision!");
					}

					resultData = checkTicketOrder(json.toString());
					if (!resultData.getStatus()) {
						context.setRollbackOnly();
						return resultData;
					}

					try {

						if (ticket.getAdditionalData() != null)
							json = new JSONObject(ticket.getAdditionalData());
						else
							json = new JSONObject();
						json.put("orders", orders.split(","));
						ticket.setAdditionalData(json.toString());

					} catch (Exception exception) {
						System.err.println("Can't parse data for imaging orders!");
					}
				}

				entityManager.persist(ticket);

				SystemUserTicket systemUserTicket = new SystemUserTicket();

				systemUserTicket.setAddress(address);
				systemUserTicket.setAction(parameterFacadeLocal.findByParameter("TICKET-ACTION-GENERATE").getValue());
				systemUserTicket.setDateTime(ticket.getGenerationDateTime());
				systemUserTicket.setTicketFk(ticket);
				systemUserTicket.setSystemUserFk(systemUser);

				entityManager.persist(systemUserTicket);

				data.put("oldScheduledTicket", ticket);

			}

			resultData.setStatus(true);
			resultData.setMessage(null);
			resultData.setResultList(null);

			return resultData;

		} catch (Exception exception) {

			context.setRollbackOnly();

			resultData.setStatus(false);
			resultData.setMessage(null);
			resultData.setResultList(null);

			return resultData;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findScheduledTicketBySystemUser(Date startDate, Date endDate, Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.scheduled_ticket_by_system_user");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, startDate);
			storedProcedure.setParameter(2, endDate);
			storedProcedure.setParameter(3, idSystemUser);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findScheduledTicketByServiceType(Date startDate, Date endDate, Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.scheduled_ticket_by_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, startDate);
			storedProcedure.setParameter(2, endDate);
			storedProcedure.setParameter(3, idSystemUser);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> findScheduledTicketByPatientAndService(String code, Integer userPk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.scheduled_ticket_by_patient_and_service", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, userPk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findMonitorInfoScheduledTicketByServiceType(String ip) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.monitor_info_scheduled_ticket_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

			storedProcedure.setParameter(1, ip);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> findScheduledTicketByPatient(String code) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.scheduled_ticket_by_patient", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Integer findNextTicketNumberByServiceType(Integer serviceTypePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_next_ticket_number_by_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, serviceTypePk);

			storedProcedure.execute();

			return (Integer) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findAvailableTicket(Integer service, Integer attModOrUser, Date date, String evalType) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_available_ticket");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, service);
			storedProcedure.setParameter(2, attModOrUser);
			storedProcedure.setParameter(3, date);
			storedProcedure.setParameter(4, evalType != null ? evalType : Constant.TCK_AG_SALA);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData checkTicketAvailability(Integer service, Integer attentionModule, Date date, String evalType,
			String schedule) {

		ResultData resultData = new ResultData();

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.check_ticket_availability");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, service);
			storedProcedure.setParameter(2, attentionModule);
			storedProcedure.setParameter(3, date);
			storedProcedure.setParameter(4, evalType);
			storedProcedure.setParameter(5, schedule);
			storedProcedure.execute();

			List<Object[]> resultSet = storedProcedure.getResultList();

			if (resultSet != null && !resultSet.isEmpty()) {
				resultData.setStatus(false);
				resultData.setMessage(ErrorTypeConstant.MSJ_AGD_CUPO_NO_DISPONIBLE);
				resultData.setResultList(resultSet);
				resultData.setShowErrorPanel(true);
				resultData.setErrorType(ErrorTypeConstant.AGD_CUPO_NO_DISPONIBLE);
				return resultData;
			} else {
				resultData.setStatus(true);
				return resultData;
			}

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData checkTicketCollision(String string) {

		ResultData resultData = new ResultData();

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.check_ticket_collision");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, string);
			storedProcedure.execute();
			List<Object[]> resultSet = storedProcedure.getResultList();

			if (resultSet != null && !resultSet.isEmpty()) {
				resultData.setStatus(false);
				resultData.setMessage(ErrorTypeConstant.MSJ_AGD_COLISION_HORARIO);
				resultData.setResultList(resultSet);
				resultData.setShowErrorPanel(true);
				resultData.setErrorType(ErrorTypeConstant.AGD_COLISION_HORARIO);
				return resultData;
			} else {
				resultData.setStatus(true);
				return resultData;
			}

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData checkTicketOrder(String orders) {

		ResultData resultData = new ResultData();

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.check_ticket_order");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, orders);
			storedProcedure.execute();

			List<Object[]> resultSet = storedProcedure.getResultList();

			if (resultSet != null && !resultSet.isEmpty()) {
				resultData.setStatus(false);
				resultData.setMessage(ErrorTypeConstant.MSJ_AGD_COLISION_ORDEN);
				resultData.setResultList(resultSet);
				resultData.setShowErrorPanel(true);
				resultData.setErrorType(ErrorTypeConstant.AGD_COLISION_ORDEN);
				return resultData;
			} else {
				resultData.setStatus(true);
				return resultData;
			}

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findImgScheduledTicketByPatientAndService(String code, Integer userPk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.img_scheduled_ticket_by_patient_and_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, userPk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPhyScheduledTicketByPatientAndService(String code, Integer servicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.phy_scheduled_ticket_by_patient_and_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, servicePk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPhyScheduledTicketByServiceType(Date startDate, Date endDate, Integer servicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.phy_scheduled_ticket_by_service_type");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, startDate);
			storedProcedure.setParameter(2, endDate);
			storedProcedure.setParameter(3, servicePk);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPhyScheduledTicketBySystemUser(Date startDate, Date endDate, Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.phy_scheduled_ticket_by_system_user");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);

			storedProcedure.setParameter(1, startDate);
			storedProcedure.setParameter(2, endDate);
			storedProcedure.setParameter(3, idSystemUser);

			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findPhyClosestAvailableApptInfo(String data) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.phy_closest_available_appt_info");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, data);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public SystemUser findTicketUser(Integer pk, String type) {

		try {

			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("ticket.find_ticket_user",
					SystemUser.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, pk);
			storedProcedure.setParameter(2, type);
			storedProcedure.execute();

			return (SystemUser) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}

	}

	@Override
	public Object[] findImgScheduledTicket(Integer ticketPk, String code, Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.img_scheduled_ticket");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, ticketPk);
			storedProcedure.setParameter(2, code);
			storedProcedure.setParameter(3, idSystemUser);
			storedProcedure.execute();

			return (Object[]) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> findGeneratedTicket(String code, Integer ticketServicePk) {

		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_generated_ticket", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, ticketServicePk);
			storedProcedure.execute();

			return (List<Ticket>) storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findTicketProduction(Date startDate, Date endDate, Integer ticketServicePk,
			String actionParameter, String queryParameter) {

		try {

			String query = parameterFacadeLocal.findByParameter("QRY-TR-P-" + queryParameter).getValue();

			return entityManager.createNativeQuery(query).setParameter(1, startDate, TemporalType.DATE)
					.setParameter(2, endDate, TemporalType.DATE).setParameter(3, ticketServicePk)
					.setParameter(4, actionParameter).getResultList();

		} catch (Exception exception) {

			return null;

		}

	}

	@Override
	public Ticket findLastCalledTicketNonFinishedByUser(Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_last_called_ticket_non_finished", Ticket.class);

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, idSystemUser);
			storedProcedure.execute();

			return (Ticket) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findTicketDetailByTicketService(Date from, Date to, Integer ticketServicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.ticket_detail_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, from);
			storedProcedure.setParameter(2, to);
			storedProcedure.setParameter(3, ticketServicePk);
			storedProcedure.execute();

			return (List<Object[]>) storedProcedure.getResultList();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}
	}

	@Override
	public ResultData saveTicketHeader(HashMap<String, Object> data) {

		ResultData resultData = new ResultData();

		try {

			Patient patient = (Patient) data.get("patient");
			SystemUser systemUser = (SystemUser) data.get("systemUser");
			SystemUser assignedUser = (SystemUser) data.get("assignedUser");
			AttentionModule attentionModule = (AttentionModule) data.get("attentionModule");
			ServiceType serviceType = (ServiceType) data.get("serviceType");
			Integer appointments = (Integer) data.get("appointments");
			String address = (String) data.get("address");
			String jsonValue = (String) data.get("jsonValue");

			patient = patientFacadeLocal.upsert(patient);

			TicketHeader ticketHeader = new TicketHeader();

			ticketHeader.setServiceTypeFk(serviceType.getServiceTypePk());
			ticketHeader.setCreationDateTime(Calendar.getInstance().getTime());
			ticketHeader.setCreationUserFk(systemUser.getIdSystemUser());
			ticketHeader.setCreationAddress(address);
			ticketHeader.setPatientFk(patient.getPatientPk());
			ticketHeader.setExtraInfo(jsonValue);
			ticketHeader.setTableStatus(true);

			if (assignedUser != null) {
				ticketHeader.setAssignedUserFk(assignedUser.getIdSystemUser());
			}

			if (attentionModule != null) {
				ticketHeader.setAssignedModuleFk(attentionModule.getAttentionModulePk());
			}

			if (appointments != null) {
				ticketHeader.setAppointments(appointments);
			}

			entityManager.persist(ticketHeader);

			resultData.setStatus(true);
			resultData.setMessage(null);
			resultData.setResultList(null);

			return resultData;

		} catch (Exception exception) {

			context.setRollbackOnly();

			resultData.setStatus(false);
			resultData.setMessage(null);
			resultData.setResultList(null);

			return resultData;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultData saveTicketDetail(HashMap<String, Object> data) {
		ResultData resultData = new ResultData();

		try {

			TicketHeader ticketHeader = (TicketHeader) data.get("ticketHeader");
			Patient patient = (Patient) data.get("patient");
			SystemUser systemUser = (SystemUser) data.get("systemUser");
			SystemUser assignedUser = (SystemUser) data.get("assignedUser");
			AttentionModule attentionModule = (AttentionModule) data.get("attentionModule");
			ServiceType serviceType = (ServiceType) data.get("serviceType");
			String address = (String) data.get("address");
			List<AppointmentWrapper> appoinmentWrapperList = (List<AppointmentWrapper>) data
					.get("appointmentWrapperList");

			JSONObject json = null;

			for (AppointmentWrapper appointmentWrapper : appoinmentWrapperList) {

				String[] schedules = appointmentWrapper.getTimeRange().split(" - ");
				LocalDateTime dateTime = null;
				Date startDateTime = null;
				Date endDateTime = null;

				dateTime = LocalDateTime.of(Utility.toLocalDate(appointmentWrapper.getAppointment()),
						LocalTime.parse(schedules[0]));
				startDateTime = Utility.toDate(dateTime);
				dateTime = LocalDateTime.of(Utility.toLocalDate(appointmentWrapper.getAppointment()),
						LocalTime.parse(schedules[1]));
				endDateTime = Utility.toDate(dateTime);

				try {

					json = new JSONObject();
					json.put("startDateTime", startDateTime);
					json.put("endDateTime", endDateTime);
					json.put("code", patient.getCode());

				} catch (Exception exception) {
					System.err.println("Can't parse data for ticket collision!");
				}

				resultData = checkTicketCollision(json.toString());
				if (!resultData.getStatus()) {
					context.setRollbackOnly();
					return resultData;
				}

				String evalType = serviceType.getScheduleType();

				if (evalType != null) {
					if (evalType.equals(Constant.TCK_AG_USUARIO)) {
						resultData = checkTicketAvailability(serviceType.getServiceTypePk(),
								assignedUser.getIdSystemUser(), appointmentWrapper.getAppointment(),
								Constant.TCK_AG_USUARIO, appointmentWrapper.getTimeRange());
					} else if (evalType.equals(Constant.TCK_AG_SALA)) {
						resultData = checkTicketAvailability(serviceType.getServiceTypePk(),
								attentionModule.getAttentionModulePk(), appointmentWrapper.getAppointment(),
								Constant.TCK_AG_SALA, appointmentWrapper.getTimeRange());
					}
					if (!resultData.getStatus()) {
						context.setRollbackOnly();
						return resultData;
					}
				} else {
					System.err.println("Evaluation type not defined!");
					context.setRollbackOnly();
					resultData.setStatus(false);
					return resultData;
				}

				Ticket ticket = new Ticket();

				ticket.setPatientFk(patient);
				ticket.setCalled(false);
				ticket.setAttended(false);
				ticket.setEnded(false);
				ticket.setGenerated(false);
				ticket.setGenerationDateTime(Calendar.getInstance().getTime());
				ticket.setServiceTypeFk(serviceType);
				ticket.setAttentionModuleFk(attentionModule.getAttentionModulePk());
				ticket.setAttentionModule(attentionModule.getLabel());
				ticket.setSchedulingDateTime(ticket.getGenerationDateTime());
				ticket.setSchedulingStartDateTime(startDateTime);
				ticket.setSchedulingEndDateTime(endDateTime);

				if (assignedUser != null) {
					try {

						if (ticket.getAdditionalData() != null)
							json = new JSONObject(ticket.getAdditionalData());
						else
							json = new JSONObject();
						json.put("assignedUserID", assignedUser.getIdSystemUser());
						json.put("assignedUserName", assignedUser.getNombresCompletos());
						json.put("ticketHeaderPk", ticketHeader.getTicketHeaderPk());
						ticket.setAdditionalData(json.toString());

					} catch (Exception exception) {
						System.err.println("Can't parse data for assigned user!");
					}
				}

				String orders = (String) data.get("orders");
				if (orders != null) {

					try {

						json = new JSONObject();
						json.put("serviceTypePk", serviceType.getServiceTypePk());
						json.put("orders", orders);

					} catch (Exception exception) {
						System.err.println("Can't parse data for order collision!");
					}

					resultData = checkTicketOrder(json.toString());
					if (!resultData.getStatus()) {
						context.setRollbackOnly();
						return resultData;
					}

					try {

						if (ticket.getAdditionalData() != null)
							json = new JSONObject(ticket.getAdditionalData());
						else
							json = new JSONObject();
						json.put("orders", orders.split(","));
						ticket.setAdditionalData(json.toString());

					} catch (Exception exception) {
						System.err.println("Can't parse data for imaging orders!");
					}
				}

				entityManager.persist(ticket);

				SystemUserTicket systemUserTicket = new SystemUserTicket();

				systemUserTicket.setAddress(address);
				systemUserTicket.setAction(parameterFacadeLocal.findByParameter("TICKET-ACTION-GENERATE").getValue());
				systemUserTicket.setDateTime(ticket.getGenerationDateTime());
				systemUserTicket.setTicketFk(ticket);
				systemUserTicket.setSystemUserFk(systemUser);

				entityManager.persist(systemUserTicket);

				data.put("oldScheduledTicket", ticket);

			}

			ticketHeader.setTableStatus(false);
			entityManager.merge(ticketHeader);

			resultData.setStatus(true);
			resultData.setMessage(null);
			resultData.setResultList(null);

			return resultData;

		} catch (Exception exception) {

			context.setRollbackOnly();

			resultData.setStatus(false);
			resultData.setMessage(null);
			resultData.setResultList(null);

			exception.printStackTrace();

			return resultData;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findTicketHeaderByCode(String code) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_ticket_header_by_code");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.execute();

			return (List<Object[]>) storedProcedure.getResultList();

		} catch (Exception exception) {

			exception.printStackTrace();

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findLabGMScheduledTicketByPatientAndService(String code, Integer userPk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.lab_gm_scheduled_ticket_by_patient_and_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, userPk);
			storedProcedure.execute();

			return storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

	@Override
	public Object[] findLabGMScheduledTicket(Integer ticketPk, String code, Integer idSystemUser) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.lab_gm_scheduled_ticket");

			storedProcedure.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, ticketPk);
			storedProcedure.setParameter(2, code);
			storedProcedure.setParameter(3, idSystemUser);
			storedProcedure.execute();

			return (Object[]) storedProcedure.getSingleResult();

		} catch (Exception exception) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findTicketTraceByCodeAndByTicketService(String code, Integer ticketServicePk) {
		try {

			StoredProcedureQuery storedProcedure = entityManager
					.createStoredProcedureQuery("ticket.find_ticket_trace_by_code_and_by_ticket_service");

			storedProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			storedProcedure.setParameter(1, code);
			storedProcedure.setParameter(2, ticketServicePk);
			storedProcedure.execute();

			return (List<Object[]>) storedProcedure.getResultList();

		} catch (Exception exception) {

			return null;

		}
	}

}
