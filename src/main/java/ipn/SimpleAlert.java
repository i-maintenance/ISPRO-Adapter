package ipn;

public class SimpleAlert extends IPNAlert {
	public SimpleAlert() {
		super();
	}
	public SimpleAlert(String message) {
		this.setText(message);
	}
    @Override
    public String getAlertType() {
        return "http://www.predictive.at/maintenance/alert";
    }
}
