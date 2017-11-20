package things.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import http.Client;

/**
 * SensorThings object wrapping single {@link JSONObject} retrieved from
 * SensorThings server
 * 
 * @author dglachs
 *
 */
public class STObject {
    private static final String NAVIGATION_LINK_SUFFIX = "@iot.navigationLink";
    private static final String LOCAL_NAVIGATION_HOST = "http://localhost:8080";
    private static final String SENSORTHINGS_API = "v1.0";
    private final JSONObject json;
    private final String REMOTE_HOST;
    private final String REMOTE_API_VERSION;

    /**
     * Constructor requiring a JSONObject
     * 
     * @param json
     */
    public STObject(JSONObject json, String host) {
        this.json = json;
        this.REMOTE_HOST = host;
        this.REMOTE_API_VERSION = SENSORTHINGS_API;
    }

    public STObject(JSONObject json, String host, String apiVersion) {
        this.json = json;
        this.REMOTE_HOST = host;
        this.REMOTE_API_VERSION = apiVersion;
    }

    /**
     * getter for the <code>@iot.id</code>
     * 
     * @return
     */
    public Long getId() {
        return get(Long.class, "@iot.id", null);
    }

    /**
     * getter for the <code>@iod.selfLink</code>
     * 
     * @return
     */
    public String getSelf() {
        return get(String.class, "@iot.selfLink", null);
    }

    /**
     * Getter for a single long value
     * 
     * @param id
     * @param def
     * @return
     */
    public Long getLong(String id, Long... def) {
        return get(Long.class, id, def);
    }

    /**
     * Getter for a single double value
     * 
     * @param id
     * @param def
     * @return
     */
    public Double getDouble(String id, Double... def) {
        return get(Double.class, id, def);
    }

    /**
     * Getter for a single String value
     * 
     * @param id
     * @param def
     * @return
     */
    public String getString(String id, String... def) {
        return get(String.class, id, def);
    }

    /**
     * Getter for a contained object, a {@link STObject} wrapping the
     * {@link JSONObject} is returned
     * 
     * @param id
     * @return <code>null</code> when required property is not present
     */
    public STObject getObject(String id) {
        if (id.endsWith(NAVIGATION_LINK_SUFFIX) && json.get(id) instanceof String) {
            return followPropertyLink(id);
        }
        JSONObject object = get(JSONObject.class, id, null);
        return new STObject(object, this.REMOTE_HOST);
    }

    public List<STObject> getObjects(String id) {
        if (id.endsWith(NAVIGATION_LINK_SUFFIX) && json.get(id) instanceof String) {

            return followPropertiesLinks(id);
        }
        JSONArray array = get(JSONArray.class, id, null);
        return makeList(array);
    }

    private List<STObject> makeList(Object jso) {
        List<STObject> res = new ArrayList<>();
        if (jso instanceof JSONArray) {
            JSONArray array = (JSONArray) jso;
            for (int i = 0; i < array.size(); i++) {
                Object o = array.get(i);
                if (o instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) o;
                    res.add(new STObject(jsonObj, this.REMOTE_HOST));
                }
            }
        }
        return res;
    }

    /**
     * Helper method to retrieve the contained values
     * 
     * @param t
     *            the (expected) type of the value
     * @param key
     *            The key of the value
     * @param def
     *            a default value
     * @return
     */
    private <T> T get(Class<T> t, String key, T[] def) {
        int slash = key.indexOf("/");
        if (slash > 0) {
            String part1 = key.substring(0, slash);
            STObject sub = getObject(part1);

            if (sub != null) {
                if (json.get(part1) instanceof String) {
                    // keep
                    json.put(part1, sub.json);
                }
                // use the remainder to get to the sub-object
                String remainder = key.substring(slash + 1);
                return sub.get(t, remainder, def);
            }
            throw new IllegalArgumentException("Cannot find contained object by key " + part1);
        }
        // no slash, direct property access
        Object o = json.get(key);
        if (o != null) {
            if (t.isInstance(o)) {
                return t.cast(o);
            }
            throw new IllegalArgumentException("Property is not of requested type " + key);
        } else {
            if (def != null && def.length > 0) {
                return def[0];
            }
            return null;
        }
    }

    private STObject followPropertyLink(String propertyName) {
        String link = get(String.class, propertyName, null);
        if (link != null) {
            return resolveLink(link);
        }
        return null;
    }

    private List<STObject> followPropertiesLinks(String propertyName) {
        String link = get(String.class, propertyName, null);
        if (link != null) {
            return resolveLinkToArray(link);
        }
        return null;
    }

    private STObject resolveLink(String link) {
        link = cleanupLink(link);
        // if (link.startsWith(LOCAL_NAVIGATION_HOST)) {
        // link = link.replace(LOCAL_NAVIGATION_HOST, REMOTE_HOST);
        // }
        try {
            String json = new Client(link).doGet();
            return parse(json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private List<STObject> resolveLinkToArray(String link) {
        link = cleanupLink(link);
        // if (link.startsWith(LOCAL_NAVIGATION_HOST)) {
        // link = link.replace(LOCAL_NAVIGATION_HOST, REMOTE_HOST);
        // }
        try {
            String json = new Client(link).doGet();
            return parseList(json);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private String cleanupLink(String link) {
        if (link.startsWith(LOCAL_NAVIGATION_HOST)) {
            link = link.replace(LOCAL_NAVIGATION_HOST, REMOTE_HOST);
        }

        return link;
    }

    private STObject parse(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        Object o;
        o = parser.parse(json);
        if (o instanceof JSONObject) {
            return new STObject((JSONObject) o, REMOTE_HOST);
        }
        return null;

    }

    private List<STObject> parseList(String json) throws ParseException {

        JSONParser parser = new JSONParser();
        Object o = parser.parse(json);
        if (o instanceof JSONObject) {
            JSONObject jso = (JSONObject) o;
            List<STObject> list = new ArrayList<>();
            if (jso.get("value") instanceof JSONArray) {
                // now create the list
                list.addAll(makeList(jso.get("value")));
            }
            if (jso.get("@iot.nextLink") != null) {
                //
                String nextLink = (String) jso.get("@iot.nextLink");
                list.addAll(resolveLinkToArray(nextLink));
            }
            return list;

        }
        return new ArrayList<>();

    }
}
