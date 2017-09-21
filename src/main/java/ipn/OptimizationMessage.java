package ipn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value=Include.NON_NULL)
public class OptimizationMessage extends Message {
	@JsonProperty("currentValue")
	public Double currentValue; 
	@JsonProperty("targetValue")
	public Double targetValue; 

}
