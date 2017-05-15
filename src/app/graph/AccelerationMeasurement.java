package app.graph;

public class AccelerationMeasurement {
	private int packetCounter;
	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;
	public boolean isMotionless;

	public AccelerationMeasurement(int packetCounter, double accelerationX, double accelerationY, double accelerationZ) {
		super();
		this.packetCounter = packetCounter;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.accelerationZ = accelerationZ;
	}

	public int getPacketCounter() {
		return packetCounter;
	}

	public void setPacketCounter(int packetCounter) {
		this.packetCounter = packetCounter;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public double getAccelerationZ() {
		return accelerationZ;
	}

	public void setAccelerationZ(double accelerationZ) {
		this.accelerationZ = accelerationZ;
	}

	public boolean isMotionless() {
		return isMotionless;
	}

	public void setMotionless(boolean isMotionless) {
		this.isMotionless = isMotionless;
	}
}