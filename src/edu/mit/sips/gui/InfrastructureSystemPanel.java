package edu.mit.sips.gui;

import java.awt.Color;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;

/**
 * The Class InfrastructureSystemPanel.
 */
public abstract class InfrastructureSystemPanel extends JTabbedPane implements UpdateListener {
	private static final long serialVersionUID = -5223317851664526305L;
	private final InfrastructureSystem.Local infrastructureSystem;

	/**
	 * Instantiates a new infrastructure system panel.
	 *
	 * @param infrastructureSystem the infrastructure system
	 */
	public InfrastructureSystemPanel(InfrastructureSystem.Local infrastructureSystem) {
		this.infrastructureSystem = infrastructureSystem;
	}
	
	/**
	 * Gets the infrastructure system.
	 *
	 * @return the infrastructure system
	 */
	public InfrastructureSystem.Local getInfrastructureSystem() {
		return infrastructureSystem;
	}
	
	/**
	 * Gets the society.
	 *
	 * @return the society
	 */
	public Society getSociety() {
		return infrastructureSystem.getSociety();
	}

	/**
	 * Initialize.
	 */
	public abstract void initialize();
	
	/**
	 * Update.
	 */
	public abstract void update(int year);
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param valueAxis the value axis
	 * @param areaDataset the dataset
	 * @return the j free chart
	 */
	protected ChartPanel createStackedAreaChart(String valueAxis,
			TableXYDataset areaDataset, TableXYDataset lineDataset) {
		JFreeChart chart = ChartFactory.createStackedXYAreaChart(
						null, "Year", valueAxis, areaDataset, 
						PlotOrientation.VERTICAL, true, false, false);
		if(chart.getPlot() instanceof XYPlot) {
			XYPlot xyPlot = (XYPlot) chart.getPlot();
			xyPlot.setBackgroundPaint(Color.WHITE);
			xyPlot.setDomainGridlinePaint(Color.GRAY);
			xyPlot.setRangeGridlinePaint(Color.GRAY);
			// Change to a DateAxis to display years: maintain default styling.
			ValueAxis oldAxis = xyPlot.getDomainAxis();
			xyPlot.setDomainAxis(new DateAxis("Year"));
			xyPlot.getDomainAxis().setLabelFont(oldAxis.getLabelFont());
			xyPlot.getDomainAxis().setTickLabelFont(oldAxis.getTickLabelFont());
			
			if(lineDataset != null) {
				xyPlot.setDataset(1, lineDataset);
				xyPlot.setRenderer(1, new XYLineAndShapeRenderer());
				xyPlot.getRenderer(1).setSeriesPaint(0, Color.black);
				xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
			}
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumDrawHeight(400);
		chartPanel.setMaximumDrawHeight(1050);
		chartPanel.setMinimumDrawWidth(600);
		chartPanel.setMaximumDrawWidth(1680);
		return chartPanel;
	}
	/**
	 * Creates the stacked area chart.
	 *
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @return the chart panel
	 */
	protected ChartPanel createStackedAreaChart(String valueAxis,
			TableXYDataset areaDataset) {
		return createStackedAreaChart(valueAxis, areaDataset, null);
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param valueAxis the value axis
	 * @param seriesCollection the series collection
	 * @return the j free chart
	 */
	protected ChartPanel createTimeSeriesChart(String valueAxis, 
			TimeSeriesCollection seriesCollection) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, "Year", valueAxis, seriesCollection, 
				true, false, false);
		if(chart.getPlot() instanceof XYPlot) {
			XYPlot xyPlot = (XYPlot) chart.getPlot();
			xyPlot.setRenderer(new XYLineAndShapeRenderer());
			xyPlot.setBackgroundPaint(Color.WHITE);
			xyPlot.setDomainGridlinePaint(Color.GRAY);
			xyPlot.setRangeGridlinePaint(Color.GRAY);
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumDrawHeight(400);
		chartPanel.setMaximumDrawHeight(1050);
		chartPanel.setMinimumDrawWidth(600);
		chartPanel.setMaximumDrawWidth(1680);
		return chartPanel;
	}
	

	
	/**
	 * Update series.
	 *
	 * @param series the series
	 * @param year the year
	 * @param value the value
	 */
	protected void updateSeries(DefaultTableXYDataset dataset, Comparable<?> key, int year, double value) {
		int index = dataset.indexOf(key);
		if(index < 0) {
			dataset.addSeries(new XYSeries(key, true, false));
			index = dataset.indexOf(key);
		}
		dataset.getSeries(index).addOrUpdate(new Year(year).getLastMillisecond(), value);
	}
	
	/**
	 * Update a series collection.
	 *
	 * @param seriesCollection the series collection
	 * @param key the key
	 * @param year the year
	 * @param value the value
	 */
	protected void updateSeriesCollection(TimeSeriesCollection seriesCollection, 
			Comparable<?> key, int year, double value) {
		// Note: TimeSeriesCollection.getSeries returns a null TimeSeries
		// if the key does not exist, however XYSeriesCollection.getSeries
		// actually throws an UnknownKeyException which must be handled 
		// differently. Both are included here for good programming practice.
		TimeSeries series;
		try {
			series = seriesCollection.getSeries(key);
		} catch(UnknownKeyException ex) {
			series = null;
		}
		if(series == null) {
			series = new TimeSeries(key);
			seriesCollection.addSeries(series);
		}
		series.addOrUpdate(new Year(year), value);
	}
}
