package ispro.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.apache.http.entity.ContentType;

import http.Client;
import ispro.ISPROAdapter;
import model.Authorisation;
import model.ISPROResult;
import model.MaintenanceAlert;
import model.MaintenanceModel;
import model.PlantStructure;

public class ISPROAdapterImpl implements ISPROAdapter {
	private static final String maintenanceUri = "http://192.168.48.64/IsproWebApi/External/Maintenance";

	@Override
	public boolean processMessage(String topic, String payload) {
		System.out.println(payload);
		if ( false) {
			// TODO: inspect payload to check for the proper message
			PlantStructure plant = new PlantStructure(10, null, null);
			MaintenanceModel model = new MaintenanceModel();
			model.setAuthor("ADM");
			model.setText(payload);
			model.setCause("Unknown");
			model.setCauseOfError("Unknown");
			model.setId(1);
			model.setJobDate(Instant.now());
			model.setMaintenanceType("Predictive Alert");
			model.setPriority(3);
			model.setTechnician1("Unkown");
			model.setNote(payload);
			model.setTechnician1(payload);
			// now post the alert to ISPRO
			boolean success = processISPROAlert(plant, model);
			
		}
		return true;
	}
	
	private boolean processISPROAlert(PlantStructure plant, MaintenanceModel model) {
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
