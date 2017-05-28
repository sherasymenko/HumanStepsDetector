package app.graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import app.graph.acceleration.AccelerationDataAnalyzer;
import app.graph.acceleration.AccelerationFileReader;
import app.graph.acceleration.AccelerationMeasurement;
import app.graph.euler_orientation.EulerOrientationDataAnalyzer;
import app.graph.euler_orientation.EulerOrientationFileReader;
import app.graph.euler_orientation.EulerOrientationMeasurement;
import app.main.AppText;
import app.main.SettingPanel;

@SuppressWarnings("serial")
public class OpenSheet extends ApplicationFrame {
	private JPanel content = new JPanel(new BorderLayout());
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private XYSeries[] graphs;
	private XYPlot xyPlot;
	private ChartPanel chartPanel;
	private JFreeChart xylineChart;
	private Timer timer;
	private AccelerationFileReader accReader;
	private EulerOrientationFileReader eulerReader;
	private Draw draw = new Draw();
	private int index = 0;
	private double timeTest = 0;
	private boolean isClean = false;
	// private long sleepTime;
	private int i = 0;
	private Double frequency;
	private String chartType;
	private String eulerFilePath;
public boolean toClean = false;
	public OpenSheet(String filePath, String chartType, Double frequency) {
		super(AppText.APPLICATION_TITLE.value());
		this.frequency = frequency;
		this.chartType = chartType;
		try {
			if (chartType.equals(AppText.ACC_CHART.value())) {
				accReader = new AccelerationFileReader(filePath, new Double(0), frequency);
			} else {
				eulerReader = new EulerOrientationFileReader(filePath, new Double(0), frequency);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// sleepTime = (long) (1 / reader2.getFrequency() * 1000);
		graphs = new XYSeries[3];
		if (chartType.equals(AppText.ACC_CHART.value())) {
			graphs[0] = new XYSeries(AppText.ACC_X.value());
			graphs[1] = new XYSeries(AppText.ACC_Y.value());
			graphs[2] = new XYSeries(AppText.ACC_Z.value());
			xylineChart = ChartFactory.createXYLineChart(AppText.ACC_CHART_TITLE.value(),
					AppText.X_AXIX_CHART_LABEL.value(), AppText.Y_AXIX_ACC_CHART_LABEL.value(), createDataset(),
					PlotOrientation.VERTICAL, true, true, false);
		} else {
			graphs[0] = new XYSeries(AppText.EULER_X.value());
			graphs[1] = new XYSeries(AppText.EULER_Y.value());
			graphs[2] = new XYSeries(AppText.EULER_Z.value());
			xylineChart = ChartFactory.createXYLineChart(AppText.EULER_CHART_TITLE.value(),
					AppText.X_AXIX_CHART_LABEL.value(), AppText.Y_AXIX_EULER_CHART_LABEL.value(), createDataset(),
					PlotOrientation.VERTICAL, true, true, false);
		}

		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 300));
		chartPanel.setMouseZoomable(false);
		xyPlot = xylineChart.getXYPlot();
		if (chartType.equals(AppText.ACC_CHART.value())) {
			xyPlot.getDomainAxis().setRange(accReader.getMinX(), accReader.getMaxX());
			xyPlot.getRangeAxis().setRange(accReader.getMinY(), accReader.getMaxY());
		} else {
			xyPlot.getDomainAxis().setRange(eulerReader.getMinX(), eulerReader.getMaxX());
			xyPlot.getRangeAxis().setRange(eulerReader.getMinY(), eulerReader.getMaxY());
		}
		xyPlot.setBackgroundPaint(Color.WHITE);
		xyPlot.setRangeGridlinePaint(Color.BLACK);
		xyPlot.setDomainGridlinePaint(Color.BLACK);
		xyPlot.setRangeCrosshairPaint(Color.BLACK);
		xyPlot.getRendererForDataset(xyPlot.getDataset(0)).setSeriesPaint(0, Color.RED);
		xyPlot.getRendererForDataset(xyPlot.getDataset(0)).setSeriesPaint(1, Color.BLUE);
		xyPlot.getRendererForDataset(xyPlot.getDataset(0)).setSeriesPaint(2, Color.GREEN);
		xyPlot.setDomainGridlinesVisible(true);
		xyPlot.setRangeGridlinesVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setRangeCrosshairStroke(new BasicStroke(2.0f));
		xyPlot.getRenderer().setSeriesStroke(0, new BasicStroke(1.2f));
		xyPlot.getRenderer().setSeriesStroke(1, new BasicStroke(1.2f));
		xyPlot.getRenderer().setSeriesStroke(2, new BasicStroke(1.2f));
		chartPanel.setChart(xylineChart);
		content.add(chartPanel, BorderLayout.CENTER);
		setContentPane(content);
		Frame frame = (Frame) SwingUtilities.getRoot(content);
		frame.setName(chartType);
	}

	
	public String getEulerFilePath() {
		return eulerFilePath;
	}


