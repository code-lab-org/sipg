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
package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The default implementation of the social system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultSocialSoS extends LocalSocialSystem implements SocialSoS {
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;

	/**
	 * Instantiates a new default social system-of-systems.
	 */
	public DefaultSocialSoS() {
		super();
	}

	@Override
	public double getCapitalExpense() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCapitalExpense();
		}
		return value;
	}

	@Override
	public double getCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
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
	public double getFoodConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getFoodConsumption();
		}
		return value;
	}

	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	@Override
	public List<SocialSystem> getNestedSystems() {
		List<SocialSystem> systems = new ArrayList<SocialSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getSocialSystem());
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
		for(SocialSystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	@Override
	public long getPopulation() {
		long value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getPopulation();
		}
		return value;
	}

	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
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
