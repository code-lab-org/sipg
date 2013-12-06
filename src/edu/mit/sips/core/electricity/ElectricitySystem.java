package edu.mit.sips.core.electricity;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface ElectricitySystem.
 */
public interface ElectricitySystem extends InfrastructureSystem, 
		WaterUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends ElectricitySystem, InfrastructureSystem.Local {

		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(ElectricityElement element);
		
		/**
		 * Gets the petroleum intensity of private production.
		 *
		 * @return the petroleum intensity of private production
		 */
		public double getPetroleumIntensityOfPrivateProduction();
		
		/**
		 * Gets the electricity from private production.
		 *
		 * @return the electricity from private production
		 */
		public double getElectricityFromPrivateProduction();
		
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
		 * Gets the electricity out distribution losses.
		 *
		 * @return the electricity out distribution losses
		 */
		public double getElectricityOutDistributionLosses();
		
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
		 * Gets the local electricity fraction.
		 *
		 * @return the local electricity fraction
		 */
		public double getLocalElectricityFraction();
		
		/**
		 * Gets the petroleum consumption from private production.
		 *
		 * @return the petroleum consumption from private production
		 */
		public double getPetroleumConsumptionFromPrivateProduction();

		/**
		 * Gets the petroleum consumption from public production.
		 *
		 * @return the petroleum consumption from public production
		 */
		public double getPetroleumConsumptionFromPublicProduction();
		
		/**
		 * Gets the renewable electricity fraction.
		 *
		 * @return the renewable electricity fraction
		 */
		public double getRenewableElectricityFraction();
		
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
		
		/**
		 * Removes the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(ElectricityElement element);
	}
	
	/**
	 * Gets the electricity domestic price.
	 *
	 * @return the electricity domestic price
	 */
	public double getElectricityDomesticPrice();
	
	/**
	 * Gets the petroleum consumption.
	 *
	 * @return the petroleum consumption
	 */
	public double getPetroleumConsumption();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
