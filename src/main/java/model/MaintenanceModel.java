package model;

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.json.InstantDeserializer;
import model.json.InstantSerializer;

public class MaintenanceModel {
	private int id;
	private String text;
	private String note;
	private String cause;
	private String causeOfError;
	private int operatingHours;
	private String technician1;
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	private Instant jobDate;
	private String maintenanceType;
	private int priority;
	private String author;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCauseOfError() {
		return causeOfError;
	}
	public void setCauseOfError(String causeOfError) {
		this.causeOfError = causeOfError;
	}
	public int getOperatingHours() {
		return operatingHours;
	}
	public void setOperatingHours(int operatingHours) {
		this.operatingHours = operatingHours;
	}
	public String getTechnician1() {
		return technician1;
	}
	public void setTechnician1(String technician1) {
		this.technician1 = technician1;
	}
	public Instant getJobDate() {
		return jobDate;
	}
	public void setJobDate(Instant jobDate) {
		this.jobDate = jobDate;
	}
	public String getMaintenanceType() {
		return maintenanceType;
	}
	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
