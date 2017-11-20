package ispro.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.databind.ObjectMapper;

import http.Client;
import ipn.Message;
import ipn.SimpleAlert;
import ispro.ISPROAdapter;
import ispro.model.Authorisation;
import ispro.model.ISPROResult;
import ispro.model.MaintenanceAlert;
import ispro.model.MaintenanceModel;
import ispro.model.MaintenanceType;
import ispro.model.PlantStructure;
import things.model.STClient;
import things.model.STObject;

public class ISPROAdapterImpl implements ISPROAdapter {
    private static final String maintenanceUri = "http://192.168.48.64/IsproWebApi/External/Maintenance";

    @Override
    public boolean processMessage(String topic, String key, String payload) {
        System.out.println(payload);
        if (key != null) {
            switch (key) {
            case "alert":
            case "remainingTimeAlert":
            case "qualityAlert":
                Message<?> m = parsePayload(payload);
                // currently only SimpleAlert are sent / checked
                if (m.getResult() instanceof SimpleAlert) {
                    SimpleAlert alert = (SimpleAlert)m.getResult();
                    STObject stream = STClient.getDatastream(m.getDatastream().getId());
                    // obtaint the description
                    STObject thing = stream.getObject("Thing@iot.navigationLink");
                    String description = thing.getString("description");
                    // obtain a value from the properties
                    String thing_uuid = thing.getString("properties/isprong_uuid");
                    if ( thing_uuid!=null) {
                        // the resultTime holds the time of creating the message
                        // the validTime denotes the time range of the validity (eg. PT1M for 1 minute)!
                        if ( Instant.now().isBefore(m.getResultTime().plus(m.getValidTime()) ) ) {
                            PlantStructure plant = new PlantStructure(thing_uuid);
                            MaintenanceModel maintenanceModel = new MaintenanceModel();
                            maintenanceModel.setNote(description);
                            maintenanceModel.setCause(description);
                            maintenanceModel.setCauseOfError(alert.getText());
                            maintenanceModel.setText(alert.getText());
                            //maintenanceModel.setAuthor(this.getClass().getSimpleName());
                            maintenanceModel.setJobDate(Instant.now());
                            maintenanceModel.setMaintenanceType(MaintenanceType.Wartung);
                            // send to ISPRO
                            processISPROAlert(plant, maintenanceModel);
                        }
                        else {
                            // message arrived too late
                            // TODO: react accordingly
                        }
                    }
                }
                break;
            }
        }

        return true;
    }

    private Message<?> parsePayload(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message<?> result = mapper.readValue(payload, Message.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
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
