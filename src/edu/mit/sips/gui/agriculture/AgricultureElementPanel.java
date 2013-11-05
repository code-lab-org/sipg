package edu.mit.sips.gui.agriculture;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
	private final JLabel outputLabel, variableCostLabel, 
			waterUseLabel, laborUseLabel;

	private final JFormattedTextField maxFoodInput;
	private final JFormattedTextField initialFoodInput;
	private final JFormattedTextField distributionEfficiency;
	private final JFormattedTextField variableOperationsCostOfFoodDistribution;

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
							outputLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * FoodUnits.convertFlow(
											element.getFoodIntensityOfLandUsed(), 
											element, thisPanel)));
							variableCostLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * CurrencyUnits.convertFlow(
											element.getCostIntensityOfLandUsed(),
											element, thisPanel)));
							waterUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * WaterUnits.convertFlow(
											element.getWaterIntensityOfLandUsed(), 
											element, thisPanel)));
							laborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * 
									element.getLaborIntensityOfLandUsed()));
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
							variableCostLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * CurrencyUnits.convertFlow(
											element.getCostIntensityOfLandUsed(), 
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
							outputLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * FoodUnits.convertFlow(
											element.getFoodIntensityOfLandUsed(),
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
							laborUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getLaborIntensityOfLandUsed()
									*element.getInitialLandArea()));
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
							waterUseLabel.setText(NumberFormat.getNumberInstance().format(
									element.getInitialLandArea() * WaterUnits.convertFlow(
											element.getWaterIntensityOfLandUsed(), 
											element, thisPanel)));
							initialLandAreaText.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							initialLandAreaText.setForeground(Color.red);
						}
					}
				});
		outputLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getInitialLandArea() * FoodUnits.convertFlow(
						element.getFoodIntensityOfLandUsed(), 
						element, this)), JLabel.RIGHT);
		variableCostLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getInitialLandArea() * CurrencyUnits.convertFlow(
				element.getCostIntensityOfLandUsed(),
				element, this)), JLabel.RIGHT);
		waterUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getInitialLandArea() * WaterUnits.convertFlow(
						element.getWaterIntensityOfLandUsed(),
						element, this)), JLabel.RIGHT);
		laborUseLabel = new JLabel(NumberFormat.getNumberInstance().format(
				element.getLaborIntensityOfLandUsed()
				*element.getInitialLandArea()), JLabel.RIGHT);

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
							variableOperationsCostOfFoodDistribution.setForeground(Color.black);
						} catch(NumberFormatException ex) {
							variableOperationsCostOfFoodDistribution.setForeground(Color.red);
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
			addInput(elementPanel, c, "Max Land Area", 
					maxLandAreaText, "km^2");
			addInput(elementPanel, c, "Initial Land Area",
					initialLandAreaText, "km^2");
			addInput(elementPanel, c, "Food Yield", foodIntensityText, 
					foodUnits + "/" + foodTimeUnits + "/km^2");
			addInput(elementPanel, c, "Cost Intensity", costIntensityText, 
					currencyUnits + "/km^2");
			addInput(elementPanel, c, "Water Intensity", waterIntensityText, 
					waterUnits + "/km^2");
			addInput(elementPanel, c, "Labor Intensity", laborIntensityText, 
					"people/km^2");
			JPanel productPanel = new JPanel();
			productPanel.setLayout(new GridLayout(4, 3, 2, 2));
			productPanel.add(new JLabel("Initial Food Output"));
			productPanel.add(outputLabel);
			productPanel.add(new JLabel(foodUnits + "/" + foodTimeUnits));
			productPanel.add(new JLabel("Initial Variable Cost"));
			productPanel.add(variableCostLabel);
			productPanel.add(new JLabel(currencyUnits.toString()));
			productPanel.add(new JLabel("Initial Water Use"));
			productPanel.add(waterUseLabel);
			productPanel.add(new JLabel(waterUnits.toString()));
			productPanel.add(new JLabel("Initial Labor Use"));
			productPanel.add(laborUseLabel);
			productPanel.add(new JLabel("people"));
			addInput(elementPanel, c, "", productPanel, "");
		}
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			c.gridx = 3;
			c.gridy = 0;
			addInput(elementPanel, c, "Max Food Input", 
					maxFoodInput, foodUnits + "/" + foodTimeUnits);
			addInput(elementPanel, c, "Initial Food Input", 
					initialFoodInput, foodUnits + "/" + foodTimeUnits);
			addInput(elementPanel, c, "Distribution Efficiency", 
					distributionEfficiency, "-");
			addInput(elementPanel, c, "Operations Cost of Distribution", 
					variableOperationsCostOfFoodDistribution, currencyUnits + "/" + foodUnits);
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
