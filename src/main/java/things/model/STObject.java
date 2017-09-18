package things.model;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import http.Client;
/**
 * SensorThings object wrapping single {@link JSONObject} 
 * retrieved from SensorThings server
 * @author dglachs
 *
 */
public class STObject {
	private static final String NAVIGATION_LINK_SUFFIX = "@iot.navigationLink";
	private static final String LOCAL_NAVIGATION_PREFIX = "http://localhost:8080/v1.0/";
	private final JSONObject json;
	private final String REMOTE_NAVIGATION_PREFIX;
	/**
	 * Constructor requiring a JSONObject
	 * @param json
	 */
	public STObject(JSONObject json, String prefix) {
		this.json = json;
		this.REMOTE_NAVIGATION_PREFIX = prefix;
	}

	/**
	 * getter for the <code>@iot.id</code>
	 * @return
	 */
	public Long getId() {
		return get(Long.class, "@iot.id", null);
	}
	/**
	 * getter for the <code>@iod.selfLink</code>
	 * @return
	 */
	public String getSelf() {
		return get(String.class, "@iot.selfLink", null);
	}
	/**
	 * Getter for a single long value
	 * @param id
	 * @param def
	 * @return
	 */
	public Long getLong(String id, Long ...def) {
		return get(Long.class, id, def);
	}
	/**
	 * Getter for a single double value
	 * @param id
	 * @param def
	 * @return
	 */
	public Double getDouble(String id, Double ...def) {
		return get(Double.class, id, def);
	}
	/**
	 * Getter for a single String value
	 * @param id
	 * @param def
	 * @return
	 */
	public String getString(String id, String ...def) {
		return get(String.class, id, def);
	}
	/** 
	 * Getter for a contained object, a {@link STObject} wrapping
	 * the {@link JSONObject} is returned
	 * @param id
	 * @return <code>null</code> when required property is not present
	 */
	public STObject getObject(String id) {
		if ( id.endsWith(NAVIGATION_LINK_SUFFIX)) {
			return followLink(id);
		}
		JSONObject object = get(JSONObject.class, id, null);
		return new STObject(object, this.REMOTE_NAVIGATION_PREFIX);
	}
	/**
	 * Helper method to retrieve the contained values
	 * @param t the (expected) type of the value
	 * @param key The key of the value
	 * @param def a default value
	 * @return
	 */
	private <T> T get(Class<T> t, String key,  T []def) {
		int slash = key.indexOf("/");
		if ( slash>0) {
			String part1 = key.substring(0, slash);
			STObject sub = getObject(part1);
			if (sub != null ) {
				// use the remainder to get to the sub-object
				String remainder = key.substring(slash+1);
				return sub.get(t, remainder, def);
			}
			throw new IllegalArgumentException("Cannot find contained object by key " + part1);
		}
		// no slash, direct property access
		Object o = json.get(key);
		if ( o != null ) {
			if ( t.isInstance(o)) {
				return t.cast(o);
			}
			throw new IllegalArgumentException("Property is not of requested type " + key);
		}
		else {
			if ( def != null && def.length >0 ) {
				return def[0];
			}
			return null;
		}
	}
	/**
	 * Convenience method to obtain a linked SensorThings object
	 * @param id The id of the property containing the link to the requested object 
	 * 
	 * @return The object if found, null otherwise
	 */
	public STObject followLink(String id) {
		String link = get(String.class, id, null);
		if ( link != null ) {
			if ( link.startsWith(LOCAL_NAVIGATION_PREFIX)) {
				link = link.replace(LOCAL_NAVIGATION_PREFIX, REMOTE_NAVIGATION_PREFIX);
			}
			try {
				String json = new Client(link).doGet();
				JSONParser parser = new JSONParser();
				Object o = parser.parse(json);
				if ( o instanceof JSONObject ) {
					return new STObject((JSONObject)o, REMOTE_NAVIGATION_PREFIX);
				}
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
		throw new IllegalArgumentException("Property not found " + id);
	}

}
