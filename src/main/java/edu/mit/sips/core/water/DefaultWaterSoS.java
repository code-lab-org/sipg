package edu.mit.sips.core.water;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultWaterSoS.
 */
public class DefaultWaterSoS extends DefaultInfrastructureSoS implements WaterSoS {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private List<Double> aquiferSecurityHistory = new ArrayList<Double>();

	/**
	 * Instantiates a new default water so s.
	 */
	public DefaultWaterSoS() {
		super("Water");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getAquiferLifetime()
	 */
	@Override
	public double getAquiferLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getWaterReservoirVolume() / getReservoirWithdrawals());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += ElectricityUnits.convertFlow(system.getElectricityConsumption(), system, this);
		}
		return value;
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
	public List<WaterSystem> getNestedSystems() {
		List<WaterSystem> systems = new ArrayList<WaterSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getWaterSystem());
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getReservoirWithdrawals()
	 */
	@Override
	public double getReservoirWithdrawals() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += system.getReservoirWithdrawals();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterAgriculturalPrice()
	 */
	@Override
	public double getWaterAgriculturalPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += CurrencyUnits.convertStock(system.getWaterAgriculturalPrice(), system, this);
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
	 */
	@Override
	public double getWaterDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += CurrencyUnits.convertStock(system.getWaterDomesticPrice(), system, this);
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
	 */
	@Override
	public double getWaterImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(WaterSystem system : getNestedSystems()) {
				value += CurrencyUnits.convertStock(system.getWaterImportPrice(), system, this);
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterReservoirVolume()
	 */
	@Override
	public double getWaterReservoirVolume() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += system.getWaterReservoirVolume();
		}
		return value;
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
	public void initialize(long time) {
		super.initialize(time);
		aquiferSecurityHistory.clear();
	}
	
	@Override
	public void tick() {
		super.tick();
		this.aquiferSecurityHistory.add(computeAquiferSecurityScore());
	}
	
	/**
	 * Compute aquifer security score.
	 *
	 * @return the double
	 */
	private double computeAquiferSecurityScore() {
		double minLifetime = 20;
		double maxLifetime = 200;
		if(getAquiferLifetime() < minLifetime) {
			return 0;
		} else if(getAquiferLifetime() > maxLifetime) {
			return 1000;
		} else {
			return 1000 * (getAquiferLifetime() - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	@Override
	public double getAquiferSecurityScore() {
		double value = 0;
		for(double item : aquiferSecurityHistory) {
			value += item;
		}
		return value / aquiferSecurityHistory.size();
	}
}
