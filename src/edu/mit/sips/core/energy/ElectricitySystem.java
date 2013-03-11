package edu.mit.sips.core.energy;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;

public interface ElectricitySystem extends InfrastructureSystem.Local {
	
	/**
	 * Gets the electricity from burning petroleum.
	 *
	 * @return the electricity from burning petroleum
	 */
	public double getElectricityFromBurningPetroleum();
	
	/**
	 * Gets the electricity in distribution.
	 *
	 * @return the electricity in distribution
	 */
	public double getElectricityInDistribution();
	
	/**
	 * Gets the electricity out distribution.
	 *
	 * @return the electricity out distribution
	 */
	public double getElectricityOutDistribution();
	
	/**
	 * Gets the electricity production.
	 *
	 * @return the electricity production
	 */
	public double getElectricityProduction();
	
	/**
	 * Gets the electricity wasted.
	 *
	 * @return the electricity wasted
	 */
	public double getElectricityWasted();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getElements()
	 */
	public List<? extends ElectricityElement> getElements();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
	 */
	public List<? extends ElectricityElement> getExternalElements();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	public List<? extends ElectricityElement> getInternalElements();
	
	/**
	 * Gets the national electricity system.
	 *
	 * @return the national electricity system
	 */
	public ElectricitySystem getNationalElectricitySystem();
	
	/**
	 * Gets the petroleum burned.
	 *
	 * @return the petroleum burned
	 */
	public double getPetroleumBurned();
	
	/**
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
	
	/**
	 * Gets the renewable energy production.
	 *
	 * @return the renewable energy production
	 */
	public double getRenewableElectricityProduction();
	
	/**
	 * Gets the total electricity supply.
	 *
	 * @return the total electricity supply
	 */
	public double getTotalElectricitySupply();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
	
	/**
	 * Optimize electricity production and distribution.
	 *
	 * @param deltaProductionCost the delta production cost
	 */
	public void optimizeElectricityProductionAndDistribution(double deltaProductionCost);
	
	/**
	 * Optimize electricity distribution.
	 */
	public void optimizeElectricityDistribution();
}
