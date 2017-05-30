package app.graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import app.graph.acceleration.AccelerationFileReader;
import app.graph.acceleration.AccelerationMeasurement;
import app.graph.euler_orientation.EulerOrientationDataAnalyzer;
import app.graph.euler_orientation.EulerOrientationFileReader;
import app.graph.euler_orientation.EulerOrientationMeasurement;
import app.main.AppText;
import app.main.SettingPanel;

@SuppressWarnings("serial")
public class OpenSheet extends ApplicationFrame {
	private final Logger LOGGER = Logger.getLogger(ApplicationFrame.class.getName());
	private JPanel content = new JPanel(new BorderLayout());
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private XYSeries[] graphs;
	private XYPlot xyPlot;
	private ChartPanel chartPanel;
	private JFreeChart xylineChart;
	private Timer timer;
	private AccelerationFileReader accReader;
	private EulerOrientationFileReader eulerReader;
	private Draw draw;
	private int index = 0;
	private double timeValue = 0;
	private int i = 0;
	private Double frequency;
	private String chartType;
	private String eulerFilePath;
	public boolean toClean = false;

	public OpenSheet(String filePath, String chartType, Double frequency) throws Exception {
		super(AppText.APPLICATION_TITLE.get());
		this.frequency = frequency;
		this.chartType = chartType;
		try {
			if (chartType.equals(AppText.ACC_CHART.get())) {
				accReader = new AccelerationFileReader(filePath, frequency);
			} else {
				eulerReader = new EulerOrientationFileReader(filePath, frequency);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		graphs = new XYSeries[3];
		if (chartType.equals(AppText.ACC_CHART.get())) {
			graphs[0] = new XYSeries(AppText.ACC_X.get());
			graphs[1] = new XYSeries(AppText.ACC_Y.get());
			graphs[2] = new XYSeries(AppText.ACC_Z.get());
			xylineChart = ChartFactory.createXYLineChart(AppText.ACC_CHART_TITLE.get(),
					AppText.X_AXIX_CHART_LABEL.get(), AppText.Y_AXIX_ACC_CHART_LABEL.get(), createDataset(),
					PlotOrientation.VERTICAL, true, true, false);
		} else {
			graphs[0] = new XYSeries(AppText.EULER_X.get());
			graphs[1] = new XYSeries(AppText.EULER_Y.get());
			graphs[2] = new XYSeries(AppText.EULER_Z.get());
			xylineChart = ChartFactory.createXYLineChart(AppText.EULER_CHART_TITLE.get(),
					AppText.X_AXIX_CHART_LABEL.get(), AppText.Y_AXIX_EULER_CHART_LABEL.get(), createDataset(),
					PlotOrientation.VERTICAL, true, true, false);
		}

		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 300));
		chartPanel.setMouseZoomable(false);
		xyPlot = xylineChart.getXYPlot();
		if (chartType.equals(AppText.ACC_CHART.get())) {
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
		timeValue = 0;
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
		allData.put(AppText.ACC_X_AXIS_IN_FILE.get(), dataX);
		allData.put(AppText.ACC_Y_AXIS_IN_FILE.get(), dataY);
		allData.put(AppText.ACC_Z_AXIS_IN_FILE.get(), dataZ);
		allData.put(AppText.PACKET_COUNTER.get(), pc);
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
		allData.put(AppText.ACC_X_AXIS_IN_FILE.get(), dataX);
		allData.put(AppText.ACC_Y_AXIS_IN_FILE.get(), dataY);
		allData.put(AppText.ACC_Z_AXIS_IN_FILE.get(), dataZ);
		allData.put(AppText.PACKET_COUNTER.get(), pc);
		return allData;

	}

	public void startDraw(String filePath, boolean toCleantest, double speed) throws IOException {
		if (toClean) {
			SettingPanel.setRestartButton();
			for (int i = 0; i < graphs.length; i++) {
				graphs[i].clear();
			}
			xylineChart.getXYPlot().clearDomainMarkers();
			index = 0;
			timeValue = 0;
		}
		timer = new Timer();

		if (chartType.equals(AppText.ACC_CHART.get())) {
			eulerReader = new EulerOrientationFileReader(eulerFilePath, frequency);
			EulerOrientationDataAnalyzer a = new EulerOrientationDataAnalyzer(
					eulerReader.getEulerOrientationMeasurement());
			List<Integer> list = a.getList();
			List<Integer> maxList = a.getMaxList();
			List<Integer> minList = a.getMinList();
			List<Double> iList = a.getPointTime(eulerReader.getEulerOrientationMeasurement(), list, frequency);
			Map<String, String[]> allData = convertAcc(accReader.getAccelerationMeasurements());
			draw = new Draw(accReader.getRowNumber(), timer, index, timeValue, allData, list, maxList, minList, iList);
		} else {
			EulerOrientationDataAnalyzer a = new EulerOrientationDataAnalyzer(
					eulerReader.getEulerOrientationMeasurement());
			List<Integer> list = a.getList();
			List<Integer> maxList = a.getMaxList();
			List<Integer> minList = a.getMinList();
			List<Double> iList = a.getPointTime(eulerReader.getEulerOrientationMeasurement(), list, frequency);
			Map<String, String[]> allData = convertEuler(eulerReader.getEulerOrientationMeasurement());
			draw = new Draw(eulerReader.getRowNumber(), timer, index, timeValue, allData, list, maxList, minList,
					iList);
		}
		long period = (long) ((1 / frequency * 1000) / speed);
		timer.schedule(draw, 0, period);
	}

	public void pauseDraw(String filePath) {
		index = draw.getI();
		timeValue = draw.getTime();
		timer.cancel();
	}

	private XYDataset createDataset() {
		for (int i = 0; i < graphs.length; i++) {
			dataset.addSeries(graphs[i]);
		}
		return dataset;
	}

	private class Draw extends TimerTask {
		private int i = 0;
		private double time = 0;
		private Map<String, String[]> allData = new HashMap<String, String[]>();
		List<Integer> list;
		List<Integer> maxList;
		List<Integer> minList;
		int rowNumber;

		public Draw(int rowNumber, Timer timer, int index, double time, Map<String, String[]> allData,
				List<Integer> list, List<Integer> maxList, List<Integer> minList, List<Double> iList) {
			this.rowNumber = rowNumber;
			i = index;
			this.time = time;
			this.allData = allData;
			this.list = list;
			this.maxList = maxList;
			this.minList = minList;
		}

		public int getI() {
			return i;
		}

		public double getTime() {
			return time;
		}

		public void run() {
			try {
				if ((rowNumber - 1) != i) {
					toClean = false;
					graphs[0].add(time, new Double(allData.get(AppText.ACC_X_AXIS_IN_FILE.get())[i]));
					graphs[1].add(time, new Double(allData.get(AppText.ACC_Y_AXIS_IN_FILE.get())[i]));
					graphs[2].add(time, new Double(allData.get(AppText.ACC_Z_AXIS_IN_FILE.get())[i]));
					if (minList.contains(new Integer(allData.get(AppText.PACKET_COUNTER.get())[i]))) {
						xylineChart.getXYPlot().addDomainMarker(new ValueMarker(time, Color.BLACK, new BasicStroke(3f)));
					}
					if (maxList.contains(new Integer(allData.get(AppText.PACKET_COUNTER.get())[i]))) {
						xylineChart.getXYPlot().addDomainMarker(new ValueMarker(time, Color.DARK_GRAY, new BasicStroke(2f)));
					}
					if (list.contains(new Integer(allData.get(AppText.PACKET_COUNTER.get())[i]))) {
						xylineChart.getXYPlot().addDomainMarker(new ValueMarker(time, Color.GRAY, new BasicStroke(2f)));
					}
					time = time + 1 / frequency;
					i++;
				} else {
					SettingPanel.setRestartButton();
					toClean = true;
					timer.cancel();
				}
			} catch (NumberFormatException e) {
				LOGGER.warning(new Date() + " NumberFormatException: " + e.getMessage());
			}
		}
	}
}