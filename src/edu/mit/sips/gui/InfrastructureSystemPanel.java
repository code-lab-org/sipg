package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;

/**
 * The Class InfrastructureSystemPanel.
 */
public abstract class InfrastructureSystemPanel extends JTabbedPane implements UpdateListener {
	private static final long serialVersionUID = -5223317851664526305L;
	private final InfrastructureSystem infrastructureSystem;
	private final JFileChooser fileChooser = new JFileChooser();

	/**
	 * Instantiates a new infrastructure system panel.
	 *
	 * @param infrastructureSystem the infrastructure system
	 */
	public InfrastructureSystemPanel(InfrastructureSystem infrastructureSystem) {
		this.infrastructureSystem = infrastructureSystem;
	}
	
	/**
	 * Gets the infrastructure system.
	 *
	 * @return the infrastructure system
	 */
	public InfrastructureSystem getInfrastructureSystem() {
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
	protected Component createStackedAreaChart(final String valueAxis,
			final TableXYDataset areaDataset, final String valueAxis2, 
			final TableXYDataset lineDataset) {
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
				if(valueAxis2 != null) {
					xyPlot.setRangeAxis(1,new NumberAxis(valueAxis2));
					xyPlot.getRangeAxis(1).setLabelFont(
							xyPlot.getRangeAxis(0).getLabelFont());
					xyPlot.getRangeAxis(1).setTickLabelFont(
							xyPlot.getRangeAxis(0).getTickLabelFont());
					xyPlot.mapDatasetToRangeAxis(1, 1);
				}
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
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton exportAreaButton = new JButton(new AbstractAction("Export Area Data") {
			private static final long serialVersionUID = -7171676288524836282L;

			public void actionPerformed(ActionEvent e) {
				exportDataset(valueAxis, areaDataset);
			}
		});
		buttonPanel.add(exportAreaButton);
		if(lineDataset != null) {
			JButton exportLineButton = new JButton(new AbstractAction("Export Line Data") {
				private static final long serialVersionUID = -7171676288524836282L;
	
				public void actionPerformed(ActionEvent e) {
					exportDataset(valueAxis2, lineDataset);
				}
			});
			buttonPanel.add(exportLineButton);
		}
		panel.add(buttonPanel, BorderLayout.SOUTH);
		return panel;
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @return the chart panel
	 */
	protected Component createStackedAreaChart(String valueAxis,
			TableXYDataset areaDataset) {
		return createStackedAreaChart(valueAxis, areaDataset, null, null);
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param valueAxis the value axis
	 * @param seriesCollection the series collection
	 * @return the j free chart
	 */
	protected Component createTimeSeriesChart(final String valueAxis, 
			final TimeSeriesCollection seriesCollection) {
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

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		JButton exportAreaButton = new JButton(new AbstractAction("Export Data") {
			private static final long serialVersionUID = -7171676288524836282L;

			public void actionPerformed(ActionEvent e) {
				exportDataset(valueAxis, seriesCollection);
			}
		});
		panel.add(exportAreaButton, BorderLayout.SOUTH);
		return panel;
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
		dataset.getSeries(index).addOrUpdate(new Year(year).getFirstMillisecond(), value);
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

	/**
	 * Export dataset.
	 */
	protected void exportDataset(String valueAxis, XYDataset dataset) {
		if(JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
			File f = fileChooser.getSelectedFile();
			
			if(!f.exists() || JOptionPane.YES_OPTION == 
					JOptionPane.showConfirmDialog(this, 
							"Overwrite existing file " + f.getName() + "?",
							"File Exists", JOptionPane.YES_NO_OPTION)) {

				try {
					BufferedWriter bw = Files.newBufferedWriter(
							Paths.get(f.getPath()), Charset.defaultCharset());

					StringBuilder b = new StringBuilder();
					b.append(valueAxis)
						.append("\n");

					Calendar calendar = Calendar.getInstance();
					if(dataset.getSeriesCount() > 0) {
						b.append("Year");
						for(int j = 0; j < dataset.getSeriesCount(); j++) {
							b.append(", ").append(dataset.getSeriesKey(j));
						}
						b.append("\n");
						for(int i = 0; i < dataset.getItemCount(0); i++) {
							calendar.setTimeInMillis((long)dataset.getXValue(0, i));
							b.append(calendar.get(Calendar.YEAR));
							for(int j = 0; j < dataset.getSeriesCount(); j++) {
								b.append(", ").append(dataset.getYValue(j, i));
							}
							b.append("\n");
						}
					}
					/*
					for(int i = 0; i < dataset.getSeriesCount(); i++) {
						b.append(dataset.getSeriesKey(i))
							.append("\n");
						for(int j = 0; j < dataset.getItemCount(i); j++) {
							b.append(dataset.getXValue(i, j))
								.append(", ")
								.append(dataset.getYValue(i, j))
								.append("\n");
						}
					}
					*/
					
					bw.write(b.toString());
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
