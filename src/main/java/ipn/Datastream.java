package ipn;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Simple object holding the datastream id (@iot.id)
 * @author dglachs
 *
 */
public class Datastream {
	@JsonProperty("@iot.id")
	private Long id;

	public Datastream() {
		// default
	}
	public Datastream(Long id) {
		this.id = id;
	}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
