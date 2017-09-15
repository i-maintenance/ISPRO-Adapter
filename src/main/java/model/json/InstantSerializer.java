package model.json;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class InstantSerializer extends JsonSerializer<Instant> {
//	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withLocale(Locale.GERMAN);
//
	@Override
	public void serialize(Instant date, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		String dateString = date.toString();
		generator.writeString(dateString);
	}
}
