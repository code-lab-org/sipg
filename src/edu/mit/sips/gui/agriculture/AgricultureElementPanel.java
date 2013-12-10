package edu.mit.sips.gui.agriculture;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.gui.DocumentChangeListener;
import edu.mit.sips.gui.ElementPanel;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class AgricultureElementPanel.
 */
public class AgricultureElementPanel extends ElementPanel 
		implements WaterUnitsOutput, CurrencyUnitsOutput, FoodUnitsOutput {
	private static final long serialVersionUID = -9048149807650177253L;
	
	private final JFormattedTextField maxLandAreaText;
	private final JFormattedTextField initialLandAreaText;
	private final JFormattedTextField costIntensityText;
	private final JFormattedTextField laborIntensityText;
	private final JFormattedTextField foodIntensityText;
	private final JFormattedTextField waterIntensityText;
	private final JLabel maxYieldLabel, maxVariableCostLabel, 
			maxWaterUseLabel, maxLaborUseLabel;

	private final JFormattedTextField maxFoodInput;
	private final JFormattedTextField initialFoodInput;
	private final JFormattedTextField distributionEfficiency;
	private final JFormattedTextField variableOperationsCostOfFoodDistribution;
	private final JLabel maxOutputLabel, maxVariableCostDistLabel;

	private final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final FoodUnits foodUnits = FoodUnits.TJ;
	private final TimeUnits foodTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.MCM;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * Instantiates a new agriculture element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 */
	public AgricultureElementPanel(Scenario scenario, 
			final MutableAgricultureElement element) {
		super(scenario, element);
		
		final AgricultureElementPanel thisPanel = this;
		
		maxLandAreaText = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxLandAreaText.setColumns(10);
		maxLandAreaText.setHorizontalAlignment(JTextField.RIGHT);
		maxLandAreaText.setValue(element.getMaxLandArea());
		maxLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxLandArea(
									((Number) maxLandAreaText.getValue()).doubleValue());
							maxYieldLabel.setText(NumberFormat.getNumberInstance().format(
									FoodUnits.convertFlow(element.getMaxLandArea() 
											* element.getFoodIntensityOfLandUsed(), 
											element, thisPanel)));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxLandArea() 
											* element.getCostIntensityOfLandUsed(), 
											element, thisPanel)));
							maxWaterUseLabel.setText(NumberFormat.getNumberInstance().format(
									WaterUnits.convertFlow(element.getMaxLandArea() 
											* element.getWaterIntensityOfLandUsed(), 
											element, thisPanel)));
							maxLaborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getMaxLandArea() 
									* element.getLaborIntensityOfLandUsed()));
							maxLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxLandAreaText.setForeground(Color.red);
						}
					}
				});
		initialLandAreaText = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialLandAreaText.setColumns(10);
		initialLandAreaText.setHorizontalAlignment(JTextField.RIGHT);
		initialLandAreaText.setValue(element.getInitialLandArea());
		initialLandAreaText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialLandArea(
									((Number) initialLandAreaText.getValue()).doubleValue());
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		costIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		costIntensityText.setColumns(10);
		costIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		costIntensityText.setValue(CurrencyUnits.convertFlow(
				element.getCostIntensityOfLandUsed(), element, this));
		costIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setCostIntensityOfLandUsed(CurrencyUnits.convertFlow(
									((Number) costIntensityText.getValue()).doubleValue(), 
									thisPanel, element));
							maxVariableCostLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxLandArea() 
											* element.getCostIntensityOfLandUsed(), 
											element, thisPanel)));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		foodIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		foodIntensityText.setColumns(10);
		foodIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		foodIntensityText.setValue(FoodUnits.convertFlow(
				element.getFoodIntensityOfLandUsed(), element, this));
		foodIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setFoodIntensityOfLandUsed(FoodUnits.convertFlow(
									((Number) foodIntensityText.getValue()).doubleValue(),
									thisPanel, element));
							maxYieldLabel.setText(NumberFormat.getNumberInstance().format(
									FoodUnits.convertFlow(element.getMaxLandArea() 
											* element.getFoodIntensityOfLandUsed(), 
											element, thisPanel)));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		laborIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		laborIntensityText.setColumns(10);
		laborIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		laborIntensityText.setValue(element.getLaborIntensityOfLandUsed());
		laborIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setLaborIntensityOfLandUsed(
									((Number) laborIntensityText.getValue()).doubleValue());
							maxLaborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getMaxLandArea() 
									* element.getLaborIntensityOfLandUsed()));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		waterIntensityText = new JFormattedTextField(NumberFormat.getNumberInstance());
		waterIntensityText.setColumns(10);
		waterIntensityText.setHorizontalAlignment(JTextField.RIGHT);
		waterIntensityText.setValue(WaterUnits.convertFlow(
				element.getWaterIntensityOfLandUsed(), element, this));
		waterIntensityText.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setWaterIntensityOfLandUsed(WaterUnits.convertFlow(
									((Number) waterIntensityText.getValue()).doubleValue(),
									thisPanel, element));
							maxWaterUseLabel.setText(NumberFormat.getNumberInstance().format(
									WaterUnits.convertFlow(element.getMaxLandArea() 
											* element.getWaterIntensityOfLandUsed(), 
											element, thisPanel)));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		maxYieldLabel = new JLabel(NumberFormat.getNumberInstance().format(
				FoodUnits.convertFlow(element.getMaxLandArea() 
						* element.getFoodIntensityOfLandUsed(), element, this)), 
						JLabel.RIGHT);
		maxVariableCostLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxLandArea() 
						* element.getCostIntensityOfLandUsed(), element, this)),
						JLabel.RIGHT);
		maxWaterUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				WaterUnits.convertFlow(element.getMaxLandArea() 
						* element.getWaterIntensityOfLandUsed(), element, this)), 
						JLabel.RIGHT);
		maxLaborUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getMaxLandArea() * element.getLaborIntensityOfLandUsed()), 
				JLabel.RIGHT);

		maxFoodInput = new JFormattedTextField(NumberFormat.getNumberInstance());
		maxFoodInput.setColumns(10);
		maxFoodInput.setHorizontalAlignment(JTextField.RIGHT);
		maxFoodInput.setValue(FoodUnits.convertFlow(
				element.getMaxFoodInput(), element, this));
		maxFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setMaxFoodInput(FoodUnits.convertFlow(
									((Number) maxFoodInput.getValue()).doubleValue(),
									thisPanel, element));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxFoodInput()
											* element.getVariableOperationsCostOfFoodDistribution(), 
											element, thisPanel)));
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									FoodUnits.convertFlow(element.getMaxFoodInput(), 
											element, thisPanel)
									* element.getDistributionEfficiency()));
							maxFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							maxFoodInput.setForeground(Color.red);
						}
					}
				});
		initialFoodInput = new JFormattedTextField(NumberFormat.getNumberInstance());
		initialFoodInput.setColumns(10);
		initialFoodInput.setHorizontalAlignment(JTextField.RIGHT);
		initialFoodInput.setValue(FoodUnits.convertFlow(
				element.getInitialFoodInput(), element, this));
		initialFoodInput.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setInitialFoodInput(FoodUnits.convertFlow(
									((Number) initialFoodInput.getValue()).doubleValue(),
									thisPanel, element));
							initialFoodInput.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialFoodInput.setForeground(Color.red);
						}
					}
				});
		distributionEfficiency = new JFormattedTextField(NumberFormat.getNumberInstance());
		distributionEfficiency.setColumns(10);
		distributionEfficiency.setHorizontalAlignment(JTextField.RIGHT);
		distributionEfficiency.setValue(element.getDistributionEfficiency());
		distributionEfficiency.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setDistributionEfficiency(
									((Number) distributionEfficiency.getValue()).doubleValue());
							maxOutputLabel.setText(NumberFormat.getNumberInstance().format(
									FoodUnits.convertFlow(element.getMaxFoodInput(), 
											element, thisPanel)
									* element.getDistributionEfficiency()));
							distributionEfficiency.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							distributionEfficiency.setForeground(Color.red);
						}
					}
				});
		variableOperationsCostOfFoodDistribution = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		variableOperationsCostOfFoodDistribution.setColumns(10);
		variableOperationsCostOfFoodDistribution.setHorizontalAlignment(JTextField.RIGHT);
		variableOperationsCostOfFoodDistribution.setValue(DefaultUnits.convert(
				element.getVariableOperationsCostOfFoodDistribution(),
				element.getCurrencyUnits(), element.getFoodUnits(),
				getCurrencyUnits(), getFoodUnits()));
		variableOperationsCostOfFoodDistribution.getDocument().addDocumentListener(
				new DocumentChangeListener() {
					public void documentChanged() {
						try {
							element.setVariableOperationsCostOfFoodDistribution(DefaultUnits.convert(
									((Number) variableOperationsCostOfFoodDistribution.getValue()).doubleValue(),
									getCurrencyUnits(), getFoodUnits(),
									element.getCurrencyUnits(), element.getFoodUnits()));
							maxVariableCostDistLabel.setText(NumberFormat.getNumberInstance().format(
									CurrencyUnits.convertFlow(element.getMaxFoodInput()
											* element.getVariableOperationsCostOfFoodDistribution(), 
											element, thisPanel)));
							variableOperationsCostOfFoodDistribution.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfFoodDistribution.setForeground(Color.red);
						}
					}
				});
		maxOutputLabel = new JLabel(NumberFormat.getNumberInstance().format(
				FoodUnits.convertFlow(element.getMaxFoodInput(), element, this)
				* element.getDistributionEfficiency()), JLabel.RIGHT);
		maxVariableCostDistLabel = new JLabel(NumberFormat.getNumberInstance().format(
				CurrencyUnits.convertFlow(element.getMaxFoodInput()
						* element.getVariableOperationsCostOfFoodDistribution(), 
						element, this)), JLabel.RIGHT);

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
			addInput(elementPanel, c, "Maximum Land Area", 
					maxLandAreaText, "km^2");
			// TODO addInput(elementPanel, c, "Initial Land Area", initialLandAreaText, "km^2");
			addInput(elementPanel, c, "Specific Food Yield", 
					foodIntensityText, 	
					foodUnits + "/" + foodTimeUnits + "/km^2",
					"Maximum Food Yield", 
					maxYieldLabel, foodUnits + "/" + foodTimeUnits);
			addInput(elementPanel, c, "Variable Operations Cost", 
					costIntensityText, currencyUnits + "/km^2",
					"Maximum Variable Cost", 
					maxVariableCostLabel, currencyUnits + "/" + currencyTimeUnits);
			addInput(elementPanel, c, "Specific Water Consumption", 
					waterIntensityText, waterUnits + "/km^2",
					"Maximum Water Consumption", 
					maxWaterUseLabel, waterUnits + "/" + waterTimeUnits);
			addInput(elementPanel, c, "Specific Labor Use", 
					laborIntensityText, "people/km^2",
					"Maximum Labor Use", 
					maxLaborUseLabel, "people");
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Maximum Food Input", 
					maxFoodInput, foodUnits + "/" + foodTimeUnits);
			// TODO addInput(elementPanel, c, "Initial Food Input", initialFoodInput, foodUnits + "/" + foodTimeUnits);
			addInput(elementPanel, c, "Distribution Efficiency", 
					distributionEfficiency, foodUnits + "/" + foodUnits,
					"Maximum Food Output", maxOutputLabel, 
					foodUnits + "/" + foodTimeUnits);
			addInput(elementPanel, c, "Variable Operations Cost", 
					variableOperationsCostOfFoodDistribution, 
					currencyUnits + "/" + foodUnits,
					"Maximum Variable Cost", maxVariableCostDistLabel,
					currencyUnits + "/" + currencyTimeUnits);
		}

		// set input enabled state
		maxLandAreaText.setEnabled(element.getTemplateName() == null);
		maxFoodInput.setEnabled(element.getTemplateName() == null);
		costIntensityText.setEnabled(element.getTemplateName() == null);
		foodIntensityText.setEnabled(element.getTemplateName() == null);
		laborIntensityText.setEnabled(element.getTemplateName() == null);
		waterIntensityText.setEnabled(element.getTemplateName() == null);
		distributionEfficiency.setEnabled(element.getTemplateName() == null);
		variableOperationsCostOfFoodDistribution.setEnabled(element.getTemplateName() == null);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}
}
