package edu.mit.sips.core.energy;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface EnergySystem.
 */
public interface EnergySystem extends InfrastructureSystem {
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "electricityConsumption",
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "petroleumConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "waterConsumption";
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends EnergySystem, InfrastructureSystem.Local {
		
		/**
		 * Gets the electricity system.
		 *
		 * @return the electricity system
		 */
		public ElectricitySystem getElectricitySystem();
		
		/**
		 * Gets the national energy system.
		 *
		 * @return the national energy system
		 */
		public EnergySystem.Local getNationalEnergySystem();
		
		/**
		 * Gets the petroleum system.
		 *
		 * @return the petroleum system
		 */
		public PetroleumSystem getPetroleumSystem();
		
		/**
		 * Optimize energy distribution.
		 */
		public void optimizeEnergyDistribution();
		
		/**
		 * Optimize energy production and distribution.
		 *
		 * @param deltaPetroleumProductionCost the delta petroleum production cost
		 * @param deltaElectricityProductionCost the delta electricity production cost
		 */
		public void optimizeEnergyProductionAndDistribution(
				double deltaPetroleumProductionCost, 
				double deltaElectricityProductionCost);
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends EnergySystem, InfrastructureSystem.Remote {
		
		/**
		 * Gets the water consumption.
		 *
		 * @return the water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
		
		/**
		 * Sets the electricity consumption.
		 *
		 * @param electricityConsumption the new electricity consumption
		 */
		public void setElectricityConsumption(double electricityConsumption);
		
		/**
		 * Sets the petroleum consumption.
		 *
		 * @param petroleumConsumption the new petroleum consumption
		 */
		public void setPetroleumConsumption(double petroleumConsumption);
	}
	
	/**
	 * Gets the electricity consumption.
	 *
	 * @return the electricity consumption
	 */
	public double getElectricityConsumption();
	
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
