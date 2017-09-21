package ispro;

/**
 * Adapter processing the consumed messages
 * @author dglachs
 *
 */
public interface ISPROAdapter {
	boolean processMessage(String topic, String key, String payload);

}
