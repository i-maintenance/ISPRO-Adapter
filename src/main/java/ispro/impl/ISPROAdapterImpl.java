package ispro.impl;

import static org.junit.Assert.fail;

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
				if (m.getResultTime().plus(m.getValidTime()).isAfter(Instant.now())) {
					System.out.println("In Time");
					System.out.println(m.getResult().getText());
					
				}
				if ( m.getResult() instanceof SimpleAlert ) {
					// 
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
