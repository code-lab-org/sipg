package edu.mit.sips.core.energy;

import edu.mit.sips.core.Society;

/**
 * The Class RegionalWaterSystem.
 */
public class RegionalPetroleumSystem extends DefaultPetroleumSystem {
	
	/**
	 * Instantiates a new regional petroleum system.
	 */
	public RegionalPetroleumSystem() {
		super("Regional Petroleum");
	}
	
	/**
	 * Instantiates a new regional petroleum system.
	 *
	 * @param name the name
	 */
	protected RegionalPetroleumSystem(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getMaxPetroleumReservoirVolume()
	 */
	@Override
	public double getMaxPetroleumReservoirVolume() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) {
				value += ((EnergySystem.Local)
						society.getEnergySystem()).getPetroleumSystem()
						.getMaxPetroleumReservoirVolume();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getEnergySystem() instanceof EnergySystem.Local) {
				value += ((EnergySystem.Local)
						society.getEnergySystem()).getPetroleumSystem()
						.getPetroleumReservoirVolume();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		
	}
}
