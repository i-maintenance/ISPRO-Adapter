package ipn;

import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Dedicated result class specifying a remaining time and a confidence
 * @author dglachs
 *
 */
public class ProductQuality extends Result {
	
	@JsonProperty("confidence")
	private Double confidence;
	@JsonProperty("qualityPrediction")
	private Double qualityPrediction;
	
	public ProductQuality() {
		// JSON required default
	}
	public ProductQuality(Double qualityPrediction, Double confidence) {
		this(qualityPrediction, confidence, null);
	}
	public ProductQuality(Double qualityPrediction, Double confidence, String message) {
		this.qualityPrediction = qualityPrediction;
		this.confidence = confidence;
		
		setText(message);
		
	}
	public Double getConfidence() {
		return confidence;
	}
	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}
	public Double getQualityPrediction() {
		return qualityPrediction;
	}
	public void setQualityPrediction(Double qualityPrediction) {
		this.qualityPrediction = qualityPrediction;
	}
	@Override
	public QualityMessage createPayload(Long datastream, Instant phenomenon) {
		QualityMessage message = new QualityMessage(this);
		message.setDatastream(new Datastream(datastream));
		message.setPhenonmenonTime(phenomenon);
		message.setResultTime(Instant.now());
		message.setValidTime(Duration.ofSeconds(30));
		return message;

	}
	
}
