package edu.mit.sips.gui.agriculture;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.base.ElementOperationsPanel;

/**
 * The Class AgricultureElementOperationsPanel.
 */
public class AgricultureElementOperationsPanel extends ElementOperationsPanel {
	private static final long serialVersionUID = 1378172634918587905L;

	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JFormattedTextField landAreaText;
	private final JFormattedTextField foodInputText;
	private final JLabel foodProductionLabel, waterConsumptionLabel, foodOutputLabel, totalExpenseLabel;
	
	/**
	 * Instantiates a new agriculture element operations panel.
	 *
	 * @param element the element
	 */
	public AgricultureElementOperationsPanel(final AgricultureElement element) {
		super(element);
		
		landAreaText = new JFormattedTextField(format);
		landAreaText.setEnabled(element.isOperational());
		landAreaText.setValue(element.getLandArea());
		landAreaText.setColumns(10);
		landAreaText.setHorizontalAlignment(JTextField.RIGHT);
		landAreaText.setValue(element.getMaxLandArea());
		landAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setLandArea(
									((Number) landAreaText.getValue()).doubleValue());
						} catch (IllegalArgumentException ex) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									// TODO exception
									landAreaText.setValue(element.getLandArea());
								}
							});
							JOptionPane.showMessageDialog(getTopLevelAncestor(), 
									ex.getMessage(), "Error", 
									JOptionPane.ERROR_MESSAGE);
						}
						foodProductionLabel.setText(
								format.format(element.getFoodProduction()));
						waterConsumptionLabel.setText(
								format.format(element.getWaterConsumption()));
						totalExpenseLabel.setText(
								format.format(element.getTotalExpense()));
					}
				});
		foodProductionLabel = new JLabel(
				format.format(element.getFoodProduction()), 
				JTextField.RIGHT);
		waterConsumptionLabel = new JLabel(
				format.format(element.getWaterConsumption()), 
				JTextField.RIGHT);
		
		foodInputText = new JFormattedTextField(format);
		foodInputText.setEnabled(element.isOperational());
		foodInputText.setValue(element.getFoodInput());
		foodInputText.setColumns(10);
		foodInputText.setHorizontalAlignment(JTextField.RIGHT);
		foodInputText.setValue(element.getMaxFoodInput());
		foodInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setFoodInput(((Number) 
									foodInputText.getValue()).doubleValue());
						} catch (IllegalArgumentException ex) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									// TODO exception
									foodInputText.setValue(element.getFoodInput());
								}
							});
							JOptionPane.showMessageDialog(getTopLevelAncestor(), 
									ex.getMessage(), "Error", 
									JOptionPane.ERROR_MESSAGE);
						}
						totalExpenseLabel.setText(
								format.format(element.getTotalExpense()));
					}
				});
		foodOutputLabel = new JLabel(
				format.format(element.getFoodOutput()),
				JTextField.RIGHT);
		totalExpenseLabel = new JLabel(
				format.format(element.getTotalExpense()),
				JTextField.RIGHT);
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		if(element.getTemplateName() == null 
				|| element.getMaxLandArea() > 0) {
			c.gridx = 0;
			addInput(elementPanel, c, "Land Area", 
					landAreaText, "<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Max Land Area",
					new JLabel(format.format(element.getMaxLandArea()), 
							JLabel.RIGHT), 
					"<html>km<sup>2</sup></html>");
			addInput(elementPanel, c, "Food Production",
					foodProductionLabel, "kcal/day/year");
			addInput(elementPanel, c, "Water Consumption",
					waterConsumptionLabel, 
					"<html>m<sup>3</sup>/year</html>");
		}
		if(element.getTemplateName() == null 
				|| element.getMaxFoodInput() > 0) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Food Input", 
					landAreaText, "kcal/day/year");
			addInput(elementPanel, c, "Max Food Input",
					new JLabel(format.format(element.getMaxFoodInput()), 
							JLabel.RIGHT), 
					"kcal/day/year");
			addInput(elementPanel, c, "Food Output",
					foodOutputLabel, "kcal/day/year");
		}
		addInput(elementPanel, c, "Total Expense",
				totalExpenseLabel, "SAR/year");
	}

}
