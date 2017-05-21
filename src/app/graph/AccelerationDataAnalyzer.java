package app.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.main.AppText;

public class AccelerationDataAnalyzer {

	/*public List analyze(Map data) {
		String[] values = (String[]) data.get(AppText.ACC_Z_AXIS_IN_FILE.value());
		String[] packetCounter = (String[]) data.get(AppText.PACKET_COUNTER.value());
		// sortDesc(values);
		showChanges(values);
		// getStartsOfSteps(getTops(data, packetCounter, values));
		return getStartTimeOfSteps(packetCounter, getStartsOfSteps(getTops(data, packetCounter, values)));

	}
*/
	public double getMaxValue(Map data) {
		String[] graphs = { AppText.ACC_X_AXIS_IN_FILE.value(), AppText.ACC_Y_AXIS_IN_FILE.value(),
				AppText.ACC_Z_AXIS_IN_FILE.value() };
		String[] values = (String[]) data.get(graphs[0]);
		// System.out.println(" values.length " + values.length);
		double maxValue = new Double(values[0]);
		for (int i = 0; i < graphs.length; i++) {
			values = (String[]) data.get(graphs[i]);
			for (int j = 0; j < values.length; j++) {
				// System.out.println("j " + j + " new Double(values[j]) " + new
				// Double(values[j]));
				maxValue = Double.max(maxValue, new Double(values[j]));
			}
		}
		// System.out.println("max value " + maxValue);
		return maxValue;
	}

	public void showChanges(String[] data) {
		for (int i = 1; i < data.length; i++) {
			Double d1 = new Double(data[i - 1]);
			Double d2 = new Double(data[i]);
			// System.out.println(i);
			if (Double.compare(d1, d2) == 0) {
				// System.out.println("The chart is fixed.");
			} else if (Double.compare(d1, d2) < 0) {
				// System.out.println("The chart is rising.");
			} else if (Double.compare(d1, d2) > 0) {
				// System.out.println("The graph is decreasing.");
			}
		}
	}

	public Double[] sortDesc(String[] data) {
		Double[] values = new Double[data.length];
		for (int i = 0; i < data.length; i++) {
			values[i] = new Double(data[i]);
		}
		Arrays.sort(values, Collections.reverseOrder());

		for (int i = 0; i < values.length; i++) {
			// System.out.println(values[i]);
		}
		return values;
	}

	public Integer[] getTops(Map allData, String[] packetCounter, String[] data) {
		Double[] sortValues = sortDesc(data);

		int size = 1;
		for (int i = 1; i < sortValues.length; i++) {
			if (Double.compare(new Double(20), sortValues[i]) < 0) {
				size++;
			} else {
				break;
			}
		}

		Double[] tops = new Double[size];
		Integer[] topsCount = new Integer[size];
		for (int i = 0; i < size; i++) {
			tops[i] = sortValues[i];
			for (int j = 1; j < data.length; j++) {
				if (Double.compare(tops[i], new Double(data[j])) == 0) {
					topsCount[i] = new Integer(packetCounter[j]);
				}
			}
			// System.out.println("Id: " + topsCount[i] + " = " + tops[i]);
		}
		return topsCount;
	}

	public Integer[] sortDescInt(Integer[] data) {
		Integer[] values = new Integer[data.length];
		for (int i = 0; i < data.length; i++) {
			values[i] = new Integer(data[i]);
		}
		Arrays.sort(values);

		for (int i = 0; i < values.length; i++) {
			// System.out.println(values[i]);
		}
		return values;
	}

	public Integer[] getStartsOfSteps(Integer[] topsCount) {
		topsCount = sortDescInt(topsCount);
		Integer[] startsOfSteps = new Integer[topsCount.length - 1];
		for (int i = 0; i < startsOfSteps.length; i++) {
			// System.out.println("(topsCount[i + 1] " + (topsCount[i + 1]));
			// System.out.println("(topsCount[i] " + (topsCount[i]));
			// System.out.println("(topsCount[i + 1] - topsCount[i]) / 10 " +
			// ((topsCount[i + 1] - topsCount[i]) / 10));
			// System.out.println("Math.round((topsCount[i + 1] - topsCount[i])
			// / 10 " + Math.round((topsCount[i + 1] - topsCount[i]) / 10));

			startsOfSteps[i] = topsCount[i] + Math.round((topsCount[i + 1] - topsCount[i]) / 10);
			// System.out.println("startsOfSteps[i] " + startsOfSteps[i]);
		}

		return startsOfSteps;
	}

