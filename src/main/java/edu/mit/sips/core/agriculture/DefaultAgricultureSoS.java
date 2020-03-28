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
package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.DefaultInfrastructureSoS;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The default implementation of the agriculture system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultAgricultureSoS extends DefaultInfrastructureSoS implements AgricultureSoS {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private List<Double> foodSecurityHistory = new ArrayList<Double>();
	
	/**
	 * Instantiates a new default agriculture so s.
	 */
	public DefaultAgricultureSoS() {
		super("Agriculture");
	}

	/**
	 * Compute food security score.
	 *
	 * @return the double
	 */
	private double computeFoodSecurityScore() {
		return 1000 / 0.75 * Math.max(Math.min(this.getFoodSecurity(), 0.75), 0);
	}

	@Override
	public double getFoodDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodDomesticPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getFoodExportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodExportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	@Override
	public double getFoodImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodImportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}
	
	@Override
	public double getFoodProduction() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getFoodProduction();
		}
		return value;
	}
	
	@Override
	public double getFoodSecurity() {
		return getTotalFoodSupply() == 0 ? 1 
				: (getFoodProduction() / getTotalFoodSupply());
	}

	@Override
	public double getFoodSecurityScore() {
		double value = 0;
		for(double item : foodSecurityHistory) {
			value += item;
		}
		return value / foodSecurityHistory.size();
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
	public List<AgricultureSystem> getNestedSystems() {
		List<AgricultureSystem> systems = new ArrayList<AgricultureSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getAgricultureSystem());
		}
		return Collections.unmodifiableList(systems);
	}

	@Override
	public double getTotalFoodSupply() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getTotalFoodSupply();
		}
		return value;
	}

	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
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
	
	@Override
	public void initialize(long time) {
		super.initialize(time);
		foodSecurityHistory.clear();
	}

	@Override
	public void tick() {
		super.tick();
		this.foodSecurityHistory.add(computeFoodSecurityScore());
	}
}
