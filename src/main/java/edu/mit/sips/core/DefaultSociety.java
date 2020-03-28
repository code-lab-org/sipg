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
package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.scenario.Sector;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The default implementation of the society interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class DefaultSociety implements Society {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private String name;
	private transient Society society;
	private final List<? extends Society> nestedSocieties;

	private double cumulativeCashFlow;
	private transient double nextTotalCashFlow;
	private double cumulativeCapitalExpense;
	private transient double nextTotalCapitalExpense;

	/**
	 * Instantiates a new default society.
	 *
	 * @param name the society name
	 * @param nestedSocieties the set of nested societies
	 */
	public DefaultSociety(String name, List<? extends Society> nestedSocieties) {
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
		
		if(nestedSocieties == null) {
			throw new IllegalArgumentException(
					"Nested societies list cannot be null.");
		}
		for(Society nestedSociety : nestedSocieties) {
			if(nestedSociety == null) {
				throw new IllegalArgumentException(
						"Nested society cannot be null.");
			}
		}
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>(nestedSocieties));
		for(Society nestedSociety : nestedSocieties) {
			nestedSociety.setSociety(this);
		}
	}

	@Override
	public double getCumulativeCapitalExpense() {
		return cumulativeCapitalExpense;
	}

	@Override
	public double getCumulativeCashFlow() {
		return cumulativeCashFlow;
	}
	
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}
	
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
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
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	@Override
	public List<? extends InfrastructureSystem> getInfrastructureSystems() {
		return Arrays.asList(getAgricultureSystem(), getWaterSystem(), 
				getPetroleumSystem(), getElectricitySystem(), getSocialSystem());
	}

	@Override
	public List<InfrastructureElement> getInternalElements() {
		List<InfrastructureElement> elements = 
				new ArrayList<InfrastructureElement>();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				elements.addAll(((InfrastructureSystem.Local)system)
						.getInternalElements());
			}
		}
		return Collections.unmodifiableList(elements);
	}	
	
	@Override
	public final Collection<Sector> getLocalSectors() {
		List<Sector> sectors = new ArrayList<Sector>();
		if(getAgricultureSystem().isLocal()){
			sectors.add(Sector.AGRICULTURE);
		}
		if(getWaterSystem().isLocal()) {
			sectors.add(Sector.WATER);
		}
		if(getPetroleumSystem().isLocal()) {
			sectors.add(Sector.PETROLEUM);
		}
		if(getElectricitySystem().isLocal()) {
			sectors.add(Sector.ELECTRICITY);
		}
		return sectors;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<? extends Society> getNestedSocieties() {
		return nestedSocieties;
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
	public long getPopulation() {
		return getSocialSystem().getPopulation();
	}

	@Override
	public List<? extends Society> getSocieties() {
		List<Society> societies = new ArrayList<Society>();
		societies.add(this);
		for(Society society : getNestedSocieties()) {
			societies.addAll(society.getSocieties());
		}
		return societies;
	}

	@Override
	public final Society getSociety() {
		return society;
	}

	@Override
	public double getTotalCapitalExpense() {
		double value = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			value += system.getCapitalExpense();
		}
		return value;
	}

	@Override
	public double getTotalCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	@Override
	public double getTotalElectricityDemand() {
		return ElectricityUnits.convertFlow(
				getWaterSystem().getElectricityConsumption(),
				getWaterSystem(), this)
				+ ElectricityUnits.convertFlow(
						getPetroleumSystem().getElectricityConsumption(), 
						getPetroleumSystem(), this)
						+ ElectricityUnits.convertFlow(
								getSocialSystem().getElectricityConsumption(), 
								getSocialSystem(), this);
	}

	@Override
	public double getTotalFoodDemand() {
		return FoodUnits.convertFlow(
				getSocialSystem().getFoodConsumption(), 
				getSocialSystem(), this);
	}

	@Override
	public double getTotalPetroleumDemand() {
		return OilUnits.convertFlow(
				getSocialSystem().getPetroleumConsumption(),
				getSocialSystem(), this)
				+ OilUnits.convertFlow(
						getElectricitySystem().getPetroleumConsumption(),
						getElectricitySystem(), this);
	}

	@Override
	public double getTotalWaterDemand() {
		return WaterUnits.convertFlow(
				getAgricultureSystem().getWaterConsumption(),
				getAgricultureSystem(), this)
				+ WaterUnits.convertFlow(
						getElectricitySystem().getWaterConsumption(),
						getElectricitySystem(), this)
						+ WaterUnits.convertFlow(
								getSocialSystem().getWaterConsumption(),
								getSocialSystem(), this);
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
		cumulativeCashFlow = 0;
		cumulativeCapitalExpense = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.initialize(time);
		}
		for(Society society : getNestedSocieties()) {
			society.initialize(time);
		}
	}

	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	@Override
	public void tick() {
		nextTotalCashFlow = getTotalCashFlow();
		nextTotalCapitalExpense = getTotalCapitalExpense();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.tick();
		}
		for(Society society : getNestedSocieties()) {
			society.tick();
		}
	}

	@Override
	public void tock() {
		cumulativeCashFlow += nextTotalCashFlow;
		cumulativeCapitalExpense += nextTotalCapitalExpense;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.tock();
		}
		for(Society society : getNestedSocieties()) {
			society.tock();
		}
	}
}
