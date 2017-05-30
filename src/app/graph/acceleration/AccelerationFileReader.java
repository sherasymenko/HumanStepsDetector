package app.graph.acceleration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import app.main.AppText;

public class AccelerationFileReader {
	private final Logger LOGGER = Logger.getLogger(AccelerationFileReader.class.getName());
	private List<AccelerationMeasurement> accelerationMeasurements = new ArrayList<AccelerationMeasurement>();
	private int rowNumber = 0;
	private double maxAccX;
	private double maxAccY;
	private double maxAccZ;
	private double minAccX;
	private double minAccY;
	private double minAccZ;
	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	public AccelerationFileReader(String filePath, double frequency) throws Exception {
		super();
		Stream<String> stream = Files.lines(Paths.get(filePath));
		stream.forEach(e -> {
			if (!e.contains(AppText.PACKET_COUNTER.get())) {
				String rowData[] = e.split(";");
				accelerationMeasurements.add(new AccelerationMeasurement(new Integer(rowData[0]),
						new Double(rowData[1]), new Double(rowData[2]), new Double(rowData[3])));
				rowNumber++;
			}
		});
		stream.close();
		this.maxAccX = accelerationMeasurements.stream().max((m1, m2) -> {
			return Double.compare(m1.getAccelerationX(), m2.getAccelerationX());
		}).get().getAccelerationX();

		this.maxAccY = accelerationMeasurements.stream().max((m1, m2) -> {
			return Double.compare(m1.getAccelerationY(), m2.getAccelerationY());
		}).get().getAccelerationY();

		this.maxAccZ = accelerationMeasurements.stream().max((m1, m2) -> {
			return Double.compare(m1.getAccelerationZ(), m2.getAccelerationZ());
		}).get().getAccelerationZ();

		this.minAccX = accelerationMeasurements.stream().min((m1, m2) -> {
			return Double.compare(m1.getAccelerationX(), m2.getAccelerationX());
		}).get().getAccelerationX();

		this.minAccY = accelerationMeasurements.stream().min((m1, m2) -> {
			return Double.compare(m1.getAccelerationY(), m2.getAccelerationY());
		}).get().getAccelerationY();

		this.minAccZ = accelerationMeasurements.stream().min((m1, m2) -> {
			return Double.compare(m1.getAccelerationZ(), m2.getAccelerationZ());
		}).get().getAccelerationZ();

		this.maxX = 1 / frequency * rowNumber;
		this.minX = new Double(0);
		this.maxY = Double.max(Double.max(maxAccX, maxAccY), maxAccZ);
		this.minY = Double.min(Double.min(minAccX, minAccY), minAccZ);
	}

	public List<AccelerationMeasurement> getAccelerationMeasurements() {
		return accelerationMeasurements;
	}

	public void setAccelerationMeasurements(List<AccelerationMeasurement> accelerationMeasurements) {
		this.accelerationMeasurements = accelerationMeasurements;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}
}