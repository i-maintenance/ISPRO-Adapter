import java.time.Instant;

import ipn.SimpleAlert;
import kafka.IPNProducer;
/**
 * Dummy runner
 * @author dglachs
 *
 */
public class Main2 {
	public static void main(String[] args) {
		String groupId = "imaintenance-ispro-group";
		IPNProducer producer = new IPNProducer("ipn", "il061:9092", groupId);
		
		producer.sendAlert("ipn-maintenance-alert", "alert", new SimpleAlert("Message").createPayload(2l, Instant.now()));
		producer.shutdown();

	}

}
