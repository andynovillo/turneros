package utility;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;

import javafx.scene.control.Cell;
import sessionBean.auth.OptionFacadeLocal;
import sessionBean.pub.ParameterFacadeLocal;

public final class Utility {

	/* Files */

	public static String DEFAULT_TITLE = "Datos";
	public static String DEFAULT_XLSX_FILE_NAME = "Datos.xlsx";

	/* Content type */

	public static String CT_XLS = "application/vnd.ms-excel";
	public static String CT_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static String CT_DOC = "application/msword";
	public static String CT_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static String CT_XLSM = "application/vnd.ms-excel.sheet.macroEnabled.12";
	public static String CT_PPT = "application/vnd.ms-powerpoint";
	public static String CT_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

	/* Date */

	public static String BASE_DATE = "19700101";
	public static String LIMIT_DATE = "99991231";

	/* String */

	public static String NO_ANSWER_STRING = "N/A";
	public static String NULL_STRING = "N/D";

	private Utility() {

	}

	public static SimpleDateFormat longDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy");

	public static Date toDate(LocalDate localDate) {

		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);

	}

	public static LocalDate toLocalDate(Date date) {

		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

	}

	public static Date toDate(LocalDateTime localDateTime) {

		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

	}

	public static long getDateDiff(Date start, Date end) {

		if (start == null || end == null)
			return 0;

		LocalDate dateBefore = toLocalDate(start);
		LocalDate dateAfter = toLocalDate(end);

		return java.time.temporal.ChronoUnit.DAYS.between(dateBefore, dateAfter);

	}

	public static void printInfo(Object... strings) {

		if (strings != null && strings.length > 0) {

			int size = strings.length;

			for (int index = 0; index < size; index++) {
				if (index < size - 1)
					System.out.print(strings[index] + " - ");
				else
					System.out.print(strings[index]);
			}

		}

		System.out.println();

	}

	public static String removeWhiteSpaces(String string) {

		return string != null ? string.trim().replaceAll("\\s+", " ") : "";

	}

	/**
	 * 
	 * Fecha a formato AAAAMMDD.
	 * 
	 * @param date La fecha que se desea dar formato
	 * @return La fecha en formato AAAAMMDD como texto
	 */
	public static String formatDate(Date date) {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		return dateFormat.format(date);

	}

	/**
	 * 
	 * Texto AAAAMMDD a fecha.
	 * 
	 * @param date El texto a ser transformado
	 * @return El texto AAAAMMDD en tipo fecha
	 */
	public static Date parseString(String date) {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		try {
			return dateFormat.parse(date);
		} catch (ParseException exception) {
			System.out.println("Exception: " + exception.getMessage());
			System.out.println("Can't parse date " + date);
		}

		return Calendar.getInstance().getTime();

	}

	public static String genericFormatter(Date date, String pattern) {

		if (date != null) {

			DateFormat dateFormat = new SimpleDateFormat(pattern);

			return dateFormat.format(date);

		} else {

			return null;

		}

	}

	/* Text */

	public static final Pattern DIACRITICS_AND_FRIENDS = Pattern
			.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	public static String stripDiacritics(String string) {
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string = DIACRITICS_AND_FRIENDS.matcher(string).replaceAll("");
		return string;
	}

	public static Date genericParser(String date, String pattern) {

		DateFormat dateFormat = new SimpleDateFormat(pattern);

		try {
			return dateFormat.parse(date);
		} catch (ParseException exception) {
			printInfo("genericParser!", date, pattern);
			return null;
		}

	}

	public static String[] defineColumns(String string) {

		String[] columnList = string.split(",");

		for (int index = 0; index < columnList.length; index++) {
			columnList[index] = columnList[index].trim().split(":")[0];
		}

		return columnList;

	}

	public static void setCellValue(Cell cell, Object object) {

		if (object == null)
			return;
		if (object instanceof Integer)
			cell.setCellValue((int) object);
		else if (object instanceof Double)
			cell.setCellValue((double) object);
		else if (object instanceof String)
			cell.setCellValue((String) object);
		else if (object instanceof BigDecimal) {
			cell.setCellValue(new BigDecimal(object.toString()).doubleValue());
		} else
			cell.setCellValue(object.toString());

	}

	public static short paintCell(Cell cell) {

		DataFormatter formatter = new DataFormatter();

		String value = formatter.formatCellValue(cell);

		switch (value) {
		case "grey":
			return IndexedColors.GREY_25_PERCENT.getIndex();
		case "light yellow":
			return IndexedColors.LIGHT_YELLOW.getIndex();
		case "light green":
			return IndexedColors.LIGHT_GREEN.getIndex();
		case "light orange":
			return IndexedColors.LIGHT_ORANGE.getIndex();
		case "light blue":
			return IndexedColors.LIGHT_BLUE.getIndex();
		case "yellow":
			return IndexedColors.YELLOW.getIndex();
		case "green":
			return IndexedColors.GREEN.getIndex();
		case "orange":
			return IndexedColors.ORANGE.getIndex();
		case "blue":
			return IndexedColors.BLUE.getIndex();
		case "red":
			return IndexedColors.RED.getIndex();
		case "aqua":
			return IndexedColors.AQUA.getIndex();
		case "coral":
			return IndexedColors.CORAL.getIndex();
		case "violet":
			return IndexedColors.LAVENDER.getIndex();
		default:
			return IndexedColors.WHITE.getIndex();
		}

	}

	public static void printExceptionMessage(Exception exception) {

		System.err.print(String.format("Exception: %s \n", exception.getMessage()));

	}

	public static void printErrorMessage(String string, Exception exception) {

		System.err.print(String.format("Message: %s \n", string));
		printExceptionMessage(exception);

	}

	/**
	 * @param className -> this.getClass().getName()
	 * @param method    -> MethodName.methodName()
	 * @param message   -> General message
	 * @param exception -> Exception object
	 * @return Nothing
	 */

	public static void printError(String className, String method, String string, Exception exception) {

		System.err.print(String.format("Class: %s\nMethod: %s\n", className, method));
		printErrorMessage(string, exception);

	}

	public static String getProperty(String string) {

		return PropertyContainer.getProperty(string);

	}

	static String parameterJNDI = "java:global/AS400-Integration/AS400-IntegrationEJB/ParameterFacade!sessionBeans.ParameterFacadeLocal";

	static String optionJNDI = "java:global/AS400-Integration/AS400-IntegrationEJB/OptionFacade!sessionBeans.OptionFacadeLocal";

	public static String getParamater(String parameter) {

		ParameterFacadeLocal parameterFacadeLocal = null;
		try {
			parameterFacadeLocal = (ParameterFacadeLocal) new InitialContext().lookup(parameterJNDI);
		} catch (NamingException exception) {
			Utility.printErrorMessage("Module not found!", exception);
		}

		try {

			return parameterFacadeLocal.findByParameter(parameter).getValue();

		} catch (Exception exception) {
			Utility.printErrorMessage("Parameter not found!", exception);
		}

		return null;

	}

	public static List<Option> getOptions() {

		OptionFacadeLocal facadeLocal = null;
		try {
			facadeLocal = (OptionFacadeLocal) new InitialContext().lookup(optionJNDI);
			return facadeLocal.findAll();
		} catch (NamingException exception) {
			Utility.printErrorMessage("Module not found!", exception);
		}
		return null;

	}

	// Old Method
	@Deprecated
	public static List<ColumnModel> defineColumnModelList(String parameter) {

		ParameterFacadeLocal parameterFacadeLocal = null;
		try {
			parameterFacadeLocal = (ParameterFacadeLocal) new InitialContext().lookup(parameterJNDI);
		} catch (NamingException exception) {
			Utility.printErrorMessage("Module not found!", exception);
		}

		try {

			List<ColumnModel> columnList = new ArrayList<>();

			for (String string : parameterFacadeLocal.findByParameter(parameter).getValue().split(",")) {

				String[] text = string.trim().split(":");

				// 0 : Header
				// 1 : Column width class
				// 2 : Aditional column class
				// 3 : Paintable
				// 4 : Rendered
				// 5 : Exportable

				columnList.add(new ColumnModel(text[0].toString(), null,
						getColumnClass(text[1].toString()) + text[2].toString(), Boolean.valueOf(text[3].toString())));

			}

			return columnList;

		} catch (Exception exception) {
			Utility.printErrorMessage("Parameter not found!", exception);
		}

		return null;

	}

	// New Method
	public static List<ColumnModel> getColumnModelList(String parameter) {

		ParameterFacadeLocal parameterFacadeLocal = null;
		try {
			parameterFacadeLocal = (ParameterFacadeLocal) new InitialContext().lookup(parameterJNDI);
		} catch (NamingException exception) {
			Utility.printErrorMessage("Module not found!", exception);
		}

		try {

			List<ColumnModel> columnList = new ArrayList<>();

			for (String string : parameterFacadeLocal.findByParameter(parameter).getValue().split(",")) {

				String[] text = string.trim().split(":");

				// 0 : Header
				// 1 : Column width class
				// 2 : Aditional column class
				// 3 : Paintable
				// 4 : Rendered
				// 5 : Exportable

				columnList.add(new ColumnModel(text[0].toString(), null,
						getColumnClass(text[1].toString()) + text[2].toString(), Boolean.valueOf(text[3].toString()),
						Boolean.valueOf(text[4].toString()), Boolean.valueOf(text[5].toString())));

			}

			return columnList;

		} catch (Exception exception) {

			Utility.printErrorMessage("Parameter not found! " + parameter, exception);
		}

		return null;

	}

	public static String[] getColumnHeaders(List<ColumnModel> columnModelList) {

		String[] columnList = new String[0];

		if (columnModelList != null) {

			columnList = new String[columnModelList.size()];

			for (int index = 0; index < columnModelList.size(); index++) {
				columnList[index] = columnModelList.get(index).getHeader();
			}

		}

		return columnList;

	}

	public static String getColumnClass(String string) {

		if (string != null) {

			switch (string) {

			case "xs-btn":
				return "xs-btn-col";
			case "s-btn":
				return "s-btn-col";
			case "btn":
				return "btn-col";
			case "xs":
				return "xs-col";
			case "s":
				return "s-col";
			case "c":
				return "c-col";
			case "cr":
				return "cr-col";
			case "r":
				return "r-col";
			case "n":
				return "n-col";
			case "ms":
				return "ms-col";
			case "m":
				return "m-col";
			case "ml":
				return "ml-col";
			case "l":
				return "l-col";
			case "xl":
				return "xl-col";
			case "f":
				return "full-col";
			case "a":
				return "auto-col";

			}

		}

		return "";

	}

	public static String saveFileToDisk(FileWrapper fileWrapper) {

		if (fileWrapper.getFilePath() == null || fileWrapper.getFileName() == null || fileWrapper.getContent() == null)
			return null;

		try {

			fileWrapper.setRealName(FilenameUtils.removeExtension(fileWrapper.getFileName()));
			fileWrapper.setFileName(clearFileName(fileWrapper.getFileName()));

			Path path = Paths.get(fileWrapper.getFilePath(), fileWrapper.getFileName());

			if (path.toFile().exists()) {
				fileWrapper.setFileName(Calendar.getInstance().getTimeInMillis() + fileWrapper.getFileName());
			} else {
				// nothing to do
			}

		} catch (Exception exception) {

			Utility.printErrorMessage("Can't delete file!", exception);

		}

		try {

			Path path = Paths.get(fileWrapper.getFilePath(), fileWrapper.getFileName());

			if (fileWrapper.getContent() != null)
				FileUtils.writeByteArrayToFile(path.toFile(), fileWrapper.getContent());

			return path.toString();

		} catch (Exception exception) {

			Utility.printErrorMessage("Can't save file to disk!", exception);

		}

		return null;

	}

	public static String clearFileName(String fileName) {

		return fileName.trim().replaceAll("[^a-zA-Z0-9.-]", "_").toLowerCase();

	}

	public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	public static String generateArray(List<Long> objects) {

		int size = 0;
		size = objects.size();

		String string = "";

		for (int index = 0; index < size; index++) {

			if (index < size - 1)
				string += objects.get(index) + ",";
			else
				string += objects.get(index);

		}

		return string;
	}

}
