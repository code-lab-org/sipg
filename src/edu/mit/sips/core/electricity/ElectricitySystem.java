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
		 * Gets the electrical intensity of burning petroleum.
		 *
		 * @return the electrical intensity of burning petroleum
		 */
		public double getElectricalIntensityOfBurningPetroleum();
		
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
		 * Gets the petroleum burned.
		 *
		 * @return the petroleum burned
		 */
		public double getPetroleumBurned();
		
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
	 * The Interface Remote.
	 */
	public static interface Remote extends ElectricitySystem, InfrastructureSystem.Remote {
		
		/**
		 * Sets the electricity domestic price.
		 *
		 * @param domesticPrice the new electricity domestic price
		 */
		public void setElectricityDomesticPrice(double domesticPrice);
		
		/**
		 * Sets the petroleum consumption.
		 *
		 * @param petroleumConsumption the new petroleum consumption
		 */
		public void setPetroleumConsumption(double petroleumConsumption);
		
		/**
		 * Sets the water consumption.
		 *
		 * @param waterConsumption the new water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
	}
	
	/** The water consumption attribute. */
	public static String WATER_CONSUMPTION_ATTRIBUTE = "waterConsumption",
			PETROLEUM_CONSUMPTION_ATTRIBUTE = "petroleumConsumption",
			ELECTRICITY_DOMESTIC_PRICE_ATTRIBUTE = "electricityDomesticPrice";
	
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
