package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import edu.mit.sips.core.Country;
import edu.mit.sips.io.Icons;

/**
 * The Class GlobalsPane.
 */
public class GlobalsPane extends JTabbedPane {
	private static final long serialVersionUID = 4456067057386611657L;

	private final Country country;

	private final JTextField initialFunds = new JTextField(15);

	private final JTextField foodDomesticPrice = new JTextField(15);
	private final JTextField foodImportPrice = new JTextField(15);
	private final JTextField foodExportPrice = new JTextField(15);
	private final JTextField privateConsumptionFromFoodProduction = new JTextField(15);
	private final JTextField privateConsumptionFromFoodConsumption = new JTextField(15);
	private final JTextField agricultureLaborParticipationRate = new JTextField(15);

	private final JTextField waterImportPrice = new JTextField(15);
	private final JTextField waterDomesticPrice = new JTextField(15);
	private final JTextField privateConsumptionFromWaterProduction = new JTextField(15);
	private final JTextField privateConsumptionFromWaterConsumption = new JTextField(15);
	private final JTextField minWaterDemandPerCapita = new JTextField(15);
	private final JTextField maxWaterDemandPerCapita = new JTextField(15);

	private final JTextField petroleumDomesticPrice = new JTextField(15);
	private final JTextField privateConsumptionFromPetroleumProduction = new JTextField(15);
	private final JTextField petroleumExportPrice = new JTextField(15);
	private final JTextField petroleumImportPrice = new JTextField(15);

	private final JTextField electricityDomesticPrice = new JTextField(15);
	private final JTextField privateConsumptionFromElectricityProduction = new JTextField(15);
	private final JTextField privateConsumptionFromElectricityConsumption = new JTextField(15);
	private final JTextField electricalIntensityOfBurningPetroleum = new JTextField(15);

