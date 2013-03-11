package edu.mit.sips.core.agriculture;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface AgricultureSystem.
 */
public interface AgricultureSystem extends InfrastructureSystem {
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends AgricultureSystem, InfrastructureSystem.Local {
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
		 * Gets the food production.
		 *
		 * @return the food production
		 */
		public double getFoodProduction();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		public List<? extends AgricultureElement> getInternalElements();
		
		/**
		 * Gets the land area used.
		 *
		 * @return the land area used
		 */
		public double getLandAreaUsed();
		
		/**
		 * Gets the local food supply.
		 *
		 * @return the local food supply
		 */
		public double getLocalFoodSupply();
		
		/**
		 * Gets the national agriculture system.
		 *
		 * @return the national agriculture system
		 */
		public AgricultureSystem.Local getNationalAgricultureSystem();
		
		/**
		 * Gets the total food supply.
		 *
		 * @return the total food supply
		 */
		public double getTotalFoodSupply();
		
		/**
		 * Optimize food distribution.
		 */
		public void optimizeFoodDistribution();
		
		/**
		 * Optimize food production and distribution.
		 *
		 * @param deltaProductionCost the delta production cost
		 */
		public void optimizeFoodProductionAndDistribution(double deltaProductionCost);
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends AgricultureSystem, InfrastructureSystem.Remote {
		
		/**
		 * Sets the water consumption.
		 *
		 * @param waterConsumption the new water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
	}

	/**
	 * Gets the water consumption.
	 *
	 * @return the water consumption
	 */
	public double getWaterConsumption();
}