	/*public List getStartTimeOfSteps(String[] packetCounter, Integer[] startsOfSteps) {
		List times = new ArrayList();
		double time = 0;
		for (int i = 0; i < packetCounter.length; i++) {
			for (int j = 0; j < startsOfSteps.length; j++) {
				// System.out.println("startsOfSteps[j] " + startsOfSteps[j]);
				// System.out.println("new Integer(packetCounter[i]) " + new
				// Integer(packetCounter[i]));
				if (startsOfSteps[j].equals(new Integer(packetCounter[i]))) {
					// System.out.println("test 2");
					times.add(time);
				}
			}
			time = time + 1 / 75.0;
		}
		// System.out.println("test ");
		for (int i = 0; i < times.size(); i++) {
			// System.out.println("Time " + times.get(i));
		}
		return times;
	}*/

	public int intervalCount = 1;
	public int intervalNumber = 1;
	public int pc = 1;
	public Map<Integer, Integer> testMap = new HashMap<Integer, Integer>();
	boolean intervalCountStart = true;
	boolean accCons = true;
	public Map<Integer, Integer> starts = new HashMap<Integer, Integer>();
	int previosValue = 0;
	int f = 1;

	public List<Integer> start(List<AccelerationMeasurement> list) {
		list.forEach(a -> {
			if (intervalCount == 1) {
				// System.out.println("test 123 " + pc);
				accCons = test(a);
				pc = a.getPacketCounter();
			}

			intervalCount++;
			a.setMotionless(test(a));
			if (intervalCountStart) {
				intervalNumber++;
				// System.out.println("intervalCountStart");
				intervalCountStart = false;
				starts.put(intervalNumber - 1, intervalCount);
				testMap.put(intervalNumber - 1, pc);
				intervalCount = 1;
			}

			if (test(a) != accCons) {
				// System.out.println("isIntervalChanged");
				intervalCountStart = true;
				accCons = test(a);
			}
		});
		// System.out.println("!!!!!!!!!!!test");
		List<HumanStep> humanSteps = new ArrayList<HumanStep>();
		for (int i = 1; i <= starts.size(); i++) {
			humanSteps.add(new HumanStep(testMap.get(i), starts.get(i), i));
			// System.out.println("!!!! " + starts.get(i) + " pc = " +
			// testMap.get(i));
		}

		/*
		 * humanSteps.forEach(a -> { System.out.println(a.getId() + " " +
		 * a.getStartIntervalPacketCounter() + " " + a.getIntervalCount()); });
		 */
		Comparator<HumanStep> cmp = new Comparator<HumanStep>() {
			public int compare(HumanStep o1, HumanStep o2) {
				return new Integer(o2.getIntervalCount()).compareTo(new Integer(o1.getIntervalCount()));
			}
		};
		humanSteps.sort(cmp);

		humanSteps.forEach(a -> {
			// System.out.println(a.getId() + " " +
			// a.getStartIntervalPacketCounter() + " " + a.getIntervalCount());
		});

		List<Integer> stepStartId = new ArrayList<Integer>();

		humanSteps.forEach(a -> {
			final int pv = a.getIntervalCount();
			if (f > 3) {
				// System.out.println("a.getIntervalCount() " +
				// a.getIntervalCount());
				// System.out.println("new Double(previosValue / 2) " + new
				// Double(previosValue / 2));
				if (Double.compare(new Double(a.getIntervalCount() / 2), new Double(previosValue / 3)) > 0) {
					stepStartId.add(a.getStartIntervalPacketCounter());
				} else {
					return;
				}
			} else {
				stepStartId.add(a.getStartIntervalPacketCounter());
			}
			System.out.println(a.getIntervalCount());
			previosValue = new Integer(a.getIntervalCount());
			f++;
		});

		stepStartId.forEach(a -> {
			System.out.println(a);
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