	public void setEulerFilePath(String eulerFilePath) {
		this.eulerFilePath = eulerFilePath;
	}


	public void resetChart(String chartType) {
		content.removeAll();
		content.revalidate();
		content.repaint();
		for (int i = 0; i < graphs.length; i++) {
			graphs[i].clear();
		}
		index = 0;
		timeTest = 0;
		dataset = new XYSeriesCollection();
		index = 0;
		if (timer != null) {
			timer.cancel();
		}

		for (int i = 0; i < getFrames().length; i++) {
			if (getFrames()[i].getName().equals(chartType)) {
				getFrames()[i].dispose();
			}
		}
	}

	public Map<String, String[]> convertAcc(List<AccelerationMeasurement> m) {
		Map<String, String[]> allData = new HashMap<String, String[]>();
		String[] dataX = new String[m.size()];
		String[] dataY = new String[m.size()];
		String[] dataZ = new String[m.size()];
		String[] pc = new String[m.size()];
		i = 0;
		m.forEach(a -> {
			pc[i] = String.valueOf(a.getPacketCounter());
			dataX[i] = String.valueOf(a.getAccelerationX());
			dataY[i] = String.valueOf(a.getAccelerationY());
			dataZ[i] = String.valueOf(a.getAccelerationZ());
			i++;
		});
		allData.put(AppText.ACC_X_AXIS_IN_FILE.value(), dataX);
		allData.put(AppText.ACC_Y_AXIS_IN_FILE.value(), dataY);
		allData.put(AppText.ACC_Z_AXIS_IN_FILE.value(), dataZ);
		allData.put(AppText.PACKET_COUNTER.value(), pc);
		return allData;

	}

	public Map<String, String[]> convertEuler(List<EulerOrientationMeasurement> m) {
		Map<String, String[]> allData = new HashMap<String, String[]>();
		String[] dataX = new String[m.size()];
		String[] dataY = new String[m.size()];
		String[] dataZ = new String[m.size()];
		String[] pc = new String[m.size()];
		i = 0;
		m.forEach(a -> {
			pc[i] = String.valueOf(a.getPacketCounter());
			dataX[i] = String.valueOf(a.getRoll());
			dataY[i] = String.valueOf(a.getPitch());
			dataZ[i] = String.valueOf(a.getYaw());
			i++;
		});
		allData.put(AppText.ACC_X_AXIS_IN_FILE.value(), dataX);
		allData.put(AppText.ACC_Y_AXIS_IN_FILE.value(), dataY);
		allData.put(AppText.ACC_Z_AXIS_IN_FILE.value(), dataZ);
		allData.put(AppText.PACKET_COUNTER.value(), pc);
		return allData;

	}

