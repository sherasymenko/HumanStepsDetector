package app.graph;

public class EulerOrientationMeasurement {

	private int packetCounter;
	private double roll;
	private double pitch;
	private double yaw;

	public EulerOrientationMeasurement(int packetCounter, double roll, double pitch, double yaw) {
		super();
		this.packetCounter = packetCounter;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public int getPacketCounter() {
		return packetCounter;
	}

	public void setPacketCounter(int packetCounter) {
		this.packetCounter = packetCounter;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}
}