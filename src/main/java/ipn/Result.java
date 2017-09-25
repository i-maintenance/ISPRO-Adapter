package ipn;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value=Include.NON_NULL)
public abstract class Result {
	@JsonProperty("text")
	private String text;
	
	public Result() {
		
	}
	public Result(String message) {
		this.text = message;
	}
	public String getText() {
		return text;
	}
	public void setText(String message) {
		this.text = message;
	}
	
	public abstract Message<?> createPayload(Long datastream, Instant phenomenon);
	
}
