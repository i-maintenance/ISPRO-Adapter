package ispro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Class representing a maintenance alert
 * @author dglachs
 *
 */
@JsonInclude(value=Include.NON_NULL)
public class MaintenanceAlert {
	private PlantStructure plantStructure;
	private Authorisation autorisation;
	private MaintenanceModel model;

	public PlantStructure getPlantStructure() {
		return plantStructure;
	}
	public void setPlantStructure(PlantStructure plantStructure) {
		this.plantStructure = plantStructure;
	}
	public Authorisation getAutorisation() {
		return autorisation;
	}
	public void setAutorisation(Authorisation authorisation) {
		this.autorisation = authorisation;
	}
	public MaintenanceModel getModel() {
		return model;
	}
	public void setModel(MaintenanceModel model) {
		this.model = model;
	}

}
