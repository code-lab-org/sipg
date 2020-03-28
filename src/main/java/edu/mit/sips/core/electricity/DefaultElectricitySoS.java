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
 * The default implementation of the electricity system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultElectricitySoS extends DefaultInfrastructureSoS implements ElectricitySoS {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new default electricity system-of-systems.
	 */
	public DefaultElectricitySoS() {
		super("Electricity");
	}
	
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

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public List<ElectricitySystem> getNestedSystems() {
		List<ElectricitySystem> systems = new ArrayList<ElectricitySystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getElectricitySystem());
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
	public double getPetroleumConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(ElectricitySystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
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
}
