package edu.mit.sips.gui.electricity;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;

/**
 * The Class ElectricityElementInfoPanel.
 */
public class ElectricityPopupInfoPanel extends DefaultPopupInfoPanel {
	private static final long serialVersionUID = -821170818354101199L;

	private final ElectricityElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, waterLabel, petroleumLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public ElectricityPopupInfoPanel(ElectricityElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(
				format.format(element.getElectricityProduction()),
				JLabel.RIGHT);
		inputLabel = new JLabel(
				format.format(element.getElectricityInput()),
				JLabel.RIGHT);
		outputLabel = new JLabel(
				format.format(element.getElectricityOutput()),
				JLabel.RIGHT);
		expensesLabel = new JLabel(
				format.format(element.getTotalExpense()),
				JLabel.RIGHT);
		waterLabel = new JLabel(
				format.format(element.getWaterConsumption()),
				JLabel.RIGHT);
		petroleumLabel = new JLabel(
				format.format(element.getPetroleumConsumption()),
				JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxElectricityProduction() > 0) {
			addField("Electricity Production:", productionLabel, 
					"<html>toe</html>");
		}
		if(element.getTemplateName() == null 
				|| element.getMaxElectricityInput() > 0) {
			addField("Electricity Input:", inputLabel, 
					"<html>toe</html>");
			addField("Electricity Output:", outputLabel, 
					"<html>toe</html>");
		}
		addField("Expenses:", expensesLabel, 
				"<html>SAR</html>");
		addField("Water Use:", waterLabel, 
				"<html>m<sup>3</sup></html>");
		addField("Petroleum Use:", petroleumLabel, 
				"<html>toe</html>");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(
				format.format(element.getElectricityProduction()));
		inputLabel.setText(
				format.format(element.getElectricityInput()));
		outputLabel.setText(
				format.format(element.getElectricityOutput()));
		expensesLabel.setText(
				format.format(element.getTotalExpense()));
		waterLabel.setText(
				format.format(element.getWaterConsumption()));
		petroleumLabel.setText(
				format.format(element.getPetroleumConsumption()));
	}
}
