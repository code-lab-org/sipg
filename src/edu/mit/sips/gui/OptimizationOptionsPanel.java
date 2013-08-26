package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.OptimizationOptions;

/**
 * The Class AgricultureOptimizationOptionsPanel.
 */
public class OptimizationOptionsPanel extends JPanel {
	private static final long serialVersionUID = 5512529101744980420L;

	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JFormattedTextField domesticWaterPriceText, 
	aquiferWaterPriceText, importWaterPriceText, 
	domesticFoodPriceText, importFoodPriceText, 
	exportFoodPriceText, domesticOilPriceText, 
	reservoirOilPriceText, importOilPriceText, 
	exportOilPriceText, domesticElectricityPriceText;
	private final JButton resetDomesticWaterPriceButton, 
	resetAquiferWaterPriceButton, resetImportWaterPriceButton, 
	resetDomesticFoodPriceButton, resetImportFoodPriceButton, 
	resetExportFoodPriceButton, resetDomesticOilPriceButton, 
	resetReservoirOilPriceButton, resetImportOilPriceButton, 
	resetExportOilPriceButton, resetDomesticElectricityPriceButton;
	
	/**
	 * Instantiates a new optimization options panel.
	 *
	 * @param country the country
	 * @param optimizationOptions the optimization options
	 */
	public OptimizationOptionsPanel(final Country country, final OptimizationOptions optimizationOptions) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; 
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2,2,2,2);
		
		add(new JLabel("Domestic Water Price:"), c);
		c.gridx++;
		c.weightx = 1;
		domesticWaterPriceText = new JFormattedTextField(format);
		domesticWaterPriceText.setColumns(10);
		domesticWaterPriceText.setHorizontalAlignment(JTextField.RIGHT);
		domesticWaterPriceText.setValue(country.getWaterSystem().getWaterDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticWaterPrice());
		domesticWaterPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticWaterPrice(
							((Number) domesticWaterPriceText.getValue()).doubleValue() 
							- country.getWaterSystem().getWaterDomesticPrice());
					domesticWaterPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticWaterPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticWaterPriceText, c);
		c.gridx++;
		c.weightx = 0;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetDomesticWaterPriceButton = new JButton("Reset");
		resetDomesticWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				domesticWaterPriceText.setValue(country.getWaterSystem().getWaterDomesticPrice());
			}
		});
		add(resetDomesticWaterPriceButton, c);
		c.gridx = 0;
		c.gridy++;

		add(new JLabel("Aquifer Water Value:"), c);
		c.gridx++;
		aquiferWaterPriceText = new JFormattedTextField(format); 
		aquiferWaterPriceText.setColumns(10);
		aquiferWaterPriceText.setHorizontalAlignment(JTextField.RIGHT);
		aquiferWaterPriceText.setValue(optimizationOptions.getDeltaAquiferWaterPrice());
		aquiferWaterPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaAquiferWaterPrice(
							((Number) aquiferWaterPriceText.getValue()).doubleValue() );
					aquiferWaterPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					aquiferWaterPriceText.setForeground(Color.red);
				}
			}
		});
		add(aquiferWaterPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetAquiferWaterPriceButton = new JButton("Reset");
		resetAquiferWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				aquiferWaterPriceText.setValue(0);
			}
		});
		add(resetAquiferWaterPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Import Water Price:"), c);
		c.gridx++;
		importWaterPriceText = new JFormattedTextField(format); 
		importWaterPriceText.setColumns(10);
		importWaterPriceText.setHorizontalAlignment(JTextField.RIGHT);
		importWaterPriceText.setValue(country.getWaterSystem().getWaterImportPrice() 
				+ optimizationOptions.getDeltaImportWaterPrice());
		importWaterPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaImportWaterPrice(
							((Number) importWaterPriceText.getValue()).doubleValue() 
							- country.getWaterSystem().getWaterImportPrice());
					importWaterPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importWaterPriceText.setForeground(Color.red);
				}
			}
		});
		add(importWaterPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetImportWaterPriceButton = new JButton("Reset");
		resetImportWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportWaterPrice(0);
				importWaterPriceText.setValue(country.getWaterSystem().getWaterImportPrice());
			}
		});
		add(resetImportWaterPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Domestic Food Price:"), c);
		c.gridx++;
		domesticFoodPriceText = new JFormattedTextField(format);
		domesticFoodPriceText.setColumns(10);
		domesticFoodPriceText.setHorizontalAlignment(JTextField.RIGHT);
		domesticFoodPriceText.setValue(country.getAgricultureSystem().getFoodDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticFoodPrice());
		domesticFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticFoodPrice(
							((Number) domesticFoodPriceText.getValue()).doubleValue() 
							- country.getAgricultureSystem().getFoodDomesticPrice());
					domesticFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticFoodPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetDomesticFoodPriceButton = new JButton("Reset");
		resetDomesticFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				domesticFoodPriceText.setValue(country.getAgricultureSystem().getFoodDomesticPrice());
			}
		});
		add(resetDomesticFoodPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Import Food Price:"), c);
		c.gridx++;
		importFoodPriceText = new JFormattedTextField(format);
		importFoodPriceText.setColumns(10);
		importFoodPriceText.setHorizontalAlignment(JTextField.RIGHT);
		importFoodPriceText.setValue(country.getAgricultureSystem().getFoodImportPrice() 
				+ optimizationOptions.getDeltaImportFoodPrice());
		importFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticWaterPrice(
							((Number) importFoodPriceText.getValue()).doubleValue() 
							- country.getAgricultureSystem().getFoodImportPrice());
					importFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(importFoodPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetImportFoodPriceButton = new JButton("Reset");
		resetImportFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportFoodPrice(0);
				importFoodPriceText.setValue(country.getAgricultureSystem().getFoodImportPrice());
			}
		});
		add(resetImportFoodPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Export Food Price:"), c);
		c.gridx++;
		exportFoodPriceText = new JFormattedTextField(format);
		exportFoodPriceText.setColumns(10);
		exportFoodPriceText.setHorizontalAlignment(JTextField.RIGHT);
		exportFoodPriceText.setValue(country.getAgricultureSystem().getFoodExportPrice() 
				+ optimizationOptions.getDeltaExportFoodPrice());
		exportFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaExportFoodPrice(
							((Number) exportFoodPriceText.getValue()).doubleValue() 
							- country.getAgricultureSystem().getFoodExportPrice());
					exportFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					exportFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(exportFoodPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetExportFoodPriceButton = new JButton("Reset");
		resetExportFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaExportFoodPrice(0);
				exportFoodPriceText.setValue(country.getAgricultureSystem().getFoodExportPrice());
			}
		});
		add(resetExportFoodPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Domestic Oil Price:"), c);
		c.gridx++;
		domesticOilPriceText = new JFormattedTextField(format);
		domesticOilPriceText.setColumns(10);
		domesticOilPriceText.setHorizontalAlignment(JTextField.RIGHT);
		domesticOilPriceText.setValue(country.getPetroleumSystem().getPetroleumDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticOilPrice());
		domesticOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticOilPrice(
							((Number) domesticOilPriceText.getValue()).doubleValue() 
							- country.getPetroleumSystem().getPetroleumDomesticPrice());
					domesticOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticOilPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetDomesticOilPriceButton = new JButton("Reset");
		resetDomesticOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticOilPrice(0);
				domesticOilPriceText.setValue(country.getPetroleumSystem().getPetroleumDomesticPrice());
			}
		});
		add(resetDomesticOilPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Reservoir Oil Value:"), c);
		c.gridx++;
		reservoirOilPriceText = new JFormattedTextField(format); 
		reservoirOilPriceText.setColumns(10);
		reservoirOilPriceText.setHorizontalAlignment(JTextField.RIGHT);
		reservoirOilPriceText.setValue(optimizationOptions.getDeltaReservoirOilPrice());
		reservoirOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaReservoirOilPrice(
							((Number) reservoirOilPriceText.getValue()).doubleValue());
					reservoirOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					reservoirOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(reservoirOilPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetReservoirOilPriceButton = new JButton("Reset");
		resetReservoirOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaReservoirOilPrice(0);
				reservoirOilPriceText.setValue(0);
			}
		});
		add(resetReservoirOilPriceButton, c);
		c.gridx = 0;
		c.gridy++;

		add(new JLabel("Import Oil Price:"), c);
		c.gridx++;
		importOilPriceText = new JFormattedTextField(format);
		importOilPriceText.setColumns(10);
		importOilPriceText.setHorizontalAlignment(JTextField.RIGHT);
		importOilPriceText.setValue(country.getPetroleumSystem().getPetroleumImportPrice() 
				+ optimizationOptions.getDeltaImportOilPrice());
		importOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaImportOilPrice(
							((Number) importOilPriceText.getValue()).doubleValue() 
							- country.getPetroleumSystem().getPetroleumImportPrice());
					importOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(importOilPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetImportOilPriceButton = new JButton("Reset");
		resetImportOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportOilPrice(0);
				importOilPriceText.setValue(country.getPetroleumSystem().getPetroleumImportPrice());
			}
		});
		add(resetImportOilPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Export Oil Price:"), c);
		c.gridx++;
		exportOilPriceText = new JFormattedTextField(format);
		exportOilPriceText.setColumns(10);
		exportOilPriceText.setHorizontalAlignment(JTextField.RIGHT);
		exportOilPriceText.setValue(country.getPetroleumSystem().getPetroleumExportPrice() 
				+ optimizationOptions.getDeltaExportOilPrice());
		exportOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaExportOilPrice(
							((Number) exportOilPriceText.getValue()).doubleValue() 
							- country.getPetroleumSystem().getPetroleumExportPrice());
					exportOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					exportOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(exportOilPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetExportOilPriceButton = new JButton("Reset");
		resetExportOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaExportOilPrice(0);
				exportOilPriceText.setValue(country.getPetroleumSystem().getPetroleumExportPrice() 
						+ optimizationOptions.getDeltaExportOilPrice());
			}
		});
		add(resetExportOilPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		add(new JLabel("Domestic Electricity Price:"), c);
		c.gridx++;
		domesticElectricityPriceText = new JFormattedTextField(format);
		domesticElectricityPriceText.setColumns(10);
		domesticElectricityPriceText.setHorizontalAlignment(JTextField.RIGHT);
		domesticElectricityPriceText.setValue(country.getElectricitySystem().getElectricityDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticElectricityPrice());
		domesticElectricityPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticElectricityPrice(
							((Number) domesticElectricityPriceText.getValue()).doubleValue() 
							- country.getElectricitySystem().getElectricityDomesticPrice());
					domesticElectricityPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticElectricityPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticElectricityPriceText, c);
		c.gridx++;
		add(new JLabel("SAR"), c);
		c.gridx++;
		resetDomesticElectricityPriceButton = new JButton("Reset");
		resetDomesticElectricityPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticElectricityPrice(0);
				domesticElectricityPriceText.setValue(country.getElectricitySystem().getElectricityDomesticPrice());
			}
		});
		add(resetDomesticElectricityPriceButton, c);
		c.gridx = 0;
		c.gridy++;
		
		c.gridwidth = 4;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		JButton resetAllButton = new JButton("Reset All");
		resetAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
						getTopLevelAncestor(), "Reset all values to the default?", 
						"Reset Warning", JOptionPane.YES_NO_OPTION)) {
					optimizationOptions.setDeltaDomesticWaterPrice(0);
					domesticWaterPriceText.setValue(country.getWaterSystem().getWaterDomesticPrice());
					optimizationOptions.setDeltaDomesticWaterPrice(0);
					aquiferWaterPriceText.setValue(0);
					optimizationOptions.setDeltaImportWaterPrice(0);
					importWaterPriceText.setValue(country.getWaterSystem().getWaterImportPrice());
					optimizationOptions.setDeltaDomesticWaterPrice(0);
					domesticFoodPriceText.setValue(country.getAgricultureSystem().getFoodDomesticPrice());
					optimizationOptions.setDeltaImportFoodPrice(0);
					importFoodPriceText.setValue(country.getAgricultureSystem().getFoodImportPrice());
					optimizationOptions.setDeltaExportFoodPrice(0);
					exportFoodPriceText.setValue(country.getAgricultureSystem().getFoodExportPrice());
					optimizationOptions.setDeltaDomesticOilPrice(0);
					domesticOilPriceText.setValue(country.getPetroleumSystem().getPetroleumDomesticPrice());
					optimizationOptions.setDeltaReservoirOilPrice(0);
					reservoirOilPriceText.setValue(0);
					optimizationOptions.setDeltaImportOilPrice(0);
					importOilPriceText.setValue(country.getPetroleumSystem().getPetroleumImportPrice());
					optimizationOptions.setDeltaExportOilPrice(0);
					exportOilPriceText.setValue(country.getPetroleumSystem().getPetroleumExportPrice() 
							+ optimizationOptions.getDeltaExportOilPrice());
					optimizationOptions.setDeltaDomesticElectricityPrice(0);
					domesticElectricityPriceText.setValue(country.getElectricitySystem().getElectricityDomesticPrice());
				}
			}
		});
		add(resetAllButton, c);
	}
}
