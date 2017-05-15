package app.main;

public enum AppText {
	APPLICATION_TITLE("Wykrywacz ludzkich kroków"),
	CHART_TITLE("Wykres przyspieszenia"),
	ACCELERATION_X("Przyspieszenie X"),
	ACCELERATION_Y("Przyspieszenie Y"),
	ACCELERATION_Z("Przyspieszenie Z"),
	X_AXIX_CHART_LABEL("Czas(s)"),
	Y_AXIX_CHART_LABEL("Przyspieszenie(m/s2)"),
	PACKET_COUNTER("PacketCounter"),
	X_AXIS_IN_FILE("Acc_X"),
	Y_AXIS_IN_FILE("Acc_Y"),
	Z_AXIS_IN_FILE("Acc_Z");
	
	AppText(String value) {
		this.value = value;
	}
	private String value;

	public String value() {
		return value;
	}
}