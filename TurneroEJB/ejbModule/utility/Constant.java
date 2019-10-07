package utility;

public final class Constant {

	private Constant() {

	}

	/*
	 * General
	 */

	public final static int PASSWORD_EXPIRATION_TIME = 12; // Months
	public final static int PASSWORD_MIN_LENGTH = 6; // Chars

	/*
	 * ResultData
	 */

	public final static int METHOD_STATUS = 0;
	public final static int METHOD_MESSAGE = 1;
	public final static int METHOD_RESULT_DATA = 2;

	/*
	 * Strings
	 */

	public final static String HTMC = "Hospital de Especialidades Teodoro Maldonado Carbo";
	public final static String EMPTY_DATA = "No existen datos para presentar";

	/*
	 * Pages
	 */

	public final static String PAGE_HOME = "/pages/home.jsf";
	public final static String PAGE_CHANGE_PASSWORD = "/config/cambiarContrasena.jsf";
	public final static String PAGE_CHANGE_USER_DATA = "/config/cambiarInfoUsuario.jsf";

	/*
	 * Turnero
	 */

	public final static String TCK_GENERAR = "G";
	public final static String TCK_LLAMAR = "L";
	public final static String TCK_TODO = "T";

	public final static String TCK_AG_USUARIO = "U";
	public final static String TCK_AG_SALA = "S";
	public final static String TCK_AG_TODO = "T";

}
