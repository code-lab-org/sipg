package edu.mit.sips.core.energy;

/**
 * The Class CityEnergySystem.
 */
public class CityEnergySystem extends DefaultEnergySystem.Local {
	
	/**
	 * Instantiates a new city energy system.
	 */
	protected CityEnergySystem() {
		super(new CityPetroleumSystem(), new CityElectricitySystem());
	}
	
	/**
	 * Instantiates a new city energy system.
	 *
	 * @param maxPetroleumReservoir the max petroleum reservoir
	 * @param initialPetroleumReservoir the initial petroleum reservoir
	 */
	public CityEnergySystem(double maxPetroleumReservoir,
			double initialPetroleumReservoir) {
		super(new CityPetroleumSystem(maxPetroleumReservoir, 
				initialPetroleumReservoir), new CityElectricitySystem());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getPetroleumSystem()
	 */
	@Override
	public CityPetroleumSystem getPetroleumSystem() {
		return (CityPetroleumSystem) super.getPetroleumSystem();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getElectricitySystem()
	 */
	@Override
	public CityElectricitySystem getElectricitySystem() {
		return (CityElectricitySystem) super.getElectricitySystem();
	}
}
