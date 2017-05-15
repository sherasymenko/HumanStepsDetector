package app.graph;

public class HumanStep {

	private int startIntervalPacketCounter;
	private int intervalCount;
	private int id;

	public HumanStep(int startIntervalPacketCounter, int intervalCount, int id) {
		super();
		this.startIntervalPacketCounter = startIntervalPacketCounter;
		this.intervalCount = intervalCount;
		this.id = id;
	}

	public int getStartIntervalPacketCounter() {
		return startIntervalPacketCounter;
	}

	public void setStartIntervalPacketCounter(int startIntervalPacketCounter) {
		this.startIntervalPacketCounter = startIntervalPacketCounter;
	}

	public int getIntervalCount() {
		return intervalCount;
	}

	public void setIntervalCount(int intervalCount) {
		this.intervalCount = intervalCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}