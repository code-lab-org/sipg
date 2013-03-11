package edu.mit.sips.core.energy;

/**
 * The Class RegionalEnergySystem.
 */
public class RegionalEnergySystem extends DefaultEnergySystem.Local {
	
	/**
	 * Instantiates a new regional energy system.
	 */
	public RegionalEnergySystem() {
		super("Regional Energy", new RegionalPetroleumSystem(), 
				new RegionalElectricitySystem());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getPetroleumSystem()
	 */
	@Override
	public RegionalPetroleumSystem getPetroleumSystem() {
		return (RegionalPetroleumSystem) super.getPetroleumSystem();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.DefaultEnergySystem.Local#getElectricitySystem()
	 */
	@Override
	public RegionalElectricitySystem getElectricitySystem() {
		return (RegionalElectricitySystem) super.getElectricitySystem();
	}
}
