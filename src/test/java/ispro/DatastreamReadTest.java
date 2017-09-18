package ispro;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import http.Client;
import things.model.STObject;

public class DatastreamReadTest {
	@Test
	public void testDatastream() throws Exception {
		String s = new Client("http://il060:8082/v1.0/")
			.setPath("DataStreams(2)/Sensor")
			.doGet();
		
		String json = "[{\n" + 
				"   \"@iot.id\": 2,\n" + 
				"   \"@iot.selfLink\": \"http://localhost:8080/v1.0/Datastreams(2)\",\n" + 
				"   \"name\": \"Filament Skidrate DS\",\n" + 
				"   \"description\": \"Skid rate per unit length at feeding time.\",\n" + 
				"   \"unitOfMeasurement\": {\n" + 
				"      \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#PerMeter\",\n" + 
				"      \"name\": \"Units per meter\",\n" + 
				"      \"symbol\": \"1/m\"\n" + 
				"   },\n" + 
				"   \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" + 
				"   \"Thing@iot.navigationLink\": \"http://localhost:8080/v1.0/Datastreams(2)/Thing\",\n" + 
				"   \"Sensor@iot.navigationLink\": \"http://localhost:8080/v1.0/Datastreams(2)/Sensor\",\n" + 
				"   \"Observations@iot.navigationLink\": \"http://localhost:8080/v1.0/Datastreams(2)/Observations\",\n" + 
				"   \"ObservedProperty@iot.navigationLink\": \"http://localhost:8080/v1.0/Datastreams(2)/ObservedProperty\"\n" + 
				"}]";
		try {
			//ThingsDataStream s = mapper.readValue(json, ThingsDataStream.class);
			JSONParser parser = new JSONParser();
			
			Object o = parser.parse(json);
			if ( o instanceof JSONObject) {
				STObject to = new STObject((JSONObject)o, "http://il060:8082/v1.0/");
				System.out.println(to.getLong("@iot.id"));
				
				System.out.println(to.getString("unitOfMeasurement/name"));
				System.out.println(to.getString("unitOfMeasurement/definition"));
			}
			if ( o instanceof JSONArray) {
				Object oList = ((JSONArray) o).get(0);
				
				STObject to = new STObject((JSONObject)oList,"http://il060:8082/v1.0/");
				System.out.println(to.getLong("@iot.id"));
				
				System.out.println(to.getString("unitOfMeasurement/name"));
				System.out.println(to.getString("unitOfMeasurement/definition"));
				STObject sensor = to.getObject("Sensor@iot.navigationLink");
				System.out.println(sensor.getSelf());
				System.out.println(to.getString("Sensor@iot.navigationLink/name"));
				System.out.println(to.getLong("Sensor@iot.navigationLink/@iot.id"));
				System.out.println(to.getString("Sensor@iot.navigationLink/description"));
				
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
