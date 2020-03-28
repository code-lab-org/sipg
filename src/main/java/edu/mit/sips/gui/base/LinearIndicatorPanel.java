package edu.mit.sips.gui.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Class IndicatorPanel.
 */
public class LinearIndicatorPanel extends JPanel {
	private static final long serialVersionUID = 529051523530028975L;
	
	private final BarPanel barPanel;
	
	/**
	 * Instantiates a new linear indicator panel.
	 *
	 * @param label the label
	 * @param minValue the min value
	 * @param maxValue the max value
	 */
	public LinearIndicatorPanel(String label, double minValue, double maxValue) {
		this(label, minValue, maxValue, 
				minValue + (maxValue-minValue)*1/3d, 
				minValue + (maxValue-minValue)*2/3d);
	}
	
	/**
	 * Instantiates a new indicator panel.
	 *
	 * @param label the label
	 * @param minValue the min value
	 * @param maxValue the max value
	 * @param mediumThreshold the medium threshold
	 * @param highThreshold the high threshold
	 */
	public LinearIndicatorPanel(String label, final double minValue, final double maxValue, 
			final double mediumThreshold, final double highThreshold) {
		
		setLayout(new GridBagLayout());
		barPanel = new BarPanel(minValue, maxValue, 
				mediumThreshold, highThreshold);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(barPanel, c);
		c.gridx = 0;
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		add(new JLabel(label, JLabel.CENTER), c);
		initialize();
	}
	
	/**
	 * Update value.
	 *
	 * @param value the value
	 */
	public void setValue(double value) {
		barPanel.value = Math.min(barPanel.maxValue, 
				Math.max(barPanel.minValue, value));
		barPanel.repaint();
	}
	
	public double getValue() {
		return barPanel.value;
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		setValue(barPanel.minValue);
	}
	
	/**
	 * The Class BarPanel.
	 */
	private class BarPanel extends JPanel {
		private static final long serialVersionUID = -3462125701423535850L;

		private final DecimalFormat format = new DecimalFormat("###");
		private final int hSpacing = 5;
		private final int vSpacing = 5;
		private final int tSpacing = getFontMetrics(getFont()).getHeight() + vSpacing;
		private final double minValue, maxValue;
		private final double mediumThreshold, highThreshold;
		private double value;
		
		/**
		 * Instantiates a new bar panel.
		 *
		 * @param minValue the min value
		 * @param maxValue the max value
		 * @param mediumThreshold the medium threshold
		 * @param highThreshold the high threshold
		 */
		public BarPanel(double minValue, double maxValue, 
				double mediumThreshold, double highThreshold) {
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.mediumThreshold = mediumThreshold;
			this.highThreshold = highThreshold;
			this.value = minValue;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D) g.create();

			int barWidth = Math.min(25, getWidth());
			int barHeight = getHeight() - tSpacing - vSpacing;
			int zeroY = barHeight;
			int mediumY, highY, valueY;
			if(maxValue-minValue == 0) {
				mediumY = (int) Math.round(barHeight*(1-1/3d));
				highY = (int) Math.round(barHeight*(1-2/3d));
				valueY = (int) Math.round(barHeight*(1-0));
			} else {
				mediumY = (int) Math.round(barHeight
						* (maxValue-mediumThreshold)/(maxValue-minValue));
				highY = (int) Math.round(barHeight
						* (maxValue-highThreshold)/(maxValue-minValue));
				valueY = (int) Math.round(barHeight
						* (maxValue-value)/(maxValue-minValue));
			}
			
			g2d.setColor(Color.WHITE);
			g2d.fillRect((getWidth() - barWidth)/2, tSpacing, 
					barWidth, barHeight + vSpacing);

			g2d.setColor(Color.GRAY);
			g2d.drawLine((getWidth() - barWidth)/2, tSpacing + highY, 
					(getWidth() + barWidth)/2, tSpacing + highY);
			g2d.drawLine((getWidth() - barWidth)/2, tSpacing + mediumY, 
					(getWidth() + barWidth)/2, tSpacing + mediumY);
			g2d.drawLine((getWidth() - barWidth)/2, tSpacing + zeroY, 
					(getWidth() + barWidth)/2, tSpacing + zeroY);
			
			if(value == minValue) {
				g2d.setColor(Color.BLACK);
			} else if(value < mediumThreshold) {
				g2d.setColor(Color.RED);
			} else if(value < highThreshold) {
				g2d.setColor(Color.YELLOW);
			} else {
				g2d.setColor(Color.GREEN);
			}
			g2d.fillRect((getWidth() - barWidth)/2 + hSpacing, tSpacing + valueY, 
					barWidth - 2*hSpacing, barHeight - valueY + vSpacing);
			g2d.setColor(Color.GRAY);
			g2d.drawRect((getWidth() - barWidth)/2 + hSpacing, tSpacing + valueY, 
					barWidth - 2*hSpacing, barHeight + vSpacing);
			g2d.drawRect((getWidth() - barWidth)/2, tSpacing, 
					barWidth, barHeight + vSpacing);
			
			g2d.setColor(Color.BLACK);
			String valueLabel;
			if(maxValue-minValue == 0) {
				valueLabel = format.format(0);
			} else {
				valueLabel = format.format(100*(value-minValue)/(maxValue-minValue));
			}
			g2d.drawString(valueLabel, 
					(getWidth() - g2d.getFontMetrics().stringWidth(valueLabel))/2f, 
					tSpacing + valueY - 2);
			
			g2d.dispose();
		}
	}
}
