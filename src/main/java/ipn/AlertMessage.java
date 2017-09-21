package ipn;

import java.time.temporal.TemporalAmount;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.TemporalAmountDeserializer;
import ispro.model.json.TemporalAmountSerializer;

@JsonInclude(value=Include.NON_NULL)
public class AlertMessage extends Message {
	@JsonSerialize(using=TemporalAmountSerializer.class)
	@JsonDeserialize(using=TemporalAmountDeserializer.class)
	@JsonProperty("remainingTime")
	public TemporalAmount remainingTime; 

}
