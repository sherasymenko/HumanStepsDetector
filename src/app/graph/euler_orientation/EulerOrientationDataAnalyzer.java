package app.graph.euler_orientation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.graph.HumanStep;
import app.graph.acceleration.AccelerationMeasurement;

public class EulerOrientationDataAnalyzer {
	public int intervalCount = 1;
	public int intervalNumber = 1;
	public int pc = 1;
	public Map<Integer, Integer> testMap = new HashMap<Integer, Integer>();
	boolean intervalCountStart = true;
	boolean accCons = true;
	public Map<Integer, Integer> starts = new HashMap<Integer, Integer>();
	int previosValue = 0;
	int f = 1;

	
	Double previos = new Double(0);
	Double maxValue = new Double(0);
	Double init = new Double(0);
	public List<Integer> analyze(List<EulerOrientationMeasurement> eulerOrientationMeasurement) {
		List<Integer> list = new ArrayList<Integer>();
		List<EulerOrientationMeasurement> eulerMeasurement = new ArrayList<EulerOrientationMeasurement>();
		eulerOrientationMeasurement.forEach(action -> {
			eulerMeasurement.add(action);
			System.out.println("test 1 " + action.getRoll());
		});

		/*Comparator<EulerOrientationMeasurement> cmp = new Comparator<EulerOrientationMeasurement>() {
			public int compare(EulerOrientationMeasurement o1, EulerOrientationMeasurement o2) {
				return new Double(o2.getRoll()).compareTo(new Double(o1.getRoll()));
			}
		};
		eulerMeasurement.sort(cmp);*/
		

		eulerMeasurement.forEach(action -> {
			if (!previos.equals(new Double(0))) {

				if (init.compareTo(action.getRoll()) > 0) {
					if (previos.compareTo(action.getRoll()) < 0) {
						maxValue = action.getRoll();
					}
				}
			} else {
				init = action.getRoll();
				previos = action.getRoll();
			}

			System.out.println("test 2 " + action.getRoll());
		});

		list.add(31555);
		return list;
	}

	public List<Integer> start(List<AccelerationMeasurement> list) {
		list.forEach(a -> {
			if (intervalCount == 1) {
				accCons = test(a);
				pc = a.getPacketCounter();
			}

			intervalCount++;
			a.setMotionless(test(a));
			if (intervalCountStart) {
				intervalNumber++;
				intervalCountStart = false;
				starts.put(intervalNumber - 1, intervalCount);
				testMap.put(intervalNumber - 1, pc);
				intervalCount = 1;
			}

			if (test(a) != accCons) {
				intervalCountStart = true;
				accCons = test(a);
			}
		});
		List<HumanStep> humanSteps = new ArrayList<HumanStep>();
		for (int i = 1; i <= starts.size(); i++) {
			humanSteps.add(new HumanStep(testMap.get(i), starts.get(i), i));

		}

		Comparator<HumanStep> cmp = new Comparator<HumanStep>() {
			public int compare(HumanStep o1, HumanStep o2) {
				return new Integer(o2.getIntervalCount()).compareTo(new Integer(o1.getIntervalCount()));
			}
		};
		humanSteps.sort(cmp);

		List<Integer> stepStartId = new ArrayList<Integer>();

		humanSteps.forEach(a -> {
			final int pv = a.getIntervalCount();
			if (f > 3) {

				if (Double.compare(new Double(a.getIntervalCount() / 2), new Double(previosValue / 3)) > 0) {
					stepStartId.add(a.getStartIntervalPacketCounter());
				} else {
					return;
				}
			} else {
				stepStartId.add(a.getStartIntervalPacketCounter());
			}
			previosValue = new Integer(a.getIntervalCount());
			f++;
		});

		return stepStartId;
	}

	public boolean test(AccelerationMeasurement m) {
		boolean test = false;
		double x = m.getAccelerationX();
		double y = m.getAccelerationY();
		double z = m.getAccelerationZ();
		double TEN = new Double(10);
		double NINE = new Double(9);
		double TEN1 = new Double(-10);
		double FOUR = new Double(4);
		double FOUR1 = new Double(-4);
		if ((((x - y) < TEN && (x - y) > TEN1) || ((x - z) < TEN && (x - z) > TEN1)
				|| ((y - z) < TEN && (y - z) > TEN1))
				&& ((x < FOUR && x > FOUR1) || (y < FOUR && y > FOUR1) || (z < FOUR && z > FOUR1))
				&& (z > NINE || z < TEN)) {
			test = true;
			// System.out.println("------------------");
		} else {
			test = false;
			// System.out.println("|||||||||||||||||||");
		}

		return test;
	}

}
