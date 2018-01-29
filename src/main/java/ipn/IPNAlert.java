package ipn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "alertType"
)
@JsonSubTypes({
    @Type(value=SimpleAlert.class, name="http://www.predictive.at/maintenance/alert"),
    @Type(value=TimeRemaining.class, name="http://www.predictive.at/maintenance/remainingTime"),
    @Type(value=ProductQuality.class, name="http://www.predictive.at/maintenance/quality"),
    @Type(value=Optimization.class, name="http://www.predictive.at/maintenance/optimization"),
    
})
@JsonInclude(value=Include.NON_NULL)
public abstract class IPNAlert {
    @JsonProperty("text")
    private String text;
    public IPNAlert() {}
    
    public IPNAlert(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public abstract String getAlertType();
}
