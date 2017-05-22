package app.graph.euler_orientation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import app.main.AppText;

public class EulerOrientationFileReader {
	private final Logger LOGGER = Logger.getLogger(EulerOrientationFileReader.class.getName());
	private List<EulerOrientationMeasurement> eulerOrientationMeasurement = new ArrayList<EulerOrientationMeasurement>();
	private int rowNumber = 0;
	private double maxEulerX;
	private double maxEulerY;
	private double maxEulerZ;
	private double minEulerX;
	private double minEulerY;
	private double minEulerZ;
	private double frequency;
	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	public EulerOrientationFileReader(String filePath, double startTime, double frequency) throws IOException {
		super();
		this.frequency = frequency;
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.forEach(e -> {
				if (!e.contains(AppText.PACKET_COUNTER.value())) {
					String rowData[] = e.split(";");
					eulerOrientationMeasurement.add(new EulerOrientationMeasurement(new Integer(rowData[0]),
							new Double(rowData[1]), new Double(rowData[2]), new Double(rowData[3])));
					rowNumber++;
				}
			});
			this.maxEulerX = eulerOrientationMeasurement.stream().max((m1, m2) -> {
				return Double.compare(m1.getRoll(), m2.getRoll());
			}).get().getRoll();

			this.maxEulerY = eulerOrientationMeasurement.stream().max((m1, m2) -> {
				return Double.compare(m1.getPitch(), m2.getPitch());
			}).get().getPitch();

			this.maxEulerZ = eulerOrientationMeasurement.stream().max((m1, m2) -> {
				return Double.compare(m1.getYaw(), m2.getYaw());
			}).get().getYaw();

			this.minEulerX = eulerOrientationMeasurement.stream().min((m1, m2) -> {
				return Double.compare(m1.getRoll(), m2.getRoll());
			}).get().getRoll();

			this.minEulerY = eulerOrientationMeasurement.stream().min((m1, m2) -> {
				return Double.compare(m1.getPitch(), m2.getPitch());
			}).get().getPitch();

			this.minEulerZ = eulerOrientationMeasurement.stream().min((m1, m2) -> {
				return Double.compare(m1.getYaw(), m2.getYaw());
			}).get().getYaw();

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.maxX = 1 / frequency * rowNumber;
		this.minX = new Double(0);
		this.maxY = Double.max(Double.max(maxEulerX, maxEulerY), maxEulerZ);
		this.minY = Double.min(Double.min(minEulerX, minEulerY), minEulerZ);
	}

	public List<EulerOrientationMeasurement> getEulerOrientationMeasurement() {
		return eulerOrientationMeasurement;
	}

	public void setEulerOrientationMeasurement(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		this.eulerOrientationMeasurement = eulerOrientationMeasurement;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public double getMaxEulerX() {
		return maxEulerX;
	}

	public void setMaxEulerX(double maxEulerX) {
		this.maxEulerX = maxEulerX;
	}

	public double getMaxEulerY() {
		return maxEulerY;
	}

	public void setMaxEulerY(double maxEulerY) {
		this.maxEulerY = maxEulerY;
	}

	public double getMaxEulerZ() {
		return maxEulerZ;
	}

	public void setMaxEulerZ(double maxEulerZ) {
		this.maxEulerZ = maxEulerZ;
	}

	public double getMinEulerX() {
		return minEulerX;
	}

	public void setMinEulerX(double minEulerX) {
		this.minEulerX = minEulerX;
	}

	public double getMinEulerY() {
		return minEulerY;
	}

	public void setMinEulerY(double minEulerY) {
		this.minEulerY = minEulerY;
	}

	public double getMinEulerZ() {
		return minEulerZ;
	}

	public void setMinEulerZ(double minEulerZ) {
		this.minEulerZ = minEulerZ;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
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