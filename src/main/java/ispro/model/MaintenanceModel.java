package ispro.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.InstantDeserializer;
import ispro.model.json.InstantSerializer;

@JsonInclude(value=Include.NON_NULL)
public class MaintenanceModel {
	private Integer id;
	private String text;
	private String note;
	private String cause;
	private String causeOfError;
	private int operatingHours;
	private String technician1;
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	private Instant jobDate;
	private MaintenanceType maintenanceType;
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
		return maintenanceType.key();
	}
	public void setMaintenanceType(String type) {
		this.maintenanceType = MaintenanceType.fromKey(type);
	}
	public void setMaintenanceType(MaintenanceType maintenanceType) {
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
