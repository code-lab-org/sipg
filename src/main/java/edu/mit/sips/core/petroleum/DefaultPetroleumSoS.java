/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

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
 * The default implementation of the petroleum system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultPetroleumSoS extends DefaultInfrastructureSoS implements PetroleumSoS {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private List<Double> reservoirSecurityHistory = new ArrayList<Double>();

	/**
	 * Instantiates a new default petroleum system-of-systems.
	 */
	public DefaultPetroleumSoS() {
		super("Petroleum");
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
	public double getElectricityConsumption() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}
	
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}
	
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public List<PetroleumSystem> getNestedSystems() {
		List<PetroleumSystem> systems = new ArrayList<PetroleumSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getPetroleumSystem());
		}
		return Collections.unmodifiableList(systems);
	}
	
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

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

	@Override
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirSecurityScore() {
		double value = 0;
		for(double item : reservoirSecurityHistory) {
			value += item;
		}
		return value / reservoirSecurityHistory.size();
	}

	@Override
	public double getReservoirVolume() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getReservoirVolume();
		}
		return value;
	}
	
	@Override
	public double getReservoirWithdrawals() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getReservoirWithdrawals();
		}
		return value;
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
}
