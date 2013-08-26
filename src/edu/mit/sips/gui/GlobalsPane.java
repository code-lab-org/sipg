package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.Country;
import edu.mit.sips.io.Icons;

/**
 * The Class GlobalsPane.
 */
public class GlobalsPane extends JPanel {
	private static final long serialVersionUID = 4456067057386611657L;

	private final Country country;

	private final JTextField initialFunds = new JTextField(15);

	private final JTextField privateConsumptionFromFoodProduction = new JTextField(15);
	private final JTextField privateConsumptionFromFoodConsumption = new JTextField(15);
	private final JTextField agricultureLaborParticipationRate = new JTextField(15);

	private final JTextField privateConsumptionFromWaterProduction = new JTextField(15);
	private final JTextField privateConsumptionFromWaterConsumption = new JTextField(15);

	private final JTextField privateConsumptionFromPetroleumProduction = new JTextField(15);

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
		privateConsumptionFromPetroleumProduction.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				country.getGlobals().setEconomicIntensityOfPetroleumProduction(tryParse(
						privateConsumptionFromPetroleumProduction,
						country.getGlobals().getPrivateConsumptionFromPetroleumProduction()));
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
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		JLabel nationalLabel = new JLabel("National");
		nationalLabel.setIcon(Icons.COUNTRY);
		add(nationalLabel, c);
		c.gridwidth = 1;
		c.gridy++;
		addField(this, "Initial Funds (SAR)", 
				initialFunds, c);
		c.gridwidth = 2;
		c.gridx = 0;
		JLabel foodLabel = new JLabel("Agriculture");
		foodLabel.setIcon(Icons.AGRICULTURE);
		add(foodLabel, c);
		c.gridwidth = 1;
		c.gridy++;
		addField(this, "<html>Secondary Private Consumption from Food Production (SAR/GJ)</html>",
				privateConsumptionFromFoodProduction, c);
		addField(this, "<html>Secondary Private Consumption from Food Consumption (SAR/GJ)</html>",
				privateConsumptionFromFoodConsumption, c);
		addField(this, "Labor Participation Rate (-)",
				agricultureLaborParticipationRate, c);
		c.gridwidth = 2;
		c.gridx = 0;
		JLabel waterLabel = new JLabel("Water");
		waterLabel.setIcon(Icons.WATER);
		add(waterLabel, c);
		c.gridwidth = 1;
		c.gridy++;
		addField(this, "<html>Secondary Private Consumption from Water Production (SAR/m<sup>3</sup>)</html>",
				privateConsumptionFromWaterProduction, c);
		addField(this, "<html>Secondary Private Consumption from Water Consumption (SAR/m<sup>3</sup>)</html>",
				privateConsumptionFromWaterConsumption, c);
		c.gridwidth = 2;
		c.gridx = 0;
		JLabel electricityLabel = new JLabel("Electricity");
		electricityLabel.setIcon(Icons.ELECTRICITY);
		add(electricityLabel, c);
		c.gridwidth = 1;
		c.gridy++;
		addField(this, "Secondary Private Consumption from Electricity Production (SAR/MWh)", 
				privateConsumptionFromElectricityProduction, c);
		addField(this, "Secondary Private Consumption from Electricity Consumption (SAR/MWh)", 
				privateConsumptionFromElectricityConsumption, c);
		addField(this, "Electrical Intensity of Burning Petroleum (MWh/bbl)", 
				electricalIntensityOfBurningPetroleum, c);
		c.gridwidth = 2;
		c.gridx = 0;
		JLabel petroleumLabel = new JLabel("Petroleum");
		petroleumLabel.setIcon(Icons.PETROLEUM);
		add(petroleumLabel, c);
		c.gridwidth = 1;
		c.gridy++;
		addField(this, "Secondary Private Consumption from Petroleum Production (SAR/bbl)", 
				privateConsumptionFromPetroleumProduction, c);
		
		initialize();
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
		
		privateConsumptionFromFoodProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromFoodProduction()).toString());
		privateConsumptionFromFoodConsumption.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromFoodConsumption()).toString());
		agricultureLaborParticipationRate.setText(new Double(
				country.getGlobals().getAgricultureLaborParticipationRate()).toString());
		
		privateConsumptionFromWaterProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromWaterProduction()).toString());
		privateConsumptionFromWaterConsumption.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromWaterConsumption()).toString());

		privateConsumptionFromPetroleumProduction.setText(new Double(
				country.getGlobals().getPrivateConsumptionFromPetroleumProduction()).toString());
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
