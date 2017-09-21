package ispro.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.apache.http.entity.ContentType;

import http.Client;
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
		if ( true) {
			// TODO: inspect payload to check for the proper message
			PlantStructure plant = new PlantStructure(10, null, null);
			MaintenanceModel model = new MaintenanceModel();
			model.setAuthor("ADM");
			model.setText(payload);
			model.setCause(payload);
			model.setCauseOfError("Unknown");
			model.setId(1);
			model.setJobDate(Instant.now());
			model.setMaintenanceType(MaintenanceType.Wartung);
			model.setPriority(3);
			model.setTechnician1("Unkown");
			model.setNote(payload);
			model.setTechnician1(payload);
			// now post the alert to ISPRO
			boolean success = processISPROAlert(plant, model);
			return success;
		}
		return true;
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
