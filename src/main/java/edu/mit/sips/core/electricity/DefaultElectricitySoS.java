package edu.mit.sips.core.electricity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultElectricitySoS.
 */
public class DefaultElectricitySoS extends DefaultInfrastructureSoS implements ElectricitySoS {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new default electricity so s.
	 */
	public DefaultElectricitySoS() {
		super("Electricity");
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.electricity.ElectricitySystem#getElectricityDomesticPrice()
	 */
	@Override
	public double getElectricityDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(ElectricitySystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getElectricityDomesticPrice(), 
						system.getCurrencyUnits(), system.getElectricityUnits(),
						getCurrencyUnits(), getElectricityUnits());
			}
			return value / getNestedSystems().size();
		}
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
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<ElectricitySystem> getNestedSystems() {
		List<ElectricitySystem> systems = new ArrayList<ElectricitySystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getElectricitySystem());
		}
		return Collections.unmodifiableList(systems);
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
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}
}
