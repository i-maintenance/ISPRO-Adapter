package model;

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
