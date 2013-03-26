package edu.mit.sips.core.energy;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Interface EnergySystem.
 */
public interface EnergySystem extends InfrastructureSystem {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends EnergySystem, InfrastructureSystem.Local {
		
		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(EnergyElement element);
		
		/**
		 * Gets the electricity system.
		 *
		 * @return the electricity system
		 */
		public ElectricitySystem getElectricitySystem();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
		 */
		public List<? extends EnergyElement> getElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		public List<? extends EnergyElement> getExternalElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		public List<? extends EnergyElement> getInternalElements();
		
		/**
		 * Gets the petroleum system.
		 *
		 * @return the petroleum system
		 */
		public PetroleumSystem getPetroleumSystem();
		
		/**
		 * Removes the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(EnergyElement element);
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends EnergySystem, InfrastructureSystem.Remote {
		
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
		
		/**
		 * Gets the water consumption.
		 *
		 * @return the water consumption
		 */
		public void setWaterConsumption(double waterConsumption);
	}
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "electricityConsumption",
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "petroleumConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "waterConsumption";
	
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
