package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.List;

public class EulerOrientationDataAnalyzer {
	private Double previos = new Double(0);
	private int maxValuePC = 0;
	private int minValuePC = 0;
	private Double initRoll = new Double(0);
	private boolean start = true;
	private boolean startMax = true;
	private boolean islikeInit = false;
	private List<Integer> list = new ArrayList<Integer>();
	private List<Integer> maxList = new ArrayList<Integer>();
	private List<Integer> minList = new ArrayList<Integer>();
	private int iterator = 0;
	private double time = new Double(0);

	public boolean isChartTypeMinus(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		double max = new Double(0);
		double min = new Double(0);
		double init = new Double(0);
		init = eulerOrientationMeasurement.stream().findFirst().get().getRoll();
		max = eulerOrientationMeasurement.stream().max((m1, m2) -> {
			return Double.compare(m1.getRoll(), m2.getRoll());
		}).get().getRoll();
		min = eulerOrientationMeasurement.stream().min((m1, m2) -> {
			return Double.compare(m1.getRoll(), m2.getRoll());
		}).get().getRoll();
		boolean minus = false;
		if (Double.compare((max - min) / 2 + min, init) < 0) {
			minus = true;
		} else {
			minus = false;
		}
		return minus;
	}

	public EulerOrientationDataAnalyzer(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		List<EulerOrientationMeasurement> eulerMeasurement = new ArrayList<EulerOrientationMeasurement>();
		eulerOrientationMeasurement.forEach(action -> {
			eulerMeasurement.add(action);
		});
		initRoll = eulerMeasurement.get(0).getRoll();
		eulerMeasurement.forEach(action -> {
			if (start) {
				previos = action.getRoll();
				start = false;
			} else {
				if (isChartTypeMinus(eulerOrientationMeasurement)) {
					if (new Double(action.getRoll()).compareTo(initRoll - new Double(5)) < 0) {
						if (startMax) {
							addToList(list, action.getPacketCounter());
							startMax = false;
						}
						if (previos.compareTo(action.getRoll()) > 0) {
							maxValuePC = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(minList, minValuePC);
						islikeInit = true;
					} else if (new Double(action.getRoll()).compareTo(initRoll + new Double(5)) > 0) {
						startMax = true;
						if (previos.compareTo(action.getRoll()) < 0) {
							minValuePC = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(maxList, maxValuePC);
						islikeInit = true;
					} else {
						addToList(maxList, maxValuePC);
						addToList(minList, minValuePC);
						if (islikeInit) {
							if (!start)
								addToList(list, action.getPacketCounter());
							islikeInit = false;
						}
						previos = action.getRoll();
					}
				} else {
					if (new Double(action.getRoll()).compareTo(initRoll + new Double(5)) > 0) {
						if (startMax) {
							addToList(list, action.getPacketCounter());
							startMax = false;
						}
						if (previos.compareTo(action.getRoll()) < 0) {
							maxValuePC = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(minList, minValuePC);
						islikeInit = true;
					} else if (new Double(action.getRoll()).compareTo(initRoll - new Double(5)) < 0) {
						startMax = true;
						if (previos.compareTo(action.getRoll()) > 0) {
							minValuePC = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(maxList, maxValuePC);
						islikeInit = true;
					} else {
						addToList(maxList, maxValuePC);
						addToList(minList, minValuePC);
						if (islikeInit) {
							if (!start)
								addToList(list, action.getPacketCounter());
							islikeInit = false;
						}
						previos = action.getRoll();
					}

				}

			}
			iterator++;
		});
		isChartTypeMinus(eulerOrientationMeasurement);
		getPointTime(eulerOrientationMeasurement, list, new Double(75));
	}

	public List<Double> getPointTime(List<EulerOrientationMeasurement> eulerOrientationMeasurement,
			List<Integer> listPC, Double frequency) {
		List<Double> list = new ArrayList<Double>();
		eulerOrientationMeasurement.forEach(action -> {
			time = time + 1 / frequency;
			if (listPC.contains(action.getPacketCounter())) {
				list.add(time);
			}
		});
		return list;
	}

	private void addToList(List<Integer> list, int pc) {
		if (!list.contains(pc)) {
			list.add(pc);
		}
	}

	public List<Integer> getList() {
		return list;
	}

	public List<Integer> getMaxList() {
		return maxList;
	}

	public List<Integer> getMinList() {
		return minList;
	}
}