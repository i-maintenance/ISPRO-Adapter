package model;

import java.util.List;
import java.util.Map;

public class StandardMessage {
	private int code;
	private String message;
	private String ressource;
	private boolean error;
	private String stacktrace;
	private List<Object> parameter;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRessource() {
		return ressource;
	}
	public void setRessource(String ressource) {
		this.ressource = ressource;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getStacktrace() {
		return stacktrace;
	}
	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}
	public List<Object> getParameter() {
		return parameter;
	}
	public void setParameter(List<Object> parameter) {
		this.parameter = parameter;
	}

}
