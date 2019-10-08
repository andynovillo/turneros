package utility;

import java.util.HashMap;
import java.util.List;

public class ResultData {

	private Boolean status;
	private String message;
	private HashMap<String, Object> result;
	private List<String> columnList;
	private List<?> resultList;
	private Integer errorType;
	private Boolean showErrorPanel;

	public ResultData() {

	}

	public Integer getErrorType() {
		return errorType;
	}

	public void setErrorType(Integer errorType) {
		this.errorType = errorType;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<?> getResultList() {
		return resultList;
	}

	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}

	public Boolean getShowErrorPanel() {
		return showErrorPanel;
	}

	public void setShowErrorPanel(Boolean showErrorPanel) {
		this.showErrorPanel = showErrorPanel;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public HashMap<String, Object> getResult() {
		return result;
	}

	public void setResult(HashMap<String, Object> result) {
		this.result = result;
	}

}
