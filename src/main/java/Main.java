import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ispro.ISPROAdapter;
import ispro.impl.ISPROAdapterImpl;
import kafka.ISPROConsumer;
/**
 * Dummy runner
 * @author dglachs
 *
 */
public class Main {
	public static void main(String[] args) {
		int numConsumers = 3;
		String groupId = "imaintenance-ispro-group";
		List<String> topics = Arrays.asList("Malfunctions");
		ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

		final List<ISPROConsumer> consumers = new ArrayList<>();
		final ISPROAdapter isproAdapter = new ISPROAdapterImpl();
		for (int i = 0; i < numConsumers; i++) {
			// 
			ISPROConsumer consumer = new ISPROConsumer(i, "il061:9092", groupId, isproAdapter, topics);
			consumers.add(consumer);
			executor.submit(consumer);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (ISPROConsumer consumer : consumers) {
					consumer.shutdown();
				}
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
