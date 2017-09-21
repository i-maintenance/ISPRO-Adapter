package ispro.model.json;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

public class TemporalAmountDeserializer extends JsonDeserializer<TemporalAmount> {
	 
    @Override
    public TemporalAmount deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
 
        ObjectCodec oc = jp.getCodec();
        TextNode node = (TextNode) oc.readTree(jp);
        try {
        		return parse(node.textValue());
        } catch (DateTimeParseException e) {
        		return Period.ZERO;
        }
     }
    private TemporalAmount parse(String text) throws DateTimeParseException {
    		try {
    			// try duration first
    			return Duration.parse(text);
    		} catch (DateTimeParseException e) {
    			// no duration, the try period
    			return Period.parse(text);
    		}
    }
}