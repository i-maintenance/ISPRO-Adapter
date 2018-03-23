package ispro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class SensorValue {
    private SensorNameModel sensor;
    private PlantStructure plantStructure;
    private Authorisation autorisation;
    private SensorValueModel model;
    public SensorNameModel getSensor() {
        return sensor;
    }
    public void setSensor(SensorNameModel sensor) {
        this.sensor = sensor;
    }
    public PlantStructure getPlantStructure() {
        return plantStructure;
    }
    public void setPlantStructure(PlantStructure plantStructure) {
        this.plantStructure = plantStructure;
    }
    public Authorisation getAutorisation() {
        return autorisation;
    }
    public void setAutorisation(Authorisation autorisation) {
        this.autorisation = autorisation;
    }
    public SensorValueModel getModel() {
        return model;
    }
    public void setModel(SensorValueModel model) {
        this.model = model;
    }

}
