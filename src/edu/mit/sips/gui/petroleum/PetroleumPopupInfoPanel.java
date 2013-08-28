package edu.mit.sips.gui.petroleum;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;

/**
 * The Class PetroleumElementInfoPanel.
 */
public class PetroleumPopupInfoPanel extends DefaultPopupInfoPanel {
	private static final long serialVersionUID = -821170818354101199L;

	private final PetroleumElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, withdrawalsLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, electricityLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public PetroleumPopupInfoPanel(PetroleumElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(
				format.format(element.getPetroleumProduction()),
				JLabel.RIGHT);
		withdrawalsLabel = new JLabel(
				format.format(element.getPetroleumWithdrawals()),
				JLabel.RIGHT);
		inputLabel = new JLabel(
				format.format(element.getPetroleumInput()),
				JLabel.RIGHT);
		outputLabel = new JLabel(
				format.format(element.getPetroleumOutput()),
				JLabel.RIGHT);
		expensesLabel = new JLabel(
				format.format(element.getTotalExpense()),
				JLabel.RIGHT);
		electricityLabel = new JLabel(
				format.format(element.getElectricityConsumption()),
				JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxPetroleumProduction() > 0) {
			addField("Petroleum Production:", productionLabel, 
					"<html>m<sup>3</sup></html>");
			addField("Land Use:", withdrawalsLabel, 
					"<html>m<sup>3</sup></html>");
		}
		if(element.getTemplateName() == null 
				|| element.getMaxPetroleumInput() > 0) {
			addField("Petroleum Input:", inputLabel, 
					"<html>m<sup>3</sup></html>");
			addField("Petroleum Output:", outputLabel, 
					"<html>m<sup>3</sup></html>");
		}
		addField("Total Expenses:", expensesLabel, 
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
				format.format(element.getPetroleumProduction()));
		withdrawalsLabel.setText(
				format.format(element.getPetroleumWithdrawals()));
		inputLabel.setText(
				format.format(element.getPetroleumInput()));
		outputLabel.setText(
				format.format(element.getPetroleumOutput()));
		expensesLabel.setText(
				format.format(element.getTotalExpense()));
		electricityLabel.setText(
				format.format(element.getElectricityConsumption()));
	}
}
