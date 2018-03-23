package ispro.model;

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ispro.model.json.InstantDeserializer;
import ispro.model.json.InstantSerializer;

public class SensorValueModel {
    private Integer id = 0;
    private Double value = 0d;
    @JsonDeserialize(using=InstantDeserializer.class)
    @JsonSerialize(using=InstantSerializer.class)
    private Instant date;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Instant getDate() {
        return date;
    }
    public void setDate(Instant date) {
        this.date = date;
    }
}
