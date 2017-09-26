package ipn;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.Instant;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IPNMessageTest {
	@Test
	public void testMessage() throws Exception {
		
		AlertMessage alert = new AlertMessage();
		alert.setResult(new SimpleAlert("The alert"));
		SimpleAlert result = new SimpleAlert("The alert");
		AlertMessage am = result.createPayload(2l, Instant.now().minusSeconds(2));
		AlertMessage transformed = serializeToJson(AlertMessage.class, am);
		
		ProductQuality q = new ProductQuality(0.8, 0.5);
		
		
		QualityMessage qm = serializeToJson(QualityMessage.class, q.createPayload(3l, Instant.now()));
		qm.getResult().getConfidence();
//		alert.datastream = new Datastream(20l);
//		alert.phenonmenonTime = Instant.now();
//		alert.resultTime = Instant.now();
//		alert.setResultMessage("Message provided by IPN");
//		alert.validTime = Duration.ofMinutes(2);
//		
//		AlertMessage sent = serializeToJson(AlertMessage.class, alert);
//		
//		RemainingTimeMessage remaining = new RemainingTimeMessage();
//		remaining.datastream = new Datastream(20l);
//		remaining.phenonmenonTime = Instant.now();
//		remaining.resultTime = Instant.now();
//		remaining.setResult(Duration.ofHours(200), 0.8, "Problem mit Wahrscheinlichkeit 80% in 200h");
//		remaining.validTime = Duration.ofMinutes(2);
//
//		RemainingTimeMessage timeRemaining = serializeToJson(RemainingTimeMessage.class, remaining);

	}

	
	private <T> T serializeToJson(Class<T> clazz, T object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
			System.out.println(object.getClass().getSimpleName()+":  " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("" + e.getLocalizedMessage());
		}
		try {
			T result = mapper.readValue(json, clazz);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			fail("" + e.getLocalizedMessage());
		}
		return null;
	}


}
