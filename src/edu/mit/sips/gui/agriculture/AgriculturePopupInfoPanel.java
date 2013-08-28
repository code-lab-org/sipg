package edu.mit.sips.gui.agriculture;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;

/**
 * The Class FoodElementInfoPanel.
 */
public class AgriculturePopupInfoPanel extends DefaultPopupInfoPanel {
	private static final long serialVersionUID = -821170818354101199L;

	private final AgricultureElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, landLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, waterLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public AgriculturePopupInfoPanel(AgricultureElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(
				format.format(element.getFoodProduction()),
				JLabel.RIGHT);
		landLabel = new JLabel(
				format.format(element.getLandArea()),
				JLabel.RIGHT);
		inputLabel = new JLabel(
				format.format(element.getFoodInput()),
				JLabel.RIGHT);
		outputLabel = new JLabel(
				format.format(element.getFoodOutput()),
				JLabel.RIGHT);
		expensesLabel = new JLabel(
				format.format(element.getTotalExpense()),
				JLabel.RIGHT);
		waterLabel = new JLabel(
				format.format(element.getWaterConsumption()),
				JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxLandArea() > 0) {
			addField("Food Production:", productionLabel, 
					"<html>kcal/year</html>");
			addField("Land Use:", landLabel, 
					"<html>km<sup>2</sup></html>");
		}
		if(element.getTemplateName() == null 
				|| element.getMaxFoodInput() > 0) {
			addField("Food Input:", inputLabel, 
					"<html>kcal/year</html>");
			addField("Food Output:", outputLabel, 
					"<html>kcal/year</html>");
		}
		addField("Expenses:", expensesLabel, 
				"<html>SAR/year</html>");
		addField("Water Use:", waterLabel, 
				"<html>m<sup>3</sup>/year</html>");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(
				format.format(element.getFoodProduction()));
		landLabel.setText(
				format.format(element.getLandArea()));
		inputLabel.setText(
				format.format(element.getFoodInput()));
		outputLabel.setText(
				format.format(element.getFoodOutput()));
		expensesLabel.setText(
				format.format(element.getTotalExpense()));
		waterLabel.setText(
				format.format(element.getWaterConsumption()));
	}
}
