package web.utility;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import entity.auth.SystemUser;

public class JSFUtility {

	/* Global messages */

	public static String INFO = "Información";
	public static String WARN = "Advertencia";
	public static String ERROR = "Error";
	public static String SUCCESSFUL_TRANSACTION = "Proceso realizado con éxito";
	public static String UNSUCCESSFUL_TRANSACTION = "Proceso no realizado correctamente";
	public static String SOMETHING_WENT_WRONG = "Algo ha salido mal, lamentamos los inconvenientes";
	public static String DATA_FOUND = "Se encontraron resultados";
	public static String DATA_NOT_FOUND = "No se encontraron resultados";
	public static String DATA_UPDATED = "Datos actualizados";
	public static String DATA_NOT_UPDATED = "Datos no actualizados";
	public static String SUCCESSFUL_PRINTING = "Impresión exitosa";
	public static String UNSUCCESSFUL_PRINTING = "No se pudo imprimir";
	public static String PATIENT_DATA_FOUND = "Se ha encontrado información relacionada al afiliado";
	public static String PATIENT_DATA_NOT_FOUND = "No se ha encontrado información relacionada al afiliado";
	public static String READY_TO_SAVE_FILE = "Listos para guardar archivos";
	public static String NOT_READY_TO_SAVE_FILE = "Aún no estamos listos para guardar los archivos";

	/* Extensions */

	public static String FACELETS_EXTENSION = ".xhtml";

	/* Session vars */

	public static String USER_IN_SESSION = "userInSession";
	public static String ROLES_IN_SESSION = "tiposDeUsuario";

	/* Dialog vars */

	public static String OPEN = "show";
	public static String CLOSE = "hide";

	public static enum DialogType {
		BS, PF, BS_ESCAPED
	};

	/* Methods */

	/* Session */

	public static HttpSession getSession() {

		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			return session;

		} catch (Exception exception) {
			System.out.println("Session is null!");
			return null;
		}

	}

	public static Object getAttributeFromSession(String attribute) {

		try {

			Object obj = getSession().getAttribute(attribute);

			return obj;

		} catch (Exception exception) {

			System.out.println("Attribute " + attribute + " in session not found!");
			return null;

		}

	}

	public static void setAttributeToSession(String attribute, Object object) {

		try {

			getSession().setAttribute(attribute, object);

		} catch (Exception exception) {

			System.out.println("Can't add attribute " + attribute + " to session!");

		}

	}

	public static SystemUser getUserInSession() {

		try {

			return (SystemUser) getAttributeFromSession(JSFUtility.USER_IN_SESSION);

		} catch (Exception exception) {

			System.out.println("Method: getUserInSession!");

		}

		return null;

	}

	public static String getFullNamesFromUserInSession() {

		String author = "N/A";

		try {

			SystemUser userInSession = (SystemUser) getAttributeFromSession(JSFUtility.USER_IN_SESSION);

			if (userInSession != null)
				author = userInSession.getNombresCompletos();

		} catch (Exception exception) {

			System.out.println("Method: getFullNamesFromUserInSession!");

		}

		return author;

	}

	@SuppressWarnings("unchecked")
	public static List<Integer> getRolesInSession() {

		try {

			return (List<Integer>) getAttributeFromSession(JSFUtility.ROLES_IN_SESSION);

		} catch (Exception exception) {

			System.out.println("Method: getRolesInSession!");

		}

		return null;

	}

	/* Messages */

	public static void info(String msg) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, INFO, msg));

	}

	public static void warn(String msg) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, WARN, msg));

	}

	public static void error(String msg) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, msg));

	}

	public static void addMessage(String summary, String detail, Severity severity) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));

	}

	public static void addMessage(String message, int severity) {

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(getSeverity(severity), message, null));

	}

	public static Severity getSeverity(int severity) {

		switch (severity) {

		case 1:
			return FacesMessage.SEVERITY_INFO;
		case 2:
			return FacesMessage.SEVERITY_WARN;
		case 3:
			return FacesMessage.SEVERITY_ERROR;
		case 4:
			return FacesMessage.SEVERITY_FATAL;
		default:
			return null;

		}

	}

	public static void keepMessages() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	/* Client Side */

	public static void execute(String js) {

		PrimeFaces.current().executeScript(js);

	}

	public static void update(String component) {

		PrimeFaces.current().ajax().update(component);

	}

	public static void reset(String component) {

		PrimeFaces.current().resetInputs(component);

	}

	public static String getIP() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		return ipAddress;

	}

	/* Path */

	public static String getContextPath() {

		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

	}

	/* Dialog */

	public static void dialog(String id, String operation, DialogType type) {

		switch (type) {
		case BS:
			JSFUtility.execute(String.format("$('#%s').modal('%s');", id, operation));
			break;
		case PF:
			JSFUtility.execute(String.format("PF('%s').%s();", id, operation));
			break;
		case BS_ESCAPED:
			JSFUtility.execute(String.format("$(PrimeFaces.escapeClientId('%s')).modal('%s');", id, operation));
			break;
		default:
			System.out.println(
					String.format("msg=Bad arguments for dialog operation! args=[id=%s, operation=%s]", id, operation));
			return;
		}

	}

}
