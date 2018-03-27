package ispro;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import eu.imaintenance.toolset.ToolsetClient;
import ispro.impl.ISPROHandler;
/**
 * Dummy runner
 * @author dglachs
 *
 */
public class ISPROAdapter {
	public static void main(String[] args) {
	    try {
            new ToolsetClient()
                    .withServiceUri("http://il060:8082/v1.0/")
                    .setName("ISPROClient")
                    .forThing(1l)
                    .registerHandler(new ISPROHandler(), 30l)
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
