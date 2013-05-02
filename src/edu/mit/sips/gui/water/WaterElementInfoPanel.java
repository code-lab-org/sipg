package edu.mit.sips.gui.water;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.sips.core.water.WaterElement;

/**
 * The Class WaterElementInfoPanel.
 */
public class WaterElementInfoPanel extends JPanel {
	private static final long serialVersionUID = -821170818354101199L;

	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, withdrawalsLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, electricityLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 */
	public WaterElementInfoPanel(WaterElement element) {
		productionLabel = new JLabel(
				format.format(element.getWaterProduction()),
				JLabel.RIGHT);
		withdrawalsLabel = new JLabel(
				format.format(element.getWaterWithdrawals()),
				JLabel.RIGHT);
		inputLabel = new JLabel(
				format.format(element.getWaterInput()),
				JLabel.RIGHT);
		outputLabel = new JLabel(
				format.format(element.getWaterOutput()),
				JLabel.RIGHT);
		expensesLabel = new JLabel(
				format.format(element.getTotalExpense()),
				JLabel.RIGHT);
		electricityLabel = new JLabel(
				format.format(element.getElectricityConsumption()),
				JLabel.RIGHT);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;

		if(element.getTemplate() == null 
				|| !element.getTemplate().isTransport()) {
			c.weightx = 0;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("Water Production:"), c);
			c.gridx++;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(productionLabel, c);
			c.gridx++;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("<html>m<sup>3</sup>/year</html>"), c);
			c.gridy++;
			c.gridx = 0;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("Aquifer Withdrawals:"), c);
			c.gridx++;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(withdrawalsLabel, c);
			c.gridx++;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("<html>m<sup>3</sup>/year</html>"), c);
			c.gridy++;
		}
		c.gridx = 0;
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			c.weightx = 0;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("Water Input:"), c);
			c.gridx++;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(inputLabel, c);
			c.gridx++;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("<html>m<sup>3</sup>/year</html>"), c);
			c.gridy++;
			c.gridx = 0;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("Water Output:"), c);
			c.gridx++;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(outputLabel, c);
			c.gridx++;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			add(new JLabel("<html>m<sup>3</sup>/year</html>"), c);
			c.gridy++;
		}
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Expenses:"), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(expensesLabel, c);
		c.gridx++;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("<html>SAR/year</html>"), c);
		c.gridy++;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Electricity Use:"), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(electricityLabel, c);
		c.gridx++;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("<html>MWh/year</html>"), c);
		c.gridy++;
	}
}
