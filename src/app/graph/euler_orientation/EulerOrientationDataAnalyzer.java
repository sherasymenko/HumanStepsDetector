package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.List;

public class EulerOrientationDataAnalyzer {
	Double previos = new Double(0);
	Double maxValue = new Double(0);
	int maxValuePC = 0;
	Double minValue = new Double(0);
	int minValuePC = 0;
	Double initRoll = new Double(0);
	boolean start = true;
	boolean startMax = true;
	boolean islikeInit = false;
	List<Integer> list = new ArrayList<Integer>();
	List<Integer> maxList = new ArrayList<Integer>();
	List<Integer> minList = new ArrayList<Integer>();
	
	
	
	

	public EulerOrientationDataAnalyzer(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		List<EulerOrientationMeasurement> eulerMeasurement = new ArrayList<EulerOrientationMeasurement>();
		eulerOrientationMeasurement.forEach(action -> {
			eulerMeasurement.add(action);
		});
		initRoll = eulerMeasurement.get(0).getRoll();
		System.out.println("!!! size " + eulerMeasurement.size());
		eulerMeasurement.forEach(action -> {
			if (start) {
				previos = action.getRoll();
				start = false;
			} else {
				if (new Double(action.getRoll()).compareTo(initRoll + new Double(5)) > 0) {
					if (startMax) {
						addToList(list, action.getPacketCounter());
						startMax = false;
					}
					if (previos.compareTo(action.getRoll()) < 0) {
						maxValue = action.getRoll();
						maxValuePC = action.getPacketCounter();
					}
					previos = action.getRoll();
					addToList(minList, minValuePC);
					islikeInit = true;
				} else if (new Double(action.getRoll()).compareTo(initRoll - new Double(5)) < 0) {
					startMax = true;
					if (previos.compareTo(action.getRoll()) > 0) {
						minValue = action.getRoll();
						minValuePC = action.getPacketCounter();
					}
					previos = action.getRoll();
					addToList(maxList, maxValuePC);
					// System.out.println("!!!!! " + maxValuePC);
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
		});

		list.forEach(a -> System.out.println("!!!!! " + a));
	}

	private void addToList(List<Integer> list, int pc) {
		if (!list.contains(pc))
			list.add(pc);
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public List<Integer> getMaxList() {
		return maxList;
	}

	public void setMaxList(List<Integer> maxList) {
		this.maxList = maxList;
	}

	public List<Integer> getMinList() {
		return minList;
	}

	public void setMinList(List<Integer> minList) {
		this.minList = minList;
	}
	
	
}