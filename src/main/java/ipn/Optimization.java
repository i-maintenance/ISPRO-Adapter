package ipn;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Optimization extends Result {
	@JsonProperty("currentValue")
	private Double currentValue; 
	@JsonProperty("targetValue")
	private Double targetValue;
	
	public Double getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}
	public Double getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(Double targetValue) {
		this.targetValue = targetValue;
	}
	@Override
	public Message<?> createPayload(Long datastream, Instant phenomenon) {
		// TODO Auto-generated method stub
		return null;
	} 
}
