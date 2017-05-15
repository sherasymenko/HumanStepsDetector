package app.main;

public enum AppText {
	APPLICATION_TITLE("Wykrywacz ludzkich kroków"),
	CHART_TITLE("Wykres przyspieszenia"),
	ACCELERATION_X("Przyspieszenie X"),
	ACCELERATION_Y("Przyspieszenie Y"),
	ACCELERATION_Z("Przyspieszenie Z"),
	X_AXIX_LABEL("Czas(s)"),
	Y_AXIX_LABEL("Przyspieszenie(m/s2)");
	
	AppText(String value) {
		this.value = value;
	}
	private String value;

	public String value() {
		return value;
	}
}