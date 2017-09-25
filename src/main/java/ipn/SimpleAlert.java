package ipn;

import java.time.Duration;
import java.time.Instant;

public class SimpleAlert extends Result {
	public SimpleAlert() {
		super();
	}
	public SimpleAlert(String message) {
		this.setText(message);
	}
	@Override
	public AlertMessage createPayload(Long datastream, Instant phenomenon) {
		AlertMessage message = new AlertMessage(this);
		message.setDatastream(new Datastream(datastream));
		message.setPhenonmenonTime(phenomenon);
		message.setResultTime(Instant.now());
		message.setValidTime(Duration.ofSeconds(30));
		return message;
	}

}
