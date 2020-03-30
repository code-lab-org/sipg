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
package edu.mit.sips.core.water;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.DefaultInfrastructureSoS;
import edu.mit.sips.units.CurrencyUnits;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;

/**
 * The default implementation of the water system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultWaterSoS extends DefaultInfrastructureSoS implements WaterSoS {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private List<Double> aquiferSecurityHistory = new ArrayList<Double>();

	/**
	 * Instantiates a new default water system-of-systems.
	 */
	public DefaultWaterSoS() {
		super("Water");
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
	public double getAquiferLifetime() {
		return getAquiferWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getWaterReservoirVolume() / getAquiferWithdrawals());
	}

	@Override
	public double getAquiferSecurityScore() {
		double value = 0;
		for(double item : aquiferSecurityHistory) {
			value += item;
		}
		return value / aquiferSecurityHistory.size();
	}

	@Override
	public double getAquiferWithdrawals() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += system.getAquiferWithdrawals();
		}
		return value;
	}

	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += ElectricityUnits.convertFlow(system.getElectricityConsumption(), system, this);
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
	public List<WaterSystem> getNestedSystems() {
		List<WaterSystem> systems = new ArrayList<WaterSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getWaterSystem());
		}
		return Collections.unmodifiableList(systems);
	}

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

	@Override
	public double getWaterReservoirVolume() {
		double value = 0;
		for(WaterSystem system : getNestedSystems()) {
			value += system.getWaterReservoirVolume();
		}
		return value;
	}
	
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
	
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
}
