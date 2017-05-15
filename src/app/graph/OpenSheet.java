package app.graph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import app.main.SettingPanel;

@SuppressWarnings("serial")
public class OpenSheet extends ApplicationFrame {
	private XYSeries[] graphs;
	/*
	 * private XYSeries roll = new XYSeries("Roll"); private XYSeries pitch =
	 * new XYSeries("Pitch"); private XYSeries yaw = new XYSeries("Yaw");
	 */
    public static final String STYLE_LINE = "line";
    /** Line style: dashed */
    public static final String STYLE_DASH = "dash";
    /** Line style: dotted */
    public static final String STYLE_DOT = "dot";
	private JPanel content = new JPanel(new BorderLayout());
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private Timer timer;
	private FileReader reader;
	private FileReader2 reader2;
	private String chartTitle;
	private ChartPanel chartPanel;
	private JFreeChart xylineChart;
	private Draw draw = new Draw();
	private Draw2 draw2 = new Draw2();
	private int index = 0;
	private double timeTest = 0;
	private boolean isClean = false;
	double time = 0;
	long sleepTime;
	DeviationRenderer renderer;
	XYItemRenderer[] itemRenderer;

	public OpenSheet(String applicationTitle, String chartTitle) {
		super(applicationTitle);
		this.chartTitle = chartTitle;
		/* renderer = new DeviationRenderer(true, false); 
		renderer.setSeriesFillPaint(0, Color.DARK_GRAY); 
		renderer.setSeriesFillPaint(1, new Color(255,170,140)); 
		renderer.setSeriesFillPaint(2, new Color(255,170,140)); */
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "", "", new XYSeriesCollection());
		//xylineChart.getXYPlot().setRenderer(renderer); 
		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1600, 400));
		chartPanel.setMouseZoomable(false);
		//xylineChart.getXYPlot().setRenderer(renderer); 
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(0, Color.RED); 
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(1, Color.BLUE); 
		
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(2, Color.GRAY); 
		xylineChart.getXYPlot().setBackgroundPaint(Color.WHITE);
		
		Stroke soild = new BasicStroke(2.0f);   
		xylineChart.getXYPlot().setRangeCrosshairStroke(soild);
		
		
		xylineChart.getXYPlot().setRangeGridlinesVisible(true);
		xylineChart.getXYPlot().setRangeCrosshairVisible(true);
		
		xylineChart.getXYPlot().setRangeGridlinePaint(Color.BLACK);
		xylineChart.getXYPlot().setRangeCrosshairPaint(Color.BLACK);
		content.add(chartPanel, BorderLayout.CENTER);
		setContentPane(content);
	}

	public void addData(String filePath) throws IOException {
		reader2 = new FileReader2(filePath, new Double(0));
		sleepTime = (long) (1 / reader2.getPeriod() * 1000);
		graphs = new XYSeries[3];
		graphs[0] = new XYSeries(AccelerationMeasurement.ACCELERATION_X);
		graphs[1] = new XYSeries(AccelerationMeasurement.ACCELERATION_Y);
		graphs[2] = new XYSeries(AccelerationMeasurement.ACCELERATION_Z);
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "Czas(s)", "Przyspieszenie(m/s2)", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);
		xylineChart.getXYPlot().getDomainAxis().setRange(reader2.getMinX(), reader2.getMaxX());
		xylineChart.getXYPlot().getRangeAxis().setRange(reader2.getMinY(), reader2.getMaxY());
		xylineChart.getXYPlot().setBackgroundPaint(Color.WHITE);
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(0, Color.RED); 
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(1, Color.BLUE); 
		xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesPaint(2, Color.GREEN); 
		xylineChart.getXYPlot().setDomainGridlinesVisible(true);
		xylineChart.getXYPlot().setRangeGridlinesVisible(true);
		xylineChart.getXYPlot().setRangeGridlinePaint(Color.BLACK);
		xylineChart.getXYPlot().setDomainGridlinePaint(Color.BLACK);
		xylineChart.getXYPlot().setRangeCrosshairPaint(Color.BLACK);
		xylineChart.getXYPlot().setRangeCrosshairStroke(new BasicStroke(2.0f));
		xylineChart.getXYPlot().setRangeCrosshairVisible(true);
		xylineChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(1.2f));
		xylineChart.getXYPlot().getRenderer().setSeriesStroke(1, new BasicStroke(1.2f));
		xylineChart.getXYPlot().getRenderer().setSeriesStroke(2, new BasicStroke(1.2f));

	/*	XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer() {
	         public Shape lookupLegendShape(int series)
	         {
	            return new Rectangle(15, 15);
	         }
	         public Line lookupLegendLine(int series)
	         {
	            return new Line();
	         }
	      }; 
	      rr.setShapesVisible(false);
	      itemRenderer = new XYItemRenderer[2];
	      itemRenderer[0] = rr;
	      
	      xylineChart.getXYPlot().setRenderers(itemRenderer);*/

	      /*XYLineAndShapeRenderer render2 = new XYLineAndShapeRenderer() {
	    	  Stroke regularStroke = new BasicStroke();
	    	  Stroke dashedStroke = new BasicStroke(
	    	                            1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	    	                            1.0f, new float[] {10.0f, 6.0f}, 0.0f );
	    	  @Override
	    	  public Stroke getItemStroke(int row, int column) {
	    	    
	    	    if (true) { 
	    	      return dashedStroke; 
	    	    } else { 
	    	      return regularStroke; 
	    	    }
	    	  }
	    	};
	      renderer = new DeviationRenderer(true, false); */
	  
	     // itemRenderer[1] = render2;

		//xylineChart.getXYPlot().getRenderer().setSeriesStroke(0,stroke);
		//xylineChart.getXYPlot().getRendererForDataset(xylineChart.getXYPlot().getDataset(0)).setSeriesStroke(0,soild);
		
		//xylineChart.getXYPlot().setRangeGridlinesVisible(true);
		//xylineChart.getXYPlot().setRangeCrosshairVisible(true);

		chartPanel.setChart(xylineChart);
		chartPanel.setMouseZoomable(true);

	}

	   private BasicStroke toStroke(String style) {
	        BasicStroke result = null;
	        
	        if (style != null) {
	            float lineWidth = 1.5f;
	            float dash[] = {5.0f};
	            float dot[] = {lineWidth};
	    
	            if (style.equalsIgnoreCase(STYLE_LINE)) {
	                result = new BasicStroke(lineWidth);
	            } else if (style.equalsIgnoreCase(STYLE_DASH)) {
	                result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	            } else if (style.equalsIgnoreCase(STYLE_DOT)) {
	                result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
	            }
	        }//else: input unavailable
	        
	        return result;
	    }//toStroke()
	
	public void resetChart() {
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
		timer.cancel();
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "", "", new XYSeriesCollection());
		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1600, 400));
		chartPanel.setMouseZoomable(false);
		content.add(chartPanel, BorderLayout.CENTER);
		setContentPane(content);
	}

	public void startDraw3(String filePath, boolean toClean, double speed) {
		sleepTime = (long) (1 / reader2.getPeriod() * 1000);
		if (toClean) {
			for (int i = 0; i < graphs.length; i++) {
				graphs[i].clear();
			}
			index = 0;
			timeTest = 0;
		}

		reader2.getAccelerationMeasurements().forEach(a -> {
			System.out.println("test " + time);
			timer = new Timer();
			draw2 = new Draw2(a, timer, time);
			long period = (long) ((1 / reader2.getPeriod() * 1000) / speed);
			timer.schedule(draw2, 0, period);

			time = time + 1 / reader2.getPeriod();

			try {
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

	}

	int i = 0;

	public Map<String, String[]> convert(List<AccelerationMeasurement> m) {
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
		allData.put("Acc_X", dataX);
		allData.put("Acc_Y", dataY);
		allData.put("Acc_Z", dataZ);
		allData.put("pc", pc);
		return allData;

	}

	public void startDraw(String filePath, boolean toClean, double speed) {
		
		DataAnalyzer a = new DataAnalyzer();
		List<Integer> startList = a.start(reader2.getAccelerationMeasurements());
		
		if (toClean) {
			for (int i = 0; i < graphs.length; i++) {
				graphs[i].clear();
			}
			index = 0;
			timeTest = 0;
		}

		timer = new Timer();

		Map<String, String[]> allDataTest = convert(reader2.getAccelerationMeasurements());
		// public Draw(FileReader reader, Timer timer, int index, double time,
		// boolean isClean, Map<String, double[]> allData) {

		draw = new Draw(reader2, timer, index, timeTest, isClean, allDataTest, startList);
		long period = (long) ((1 / reader2.getPeriod() * 1000) / speed);
		timer.schedule(draw, 0, period);
	}

	/*
	 * public void startDraw2(String filePath, boolean toClean, double speed) {
	 * if (toClean) { for(int i = 0;i < graphs.length; i++){ graphs[i].clear();
	 * } index = 0; timeTest = 0; }
	 * 
	 * timer = new Timer(); draw = new Draw(reader2, timer, index, timeTest,
	 * isClean, filePath); long period = (long) ((1 / reader2.getPeriod() *
	 * 1000) / speed); timer.schedule(draw, 0, period); }
	 */

	public void pauseDraw(String filePath) {
		sleepTime = new Long(2000000000);
		// index = draw.getI();
		// timeTest = draw.getTime();
		// timer.cancel();
	}

	private XYDataset createDataset() {
		for (int i = 0; i < graphs.length; i++) {
			dataset.addSeries(graphs[i]);
		}
		return dataset;
	}

	public static void main(String[] args) {
		OpenSheet chart = new OpenSheet("", "");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}

	public boolean isClean() {
		return isClean;
	}

	public void setClean(boolean isClean) {
		this.isClean = isClean;
	}

	private class Draw2 extends TimerTask {
		private double accX;
		private double accY;
		private double accZ;
		private double time = 0;
		private Timer timer;

		public Draw2(AccelerationMeasurement m, Timer timer, double time) {
			this.accX = m.getAccelerationX();
			this.accY = m.getAccelerationX();
			this.accZ = m.getAccelerationX();
			this.time = time;
			this.timer = timer;
		}

		public Draw2() {
		}

		public void run() {
			try {
				graphs[0].add(time, new Double(accX));
				graphs[1].add(time, new Double(accY));
				graphs[2].add(time, new Double(accZ));

				time = time + 1 / reader2.getPeriod();
				timer.cancel();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		public double getTime() {
			return time;
		}

	}

	private class Draw extends TimerTask {
		private int i = 0;
		private double time = 0;
		private String filePath = "";
		private FileReader2 reader = null;
		private Map<String, String[]> allData = new HashMap<String, String[]>();
		List<Integer> startList;
		public Draw(FileReader2 reader, Timer timer, int index, double time, boolean isClean,
				Map<String, String[]> allData, List<Integer> startList) {
			this.reader = reader;
			i = index;
			this.time = time;
			this.filePath = filePath;
			this.allData = allData;
			this.startList = startList;
		}

		public Draw() {
		}

		public void run() {
			try {
				if ((reader.getRowNumber() - 1) != i) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! " +
					// (reader.getRowsNumber(filePath) - 1));

					graphs[0].add(time, new Double(allData.get("Acc_X")[i]));
					graphs[1].add(time, new Double(allData.get("Acc_Y")[i]));
					graphs[2].add(time, new Double(allData.get("Acc_Z")[i]));
					/*if(startList.contains(new Integer(allData.get("pc")[i]))){
						System.out.println("!!!!!!!!!!!!!!!!!!!testtest");
						Marker m3 = new ValueMarker(time); 
						m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					}*/
					
					
					
					//DataAnalyzer dataAnalyzer = new DataAnalyzer();
					//List times = dataAnalyzer.analyze(reader.getAllData());
					//System.out.println(allData.get("pc")[i]);
					
					
					/*
					 * for(int j = 0;j < graphs.length-1; j++){
					 * graphs[j].add(time, new
					 * Double(allData.get(reader.getHeaders()[j])[i])); }
					 */
					// graphs[graphs.length-1].add(2, new
					// Double(allData.get(reader.getHeaders()[j])[i]));
					/*DataAnalyzer dataAnalyzer = new DataAnalyzer();
					List times = dataAnalyzer.analyze(allData);

					if (times.contains(time)) {
						Marker m3 = new ValueMarker(time);
						m3.setPaint(Color.BLACK);
						xylineChart.getXYPlot().addDomainMarker(m3);
					}*/
					time = time + 1 / reader.getPeriod();
					i++;
				} else {
					SettingPanel.setStartButton();
					timer.cancel();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		public double getTime() {
			return time;
		}

		public int getI() {
			return i;
		}
	}
}