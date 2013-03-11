package edu.mit.sips.core.water;

import edu.mit.sips.core.DefaultInfrastructureElement;
import edu.mit.sips.core.LifecycleModel;

/**
 * The Class DefaultWaterElement.
 */
public final class DefaultWaterElement extends DefaultInfrastructureElement implements WaterElement {
	/**
	 * Instantiates a new distribution water element.
	 *
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxWaterInput the max water input
	 * @param initialWaterInput the initial water input
	 * @param electricalIntensityOfWaterDistribution the electrical intensity of water distribution
	 * @param variableOperationsCostOfWaterDistribution the variable operations cost of water distribution
	 * @return the water element
	 */
	public static DefaultWaterElement createDistributionElement(String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxWaterInput, double initialWaterInput, 
			double electricalIntensityOfWaterDistribution,
			double variableOperationsCostOfWaterDistribution) {
		return new DefaultWaterElement(name, origin, destination, lifecycleModel, 0, 0, 0, 0, 0,
				distributionEfficiency, maxWaterInput, initialWaterInput,
				electricalIntensityOfWaterDistribution,
				variableOperationsCostOfWaterDistribution);
	}
	/**
	 * Instantiates a new fixed water element.
	 *
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfWaterProduction the reservoir intensity of water production
	 * @param maxWaterProduction the max water production
	 * @param initialWaterProduction the initial water production
	 * @param electricalIntensityOfWaterProduction the electrical intensity of water production
	 * @param variableOperationsCostOfWaterProduction the variable operations cost of water production
	 * @return the water element
	 */
	public static DefaultWaterElement createProductionElement(String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfWaterProduction,
			double maxWaterProduction, double initialWaterProduction, 
			double electricalIntensityOfWaterProduction,
			double variableOperationsCostOfWaterProduction) {
		return new DefaultWaterElement(name, origin, destination, lifecycleModel, 
				reservoirIntensityOfWaterProduction, maxWaterProduction, initialWaterProduction, 
				electricalIntensityOfWaterProduction,
				variableOperationsCostOfWaterProduction, 0, 0, 0, 0, 0);
	}

	private final double maxWaterProduction;
	private final double initialWaterProduction;
	private final double reservoirIntensityOfWaterProduction;
	private final double electricalIntensityOfWaterProduction;
	private final double variableOperationsCostOfWaterProduction;

	private final double maxWaterInput;
	private final double initialWaterInput;
	private final double distributionEfficiency;
	private final double electricalIntensityOfWaterDistribution;
	private final double variableOperationsCostOfWaterDistribution;

	private double waterProduction;
	private double waterInput;
	
	/**
	 * Instantiates a new water element.
	 */
	protected DefaultWaterElement() {
		super();
		
		reservoirIntensityOfWaterProduction = 0;
		maxWaterProduction = 0;
		electricalIntensityOfWaterProduction = 0;
		variableOperationsCostOfWaterProduction = 0;

		initialWaterProduction = 0;
		distributionEfficiency = 0;
		maxWaterInput = 0;
		initialWaterInput = 0;
		electricalIntensityOfWaterDistribution = 0;
		variableOperationsCostOfWaterDistribution = 0;
	}
	
