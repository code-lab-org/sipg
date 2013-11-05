package edu.mit.sips.gui.water;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class WaterElementPanel.
 */
public class WaterElementPanel extends ElementPanel 
		implements WaterUnitsOutput, CurrencyUnitsOutput, ElectricityUnitsOutput {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxWaterProductionText;
	private final JFormattedTextField initialWaterProductionText;
	private final JFormattedTextField reservoirIntensityOfWaterProductionText;
	private final JFormattedTextField electricalIntensityOfWaterProductionText;
	private final JFormattedTextField variableOperationsCostOfWaterProductionText;
	private final JCheckBox coastalAccessRequiredCheck;

	private final JFormattedTextField maxWaterInputText;
	private final JFormattedTextField initialWaterInputText;
	private final JFormattedTextField distributionEfficiencyText;
	private final JFormattedTextField electricalIntensityOfWaterDistributionText;
	private final JFormattedTextField variableOperationsCostOfWaterDistributionText;

	private final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.kWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.m3;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * Instantiates a new water element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 */
	public WaterElementPanel(Scenario scenario, 
			final MutableWaterElement element) {
		super(scenario, element);
		
		final WaterElementPanel thisPanel = this;
		
		maxWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxWaterProductionText.setColumns(10);
		maxWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		maxWaterProductionText.setValue(WaterUnits.convertFlow(
				element.getMaxWaterProduction(), element, this));
		maxWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterProduction(WaterUnits.convertFlow(
									((Number) maxWaterProductionText.getValue()).doubleValue(),
									thisPanel, element));
							maxWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterProductionText.setForeground(Color.red);
						}
					}
				});
		initialWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialWaterProductionText.setColumns(10);
		initialWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		initialWaterProductionText.setValue(WaterUnits.convertFlow(
				element.getInitialWaterProduction(), element, this));
		initialWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterProduction(WaterUnits.convertFlow(
									((Number) initialWaterProductionText.getValue()).doubleValue(),
									thisPanel, element));
							initialWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterProductionText.setForeground(Color.red);
						}
					}
				});
		reservoirIntensityOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		reservoirIntensityOfWaterProductionText.setColumns(10);
		reservoirIntensityOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		reservoirIntensityOfWaterProductionText.setValue(
				element.getReservoirIntensityOfWaterProduction());
		reservoirIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setReservoirIntensityOfWaterProduction(
									((Number) reservoirIntensityOfWaterProductionText.getValue()).doubleValue());
							reservoirIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							reservoirIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		electricalIntensityOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		electricalIntensityOfWaterProductionText.setColumns(10);
		electricalIntensityOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfWaterProductionText.setValue(DefaultUnits.convert(
				element.getElectricalIntensityOfWaterProduction(), 
				element.getElectricityUnits(), element.getWaterUnits(), 
				getElectricityUnits(), getWaterUnits()));
		electricalIntensityOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterProduction(DefaultUnits.convert(
									((Number) electricalIntensityOfWaterProductionText.getValue()).doubleValue(),
									getElectricityUnits(), getWaterUnits(), 
									element.getElectricityUnits(), element.getWaterUnits()));
							electricalIntensityOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfWaterProductionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		variableOperationsCostOfWaterProductionText.setColumns(10);
		variableOperationsCostOfWaterProductionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfWaterProductionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfWaterProduction(), 
				element.getCurrencyUnits(), element.getWaterUnits(), 
				getCurrencyUnits(), getWaterUnits()));
		variableOperationsCostOfWaterProductionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterProduction(DefaultUnits.convert(
									((Number) variableOperationsCostOfWaterProductionText.getValue()).doubleValue(),
									getCurrencyUnits(), getWaterUnits(), 
									element.getCurrencyUnits(), element.getWaterUnits()));
							variableOperationsCostOfWaterProductionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterProductionText.setForeground(Color.red);
						}
					}
				});
		coastalAccessRequiredCheck = new JCheckBox();
		coastalAccessRequiredCheck.setText("Coastal Access Required");
		coastalAccessRequiredCheck.setSelected(element.isCoastalAccessRequired());
		coastalAccessRequiredCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				element.setCoastalAccessRequired(coastalAccessRequiredCheck.isSelected());
			}
		});
		maxWaterInputText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxWaterInputText.setColumns(10);
		maxWaterInputText.setHorizontalAlignment(JTextField.RIGHT);
		maxWaterInputText.setValue(WaterUnits.convertFlow(
				element.getMaxWaterInput(), element, this));
		maxWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxWaterInput(WaterUnits.convertFlow(
									((Number) maxWaterInputText.getValue()).doubleValue(),
									thisPanel, element));
							maxWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxWaterInputText.setForeground(Color.red);
						}
					}
				});
		initialWaterInputText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialWaterInputText.setColumns(10);
		initialWaterInputText.setHorizontalAlignment(JTextField.RIGHT);
		initialWaterInputText.setValue(WaterUnits.convertFlow(
				element.getInitialWaterInput(), element, this));
		initialWaterInputText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialWaterInput(WaterUnits.convertFlow(
									((Number) initialWaterInputText.getValue()).doubleValue(),
									thisPanel, element));
							initialWaterInputText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialWaterInputText.setForeground(Color.red);
						}
					}
				});
		distributionEfficiencyText = new JFormattedTextField(NumberFormat.getNumberInstance());
		distributionEfficiencyText.setColumns(10);
		distributionEfficiencyText.setHorizontalAlignment(JTextField.RIGHT);
		distributionEfficiencyText.setValue(element.getDistributionEfficiency());
		distributionEfficiencyText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(
									((Number) distributionEfficiencyText.getValue()).doubleValue());
							distributionEfficiencyText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiencyText.setForeground(Color.red);
						}
					}
				});
		electricalIntensityOfWaterDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		electricalIntensityOfWaterDistributionText.setColumns(10);
		electricalIntensityOfWaterDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		electricalIntensityOfWaterDistributionText.setValue(DefaultUnits.convert(
				element.getElectricalIntensityOfWaterDistribution(), 
				element.getElectricityUnits(), element.getWaterUnits(), 
				getElectricityUnits(), getWaterUnits()));
		electricalIntensityOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setElectricalIntensityOfWaterDistribution(DefaultUnits.convert(
									((Number) electricalIntensityOfWaterDistributionText.getValue()).doubleValue(),
									getElectricityUnits(), getWaterUnits(), 
									element.getElectricityUnits(), element.getWaterUnits()));
							electricalIntensityOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							electricalIntensityOfWaterDistributionText.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfWaterDistributionText = new JFormattedTextField(NumberFormat.getNumberInstance());
		variableOperationsCostOfWaterDistributionText.setColumns(10);
		variableOperationsCostOfWaterDistributionText.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfWaterDistributionText.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfWaterDistribution(), 
				element.getCurrencyUnits(), element.getWaterUnits(), 
				getCurrencyUnits(), getWaterUnits()));
		variableOperationsCostOfWaterDistributionText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfWaterDistribution(DefaultUnits.convert(
									((Number) variableOperationsCostOfWaterDistributionText.getValue()).doubleValue(),
									getCurrencyUnits(), getWaterUnits(), 
									element.getCurrencyUnits(), element.getWaterUnits()));
							variableOperationsCostOfWaterDistributionText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfWaterDistributionText.setForeground(Color.red);
						}
					}
				});
		
		JPanel elementPanel = new JPanel();
		elementPanel.setLayout(new GridBagLayout());
		add(elementPanel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| !scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 0;
			addInput(elementPanel, c, "Max Water Production", 
					maxWaterProductionText, waterUnits + "/" + waterTimeUnits);
			addInput(elementPanel, c, "Initial Water Production",
					initialWaterProductionText, waterUnits + "/" + waterTimeUnits);
			addInput(elementPanel, c, "Reservoir Intensity of Production",
					reservoirIntensityOfWaterProductionText, waterUnits + "/" + waterUnits);
			addInput(elementPanel, c, "Electrical Intensity of Production",
					electricalIntensityOfWaterProductionText, electricityUnits + "/" + waterUnits);
			addInput(elementPanel, c, "Variable Cost of Production",
					variableOperationsCostOfWaterProductionText, currencyUnits + "/" + waterUnits);
			
			c.gridwidth = 3;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			elementPanel.add(coastalAccessRequiredCheck, c);
			c.gridwidth = 1;
			c.gridy++;
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Water Input", 
					maxWaterInputText, waterUnits + "/" + waterTimeUnits);
			addInput(elementPanel, c, "Initial Water Input",
					initialWaterInputText, waterUnits + "/" + waterTimeUnits);
			addInput(elementPanel, c, "Distribution Efficiency",
					distributionEfficiencyText, waterUnits + "/" + waterUnits);
			addInput(elementPanel, c, "Electrical Intensity of Distribution",
					electricalIntensityOfWaterDistributionText, electricityUnits + "/" + waterUnits);
			addInput(elementPanel, c, "Variable Cost of Distribution",
					variableOperationsCostOfWaterDistributionText, currencyUnits + "/" + waterUnits);
		}
		
		// set input enabled state
		maxWaterProductionText.setEnabled(element.getTemplateName() == null);
		reservoirIntensityOfWaterProductionText.setEnabled(element.getTemplateName() == null);
		electricalIntensityOfWaterProductionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfWaterProductionText.setEnabled(element.getTemplateName() == null);
		maxWaterInputText.setEnabled(element.getTemplateName() == null);
		distributionEfficiencyText.setEnabled(element.getTemplateName() == null);
		electricalIntensityOfWaterDistributionText.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfWaterDistributionText.setEnabled(element.getTemplateName() == null);
		coastalAccessRequiredCheck.setEnabled(element.getTemplateName() == null);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
	
}
