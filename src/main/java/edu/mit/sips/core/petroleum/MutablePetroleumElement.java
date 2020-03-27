
package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultMutableInfrastructureElement;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class MutablePetroleumElement.
 */
public final class MutablePetroleumElement extends DefaultMutableInfrastructureElement
		implements ElectricityUnitsOutput, OilUnitsOutput {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	private double maxPetroleumProduction;
	private double initialPetroleumProduction;
	private double reservoirIntensityOfPetroleumProduction;
	private double variableOperationsCostOfPetroleumProduction;
	
	private double maxPetroleumInput;
	private double initialPetroleumInput;
	private double distributionEfficiency;
	private double electricalIntensityOfPetroleumDistribution;
	private double variableOperationsCostOfPetroleumDistribution;

	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#createElement()
	 */
	@Override
	public PetroleumElement createElement() {
		return new DefaultPetroleumElement(getTemplateName(), getName(), 
				getOrigin(), getDestination(),
				getLifecycleModel().createLifecycleModel(), getReservoirIntensityOfPetroleumProduction(),
				getMaxPetroleumProduction(), getInitialPetroleumProduction(), 
				getVariableOperationsCostOfPetroleumProduction(),
				getDistributionEfficiency(), getMaxPetroleumInput(), 
				getInitialPetroleumInput(), 
				getElectricalIntensityOfPetroleumDistribution(),
				getVariableOperationsCostOfPetroleumDistribution());
	}

	/**
	 * Gets the distribution efficiency.
	 *
	 * @return the distribution efficiency
	 */
	public double getDistributionEfficiency() {
		return distributionEfficiency;
	}

	/**
	 * Gets the electrical intensity of petroleum distribution.
	 *
	 * @return the electrical intensity of petroleum distribution
	 */
	public double getElectricalIntensityOfPetroleumDistribution() {
		return electricalIntensityOfPetroleumDistribution;
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

	/**
	 * Gets the initial petroleum input.
	 *
	 * @return the initial petroleum input
	 */
	public double getInitialPetroleumInput() {
		return initialPetroleumInput;
	}

	/**
	 * Gets the initial petroleum production.
	 *
	 * @return the initial petroleum production
	 */
	public double getInitialPetroleumProduction() {
		return initialPetroleumProduction;
	}

	/**
	 * Gets the max petroleum input.
	 *
	 * @return the max petroleum input
	 */
	public double getMaxPetroleumInput() {
		return maxPetroleumInput;
	}

	/**
	 * Gets the max petroleum production.
	 *
	 * @return the max petroleum production
	 */
	public double getMaxPetroleumProduction() {
		return maxPetroleumProduction;
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

	/**
	 * Gets the reservoir intensity of petroleum production.
	 *
	 * @return the reservoir intensity of petroleum production
	 */
	public double getReservoirIntensityOfPetroleumProduction() {
		return reservoirIntensityOfPetroleumProduction;
	}

	/**
	 * Gets the variable operations cost of petroleum distribution.
	 *
	 * @return the variable operations cost of petroleum distribution
	 */
	public double getVariableOperationsCostOfPetroleumDistribution() {
		return variableOperationsCostOfPetroleumDistribution;
	}

	/**
	 * Gets the variable operations cost of petroleum production.
	 *
	 * @return the variable operations cost of petroleum production
	 */
	public double getVariableOperationsCostOfPetroleumProduction() {
		return variableOperationsCostOfPetroleumProduction;
	}
	/**
	 * Sets the distribution efficiency.
	 *
	 * @param distributionEfficiency the new distribution efficiency
	 */
	public void setDistributionEfficiency(double distributionEfficiency) {
		this.distributionEfficiency = distributionEfficiency;
	}
	/**
	 * Sets the electrical intensity of petroleum distribution.
	 *
	 * @param electricalIntensityOfPetroleumDistribution the new electrical intensity of petroleum distribution
	 */
	public void setElectricalIntensityOfPetroleumDistribution(
			double electricalIntensityOfPetroleumDistribution) {
		this.electricalIntensityOfPetroleumDistribution = electricalIntensityOfPetroleumDistribution;
	}
	/**
	 * Sets the initial petroleum input.
	 *
	 * @param initialPetroleumInput the new initial petroleum input
	 */
	public void setInitialPetroleumInput(double initialPetroleumInput) {
		this.initialPetroleumInput = initialPetroleumInput;
	}
	/**
	 * Sets the initial petroleum production.
	 *
	 * @param initialPetroleumProduction the new initial petroleum production
	 */
	public void setInitialPetroleumProduction(double initialPetroleumProduction) {
		this.initialPetroleumProduction = initialPetroleumProduction;
	}
	
	/**
	 * Sets the max petroleum input.
	 *
	 * @param maxPetroleumInput the new max petroleum input
	 */
	public void setMaxPetroleumInput(double maxPetroleumInput) {
		this.maxPetroleumInput = maxPetroleumInput;
	}

	/**
	 * Sets the max petroleum production.
	 *
	 * @param maxPetroleumProduction the new max petroleum production
	 */
	public void setMaxPetroleumProduction(double maxPetroleumProduction) {
		this.maxPetroleumProduction = maxPetroleumProduction;
	}

	/**
	 * Sets the reservoir intensity of petroleum production.
	 *
	 * @param reservoirIntensityOfPetroleumProduction the new reservoir intensity of petroleum production
	 */
	public void setReservoirIntensityOfPetroleumProduction(
			double reservoirIntensityOfPetroleumProduction) {
		this.reservoirIntensityOfPetroleumProduction = reservoirIntensityOfPetroleumProduction;
	}

	/**
	 * Sets the variable operations cost of petroleum distribution.
	 *
	 * @param variableOperationsCostOfPetroleumDistribution the new variable operations cost of petroleum distribution
	 */
	public void setVariableOperationsCostOfPetroleumDistribution(
			double variableOperationsCostOfPetroleumDistribution) {
		this.variableOperationsCostOfPetroleumDistribution = variableOperationsCostOfPetroleumDistribution;
	}

	/**
	 * Sets the variable operations cost of petroleum production.
	 *
	 * @param variableOperationsCostOfPetroleumProduction the new variable operations cost of petroleum production
	 */
	public void setVariableOperationsCostOfPetroleumProduction(
			double variableOperationsCostOfPetroleumProduction) {
		this.variableOperationsCostOfPetroleumProduction = variableOperationsCostOfPetroleumProduction;
	}
}