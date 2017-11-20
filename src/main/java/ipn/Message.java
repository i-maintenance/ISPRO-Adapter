package ipn;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.InstantDeserializer;
import ispro.model.json.InstantSerializer;
import ispro.model.json.TemporalAmountDeserializer;
import ispro.model.json.TemporalAmountSerializer;
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "alertType"
)
@JsonSubTypes({
	@Type(value=AlertMessage.class, name="http://www.predictive.at/maintenance/alert"),
	@Type(value=RemainingTimeMessage.class, name="http://www.predictive.at/maintenance/remainingTimeAlert"),
	@Type(value=QualityMessage.class, name="http://www.predictive.at/maintenance/qualityAlert"),
	
	


})

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public abstract class Message<T extends Result> {
	/**
	 * The time instant or period of when the Observation happens.
	 */
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	private Instant phenonmenonTime;
	/**
	 * The time instant the result was generated
	 */
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	private Instant resultTime;
	/**
	 * The time until when the message & result is valid
	 */
	@JsonDeserialize(using=TemporalAmountDeserializer.class)
	@JsonSerialize(using=TemporalAmountSerializer.class)
	private TemporalAmount validTime;

	@JsonProperty("Datastream")
	private Datastream datastream;
	@JsonProperty("observationType")
	private String observationType = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation";
	@JsonProperty("parameters")
	private Map<String, String> parameters;

	@JsonProperty("result")
	private T result;
	/**
	 * Default constructor
	 */
	public Message() {
		// required default constructor
	}
	protected Message(T result) {
		this.result = result;
	}
	public void addParameter(String key, String value) {
		if ( parameters == null ) {
			parameters = new HashMap<>();
		}
		parameters.put(key, value);
	}
	/**
	 * 
	 */
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithView(this.getClass()).writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}
	}
	public Instant getPhenonmenonTime() {
		return phenonmenonTime;
	}
	public void setPhenonmenonTime(Instant phenonmenonTime) {
		this.phenonmenonTime = phenonmenonTime;
	}
	public Instant getResultTime() {
		return resultTime;
	}
	public void setResultTime(Instant resultTime) {
		this.resultTime = resultTime;
	}
	public TemporalAmount getValidTime() {
		return validTime;
	}
	public void setValidTime(TemporalAmount validTime) {
		this.validTime = validTime;
	}
	public Datastream getDatastream() {
		return datastream;
	}
	public void setDatastream(Datastream datastream) {
		this.datastream = datastream;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public Boolean arrivedInTime() {
	    if (resultTime!=null && validTime!=null) {
	        if (Instant.now().isBefore(getResultTime().plus(getValidTime()))) {
	            return true;
	        }
	        return false;
	    }
	    return null;
	}
}
