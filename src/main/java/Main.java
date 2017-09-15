import java.util.Arrays;
import java.util.List;

import ispro.impl.ISPROAdapterImpl;
import kafka.ISPROConsumer;
/**
 * Dummy runner
 * @author dglachs
 *
 */
public class Main {
	public static void main(String[] args) {
		String groupId = "ipn-consumer-group";
		// 
		List<String> topics = Arrays.asList("node-red-message");
		// create and run a new consumer
		ISPROConsumer consumer = new ISPROConsumer(1, "il061:9092", groupId, new ISPROAdapterImpl(), topics);
		Thread runner = new Thread(consumer);
		
		runner.start();
		
		
	}

}
