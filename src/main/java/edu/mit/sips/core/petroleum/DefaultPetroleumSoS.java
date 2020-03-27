/*
 * 
 */
package edu.mit.sips.core.petroleum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultPetroleumSoS.
 */
public class DefaultPetroleumSoS extends DefaultInfrastructureSoS implements PetroleumSoS {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private List<Double> reservoirSecurityHistory = new ArrayList<Double>();

	/**
	 * Instantiates a new default petroleum so s.
	 */
	public DefaultPetroleumSoS() {
		super("Petroleum");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
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
	public List<PetroleumSystem> getNestedSystems() {
		List<PetroleumSystem> systems = new ArrayList<PetroleumSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getPetroleumSystem());
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
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumDomesticPrice()
	 */
	@Override
	public double getPetroleumDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumDomesticPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumExportPrice()
	 */
	@Override
	public double getPetroleumExportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumExportPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumImportPrice()
	 */
	@Override
	public double getPetroleumImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(PetroleumSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getPetroleumImportPrice(), 
						system.getCurrencyUnits(), system.getOilUnits(),
						getCurrencyUnits(), getOilUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getReservoirVolume() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getReservoirVolume();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumWithdrawals()
	 */
	@Override
	public double getReservoirWithdrawals() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getReservoirWithdrawals();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getReservoirLifetime()
	 */
	@Override
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public void initialize(long time) {
		super.initialize(time);
		reservoirSecurityHistory.clear();
	}
	
	@Override
	public void tick() {
		super.tick();
		this.reservoirSecurityHistory.add(computeReservoirSecurityScore());
	}
	
	/**
	 * Compute reservoir security score.
	 *
	 * @return the double
	 */
	private double computeReservoirSecurityScore() {
		double minLifetime = 0;
		double maxLifetime = 200;
		if(getReservoirLifetime() < minLifetime) {
			return 0;
		} else if(getReservoirLifetime() > maxLifetime) {
			return 1000;
		} else {
			return 1000*(getReservoirLifetime() - minLifetime)/(maxLifetime - minLifetime);
		}
	}
	
	@Override
	public double getReservoirSecurityScore() {
		double value = 0;
		for(double item : reservoirSecurityHistory) {
			value += item;
		}
		return value / reservoirSecurityHistory.size();
	}
}
