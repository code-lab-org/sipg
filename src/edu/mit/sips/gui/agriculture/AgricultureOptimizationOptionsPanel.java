package edu.mit.sips.gui.agriculture;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

/**
 * The Class AgricultureOptimizationOptionsPanel.
 */
public class AgricultureOptimizationOptionsPanel extends JPanel {
	private static final long serialVersionUID = 5512529101744980420L;

	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JFormattedTextField domesticFoodPriceText, foodExportPriceText, foodImportPriceText;
	private final JFormattedTextField domesticWaterPriceText;
	
	public AgricultureOptimizationOptionsPanel() {
		domesticFoodPriceText = new JFormattedTextField(format);
		foodExportPriceText = new JFormattedTextField(format);
		foodImportPriceText = new JFormattedTextField(format);
		domesticWaterPriceText = new JFormattedTextField(format);
	}
}
