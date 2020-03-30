/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.gui.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.gui.event.UpdateListener;

/**
 * A generic panel to investigate infrastructure system outputs.
 * 
 * @author Paul T. Grogan
 */
public abstract class InfrastructureSystemPanel extends JTabbedPane implements UpdateListener {
	private static final long serialVersionUID = -5223317851664526305L;
	private static Logger logger = Logger.getLogger(InfrastructureSystemPanel.class);
	
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
	 * Creates the chart.
	 *
	 * @param title the title
	 * @param isArea the is area
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param colors the colors
	 * @param lineDataset the line dataset
	 * @param valueAxis2 the value axis 2
	 * @param lineDataset2 the line dataset 2
	 * @return the j free chart
	 */
	private JFreeChart createChart(String title, boolean isArea, final String valueAxis,
			final TableXYDataset areaDataset, Color[] colors, final TableXYDataset lineDataset, 
			final String valueAxis2, final TableXYDataset lineDataset2) {
		final JFreeChart chart;
		if(isArea && areaDataset != null) {
			chart = ChartFactory.createStackedXYAreaChart(
					title, "Year", valueAxis, areaDataset, 
					PlotOrientation.VERTICAL, true, false, false);
			chart.getLegend().setPosition(RectangleEdge.RIGHT);
		} else if(areaDataset != null) {
			chart = ChartFactory.createXYLineChart(
					title, "Year", valueAxis, areaDataset, 
					PlotOrientation.VERTICAL, true, false, false);
			chart.getLegend().setPosition(RectangleEdge.RIGHT);
		} else {
			chart = ChartFactory.createXYLineChart(
					title, "Year", valueAxis, lineDataset, 
					PlotOrientation.VERTICAL, true, false, false);
		}
		LegendTitle lt = new LegendTitle(new LegendItemSource() {
			@Override
			public LegendItemCollection getLegendItems() {
				LegendItemCollection c = new LegendItemCollection();
				for(int i = chart.getXYPlot().getLegendItems().getItemCount() - 1; i >= 0; i--) {
					c.add(chart.getXYPlot().getLegendItems().get(i));
				}
				return c;
			}
		});
		if(chart.getTitle() != null) {
			chart.getTitle().setFont(
					chart.getTitle().getFont().deriveFont(Font.PLAIN));
		}
		lt.setItemFont(chart.getLegend().getItemFont());
		lt.setItemPaint(chart.getLegend().getItemPaint());
		lt.setBackgroundPaint(chart.getLegend().getBackgroundPaint());
		lt.setBorder(1, 1, 1, 1);
		lt.setPosition(RectangleEdge.RIGHT);
		chart.removeLegend();
		chart.addLegend(lt);
		chart.setBackgroundPaint(new JPanel().getBackground());
		
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

			if(!isArea && areaDataset != null) {
				xyPlot.setRenderer(0, new XYLineAndShapeRenderer());
			}
			
			if(areaDataset != null && lineDataset != null) {
				xyPlot.setDataset(1, lineDataset);
				xyPlot.mapDatasetToRangeAxis(1, 0);
				xyPlot.setRenderer(1, new XYLineAndShapeRenderer());
				xyPlot.getRenderer(1).setSeriesPaint(0, Color.black);
			} else if(lineDataset != null) {
				xyPlot.setRenderer(0, new XYLineAndShapeRenderer());
				xyPlot.getRenderer(0).setSeriesPaint(0, Color.black);
			}

			if(lineDataset2 != null) {
				xyPlot.setDataset(2, lineDataset2);
				if(valueAxis2 != null) {
					xyPlot.setRangeAxis(1,new NumberAxis(valueAxis2));
					xyPlot.getRangeAxis(1).setLabelFont(
							xyPlot.getRangeAxis(0).getLabelFont());
					xyPlot.getRangeAxis(1).setTickLabelFont(
							xyPlot.getRangeAxis(0).getTickLabelFont());
					xyPlot.mapDatasetToRangeAxis(2, 1);
				}
				xyPlot.setRenderer(2, new XYLineAndShapeRenderer());
				xyPlot.getRenderer(2).setSeriesPaint(0, Color.red);
			}
			if(colors != null) {
				for(int i = 0; i < colors.length; i++) {
					if(colors[i] != null) {
						xyPlot.getRenderer(0).setSeriesPaint(i, colors[i]);
					}
				}
			}
			xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		}
		return chart;
	}
	
	/**
	 * Creates the single line chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param lineDataset the line dataset
	 * @param colors the colors
	 * @return the j panel
	 */
	protected JPanel createSingleLineChart(String title, String valueAxis,
			TableXYDataset lineDataset, Color[] colors) {
		return createStackedAreaChart(title, valueAxis, null, colors, lineDataset, null, null);
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(String title, String valueAxis,
			TableXYDataset areaDataset) {
		return createStackedAreaChart(title, valueAxis, areaDataset, null, null, null, null);
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param colors the colors
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(String title, String valueAxis,
			TableXYDataset areaDataset, Color[] colors) {
		return createStackedAreaChart(title, valueAxis, areaDataset, colors, null, null);
	}

	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param colors the colors
	 * @param valueAxis2 the value axis 2
	 * @param lineDataset the line dataset
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, Color[] colors, final String valueAxis2, 
			final TableXYDataset lineDataset) {
		return createStackedAreaChart(title, valueAxis, areaDataset, colors, null, valueAxis2, lineDataset);
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param colors the colors
	 * @param lineDataset the line dataset
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, Color[] colors, final TableXYDataset lineDataset) {
		return createStackedAreaChart(title, valueAxis, areaDataset, colors, lineDataset, null, null);
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param colors the colors
	 * @param lineDataset the line dataset
	 * @param valueAxis2 the value axis 2
	 * @param lineDataset2 the line dataset 2
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, final Color[] colors, final TableXYDataset lineDataset, 
			final String valueAxis2, final TableXYDataset lineDataset2) {
		final JPanel panel = new JPanel(new BorderLayout());
		ChartPanel chartPanel = new ChartPanel(createChart(title, true, valueAxis, areaDataset, 
				colors, lineDataset, valueAxis2, lineDataset2));
		panel.add(chartPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton exportAreaButton = new JButton(new AbstractAction("Export Data") {
			private static final long serialVersionUID = -7171676288524836282L;

			public void actionPerformed(ActionEvent e) {
				if(areaDataset!=null) { 
					exportDataset(valueAxis, areaDataset, lineDataset, lineDataset2);
				} else {
					exportDataset(valueAxis, lineDataset);
				}
			}
		});
		// disable export (for now) 
		// buttonPanel.add(exportAreaButton);
		if(areaDataset != null) {
			final JCheckBox areaToggle = new JCheckBox("Stacked Area", true);
			areaToggle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i = 0; i < panel.getComponentCount(); i++) {
						if(panel.getComponent(i) instanceof ChartPanel) {
							panel.remove(i);
							break;
						}
					}
					panel.add(new ChartPanel(createChart(title, areaToggle.isSelected(), valueAxis, areaDataset, 
							colors, lineDataset, valueAxis2, lineDataset2)), BorderLayout.CENTER);
					panel.validate();
				}
			});
			buttonPanel.add(areaToggle);
		}
		panel.add(buttonPanel, BorderLayout.SOUTH);
		return panel;
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param valueAxis2 the value axis 2
	 * @param lineDataset the line dataset
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, final String valueAxis2, 
			final TableXYDataset lineDataset) {
		return createStackedAreaChart(title, valueAxis, areaDataset, null, null, valueAxis2, lineDataset);
	}
	
	/**
	 * Creates the stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the area dataset
	 * @param lineDataset the line dataset
	 * @return the j panel
	 */
	protected JPanel createStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, final TableXYDataset lineDataset) {
		return createStackedAreaChart(title, valueAxis, areaDataset, null, lineDataset, null, null);
	}

	/**
	 * Creates the toggle-able stacked area chart.
	 *
	 * @param title the title
	 * @param valueAxis the value axis
	 * @param areaDataset the dataset
	 * @param colors the colors
	 * @param areaDataset2 the area dataset 2
	 * @param colors2 the colors 2
	 * @return the j free chart
	 */
	protected JTabbedPane createToggleableStackedAreaChart(final String title, final String valueAxis,
			final TableXYDataset areaDataset, final Color[] colors, 
			final TableXYDataset areaDataset2, final Color[] colors2) {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Aggregated", createStackedAreaChart(title, valueAxis, areaDataset, colors));
		tabbedPane.addTab("Disaggregated", createStackedAreaChart(title, valueAxis, areaDataset2, colors2));
		return tabbedPane;
	}

	/**
	 * Export dataset.
	 *
	 * @param valueAxis the value axis
	 * @param dataset the dataset
	 */
	protected void exportDataset(String valueAxis, XYDataset dataset) {
		exportDataset(valueAxis, dataset, null, null);
	}
	
	/**
	 * Export dataset.
	 *
	 * @param valueAxis the value axis
	 * @param dataset the dataset
	 * @param dataset2 the dataset 2
	 * @param dataset3 the dataset 3
	 */
	protected void exportDataset(String valueAxis, XYDataset dataset, XYDataset dataset2, XYDataset dataset3) {
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
						if(dataset2 != null && dataset2.getSeriesCount() > 0) {
							for(int j = 0; j < dataset2.getSeriesCount(); j++) {
								b.append(", ").append(dataset2.getSeriesKey(j));
							}
						}
						if(dataset3 != null && dataset3.getSeriesCount() > 0) {
							for(int j = 0; j < dataset3.getSeriesCount(); j++) {
								b.append(", ").append(dataset3.getSeriesKey(j));
							}
						}
						b.append("\n");
						for(int i = 0; i < dataset.getItemCount(0); i++) {
							calendar.setTimeInMillis((long)dataset.getXValue(0, i));
							b.append(calendar.get(Calendar.YEAR));
							for(int j = 0; j < dataset.getSeriesCount(); j++) {
								b.append(", ").append(dataset.getYValue(j, i));
							}
							if(dataset2 != null && dataset2.getItemCount(0) > i) {
								for(int j = 0; j < dataset2.getSeriesCount(); j++) {
									b.append(", ").append(dataset2.getYValue(j, i));
								}
							}
							if(dataset3 != null && dataset3.getItemCount(0) > i) {
								for(int j = 0; j < dataset3.getSeriesCount(); j++) {
									b.append(", ").append(dataset3.getYValue(j, i));
								}
							}
							b.append("\n");
						}
					}
					
					bw.write(b.toString());
					bw.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
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
	 * Update series.
	 *
	 * @param dataset the dataset
	 * @param key the key
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
}
