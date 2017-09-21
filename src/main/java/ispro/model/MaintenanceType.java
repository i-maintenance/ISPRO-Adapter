package ispro.model;

public enum MaintenanceType {
	Wartung("W"),
	Reparatur("R"),
	Umbau("U"),
	Ideen("I"),
	Verbesserung("V"),
	Produktion("O"),
	Prüfbuch("P"),
	Projekt("N"),
	;
	private String key;
	MaintenanceType(String key) {
		this.key = key;
	}
	public String key() {
		return this.key;
	}
	public static MaintenanceType fromKey(String key) {
		for (MaintenanceType t : values()) {
			if ( t.key.equals(key)) {
				return t;
			}
		}
		return MaintenanceType.Wartung;
	}
}
