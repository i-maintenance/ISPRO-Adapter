package things.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThingsDataStream {
	@JsonProperty("@iot.id")
	public Long id;
	@JsonProperty("@iot.selfLink")
	public String selfLink;
	public String name;
	public String description;
	public ThingsUnit unitOfMeasurement;
	public String observationType;
	@JsonProperty("Thing@iot.navigationLink")
	public String thingsLink;
	@JsonProperty("Sensor@iot.navigationLink")
	public String sensorLink;
	@JsonProperty("Observations@iot.navigationLink")
	public String observationsLink;
	@JsonProperty("ObservedProperty@iot.navigationLink")
	public String observedPropertyLink;
}