	public void startDraw(String filePath, boolean toCleantest, double speed) throws IOException {
		if (toClean) {
			SettingPanel.setStartButton();
			for (int i = 0; i < graphs.length; i++) {
				graphs[i].clear();
			}
			xylineChart.getXYPlot().clearDomainMarkers();
			index = 0;
			timeTest = 0;
		}
		timer = new Timer();

		if (chartType.equals(AppText.ACC_CHART.value())) {
			eulerReader = new EulerOrientationFileReader(eulerFilePath, new Double(0), frequency);
			EulerOrientationDataAnalyzer a = new EulerOrientationDataAnalyzer(eulerReader.getEulerOrientationMeasurement());
			List<Integer> list = a.getList();
			List<Integer> maxList = a.getMaxList();
			List<Integer> minList = a.getMinList();
			List<Double> iList = a.getPointTime(eulerReader.getEulerOrientationMeasurement(), list, frequency);
			//iList.forEach(action -> System.out.println("!!!!! " + action));
			Map<String, String[]> allDataTest = convertAcc(accReader.getAccelerationMeasurements());
			draw = new Draw(accReader.getRowNumber(), timer, index, timeTest, isClean, allDataTest, list, maxList, minList, iList);
		} else {
			EulerOrientationDataAnalyzer a = new EulerOrientationDataAnalyzer(eulerReader.getEulerOrientationMeasurement());
			List<Integer> list = a.getList();
			List<Integer> maxList = a.getMaxList();
			List<Integer> minList = a.getMinList();
			List<Double> iList = a.getPointTime(eulerReader.getEulerOrientationMeasurement(), list, frequency);
			Map<String, String[]> allDataTest = convertEuler(eulerReader.getEulerOrientationMeasurement());
			draw = new Draw(eulerReader.getRowNumber(), timer, index, timeTest, isClean, allDataTest, list, maxList, minList, iList);

		}

		long period = (long) ((1 / frequency * 1000) / speed);
		timer.schedule(draw, 0, period);
	}

	public void pauseDraw(String filePath) {
		Long sleepTime = new Long(2000000000);
		index = draw.getI();
		timeTest = draw.getTime();
		 timer.cancel();
	}
	
	public void restartDraw(){
		
		
	}

	private XYDataset createDataset() {
		for (int i = 0; i < graphs.length; i++) {
			dataset.addSeries(graphs[i]);
		}
		return dataset;
	}

	public boolean isClean() {
		return isClean;
	}

	public void setClean(boolean isClean) {
		this.isClean = isClean;
	}

	private class Draw extends TimerTask {
		private int i = 0;
		private double time = 0;
		private AccelerationFileReader reader = null;
		private Map<String, String[]> allData = new HashMap<String, String[]>();
		List<Integer> list;
		List<Integer> maxList;
		List<Integer> minList;
		int rowNumber;
		List<Double> iList;
		public Draw(int rowNumber, Timer timer, int index, double time, boolean isClean, Map<String, String[]> allData,
				List<Integer> list, List<Integer> maxList, List<Integer> minList,	List<Double> iList) {
			this.rowNumber = rowNumber;
			i = index;
			this.time = time;
			this.allData = allData;
			this.list = list;
			this.maxList = maxList;
			this.minList = minList;
			this.iList = iList;
		}

		public Draw() {
		}

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}

		public void run() {
			try {
				if ((rowNumber - 1) != i) {
					toClean = false;
					graphs[0].add(time, new Double(allData.get(AppText.ACC_X_AXIS_IN_FILE.value())[i]));
					graphs[1].add(time, new Double(allData.get(AppText.ACC_Y_AXIS_IN_FILE.value())[i]));
					graphs[2].add(time, new Double(allData.get(AppText.ACC_Z_AXIS_IN_FILE.value())[i]));

					/*if (iList.contains(time)) {
						//System.out.println("!!!!!!!!!!!!!!!!!!!testtest");
						Marker m3 = new ValueMarker(time, Color.BLACK, new BasicStroke(3f));
						//m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					} */
					
					if (minList.contains(new Integer(allData.get(AppText.PACKET_COUNTER.value())[i]))) {
						//System.out.println("!!!!!!!!!!!!!!!!!!!testtest");
						Marker m3 = new ValueMarker(time, Color.BLACK, new BasicStroke(3f));
						//m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					} 
					if (maxList.contains(new Integer(allData.get(AppText.PACKET_COUNTER.value())[i]))) {
						//System.out.println("!!!!!!!!!!!!!!!!!!!testtest");
						Marker m3 = new ValueMarker(time, Color.DARK_GRAY, new BasicStroke(2f));
						//m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					}
					if (list.contains(new Integer(allData.get(AppText.PACKET_COUNTER.value())[i]))) {
						//System.out.println("!!!!!!!!!!!!!!!!!!!testtest");
						Marker m3 = new ValueMarker(time, Color.GRAY, new BasicStroke(2f));
						//m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					}
					time = time + 1 / frequency;
					i++;
				} else {
					SettingPanel.setStartButton();
					toClean = true;
					timer.cancel();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
}