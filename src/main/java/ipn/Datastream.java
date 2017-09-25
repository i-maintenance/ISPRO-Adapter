package ipn;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Simple object holding the datastream id (@iot.id)
 * @author dglachs
 *
 */
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
