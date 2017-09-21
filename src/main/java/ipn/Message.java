package ipn;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.InstantDeserializer;
import ispro.model.json.InstantSerializer;
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public abstract class Message {
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	public Instant phenonmenonTime;
	@JsonDeserialize(using=InstantDeserializer.class)
	@JsonSerialize(using=InstantSerializer.class)
	public Instant resultTime;
	@JsonProperty("text")
	public String text;
	@JsonProperty("Datastream")
	public Datastream datastream;

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
}
