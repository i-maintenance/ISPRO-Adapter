package ipn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Datastream {
	@JsonProperty("@iot.id")
	public Long id;

	public Datastream() {
		// default
	}
	public Datastream(Long id) {
		this.id = id;
	}
}
