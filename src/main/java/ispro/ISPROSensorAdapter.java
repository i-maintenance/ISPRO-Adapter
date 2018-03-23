package ispro;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import eu.imaintenance.toolset.ToolsetClient;
import ispro.impl.ISPROSensorHandler;
/**
 * Dummy runner
 * @author dglachs
 *
 */
public class ISPROSensorAdapter {
	public static void main(String[] args) {
	    try {
            new ToolsetClient()
                    .withServiceUri("http://il060:8082/v1.0/")
                    .setName("ISPROSensorClient")
                    .registerHandler(1l, new ISPROSensorHandler())
                    .startup();
        } catch (MalformedURLException | URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ServiceFailureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
