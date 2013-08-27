package edu.mit.sips.gui.water;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;

/**
 * The Class WaterElementInfoPanel.
 */
public class WaterPopupInfoPanel extends DefaultPopupInfoPanel {
	private static final long serialVersionUID = -821170818354101199L;

	private final WaterElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, withdrawalsLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, electricityLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public WaterPopupInfoPanel(WaterElement element) {
		super(element);
		
		this.element = element;
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


		if(element.getTemplate() == null 
				|| !element.getTemplate().isTransport()) {
			addField("Water Production:", productionLabel, 
					"<html>m<sup>3</sup></html>");
			addField("Aquifer Withdrawals:", withdrawalsLabel, 
					"<html>m<sup>3</sup></html>");
		}
		if(element.getTemplate() == null 
				|| element.getTemplate().isTransport()) {
			addField("Water Input:", inputLabel, 
					"<html>m<sup>3</sup></html>");
			addField("Water Output:", outputLabel, 
					"<html>m<sup>3</sup></html>");
		}
		addField("Expenses:", expensesLabel, 
				"<html>SAR</html>");
		addField("Electricity Use:", electricityLabel, 
				"<html>toe</html>");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(
				format.format(element.getWaterProduction()));
		withdrawalsLabel.setText(
				format.format(element.getWaterWithdrawals()));
		inputLabel.setText(
				format.format(element.getWaterInput()));
		outputLabel.setText(
				format.format(element.getWaterOutput()));
		expensesLabel.setText(
				format.format(element.getTotalExpense()));
		electricityLabel.setText(
				format.format(element.getElectricityConsumption()));
	}
}