	/**
	 * Instantiates a new water element.
	 *
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfWaterProduction the reservoir intensity of water production
	 * @param maxWaterProduction the max water production
	 * @param initialWaterProduction the initial water production
	 * @param electricalIntensityOfWaterProduction the electrical intensity of water production
	 * @param variableOperationsCostOfWaterProduction the variable operations cost of water production
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxWaterInput the max water input
	 * @param initialWaterInput the initial water input
	 * @param electricalIntensityOfWaterDistribution the electrical intensity of water distribution
	 * @param variableOperationsCostOfWaterDistribution the variable operations cost of water distribution
	 */
	protected DefaultWaterElement(String name, String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfWaterProduction,
			double maxWaterProduction, double initialWaterProduction, 
			double electricalIntensityOfWaterProduction, 
			double variableOperationsCostOfWaterProduction,
			double distributionEfficiency, double maxWaterInput, 
			double initialWaterInput, double electricalIntensityOfWaterDistribution,
			double variableOperationsCostOfWaterDistribution) {
		super(name, origin, destination, lifecycleModel);
		
		// Validate reservoir efficiency.
		if(reservoirIntensityOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Reservoir intensity cannot be negative.");
		}
		this.reservoirIntensityOfWaterProduction = reservoirIntensityOfWaterProduction;
		
		// Validate maximum water production.
		if(maxWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum water production cannot be negative.");
		}
		this.maxWaterProduction = maxWaterProduction;
		
		// Validate initial water production parameter.
		if(initialWaterProduction > maxWaterProduction) {
			throw new IllegalArgumentException(
					"Initial water production cannot exceed maximum.");
		} else if(initialWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Initial water production cannot be negative.");
		}
		this.initialWaterProduction = initialWaterProduction;
				
		// Validate energy intensity.
		if(electricalIntensityOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfWaterProduction = electricalIntensityOfWaterProduction;
		
		// Validate production cost.
		if(variableOperationsCostOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfWaterProduction = variableOperationsCostOfWaterProduction;
		
		// Validate distribution efficiency.
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		// Validate maximum food input.
		if(maxWaterInput < 0) {
			throw new IllegalArgumentException(
					"Maximum food input cannot be negative.");
		}
		this.maxWaterInput = maxWaterInput;
		
		// Validate initial water input.
		if(initialWaterInput > maxWaterInput) {
			throw new IllegalArgumentException(
					"Initial water input cannot exceed maximum.");
		} else if(initialWaterInput < 0) {
			throw new IllegalArgumentException(
					"Initial water input cannot be negative.");
		}
		this.initialWaterInput = initialWaterInput;

		// Validate energy intensity.
		if(electricalIntensityOfWaterProduction < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfWaterDistribution = electricalIntensityOfWaterProduction;
		
		// Validate distribution cost.
		if(variableOperationsCostOfWaterDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfWaterDistribution = variableOperationsCostOfWaterDistribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getDistributionEfficiency()
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
	 * @see edu.mit.sips.core.water.WaterElement#getElectricalIntensityOfWaterDistribution()
	 */
	@Override
	public double getElectricalIntensityOfWaterDistribution() {
		return electricalIntensityOfWaterDistribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getElectricalIntensityOfWaterProduction()
	 */
	@Override
	public double getElectricalIntensityOfWaterProduction() {
		return electricalIntensityOfWaterProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		if(isOperational()) {
			return waterProduction * electricalIntensityOfWaterProduction 
					+ waterInput * electricalIntensityOfWaterDistribution;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getMaxWaterInput()
	 */
	@Override
	public double getMaxWaterInput() {
		if(isOperational()) {
			return maxWaterInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getMaxWaterProduction()
	 */
	@Override
	public double getMaxWaterProduction() {
		if(isOperational()) {
			return maxWaterProduction;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getMutableElement()
	 */
	@Override
	public MutableWaterElement getMutableElement() {
		MutableWaterElement element = new MutableWaterElement();
		setMutableFields(element);
		element.setReservoirIntensityOfWaterProduction(reservoirIntensityOfWaterProduction);
		element.setMaxWaterProduction(maxWaterProduction);
		element.setInitialWaterProduction(initialWaterProduction);
		element.setElectricalIntensityOfWaterProduction(electricalIntensityOfWaterProduction);
		element.setVariableOperationsCostOfWaterProduction(variableOperationsCostOfWaterProduction);
		element.setDistributionEfficiency(distributionEfficiency);
		element.setMaxWaterInput(maxWaterInput);
		element.setInitialWaterInput(initialWaterInput);
		element.setElectricalIntensityOfWaterDistribution(electricalIntensityOfWaterDistribution);
		element.setVariableOperationsCostOfWaterDistribution(variableOperationsCostOfWaterDistribution);
		return element;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getReservoirIntensityOfWaterProduction()
	 */
	@Override
	public double getReservoirIntensityOfWaterProduction() {
		if(isOperational()) {
			return reservoirIntensityOfWaterProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getTotalOperationsExpense()
	 */
	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ variableOperationsCostOfWaterProduction * waterProduction 
				+ variableOperationsCostOfWaterDistribution * waterInput;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getVariableOperationsCostOfWaterDistribution()
	 */
	@Override
	public double getVariableOperationsCostOfWaterDistribution() {
		return variableOperationsCostOfWaterDistribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getVariableOperationsCostOfWaterProduction()
	 */
	@Override
	public double getVariableOperationsCostOfWaterProduction() {
		return variableOperationsCostOfWaterProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getWaterInput()
	 */
	@Override
	public double getWaterInput() {
		if(isOperational()) {
			return waterInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getWaterOutput()
	 */
	@Override
	public double getWaterOutput() {
		if(isOperational()) {
			return waterInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getWaterProduction()
	 */
	@Override
	public double getWaterProduction() {
		if(isOperational()) {
			return waterProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#getWaterWithdrawals()
	 */
	@Override
	public double getWaterWithdrawals() {
		if(isOperational()) {
			return getWaterProduction() * reservoirIntensityOfWaterProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#initialize(long)
	 */
	@Override 
	public void initialize(long time) {
		super.initialize(time);

		// Use mutator method to validate water production.
		setWaterProduction(initialWaterProduction);
		
		// Use mutator method to validate water input.
		setWaterInput(initialWaterInput);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#setWaterInput(double)
	 */
	@Override
	public void setWaterInput(double waterInput) {
		// Validate water input.
		if(waterInput < 0) {
			throw new IllegalArgumentException(
					"Water input cannot be negative.");
		} else if(waterInput > maxWaterInput) {
			throw new IllegalArgumentException(
					"Water input cannot exceed maximum.");
		}
		this.waterInput = waterInput;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterElement#setWaterProduction(double)
	 */
	@Override
	public void setWaterProduction(double waterProduction) {
		// Validate water production parameter.
		if(waterProduction > maxWaterProduction) {
			throw new IllegalArgumentException(
					"Water production cannot exceed maximum.");
		} else if(waterProduction < 0) {
			throw new IllegalArgumentException(
					"Water production cannot be negative.");
		}
		this.waterProduction = waterProduction;
	}
}
