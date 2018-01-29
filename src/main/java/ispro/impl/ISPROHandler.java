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
import ipn.IPNAlert;
import ipn.SimpleAlert;
import ipn.TimeRemaining;
import ispro.model.Authorisation;
import ispro.model.ISPROResult;
import ispro.model.MaintenanceAlert;
import ispro.model.MaintenanceModel;
import ispro.model.MaintenanceType;
import ispro.model.PlantStructure;

public class ISPROHandler extends AbstractObservationHandler<IPNAlert> {
    // Service Endpoint isproNG
    private final String maintenanceUri;

    Logger logger = LoggerFactory.getLogger(ISPROHandler.class);
    public ISPROHandler() {
        this.maintenanceUri = "http://192.168.48.64/IsproWebApi/External/Maintenance";

    }
    public ISPROHandler(String maintenanceUri) throws URISyntaxException {
        this.maintenanceUri = maintenanceUri;
    }

    @Override
    public void onObservation(Observation observation, IPNAlert result) {
        // handle IPN Results
        try {
            Datastream stream = observation.getDatastream();
            Thing thing = stream.getThing();
           
            if ( result instanceof SimpleAlert) {
                processSimpleAlert(thing, stream, observation, (SimpleAlert) result);
            }
            else if ( result instanceof TimeRemaining) {
                processTimeRemainingAlert(thing, stream, observation, (TimeRemaining) result);
            }
            else {
                logger.debug(String.format("Received result of type [%s], Handler not yet implemented!", result.getClass().getSimpleName() ));
            }
            //
        } catch (ServiceFailureException e) {
           logger.error(e.getLocalizedMessage(), e);
        }

    }
    private boolean processTimeRemainingAlert(Thing thing, Datastream stream, Observation observation, TimeRemaining remaining) {
        
        Object fk1 = thing.getProperties().get("isprong_uuid");

        if ( fk1!=null) {
            
            PlantStructure plant = new PlantStructure(fk1.toString());
            MaintenanceModel maintenanceModel = new MaintenanceModel();
            maintenanceModel.setNote(thing.getDescription());
            maintenanceModel.setCause(thing.getName());
            maintenanceModel.setCauseOfError(remaining.getText());
            if (remaining.getRemainingTime() instanceof Duration) {
                Duration d = (Duration) remaining.getRemainingTime();
                maintenanceModel.setText(String.format("RemainingTime: %s (Confidence %s)", d.toHours(), remaining.getConfidence().toString()));
                
            }
            if (remaining.getRemainingTime() instanceof Period ) {
                Period d = (Period) remaining.getRemainingTime();
                maintenanceModel.setText(String.format("RemainingTime: %s (Confidence %s)", d.getDays(), remaining.getConfidence().toString()));
                
            }
            //maintenanceModel.setAuthor(this.getClass().getSimpleName());
            maintenanceModel.setJobDate(Instant.now());
            maintenanceModel.setMaintenanceType(MaintenanceType.Wartung);
            // send to ISPRO
            return processISPROAlert(plant, maintenanceModel);
        }


        return false;
    }
    private boolean processSimpleAlert(Thing thing, Datastream stream, Observation observation, SimpleAlert alert) {
        // obtain a value from the properties
        Object fk1 = thing.getProperties().get("isprong_uuid");

        if ( fk1!=null) {
            // the resultTime holds the time of creating the message
            // the validTime denotes the time range of the validity (eg. PT1M for 1 minute)!
            PlantStructure plant = new PlantStructure(fk1.toString());
            MaintenanceModel maintenanceModel = new MaintenanceModel();
            maintenanceModel.setNote(thing.getDescription());
            maintenanceModel.setCause(alert.getText());
            maintenanceModel.setCauseOfError(stream.getDescription());
            maintenanceModel.setText(alert.getText());
            //maintenanceModel.setAuthor(this.getClass().getSimpleName());
            maintenanceModel.setJobDate(Instant.now());
            maintenanceModel.setMaintenanceType(MaintenanceType.Wartung);
            // send to ISPRO
            return processISPROAlert(plant, maintenanceModel);
        }
        return false;
    }


    private boolean processISPROAlert(PlantStructure plant, MaintenanceModel model) {
        // always us this user to report maintenance alerts
        Authorisation a = new Authorisation("ADM", "20salzb13");
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setPlantStructure(plant);
        alert.setAutorisation(a);
        alert.setModel(model);
        try {
            ISPROResult result = new Client(maintenanceUri)
                    .setPath("/SaveExternalMaintenanceAlert")
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
