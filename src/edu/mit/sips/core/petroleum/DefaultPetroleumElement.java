package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultInfrastructureElement;
import edu.mit.sips.core.LifecycleModel;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultPetroleumElement.
 */
public class DefaultPetroleumElement extends DefaultInfrastructureElement
		implements PetroleumElement {
	
	/**
	 * Creates the distribution element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxPetroleumInput the max petroleum input
	 * @param initialPetroleumInput the initial petroleum input
	 * @param electricalIntensityOfPetroleumDistribution the electrical intensity of petroleum distribution
	 * @param variableOperationsCostOfPetroleumDistribution the variable operations cost of petroleum distribution
	 * @return the petroleum element
	 */
	public static PetroleumElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double distributionEfficiency,
			double maxPetroleumInput, double initialPetroleumInput, 
			double electricalIntensityOfPetroleumDistribution,
			double variableOperationsCostOfPetroleumDistribution) {
		// TODO distance-based operational expense?
		return new DefaultPetroleumElement(templateName, name, origin, 
				destination, lifecycleModel, 0, 0, 0, 0, 
				distributionEfficiency, maxPetroleumInput, initialPetroleumInput,
				electricalIntensityOfPetroleumDistribution,
				variableOperationsCostOfPetroleumDistribution);
	}
	
	/**
	 * Creates the production element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfPetroleumProduction the reservoir intensity of petroleum production
	 * @param maxPetroleumProduction the max petroleum production
	 * @param initialPetroleumProduction the initial petroleum production
	 * @param variableOperationsCostOfPetroleumProduction the variable operations cost of petroleum production
	 * @return the petroleum element
	 */
	public static PetroleumElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double reservoirIntensityOfPetroleumProduction,
			double maxPetroleumProduction, double initialPetroleumProduction,
			double variableOperationsCostOfPetroleumProduction) {
		return new DefaultPetroleumElement(templateName, name, origin, 
				destination, lifecycleModel, reservoirIntensityOfPetroleumProduction,
				maxPetroleumProduction, initialPetroleumProduction,
				variableOperationsCostOfPetroleumProduction, 0, 0, 0, 0, 0);
	}
	
	private final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final OilUnits oilUnits = OilUnits.toe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;
	
	private final double reservoirIntensityOfPetroleumProduction;
	private final double maxPetroleumProduction;
	private final double initialPetroleumProduction;
	private final double distributionEfficiency;
	private final double maxPetroleumInput;
	private final double variableOperationsCostOfPetroleumProduction;
	private final double initialPetroleumInput;
	private final double electricalIntensityOfPetroleumDistribution;
	private final double variableOperationsCostOfPetroleumDistribution;
	
	private double petroleumProduction;
	private double petroleumInput;
	
	/**
	 * Instantiates a new default petroleum element.
	 */
	protected DefaultPetroleumElement() {
		reservoirIntensityOfPetroleumProduction = 0;
		maxPetroleumProduction = 0;
		initialPetroleumProduction = 0;
		distributionEfficiency = 0;
		maxPetroleumInput = 0;
		variableOperationsCostOfPetroleumProduction = 0;
		initialPetroleumInput = 0;
		electricalIntensityOfPetroleumDistribution = 0;
		variableOperationsCostOfPetroleumDistribution = 0;
	}
	
	/**
	 * Instantiates a new default petroleum element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param reservoirIntensityOfPetroleumProduction the reservoir intensity of petroleum production
	 * @param maxPetroleumProduction the max petroleum production
	 * @param initialPetroleumProduction the initial petroleum production
	 * @param variableOperationsCostOfPetroleumProduction the variable operations cost of petroleum production
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxPetroleumInput the max petroleum input
	 * @param initialPetroleumInput the initial petroleum input
	 * @param electricalIntensityOfPetroleumDistribution the electrical intensity of petroleum distribution
	 * @param variableOperationsCostOfPetroleumDistribution the variable operations cost of petroleum distribution
	 */
	protected DefaultPetroleumElement(String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double reservoirIntensityOfPetroleumProduction,
			double maxPetroleumProduction, double initialPetroleumProduction, 
			double variableOperationsCostOfPetroleumProduction,
			double distributionEfficiency, double maxPetroleumInput, 
			double initialPetroleumInput, 
			double electricalIntensityOfPetroleumDistribution,
			double variableOperationsCostOfPetroleumDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		// Validate reservoir efficiency.
		if(reservoirIntensityOfPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Reservoir intensity cannot be negative.");
		}
		this.reservoirIntensityOfPetroleumProduction = reservoirIntensityOfPetroleumProduction;
		
		// Validate maximum petroleum production.
		if(maxPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Maximum petroleum production cannot be negative.");
		}
		this.maxPetroleumProduction = maxPetroleumProduction;
		
		// Validate initial petroleum production parameter.
		if(initialPetroleumProduction > maxPetroleumProduction) {
			throw new IllegalArgumentException(
					"Initial petroleum production cannot exceed maximum.");
		} else if(initialPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum production cannot be negative.");
		}
		this.initialPetroleumProduction = initialPetroleumProduction;

		// Validate production cost.
		if(variableOperationsCostOfPetroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Variable cost of production cannot be negative.");
		}
		this.variableOperationsCostOfPetroleumProduction = variableOperationsCostOfPetroleumProduction;
		
		// Validate distribution efficiency.
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Distribution efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		// Validate maximum petroleum input.
		if(maxPetroleumInput < 0) {
			throw new IllegalArgumentException(
					"Maximum petroleum input cannot be negative.");
		}
		this.maxPetroleumInput = maxPetroleumInput;
		
		// Validate initial petroleum input.
		if(initialPetroleumInput > maxPetroleumInput) {
			throw new IllegalArgumentException(
					"Initial petroleum input cannot exceed maximum.");
		} else if(initialPetroleumInput < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum input cannot be negative.");
		}
		this.initialPetroleumInput = initialPetroleumInput;

		// Validate energy intensity.
		if(electricalIntensityOfPetroleumDistribution < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity cannot be negative.");
		}
		this.electricalIntensityOfPetroleumDistribution = electricalIntensityOfPetroleumDistribution;
		
		// Validate distribution cost.
		if(variableOperationsCostOfPetroleumDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfPetroleumDistribution = variableOperationsCostOfPetroleumDistribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getDistributionEfficiency()
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
	 * @see edu.mit.sips.core.energy.PetroleumElement#getElectricalIntensityOfPetroleumDistribution()
	 */
	@Override
	public double getElectricalIntensityOfPetroleumDistribution() {
		return electricalIntensityOfPetroleumDistribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		if(isOperational()) {
			return petroleumInput * electricalIntensityOfPetroleumDistribution;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getMaxPetroleumInput()
	 */
	@Override
	public double getMaxPetroleumInput() {
		if(isOperational()) {
			return maxPetroleumInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getMaxPetroleumProduction()
	 */
	@Override
	public double getMaxPetroleumProduction() {
		if(isOperational()) {
			return maxPetroleumProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getMutableElement()
	 */
	@Override
	public MutablePetroleumElement getMutableElement() {
		MutablePetroleumElement element = 
				new MutablePetroleumElement();
		setMutableFields(element);
		element.setReservoirIntensityOfPetroleumProduction(
				reservoirIntensityOfPetroleumProduction);
		element.setMaxPetroleumProduction(
				maxPetroleumProduction);
		element.setInitialPetroleumProduction(initialPetroleumProduction);
		element.setDistributionEfficiency(distributionEfficiency);
		element.setMaxPetroleumInput(maxPetroleumInput);
		element.setVariableOperationsCostOfPetroleumProduction(
				variableOperationsCostOfPetroleumProduction);
		element.setInitialPetroleumInput(initialPetroleumInput);
		element.setElectricalIntensityOfPetroleumDistribution(
				electricalIntensityOfPetroleumDistribution);
		element.setVariableOperationsCostOfPetroleumDistribution(
				variableOperationsCostOfPetroleumDistribution);
		return element;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
	 */
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
	 */
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getPetroleumInput()
	 */
	@Override
	public double getPetroleumInput() {
		if(isOperational()) {
			return petroleumInput;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getPetroleumOutput()
	 */
	@Override
	public double getPetroleumOutput() {
		if(isOperational()) {
			return petroleumInput * distributionEfficiency;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getPetroleumProduction()
	 */
	@Override
	public double getPetroleumProduction() {
		if(isOperational()) {
			return petroleumProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getPetroleumWithdrawals()
	 */
	@Override
	public double getPetroleumWithdrawals() {
		if(isOperational()) {
			return getPetroleumProduction() * reservoirIntensityOfPetroleumProduction;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getReservoirIntensityOfPetroleumProduction()
	 */
	@Override
	public double getReservoirIntensityOfPetroleumProduction() {
		if(isOperational()) {
			return reservoirIntensityOfPetroleumProduction;
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
				+ variableOperationsCostOfPetroleumProduction * petroleumProduction 
				+ variableOperationsCostOfPetroleumDistribution * petroleumInput;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getVariableOperationsCostOfPetroleumDistribution()
	 */
	@Override
	public double getVariableOperationsCostOfPetroleumDistribution() {
		return variableOperationsCostOfPetroleumDistribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#getVariableOperationsCostOfPetroleumProduction()
	 */
	@Override
	public double getVariableOperationsCostOfPetroleumProduction() {
		return variableOperationsCostOfPetroleumProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#initialize(long)
	 */
	@Override 
	public void initialize(long time) {
		super.initialize(time);

		// Use mutator method to validate petroleum production.
		setPetroleumProduction(initialPetroleumProduction);
		
		// Use mutator method to validate petroleum input.
		setPetroleumInput(initialPetroleumInput);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#setPetroleumInput(double)
	 */
	@Override
	public void setPetroleumInput(double petroleumInput) {
		// Validate water input.
		if(petroleumInput < 0) {
			throw new IllegalArgumentException(
					"Petroleum input cannot be negative.");
		} else if(petroleumInput > maxPetroleumInput) {
			throw new IllegalArgumentException(
					"Petroleum input cannot exceed maximum.");
		}
		this.petroleumInput = petroleumInput;
		fireElementChangeEvent();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumElement#setPetroleumProduction(double)
	 */
	@Override
	public void setPetroleumProduction(double petroleumProduction) {
		// Validate petroleum production parameter.
		if(petroleumProduction > maxPetroleumProduction) {
			throw new IllegalArgumentException(
					"Petroleum production cannot exceed maximum.");
		} else if(petroleumProduction < 0) {
			throw new IllegalArgumentException(
					"Petroleum production cannot be negative.");
		}
		this.petroleumProduction = petroleumProduction;
		fireElementChangeEvent();
	}
}
