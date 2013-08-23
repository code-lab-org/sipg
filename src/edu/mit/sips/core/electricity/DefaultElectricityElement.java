package edu.mit.sips.core.electricity;

import edu.mit.sips.ElementTemplate;
import edu.mit.sips.core.DefaultInfrastructureElement;
import edu.mit.sips.core.LifecycleModel;

/**
 * The Class DefaultElectricityElement.
 */
public class DefaultElectricityElement extends DefaultInfrastructureElement
		implements ElectricityElement {

	/**
	 * Creates the distribution eement.
	 *
	 * @param template the template
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxElectricityInput the max electricity input
	 * @param initialElectricityInput the initial electricity input
	 * @param variableOperationsCostOfElectricityDistribution the variable operations cost of electricity distribution
	 * @return the electricity element
	 */
	public static ElectricityElement createDistributionElement(
			ElementTemplate template, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxElectricityInput, double initialElectricityInput,
			double variableOperationsCostOfElectricityDistribution) {
		return new DefaultElectricityElement(template, name, origin, 
				destination, lifecycleModel, 0, 
				0, 0, 0, 0, distributionEfficiency, maxElectricityInput, 
				initialElectricityInput, variableOperationsCostOfElectricityDistribution);
	}
	
	/**
	 * Creates the production element.
	 *
	 * @param template the template
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxElectricityProduction the max electricity production
	 * @param initialElectricityProduction the initial electricity production
	 * @param petroleumIntensityOfElectricityProduction the petroleum intensity of electricity production
	 * @param waterIntensityOfElectricityProduction the water intensity of electricity production
	 * @param variableOperationsCostOfElectricityProduction the variable operations cost of electricity production
	 * @return the electricity element
	 */
	public static ElectricityElement createProductionElement(
			ElementTemplate template, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double maxElectricityProduction, 
			double initialElectricityProduction, 
			double petroleumIntensityOfElectricityProduction,
			double waterIntensityOfElectricityProduction,
			double variableOperationsCostOfElectricityProduction) {
		return new DefaultElectricityElement(template, name, origin, 
				destination, lifecycleModel, maxElectricityProduction, 
				initialElectricityProduction, petroleumIntensityOfElectricityProduction,
				waterIntensityOfElectricityProduction, 
				variableOperationsCostOfElectricityProduction, 0, 0, 0, 0);
	}
	
	private final double maxElectricityProduction;
	private final double initialElectricityProduction;
	private final double petroleumIntensityOfElectricityProduction;
	private final double waterIntensityOfElectricityProduction;
	private final double variableOperationsCostOfElectricityProduction;

	private final double maxElectricityInput;
	private final double initialElectricityInput;
	
	private final double distributionEfficiency;
	private final double variableOperationsCostOfElectricityDistribution;
	private double electricityInput;
	private double electricityProduction;
	
	/**
	 * Instantiates a new default electricity element.
	 */
	protected DefaultElectricityElement() {
		maxElectricityProduction = 0;
		initialElectricityProduction = 0;
		petroleumIntensityOfElectricityProduction = 0;
		waterIntensityOfElectricityProduction = 0;
		distributionEfficiency = 0;
		variableOperationsCostOfElectricityProduction = 0;
		
		maxElectricityInput = 0;
		initialElectricityInput = 0;
		variableOperationsCostOfElectricityDistribution = 0;
	}
	
	/**
	 * Instantiates a new default electricity element.
	 *
	 * @param template the template
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxElectricityProduction the max electricity production
	 * @param initialElectricityProduction the initial electricity production
	 * @param petroleumIntensityOfElectricityProduction the petroleum intensity of electricity production
	 * @param waterIntensityOfElectricityProduction the water intensity of electricity production
	 * @param variableOperationsCostOfElectricityProduction the variable operations cost of electricity production
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxElectricityInput the max electricity input
	 * @param initialElectricityInput the initial electricity input
	 * @param variableOperationsCostOfElectricityDistribution the variable operations cost of electricity distribution
	 */
	protected DefaultElectricityElement(ElementTemplate template, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double maxElectricityProduction, 
			double initialElectricityProduction, 
			double petroleumIntensityOfElectricityProduction,
			double waterIntensityOfElectricityProduction, 
			double variableOperationsCostOfElectricityProduction,
			double distributionEfficiency, double maxElectricityInput, 
			double initialElectricityInput,
			double variableOperationsCostOfElectricityDistribution) {
		super(template, name, origin, destination, lifecycleModel);
		
		// Validate maximum electricity production.
		if(maxElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum electricity production cannot be negative.");
		}
		this.maxElectricityProduction = maxElectricityProduction;
		
		// Validate initial electricity production parameter.
		if(initialElectricityProduction > maxElectricityProduction) {
			throw new IllegalArgumentException(
					"Initial electricity production cannot exceed maximum.");
		} else if(initialElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Initial electricity production cannot be negative.");
		}
		this.initialElectricityProduction = initialElectricityProduction;
		
		// Validate petroleum intensity.
		if(petroleumIntensityOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Petroleum intensity cannot be negative.");
		}
		this.petroleumIntensityOfElectricityProduction = petroleumIntensityOfElectricityProduction;
		
		// Validate water intensity.
		if(waterIntensityOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Water intensity cannot be negative.");
		}
		this.waterIntensityOfElectricityProduction = waterIntensityOfElectricityProduction;
		
		// Validate production cost.
		if(variableOperationsCostOfElectricityProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfElectricityProduction = variableOperationsCostOfElectricityProduction;
		
		// Validate distribution efficiency.
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		// Validate maximum food input.
		if(maxElectricityInput < 0) {
			throw new IllegalArgumentException(
					"Maximum electricity input cannot be negative.");
		}
		this.maxElectricityInput = maxElectricityInput;
		
		// Validate initial electricity input.
		if(initialElectricityInput > maxElectricityInput) {
			throw new IllegalArgumentException(
					"Initial electricity input cannot exceed maximum.");
		} else if(initialElectricityInput < 0) {
			throw new IllegalArgumentException(
					"Initial electricity input cannot be negative.");
		}
		this.initialElectricityInput = initialElectricityInput;
		
		// Validate distribution cost.
		if(variableOperationsCostOfElectricityDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfElectricityDistribution = variableOperationsCostOfElectricityDistribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getDistributionEfficiency()
	 */
	@Override
	public double getDistributionEfficiency() {
		if(isOperational()) {
			return distributionEfficiency;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getElectricityInput()
	 */
	@Override
	public double getElectricityInput() {
		if(isOperational()) {
			return electricityInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getElectricityOutput()
	 */
	@Override
	public double getElectricityOutput() {
		if(isOperational()) {
			return electricityInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getElectricityProduction()
	 */
	@Override
	public double getElectricityProduction() {
		if(isOperational()) {
			return electricityProduction;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getMaxElectricityInput()
	 */
	@Override
	public double getMaxElectricityInput() {
		if(isOperational()) {
			return maxElectricityInput;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getMaxElectricityProduction()
	 */
	@Override
	public double getMaxElectricityProduction() {
		if(isOperational()) {
			return maxElectricityProduction;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getMutableElement()
	 */
	@Override
	public MutableElectricityElement getMutableElement() {
		MutableElectricityElement element = new MutableElectricityElement();
		setMutableFields(element);
		element.setMaxElectricityProduction(maxElectricityProduction);
		element.setInitialElectricityProduction(initialElectricityProduction);
		element.setPetroleumIntensityOfElectricityProduction(
				petroleumIntensityOfElectricityProduction);
		element.setWaterIntensityOfElectricityProduction(
				waterIntensityOfElectricityProduction);
		element.setVariableOperationsCostOfElectricityProduction(
				variableOperationsCostOfElectricityProduction);
		element.setMaxElectricityInput(maxElectricityInput);
		element.setInitialElectricityInput(initialElectricityInput);
		element.setDistributionEfficiency(
				distributionEfficiency);
		element.setVariableOperationsCostOfElectricityDistribution(
				variableOperationsCostOfElectricityDistribution);
		
		return element;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		if(isOperational()) {
			return electricityProduction * petroleumIntensityOfElectricityProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getPetroleumIntensityOfElectricityProduction()
	 */
	@Override
	public double getPetroleumIntensityOfElectricityProduction() {
		return petroleumIntensityOfElectricityProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getTotalOperationsExpense()
	 */
	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ variableOperationsCostOfElectricityProduction * electricityProduction 
				+ variableOperationsCostOfElectricityDistribution * electricityInput;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getVariableOperationsCostOfElectricityDistribution()
	 */
	@Override
	public double getVariableOperationsCostOfElectricityDistribution() {
		return variableOperationsCostOfElectricityDistribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getVariableOperationsCostOfElectricityProduction()
	 */
	@Override
	public double getVariableOperationsCostOfElectricityProduction() {
		return variableOperationsCostOfElectricityProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		if(isOperational()) {
			return electricityProduction * waterIntensityOfElectricityProduction;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#getWaterIntensityOfElectricityProduction()
	 */
	@Override
	public double getWaterIntensityOfElectricityProduction() {
		return waterIntensityOfElectricityProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		super.initialize(time);
		
		// Use mutator method to validate electricity input.
		setElectricityInput(initialElectricityInput);

		// Use mutator method to validate electricity production.
		setElectricityProduction(initialElectricityProduction);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#isRenewableElectricity()
	 */
	@Override
	public boolean isRenewableElectricity() {
		return petroleumIntensityOfElectricityProduction == 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#setElectricityInput(double)
	 */
	@Override
	public void setElectricityInput(double electricityInput) {
		// Validate water input.
		if(electricityInput < 0) {
			throw new IllegalArgumentException(
					"Electricity input cannot be negative.");
		} else if(electricityInput > maxElectricityInput) {
			throw new IllegalArgumentException(
					"Electricity input cannot exceed maximum.");
		}
		this.electricityInput = electricityInput;
		fireElementChangeEvent();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricityElement#setElectricityProduction(double)
	 */
	@Override
	public void setElectricityProduction(double electricityProduction) {
		// Validate electricity production parameter.
		if(electricityProduction > maxElectricityProduction) {
			throw new IllegalArgumentException(
					"Electricity production cannot exceed maximum.");
		} else if(electricityProduction < 0) {
			throw new IllegalArgumentException(
					"Electricity production cannot be negative.");
		}
		this.electricityProduction = electricityProduction;
		fireElementChangeEvent();
	}
}
