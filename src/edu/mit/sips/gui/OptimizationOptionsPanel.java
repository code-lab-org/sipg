package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		setLayout(new GridLayout(6,3));
		add(new JLabel("Domestic Water Price:"));
		domesticWaterPriceText = new JFormattedTextField(format); 
		domesticWaterPriceText.setValue(country.getGlobals().getWaterDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticWaterPrice());
		domesticWaterPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticWaterPrice(
							((Number) domesticWaterPriceText.getValue()).doubleValue() 
							- country.getGlobals().getWaterDomesticPrice());
					domesticWaterPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticWaterPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticWaterPriceText);
		resetDomesticWaterPriceButton = new JButton("Reset");
		resetDomesticWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				domesticWaterPriceText.setValue(country.getGlobals().getWaterDomesticPrice());
			}
		});
		add(resetDomesticWaterPriceButton);

		add(new JLabel("Aquifer Water Value:"));
		aquiferWaterPriceText = new JFormattedTextField(format); 
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
		add(aquiferWaterPriceText);
		resetAquiferWaterPriceButton = new JButton("Reset");
		resetAquiferWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				aquiferWaterPriceText.setValue(0);
			}
		});
		add(resetAquiferWaterPriceButton);
		
		add(new JLabel("Import Water Price:"));
		importWaterPriceText = new JFormattedTextField(format); 
		importWaterPriceText.setValue(country.getGlobals().getWaterImportPrice() 
				+ optimizationOptions.getDeltaImportWaterPrice());
		importWaterPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaImportWaterPrice(
							((Number) importWaterPriceText.getValue()).doubleValue() 
							- country.getGlobals().getWaterImportPrice());
					importWaterPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importWaterPriceText.setForeground(Color.red);
				}
			}
		});
		add(importWaterPriceText);
		resetImportWaterPriceButton = new JButton("Reset");
		resetImportWaterPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportWaterPrice(0);
				importWaterPriceText.setValue(country.getGlobals().getWaterImportPrice());
			}
		});
		add(resetImportWaterPriceButton);
		
		add(new JLabel("Domestic Food Price:"));
		domesticFoodPriceText = new JFormattedTextField(format);
		domesticFoodPriceText.setValue(country.getGlobals().getFoodDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticFoodPrice());
		domesticFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticFoodPrice(
							((Number) domesticFoodPriceText.getValue()).doubleValue() 
							- country.getGlobals().getFoodDomesticPrice());
					domesticFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticFoodPriceText);
		resetDomesticFoodPriceButton = new JButton("Reset");
		resetDomesticFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticWaterPrice(0);
				domesticFoodPriceText.setValue(country.getGlobals().getFoodDomesticPrice());
			}
		});
		add(resetDomesticFoodPriceButton);
		
		add(new JLabel("Import Food Price:"));
		importFoodPriceText = new JFormattedTextField(format);
		importFoodPriceText.setValue(country.getGlobals().getFoodImportPrice() 
				+ optimizationOptions.getDeltaImportFoodPrice());
		importFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticWaterPrice(
							((Number) importFoodPriceText.getValue()).doubleValue() 
							- country.getGlobals().getFoodImportPrice());
					importFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(importFoodPriceText);
		resetImportFoodPriceButton = new JButton("Reset");
		resetImportFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportFoodPrice(0);
				importFoodPriceText.setValue(country.getGlobals().getFoodImportPrice());
			}
		});
		add(resetImportFoodPriceButton);
		
		add(new JLabel("Export Food Price:"));
		exportFoodPriceText = new JFormattedTextField(format);
		exportFoodPriceText.setValue(country.getGlobals().getFoodExportPrice() 
				+ optimizationOptions.getDeltaExportFoodPrice());
		exportFoodPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaExportFoodPrice(
							((Number) exportFoodPriceText.getValue()).doubleValue() 
							- country.getGlobals().getFoodExportPrice());
					exportFoodPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					exportFoodPriceText.setForeground(Color.red);
				}
			}
		});
		add(exportFoodPriceText);
		resetExportFoodPriceButton = new JButton("Reset");
		resetExportFoodPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaExportFoodPrice(0);
				exportFoodPriceText.setValue(country.getGlobals().getFoodExportPrice());
			}
		});
		add(resetExportFoodPriceButton);
		
		add(new JLabel("Domestic Oil Price:"));
		domesticOilPriceText = new JFormattedTextField(format);
		domesticOilPriceText.setValue(country.getGlobals().getPetroleumDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticOilPrice());
		domesticOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticOilPrice(
							((Number) domesticOilPriceText.getValue()).doubleValue() 
							- country.getGlobals().getPetroleumDomesticPrice());
					domesticOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticOilPriceText);
		resetDomesticOilPriceButton = new JButton("Reset");
		resetDomesticOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticOilPrice(0);
				domesticOilPriceText.setValue(country.getGlobals().getPetroleumDomesticPrice());
			}
		});
		add(resetDomesticOilPriceButton);
		
		add(new JLabel("Reservoir Oil Value:"));
		reservoirOilPriceText = new JFormattedTextField(format); 
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
		add(reservoirOilPriceText);
		resetReservoirOilPriceButton = new JButton("Reset");
		resetReservoirOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaReservoirOilPrice(0);
				reservoirOilPriceText.setValue(0);
			}
		});
		add(resetReservoirOilPriceButton);

		add(new JLabel("Import Oil Price:"));
		importOilPriceText = new JFormattedTextField(format);
		importOilPriceText.setValue(country.getGlobals().getPetroleumImportPrice() 
				+ optimizationOptions.getDeltaImportOilPrice());
		importOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaImportOilPrice(
							((Number) importOilPriceText.getValue()).doubleValue() 
							- country.getGlobals().getPetroleumImportPrice());
					importOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					importOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(importOilPriceText);
		resetImportOilPriceButton = new JButton("Reset");
		resetImportOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaImportOilPrice(0);
				importOilPriceText.setValue(country.getGlobals().getPetroleumImportPrice());
			}
		});
		add(resetImportOilPriceButton);
		
		add(new JLabel("Export Oil Price:"));
		exportOilPriceText = new JFormattedTextField(format);
		exportOilPriceText.setValue(country.getGlobals().getPetroleumExportPrice() 
				+ optimizationOptions.getDeltaExportOilPrice());
		exportOilPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaExportOilPrice(
							((Number) exportOilPriceText.getValue()).doubleValue() 
							- country.getGlobals().getPetroleumExportPrice());
					exportOilPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					exportOilPriceText.setForeground(Color.red);
				}
			}
		});
		add(exportOilPriceText);
		resetExportOilPriceButton = new JButton("Reset");
		resetExportOilPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaExportOilPrice(0);
				exportOilPriceText.setValue(country.getGlobals().getPetroleumExportPrice() 
						+ optimizationOptions.getDeltaExportOilPrice());
			}
		});
		add(resetExportOilPriceButton);
		
		add(new JLabel("Domestic Electricity Price:"));
		domesticElectricityPriceText = new JFormattedTextField(format);
		domesticElectricityPriceText.setValue(country.getGlobals().getElectricityDomesticPrice() 
				+ optimizationOptions.getDeltaDomesticElectricityPrice());
		domesticElectricityPriceText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					optimizationOptions.setDeltaDomesticElectricityPrice(
							((Number) domesticElectricityPriceText.getValue()).doubleValue() 
							- country.getGlobals().getElectricityDomesticPrice());
					domesticElectricityPriceText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					domesticElectricityPriceText.setForeground(Color.red);
				}
			}
		});
		add(domesticElectricityPriceText);
		resetDomesticElectricityPriceButton = new JButton("Reset");
		resetDomesticElectricityPriceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optimizationOptions.setDeltaDomesticElectricityPrice(0);
				domesticElectricityPriceText.setValue(country.getGlobals().getElectricityDomesticPrice());
			}
		});
		add(resetDomesticElectricityPriceButton);
	}
}
