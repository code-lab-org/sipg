package edu.mit.sips.core.energy;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface PetroleumSystem.
 */
public interface PetroleumSystem extends InfrastructureSystem.Local {
	public static String ELECTRICITY_CONSUMED_ATTRIBUTE = "electricityConsumed";
	
	/**
	 * Gets the electricity consumed.
	 *
	 * @return the electricity consumed
	 */
	public double getElectricityConsumption();

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getElements()
	 */
	public List<? extends PetroleumElement> getElements();

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
	 */
	public List<? extends PetroleumElement> getExternalElements();

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	public List<? extends PetroleumElement> getInternalElements();

	/**
	 * Gets the local petroleum supply.
	 *
	 * @return the local petroleum supply
	 */
	public double getLocalPetroleumSupply();

	/**
	 * Gets the max petroleum reservoir volume.
	 *
	 * @return the max petroleum reservoir volume
	 */
	public double getMaxPetroleumReservoirVolume();
	
	/**
	 * Gets the national petroleum system.
	 *
	 * @return the national petroleum system
	 */
	public PetroleumSystem.Local getNationalPetroleumSystem();

	/**
	 * Gets the petroleum export.
	 *
	 * @return the petroleum export
	 */
	public double getPetroleumExport();

	/**
	 * Gets the petroleum import.
	 *
	 * @return the petroleum import
	 */
	public double getPetroleumImport();

	/**
	 * Gets the petroleum in distribution.
	 *
	 * @return the petroleum in distribution
	 */
	public double getPetroleumInDistribution();

	/**
	 * Gets the petroleum out distribution.
	 *
	 * @return the petroleum out distribution
	 */
	public double getPetroleumOutDistribution();
	
	/**
	 * Gets the petroleum out distribution losses.
	 *
	 * @return the petroleum out distribution losses
	 */
	public double getPetroleumOutDistributionLosses();

	/**
	 * Gets the petroleum production.
	 *
	 * @return the petroleum production
	 */
	public double getPetroleumProduction();

	/**
	 * Gets the petroleum reservoir volume.
	 *
	 * @return the petroleum reservoir volume
	 */
	public double getPetroleumReservoirVolume();

	/**
	 * Gets the petroleum withdrawals.
	 *
	 * @return the petroleum withdrawals
	 */
	public double getPetroleumWithdrawals();

	/**
	 * Gets the total petroleum supply.
	 *
	 * @return the total petroleum supply
	 */
	public double getTotalPetroleumSupply();
	
	/**
	 * Optimize petroleum distribution.
	 */
	public void optimizePetroleumDistribution();
	
	/**
	 * Optimize petroleum production and distribution.
	 *
	 * @param deltaProductionCost the delta production cost
	 */
	public void optimizePetroleumProductionAndDistribution(double deltaProductionCost);
	
	/**
	 * Gets the unit production cost.
	 *
	 * @return the unit production cost
	 */
	public double getUnitProductionCost();
	
	/**
	 * Gets the unit supply cost.
	 *
	 * @return the unit supply cost
	 */
	public double getUnitSupplyProfit();
}
