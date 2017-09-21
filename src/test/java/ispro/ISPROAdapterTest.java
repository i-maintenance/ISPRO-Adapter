package ispro;

import org.junit.Test;

import ispro.impl.ISPROAdapterImpl;

public class ISPROAdapterTest {

	@Test
	public void sendDemoAlert() throws Exception {
		ISPROAdapter adapter = new ISPROAdapterImpl();
		adapter.processMessage("unused", "text", "a simple payload");
		
	}
}
