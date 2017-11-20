package things.model;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import http.Client;

public class STClient {
    private static final String SENSORTHINGS_HOST = "http://il060:8082";
    private static final String SENSORTHINGS_API_VERSION = "v1.0";

    public static STObject getThing(long id) {
        try {
            String json = new Client(SENSORTHINGS_HOST)
                    .setPath(SENSORTHINGS_API_VERSION + "/" + String.format("Things(%s)", id)).doGet();
            return parse(json);
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static STObject getDatastream(long id) {
        try {
            String json = new Client(SENSORTHINGS_HOST)
                    .setPath(String.format(SENSORTHINGS_API_VERSION + "/" + "Datastreams(%s)", id)).doGet();
            return parse(json);

        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static STObject getSensor(long id) {
        try {
            String json = new Client(SENSORTHINGS_HOST)
                    .setPath(SENSORTHINGS_API_VERSION + "/" + String.format("Sensors(%s)", id))
                    .doGet();
            return parse(json);
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static STObject parse(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        Object o;
        o = parser.parse(json);
        if (o instanceof JSONObject) {
            return new STObject((JSONObject) o, SENSORTHINGS_HOST);
        }
        return null;

    }

}
