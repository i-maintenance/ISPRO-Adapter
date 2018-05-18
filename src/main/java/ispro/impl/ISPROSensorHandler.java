package ispro.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import eu.imaintenance.toolset.observation.AbstractObservationHandler;
import http.Client;
import ipn.SimpleAlert;
import ispro.model.Authorisation;
import ispro.model.ISPROResult;
import ispro.model.MaintenanceModel;
import ispro.model.MaintenanceType;
import ispro.model.PlantStructure;
import ispro.model.SensorNameModel;
import ispro.model.SensorValue;
import ispro.model.SensorValueModel;

public class ISPROSensorHandler extends AbstractObservationHandler<Double> {
    // Service Endpoint isproNG
    private final String maintenanceUri;
    

    Logger logger = LoggerFactory.getLogger(ISPROSensorHandler.class);
    public ISPROSensorHandler() {
        this.maintenanceUri = "http://192.168.48.64/IsproWebApi/External/Sensor";

    }
    public ISPROSensorHandler(String maintenanceUri) throws URISyntaxException {
        this.maintenanceUri = maintenanceUri;
    }

    @Override
    public void onObservation(Observation observation, Double result) {
        // handle IPN Results
        try {
            Datastream stream = observation.getDatastream();
            Thing thing = stream.getThing();
            processObervation(thing, stream, observation, result);
           
//            if ( result instanceof SimpleAlert) {
//                processSimpleAlert(thing, stream, observation, (SimpleAlert) result);
//            }
//            else if ( result instanceof TimeRemaining) {
//                processTimeRemainingAlert(thing, stream, observation, (TimeRemaining) result);
//            }
//            else {
//                logger.debug(String.format("Received result of type [%s], Handler not yet implemented!", result.getClass().getSimpleName() ));
//            }
            //
        } catch (ServiceFailureException e) {
           logger.error(e.getLocalizedMessage(), e);
        }

    }
    private boolean processObervation(Thing thing, Datastream stream, Observation observation, Double value) throws ServiceFailureException {
        
        Object fk1 = thing.getProperties().get("isprong_uuid");

        if ( fk1!=null) {
            
            PlantStructure plant = new PlantStructure(fk1.toString());
            SensorNameModel sensorName = new SensorNameModel();
            SensorValueModel sensorValue = new SensorValueModel();
            sensorName = new SensorNameModel(); 
            sensorName.setVariableName(stream.getObservedProperty().getName()); // TODO: use sensor name from observed property
            if ( sensorName.getVariableName().startsWith("Temp")) {
                sensorName.setVariableName("temp");
            }
            sensorValue.setValue(value);
            sensorValue.setDate(observation.getPhenomenonTime().getAsDateTime().toInstant());

            return processSensorValue(plant, sensorName, sensorValue);
        }


        return false;
    }


    private boolean processSensorValue(PlantStructure plant, SensorNameModel name, SensorValueModel value) {
        // always us this user to report maintenance alerts
        Authorisation a = new Authorisation("ADM", "20salzb13");
        SensorValue alert = new SensorValue();
        alert.setPlantStructure(plant);
        alert.setAutorisation(a);
        alert.setSensor(name);
        alert.setModel(value);
        try {
            ISPROResult result = new Client(maintenanceUri)
                    .setPath("/SaveExternalSensorValue")
                    .setContentType(ContentType.APPLICATION_JSON)
                    .doPost(alert, ISPROResult.class);
            // TODO: report error message
            return result.isSuccess();
        } catch (URISyntaxException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
