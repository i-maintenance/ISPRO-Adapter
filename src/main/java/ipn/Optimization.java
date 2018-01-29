package ipn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Optimization extends IPNAlert {
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
    public String getAlertType() {
        return "http://www.predictive.at/maintenance/optimization";
    }
}
