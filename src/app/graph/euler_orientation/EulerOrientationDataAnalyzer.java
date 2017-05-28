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
	int iterator = 0;
	List<Integer> iteratorList = new ArrayList<Integer>();
	double time = new Double(0);
	boolean chartTypeMinus = false;

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
		System.out.println("min = " + min);
		System.out.println("init = " + init);
		System.out.println("max = " + max);
		System.out.println("max - min = " + (max - min));
		System.out.println("(max - min) / 2 = " + ((max - min) / 2));
		System.out.println("(max - min) / 2 + min = " + ((max - min) / 2 + min));
		if (Double.compare((max - min) / 2 + min, init) < 0) {
			minus = true;
		} else {
			minus = false;
		}
		System.out.println("minus = " + minus);
		return minus;
	}

	public EulerOrientationDataAnalyzer(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		List<EulerOrientationMeasurement> eulerMeasurement = new ArrayList<EulerOrientationMeasurement>();
		eulerOrientationMeasurement.forEach(action -> {
			eulerMeasurement.add(action);
		});
		initRoll = eulerMeasurement.get(0).getRoll();
		// System.out.println("!!! size " + eulerMeasurement.size());
		eulerMeasurement.forEach(action -> {
			if (start) {
				previos = action.getRoll();
				start = false;
			} else {
				
				
				
				
				
				if(isChartTypeMinus(eulerOrientationMeasurement)){
				
				
				if (new Double(action.getRoll()).compareTo(initRoll - new Double(5)) < 0) {
					if (startMax) {
						addToList(list, action.getPacketCounter());
						startMax = false;
					}
					if (previos.compareTo(action.getRoll()) > 0) {
						maxValue = action.getRoll();
						maxValuePC = action.getPacketCounter();
					}
					previos = action.getRoll();
					addToList(minList, minValuePC);
					islikeInit = true;
				} else if (new Double(action.getRoll()).compareTo(initRoll + new Double(5)) > 0) {
					startMax = true;
					if (previos.compareTo(action.getRoll()) < 0) {
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
				
				}else {
					
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
				
				
				
			}
			iterator++;
		});
		isChartTypeMinus(eulerOrientationMeasurement);
		/*
		 * list.forEach(a -> System.out.println("list " + a));
		 * iteratorList.forEach(b -> System.out.println("iteratorList " + b));
		 */

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
		//list.forEach(a -> System.out.println("list " + a));
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

	public int getIterator() {
		return iterator;
	}

	public void setIterator(int iterator) {
		this.iterator = iterator;
	}

	public List<Integer> getIteratorList() {
		return iteratorList;
	}

	public void setIteratorList(List<Integer> iteratorList) {
		this.iteratorList = iteratorList;
	}
}