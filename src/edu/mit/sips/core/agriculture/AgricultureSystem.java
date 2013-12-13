package edu.mit.sips.core.agriculture;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Interface AgricultureSystem.
 */
public interface AgricultureSystem extends InfrastructureSystem, 
		FoodUnitsOutput, WaterUnitsOutput {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends AgricultureSystem, InfrastructureSystem.Local {
		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(AgricultureElement element);
		
		/**
		 * Gets the arable land area.
		 *
		 * @return the arable land area
		 */
		public double getArableLandArea();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
		 */
		public List<? extends AgricultureElement> getElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		public List<? extends AgricultureElement> getExternalElements();
		
		/**
		 * Gets the food export.
		 *
		 * @return the food export
		 */
		public double getFoodExport();
		
		/**
		 * Gets the food import.
		 *
		 * @return the food import
		 */
		public double getFoodImport();
		
		/**
		 * Gets the food in distribution.
		 *
		 * @return the food in distribution
		 */
		public double getFoodInDistribution();
		
		/**
		 * Gets the food out distribution.
		 *
		 * @return the food out distribution
		 */
		public double getFoodOutDistribution();
		
		/**
		 * Gets the food out distribution losses.
		 *
		 * @return the food out distribution losses
		 */
		public double getFoodOutDistributionLosses();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		public List<? extends AgricultureElement> getInternalElements();
		
		/**
		 * Gets the labor participation rate.
		 *
		 * @return the labor participation rate
		 */
		public double getLaborParticipationRate();
		
		/**
		 * Gets the land area used.
		 *
		 * @return the land area used
		 */
		public double getLandAreaUsed();
		
		/**
		 * Gets the labor used.
		 *
		 * @return the labor used
		 */
		public long getLaborUsed();
		
		/**
		 * Gets the local food fraction.
		 *
		 * @return the local food fraction
		 */
		public double getLocalFoodFraction();
		
		/**
		 * Gets the local food supply.
		 *
		 * @return the local food supply
		 */
		public double getLocalFoodSupply();
		
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
		public boolean removeElement(AgricultureElement element);
	}
	
	/**
	 * Gets the food security.
	 *
	 * @return the food security
	 */
	public double getFoodSecurity();
	
	/**
	 * Gets the food production.
	 *
	 * @return the food production
	 */
	public double getFoodProduction();
	
	/**
	 * Gets the total food supply.
	 *
	 * @return the total food supply
	 */
	public double getTotalFoodSupply();
	
	/**
	 * Gets the food domestic price.
	 *
	 * @return the food domestic price
	 */
	public double getFoodDomesticPrice();
	
	/**
	 * Gets the food export price.
	 *
	 * @return the food export price
	 */
	public double getFoodExportPrice();
	
	/**
	 * Gets the food import price.
	 *
	 * @return the food import price
	 */
	public double getFoodImportPrice();
	
	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
