package ipn;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.TemporalUnit;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IPNMessageTest {
	@Test
	public void testMessage() throws Exception {
		AlertMessage alert = new AlertMessage();
		alert.phenonmenonTime = Instant.now();
		alert.resultTime = Instant.now();
		alert.datastream = new Datastream(10l);
		alert.text = "Alert Message";
		alert.remainingTime = Duration.ofMinutes(20);
		alert.remainingTime = Period.of(3, 2, 1);
		AlertMessage sent = serializeToJson(AlertMessage.class, alert);
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
