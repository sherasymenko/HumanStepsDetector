package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.List;

public class EulerOrientationDataAnalyzer {
	private List<Integer> stanceInitPcList = new ArrayList<Integer>();
	private List<Integer> swingInitPcList = new ArrayList<Integer>();
	private List<Integer> stepPeriodsPcList = new ArrayList<Integer>();
	private final Double deviation = new Double(5);
	private Double initX = new Double(0);
	private Double currentX = new Double(0);
	private Double previos = new Double(0);
	private int stanceInitId = 0;
	private int swingInitId = 0;
	private int currentId = 0;
	private boolean start = true;
	private boolean isNearSwingInit = true;
	private boolean isLikeInit = false;

	public EulerOrientationDataAnalyzer(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		List<EulerOrientationMeasurement> eulerMeasurement = new ArrayList<EulerOrientationMeasurement>();
		eulerOrientationMeasurement.forEach(action -> {
			eulerMeasurement.add(action);
		});
		initX = eulerMeasurement.get(0).getRoll();
		eulerMeasurement.forEach(p -> {
			currentX = p.getRoll();
			currentId = p.getPacketCounter();
			if (start) {
				previos = currentX;
				start = false;
			} else {
				if (isChartTypeMinus(eulerOrientationMeasurement)) {
					if (currentX.compareTo(initX - deviation) < 0) {
						if (isNearSwingInit) {
							addToList(stepPeriodsPcList, currentId);
							isNearSwingInit = false;
						}
						if (previos.compareTo(currentX) > 0) {
							swingInitId = currentId;
						}
						addToList(stanceInitPcList, stanceInitId);
						isLikeInit = true;
						previos = currentX;
					} else if (currentX.compareTo(initX + deviation) > 0) {
						if (previos.compareTo(currentX) < 0) {
							stanceInitId = currentId;
						}
						addToList(swingInitPcList, swingInitId);
						isNearSwingInit = true;
						isLikeInit = true;
						previos = currentX;
					} else {
						if (isLikeInit) {
							addToList(stepPeriodsPcList, currentId);
							isLikeInit = false;
						}
						addToList(swingInitPcList, swingInitId);
						addToList(stanceInitPcList, stanceInitId);
						isNearSwingInit = true;
						previos = currentX;
					}
				} else {
					if (currentX.compareTo(initX + deviation) > 0) {
						if (isNearSwingInit) {
							addToList(stepPeriodsPcList, currentId);
							isNearSwingInit = false;
						}
						if (previos.compareTo(currentX) < 0) {
							swingInitId = currentId;
						}
						addToList(stanceInitPcList, stanceInitId);
						isLikeInit = true;
						previos = currentX;
					} else if (currentX.compareTo(initX - deviation) < 0) {
						if (previos.compareTo(currentX) > 0) {
							stanceInitId = currentId;
						}
						addToList(swingInitPcList, swingInitId);
						isNearSwingInit = true;
						isLikeInit = true;
						previos = currentX;
					} else {
						if (isLikeInit) {
							addToList(stepPeriodsPcList, currentId);
							isLikeInit = false;
						}
						addToList(swingInitPcList, swingInitId);
						addToList(stanceInitPcList, stanceInitId);
						isNearSwingInit = true;
						previos = currentX;
					}

				}
			}
		});
	}

	private boolean isChartTypeMinus(List<EulerOrientationMeasurement> m) {
		double init = m.stream().findFirst().get().getRoll();
		double max = m.stream().max((m1, m2) -> {
			return Double.compare(m1.getRoll(), m2.getRoll());
		}).get().getRoll();
		double min = m.stream().min((m1, m2) -> {
			return Double.compare(m1.getRoll(), m2.getRoll());
		}).get().getRoll();
		return (Double.compare((max - min) / 2 + min, init) < 0);
	}

	private void addToList(List<Integer> list, int pc) {
		if (!list.contains(pc)) {
			list.add(pc);
		}
	}

	public List<Integer> getStepPeriodsPcList() {
		return stepPeriodsPcList;
	}

	public List<Integer> getSwingInitPcList() {
		return swingInitPcList;
	}

	public List<Integer> getStanceInitPcList() {
		return stanceInitPcList;
	}
}