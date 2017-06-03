package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.List;

public class EulerOrientationDataAnalyzer {
	private List<Integer> stanceInitIdList = new ArrayList<Integer>();
	private List<Integer> swingInitIdList = new ArrayList<Integer>();
	private List<Integer> stepPeriodsIdList = new ArrayList<Integer>();
	private final Double deviation = new Double(5);
	private Double initX = new Double(0);
	private Double currentX = new Double(0);
	private Double previous = new Double(0);
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
				previous = currentX;
				start = false;
			} else {
				if (isChartTypeMinus(eulerOrientationMeasurement)) {
					if (isFirstLarger(currentX, initX + deviation)) {
						if (isFirstLarger(currentX, previous)) {
							stanceInitId = currentId;
						}
						addToList(swingInitIdList, swingInitId);
						isNearSwingInit = true;
						isLikeInit = true;
						previous = currentX;
					} else if (isFirstLarger(initX - deviation, currentX)) {
						if (isNearSwingInit) {
							addToList(stepPeriodsIdList, currentId);
							isNearSwingInit = false;
						}
						if (isFirstLarger(previous, currentX)) {
							swingInitId = currentId;
						}
						addToList(stanceInitIdList, stanceInitId);
						isLikeInit = true;
						previous = currentX;
					} else {
						if (isLikeInit) {
							addToList(stepPeriodsIdList, currentId);
							isLikeInit = false;
						}
						addToList(swingInitIdList, swingInitId);
						addToList(stanceInitIdList, stanceInitId);
						isNearSwingInit = true;
						previous = currentX;
					}
				} else {
					if (isFirstLarger(currentX, initX + deviation)) {
						if (isNearSwingInit) {
							addToList(stepPeriodsIdList, currentId);
							isNearSwingInit = false;
						}
						if (isFirstLarger(currentX, previous)) {
							swingInitId = currentId;
						}
						addToList(stanceInitIdList, stanceInitId);
						isLikeInit = true;
						previous = currentX;
					} else if (isFirstLarger(initX - deviation, currentX)) {
						if (isFirstLarger(previous, currentX)) {
							stanceInitId = currentId;
						}
						addToList(swingInitIdList, swingInitId);
						isNearSwingInit = true;
						isLikeInit = true;
						previous = currentX;
					} else {
						if (isLikeInit) {
							addToList(stepPeriodsIdList, currentId);
							isLikeInit = false;
						}
						addToList(swingInitIdList, swingInitId);
						addToList(stanceInitIdList, stanceInitId);
						isNearSwingInit = true;
						previous = currentX;
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

	private boolean isFirstLarger(Double v1, Double v2) {
		return v1.compareTo(v2) > 0;
	}

	public List<Integer> getStepPeriodsPcList() {
		return stepPeriodsIdList;
	}

	public List<Integer> getSwingInitPcList() {
		return swingInitIdList;
	}

	public List<Integer> getStanceInitPcList() {
		return stanceInitIdList;
	}
}