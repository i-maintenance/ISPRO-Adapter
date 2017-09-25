package ipn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value=Include.NON_NULL)
public class RemainingTimeMessage extends Message<TimeRemaining> {
	@JsonProperty("result")
	private TimeRemaining result;

	public RemainingTimeMessage() {
		// default
	}
	public RemainingTimeMessage(TimeRemaining tr) {
		super(tr);
	}
}
