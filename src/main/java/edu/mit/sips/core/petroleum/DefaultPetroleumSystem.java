package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultPetroleumSystem.
 */
public class DefaultPetroleumSystem extends DefaultInfrastructureSystem implements PetroleumSystem {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getElectricityConsumption()
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
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
	 */
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
	 */
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumDomesticPrice()
	 */
	@Override
	public double getPetroleumDomesticPrice() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumExportPrice()
	 */
	@Override
	public double getPetroleumExportPrice() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumImportPrice()
	 */
	@Override
	public double getPetroleumImportPrice() {
		return 0;
	}

	@Override
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirVolume() {
		return 0;
	}

	@Override
	public double getReservoirWithdrawals() {
		return 0;
	}
}
