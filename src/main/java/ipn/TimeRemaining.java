package ipn;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.TemporalAmountDeserializer;
import ispro.model.json.TemporalAmountSerializer;
/**
 * Dedicated result class specifying a remaining time and a confidence
 * @author dglachs
 *
 */
public class TimeRemaining extends Result {
	
	@JsonProperty("confidence")
	private Double confidence;
	@JsonSerialize(using=TemporalAmountSerializer.class)
	@JsonDeserialize(using=TemporalAmountDeserializer.class)
	@JsonProperty("remainingTime")
	private TemporalAmount remainingTime; 
	
	public TimeRemaining() {
		// default constructor
	}
	public TimeRemaining(TemporalAmount remainingTime, double confidence) {
		super();
		this.remainingTime = remainingTime;
		this.confidence = confidence;
	}
	public TimeRemaining(TemporalAmount remainingTiem, Double confidence, String message) {
		super(message);
		this.remainingTime = remainingTiem;
		this.confidence = confidence;
	}
	public Double getConfidence() {
		return confidence;
	}
	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}
	public TemporalAmount getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(TemporalAmount remainingTime) {
		this.remainingTime = remainingTime;
	}
	@Override
	public RemainingTimeMessage createPayload(Long datastream, Instant phenomenon) {
		RemainingTimeMessage message = new RemainingTimeMessage(this);
		message.setDatastream(new Datastream(datastream));
		message.setPhenonmenonTime(phenomenon);
		message.setResultTime(Instant.now());
		message.setValidTime(Duration.ofSeconds(30));
		return message;

	}

}