	/**
	 * Instantiates a new globals pane.
	 */
	public GlobalsPane(final Country country) {
		this.country = country;
		initialFunds.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setInitialFunds(tryParse(
						initialFunds, 
						country.getGlobals().getInitialFunds()));
			}
		});
		foodDomesticPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setFoodDomesticPrice(tryParse(
						foodDomesticPrice, 
						country.getGlobals().getFoodDomesticPrice()));
			}
		});
		foodImportPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setFoodImportPrice(tryParse(
						foodImportPrice,
						country.getGlobals().getFoodImportPrice()));
			}
		});
		foodExportPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setFoodExportPrice(tryParse(
						foodExportPrice,
						country.getGlobals().getFoodExportPrice()));
			}
		});
		privateConsumptionFromFoodProduction.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfFoodProduction(tryParse(
						privateConsumptionFromFoodProduction,
						country.getGlobals().getPrivateConsumptionFromFoodProduction()));
			}
		});
		privateConsumptionFromFoodConsumption.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfFoodConsumption(tryParse(
						privateConsumptionFromFoodConsumption,
						country.getGlobals().getPrivateConsumptionFromFoodConsumption()));
			}
		});
		agricultureLaborParticipationRate.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setAgricultureLaborParticipationRate(tryParse(
						agricultureLaborParticipationRate,
						country.getGlobals().getAgricultureLaborParticipationRate()));
			}
		});
		waterImportPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setWaterImportPrice(tryParse(
						waterImportPrice, 
						country.getGlobals().getWaterImportPrice()));
			}
		});
		waterDomesticPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setWaterDomesticPrice(tryParse(
						waterDomesticPrice,
						country.getGlobals().getWaterDomesticPrice()));
			}
		});
		privateConsumptionFromWaterProduction.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfWaterProduction(tryParse(
						privateConsumptionFromWaterProduction,
						country.getGlobals().getPrivateConsumptionFromWaterProduction()));
			}
		});
		privateConsumptionFromWaterConsumption.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfWaterConsumption(tryParse(
						privateConsumptionFromWaterConsumption,
						country.getGlobals().getPrivateConsumptionFromWaterConsumption()));
			}
		});
		petroleumDomesticPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setPetroleumDomesticPrice(tryParse(
						petroleumDomesticPrice, 
						country.getGlobals().getPetroleumDomesticPrice()));
			}
		});
		privateConsumptionFromPetroleumProduction.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfPetroleumProduction(tryParse(
						privateConsumptionFromPetroleumProduction,
						country.getGlobals().getPrivateConsumptionFromPetroleumProduction()));
			}
		});
		petroleumExportPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setPetroleumExportPrice(tryParse(
						petroleumExportPrice,
						country.getGlobals().getPetroleumExportPrice()));
			}
		});
		petroleumImportPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setPetroleumImportPrice(tryParse(
						petroleumImportPrice,
						country.getGlobals().getPetroleumImportPrice()));
			}
		});
		electricityDomesticPrice.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setElectricityDomesticPrice(tryParse(
						electricityDomesticPrice,
						country.getGlobals().getElectricityDomesticPrice()));
			}
		});
		privateConsumptionFromElectricityProduction.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfElectricityProduction(tryParse(
						privateConsumptionFromElectricityProduction, 
						country.getGlobals().getPrivateConsumptionFromElectricityProduction()));
			}
		});
		privateConsumptionFromElectricityConsumption.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfElectricityConsumption(tryParse(
						privateConsumptionFromElectricityConsumption,
						country.getGlobals().getPrivateConsumptionFromElectricityConsumption()));
			}
		});
		electricalIntensityOfBurningPetroleum.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setElectricalIntensityOfBurningPetroleum(tryParse(
						electricalIntensityOfBurningPetroleum,
						country.getGlobals().getElectricalIntensityOfBurningPetroleum()));
			}
		});
		
		addTab("National", Icons.COUNTRY, createNationalPanel());
		addTab("Agriculture", Icons.AGRICULTURE, createAgriculturePanel());
		addTab("Water", Icons.WATER, createWaterPanel());
		addTab("Energy", Icons.ENERGY, createEnergyPanel());
		initialize();
	}
	
	private JPanel createNationalPanel() {
		JPanel nationalPanel = new JPanel();
		nationalPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		addField(nationalPanel, "Initial Funds (SAR)", 
				initialFunds, c);
		c.weighty = 1;
		nationalPanel.add(new JPanel(), c);
		return nationalPanel;
	}

	/**
	 * Creates the agriculture panel.
	 *
	 * @return the j panel
	 */
	private JPanel createAgriculturePanel() {
		JPanel foodPanel = new JPanel();
		foodPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		addField(foodPanel, "Food Domestic Price (SAR/GJ)", 
				foodDomesticPrice, c);
		addField(foodPanel, "Food Import Price (SAR/GJ)", 
				foodImportPrice, c);
		addField(foodPanel, "Food Export Price (SAR/GJ)",
				foodExportPrice, c);
		addField(foodPanel, "<html>Secondary Private Consumption from Food Production (SAR/GJ)</html>",
				privateConsumptionFromFoodProduction, c);
		addField(foodPanel, "<html>Secondary Private Consumption from Food Consumption (SAR/GJ)</html>",
				privateConsumptionFromFoodConsumption, c);
		addField(foodPanel, "Labor Participation Rate (-)",
				agricultureLaborParticipationRate, c);
		c.weighty = 1;
		foodPanel.add(new JPanel(), c);
		return foodPanel;
	}

	/**
	 * Creates the water panel.
	 *
	 * @return the j panel
	 */
	private JPanel createWaterPanel() {
		JPanel waterPanel = new JPanel();
		waterPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		addField(waterPanel, "<html>Water Import Price (SAR/m<sup>3</sup>)</html>", 
				waterImportPrice, c);
		addField(waterPanel, "<html>Water Domestic Price (SAR/m<sup>3</sup>)</html>", 
				waterDomesticPrice, c);
		addField(waterPanel, "<html>Secondary Private Consumption from Water Production (SAR/m<sup>3</sup>)</html>",
				privateConsumptionFromWaterProduction, c);
		addField(waterPanel, "<html>Secondary Private Consumption from Water Consumption (SAR/m<sup>3</sup>)</html>",
				privateConsumptionFromWaterConsumption, c);
		addField(waterPanel, "<html>Min Water Use per Capita (m<sup>3</sup>)</html>",
				minWaterDemandPerCapita, c);
		addField(waterPanel, "<html>Max Water Use per Capita (m<sup>3</sup>)</html>",
				maxWaterDemandPerCapita, c);
		c.weighty = 1;
		waterPanel.add(new JPanel(), c);
		return waterPanel;
	}

	/**
	 * Creates the energy panel.
	 *
	 * @return the j panel
	 */
	private JPanel createEnergyPanel() {
		JPanel energyPanel = new JPanel();
		energyPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		addField(energyPanel, "Petroleum Domestic Price (SAR/bbl)", 
				petroleumDomesticPrice, c);
		addField(energyPanel, "Secondary Private Consumption from Petroleum Production (SAR/bbl)", 
				privateConsumptionFromPetroleumProduction, c);
		addField(energyPanel, "Petroleum Export Price (SAR/bbl)",
				petroleumExportPrice, c);
		addField(energyPanel, "Petroleum Import Price (SAR/bbl)",
				petroleumImportPrice, c);
		addField(energyPanel, "Electricity Domestic Price (SAR/MWh)", 
				electricityDomesticPrice, c);
		addField(energyPanel, "Secondary Private Consumption from Electricity Production (SAR/MWh)", 
				privateConsumptionFromElectricityProduction, c);
		addField(energyPanel, "Secondary Private Consumption from Electricity Consumption (SAR/MWh)", 
				privateConsumptionFromElectricityConsumption, c);
		addField(energyPanel, "Electrical Intensity of Burning Petroleum (MWh/bbl)", 
				electricalIntensityOfBurningPetroleum, c);
		c.weighty = 1;
		energyPanel.add(new JPanel(), c);
		return energyPanel;
	}

	/**
	 * Adds the field.
	 *
	 * @param panel the panel
	 * @param labelText the label text
	 * @param textField the text field
	 * @param c the c
	 */
	private void addField(JPanel panel, String labelText, 
			JTextField textField, GridBagConstraints c) {
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		panel.add(new JLabel(labelText), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(textField, c);
		c.gridy++;
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		initialFunds.setText(new Double(
				country.getGlobals().getInitialFunds()).toString());
		
		foodDomesticPrice.setText(new Double(
				country.getGlobals().getFoodDomesticPrice()).toString());
		foodImportPrice.setText(new Double(
				country.getGlobals().getFoodImportPrice()).toString());
		foodExportPrice.setText(new Double(
				country.getGlobals().getFoodExportPrice()).toString());
		privateConsumptionFromFoodProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromFoodProduction()).toString());
		privateConsumptionFromFoodConsumption.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromFoodConsumption()).toString());
		agricultureLaborParticipationRate.setText(new Double(
				country.getGlobals().getAgricultureLaborParticipationRate()).toString());
		
		waterImportPrice.setText(new Double(
				country.getGlobals().getWaterImportPrice()).toString());
		waterDomesticPrice.setText(new Double(
				country.getGlobals().getWaterDomesticPrice()).toString());
		privateConsumptionFromWaterProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromWaterProduction()).toString());
		privateConsumptionFromWaterConsumption.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromWaterConsumption()).toString());

		petroleumDomesticPrice.setText(new Double(
				country.getGlobals().getPetroleumDomesticPrice()).toString());
		privateConsumptionFromPetroleumProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromPetroleumProduction()).toString());
		petroleumExportPrice.setText(new Double(
				country.getGlobals().getPetroleumExportPrice()).toString());
		petroleumImportPrice.setText(new Double(
				country.getGlobals().getPetroleumImportPrice()).toString());
		electricityDomesticPrice.setText(new Double(
				country.getGlobals().getElectricityDomesticPrice()).toString());
		privateConsumptionFromElectricityProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromElectricityProduction()).toString());
		privateConsumptionFromElectricityConsumption.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromElectricityConsumption()).toString());
		electricalIntensityOfBurningPetroleum.setText(new Double(
				country.getGlobals().getElectricalIntensityOfBurningPetroleum()).toString());
	}

	/**
	 * Try parse.
	 *
	 * @param textField the text field
	 * @param defaultValue the default value
	 * @return the double
	 */
	private double tryParse(JTextField textField, double defaultValue) {
		double returnValue = defaultValue;
		try {
			returnValue = Double.parseDouble(textField.getText());
			textField.setForeground(Color.black);
		} catch(NumberFormatException ex) {
			textField.setForeground(Color.red);
		}
		return returnValue;
	}
}
