package ispro.model.json;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TemporalAmountSerializer extends JsonSerializer<TemporalAmount> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withLocale(Locale.GERMAN);
//
	@Override
	public void serialize(TemporalAmount date, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		
		generator.writeString(date.toString());
	}
}
