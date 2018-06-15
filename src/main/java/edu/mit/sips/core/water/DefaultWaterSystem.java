package edu.mit.sips.core.water;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultWaterSystem.
 */
public class DefaultWaterSystem extends DefaultInfrastructureSystem implements WaterSystem {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterAgriculturalPrice()
	 */
	@Override
	public double getWaterAgriculturalPrice() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
	 */
	@Override
	public double getWaterDomesticPrice() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
	 */
	@Override
	public double getWaterImportPrice() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	@Override
	public double getAquiferLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getWaterReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getWaterReservoirVolume() {
		return 0;
	}

	@Override
	public double getReservoirWithdrawals() {
		return 0;
	}
}