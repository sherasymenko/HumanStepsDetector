package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.List;

public class EulerOrientationDataAnalyzer {
	private List<Integer> stanceInitPcList = new ArrayList<Integer>();
	private List<Integer> swingInitPcList = new ArrayList<Integer>();
	private List<Integer> stepPeriodsPcList = new ArrayList<Integer>();
	private final Double deviation = new Double(5);
	private Double initRoll = new Double(0);
	private Double previos = new Double(0);
	private int stanceInitPc = 0;
	private int swingInitPc = 0;
	private boolean start = true;
	private boolean isNearSwingInit = true;
	private boolean isLikeInit = false;

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
					if (new Double(action.getRoll()).compareTo(initRoll - deviation) < 0) {
						if (isNearSwingInit) {
							addToList(stepPeriodsPcList, action.getPacketCounter());
							isNearSwingInit = false;
						}
						if (previos.compareTo(action.getRoll()) > 0) {
							swingInitPc = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(stanceInitPcList, stanceInitPc);
						isLikeInit = true;
					} else if (new Double(action.getRoll()).compareTo(initRoll + deviation) > 0) {
						isNearSwingInit = true;
						if (previos.compareTo(action.getRoll()) < 0) {
							stanceInitPc = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(swingInitPcList, swingInitPc);
						isLikeInit = true;
					} else {
						addToList(swingInitPcList, swingInitPc);
						addToList(stanceInitPcList, stanceInitPc);
						if (isLikeInit) {
							addToList(stepPeriodsPcList, action.getPacketCounter());
							isLikeInit = false;
						}
						previos = action.getRoll();
					}
				} else {
					if (new Double(action.getRoll()).compareTo(initRoll + deviation) > 0) {
						if (isNearSwingInit) {
							addToList(stepPeriodsPcList, action.getPacketCounter());
							isNearSwingInit = false;
						}
						if (previos.compareTo(action.getRoll()) < 0) {
							swingInitPc = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(stanceInitPcList, stanceInitPc);
						isLikeInit = true;
					} else if (new Double(action.getRoll()).compareTo(initRoll - deviation) < 0) {
						isNearSwingInit = true;
						if (previos.compareTo(action.getRoll()) > 0) {
							stanceInitPc = action.getPacketCounter();
						}
						previos = action.getRoll();
						addToList(swingInitPcList, swingInitPc);
						isLikeInit = true;
					} else {
						addToList(swingInitPcList, swingInitPc);
						addToList(stanceInitPcList, stanceInitPc);
						if (isLikeInit) {
							addToList(stepPeriodsPcList, action.getPacketCounter());
							isLikeInit = false;
						}
						previos = action.getRoll();
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