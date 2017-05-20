package app.main;

public enum AppText {
	APPLICATION_TITLE("Wykrywacz ludzkich kroków"),
	ACC_CHART("acceleration"),
	EULER_CHART("euler_orientation"),
	ACC_CHART_TITLE("Wykres przyspieszenia"),
	EULER_CHART_TITLE("K¹ty Eulera"),
	ACC_X("Przyspieszenie X"),
	ACC_Y("Przyspieszenie Y"),
	ACC_Z("Przyspieszenie Z"),
	EULER_X("X"),
	EULER_Y("Y"),
	EULER_Z("Z"),
	X_AXIX_CHART_LABEL("Czas(s)"),
	Y_AXIX_ACC_CHART_LABEL("Przyspieszenie(m/s2)"),
	Y_AXIX_EULER_CHART_LABEL("Stopnie"),
	PACKET_COUNTER("PacketCounter"),
	ACC_X_AXIS_IN_FILE("Acc_X"),
	ACC_Y_AXIS_IN_FILE("Acc_Y"),
	ACC_Z_AXIS_IN_FILE("Acc_Z"),
	EULER_X_AXIS_IN_FILE("Roll"),
	EULER_Y_AXIS_IN_FILE("Pitch"),
	EULER_Z_AXIS_IN_FILE("Yaw");
	
	AppText(String value) {
		this.value = value;
	}
	private String value;

	public String value() {
		return value;
	}
}