package edu.mit.sips.core.water;

import edu.mit.sips.core.Society;

/**
 * The Class RegionalWaterSystem.
 */
public class RegionalWaterSystem extends DefaultWaterSystem.Local {
	
	/**
	 * Instantiates a new regional water system.
	 */
	public RegionalWaterSystem() {
		super("Regional Water");
	}
	
	/**
	 * Instantiates a new regional water system.
	 *
	 * @param name the name
	 */
	protected RegionalWaterSystem(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getMaxWaterReservoirVolume()
	 */
	@Override
	public double getMaxWaterReservoirVolume() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				value += ((WaterSystem.Local)
						society.getWaterSystem()).getMaxWaterReservoirVolume();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirRechargeRate()
	 */
	@Override
	public double getWaterReservoirRechargeRate() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				value += ((WaterSystem.Local)
						society.getWaterSystem()).getWaterReservoirRechargeRate();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirVolume()
	 */
	@Override
	public double getWaterReservoirVolume() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				value += ((WaterSystem.Local)
						society.getWaterSystem()).getWaterReservoirVolume();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#setWaterSupplyPerCapita(double)
	 */
	@Override
	public void setWaterSupplyPerCapita(double waterSupplyPerCapita) {
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				((WaterSystem.Local) society.getWaterSystem())
				.setWaterSupplyPerCapita(waterSupplyPerCapita);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterSupplyPerCapita()
	 */
	@Override
	public double getWaterSupplyPerCapita() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				value += ((WaterSystem.Local)
						society.getWaterSystem()).getWaterSupplyPerCapita() * 
						society.getSocialSystem().getPopulation();
			}
		}
		if(getSociety().getSocialSystem().getPopulation() > 0) {
			return value / getSociety().getSocialSystem().getPopulation();
		}
		return 0;
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
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.DefaultWaterSystem.Local#getWaterFromArtesianWell()
	 */
	@Override
	public double getWaterFromArtesianWell() {
		// need to override this function to prevent cross-linking
		// reservoir capacity at the regional level
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getWaterSystem() instanceof WaterSystem.Local) {
				value += ((WaterSystem.Local)
						society.getWaterSystem()).getWaterFromArtesianWell();
			}
		}
		return value;
	}
}
