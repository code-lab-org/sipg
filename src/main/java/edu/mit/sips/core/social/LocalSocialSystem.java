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

import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.base.LocalInfrastructureSystem;
import edu.mit.sips.core.social.demand.DefaultDemandModel;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.DefaultPopulationModel;
import edu.mit.sips.core.social.population.PopulationModel;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.FoodUnits;
import edu.mit.sips.units.OilUnits;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;

/**
 * The locally-controlled implementation of the social system interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalSocialSystem extends LocalInfrastructureSystem implements SocialSystem.Local {
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	
	private final PopulationModel populationModel;
	private final DemandModel electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel;

	/**
	 * Instantiates a new local social system.
	 */
	public LocalSocialSystem() {
		super("Society");
		this.populationModel = new DefaultPopulationModel();
		this.electricityDemandModel = new DefaultDemandModel();
		this.foodDemandModel = new DefaultDemandModel();
		this.waterDemandModel = new DefaultDemandModel();
		this.petroleumDemandModel = new DefaultDemandModel();
	}

	/**
	 * Instantiates a new local social system.
	 *
	 * @param populationModel the population model
	 * @param electricityDemandModel the electricity demand model
	 * @param foodDemandModel the food demand model
	 * @param waterDemandModel the water demand model
	 * @param petroleumDemandModel the petroleum demand model
	 */
	public LocalSocialSystem(PopulationModel populationModel, 
			DemandModel electricityDemandModel, 
			DemandModel foodDemandModel,
			DemandModel waterDemandModel, 
			DemandModel petroleumDemandModel) {
		super("Society");

		if(populationModel == null) {
			throw new IllegalArgumentException(
					"Population model cannot be null.");
		}
		this.populationModel = populationModel;

		if(electricityDemandModel == null) {
			throw new IllegalArgumentException(
					"Electricity demand model cannot be null.");
		}
		this.electricityDemandModel = electricityDemandModel;

		if(foodDemandModel == null) {
			throw new IllegalArgumentException(
					"Food demand model cannot be null.");
		}
		this.foodDemandModel = foodDemandModel;

		if(waterDemandModel == null) {
			throw new IllegalArgumentException(
					"Water demand model cannot be null.");
		}
		this.waterDemandModel = waterDemandModel;

		if(petroleumDemandModel == null) {
			throw new IllegalArgumentException(
					"Petroleum demand model cannot be null.");
		}
		this.petroleumDemandModel = petroleumDemandModel;
	}

	@Override
	public double getConsumptionExpense() {
		return 0;
	}

	@Override
	public double getDistributionExpense() {
		return 0;
	}

	@Override
	public double getDistributionRevenue() {
		return 0;
	}

	@Override
	public double getElectricityConsumption() {
		return electricityDemandModel.getDemand(getSociety());
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
	public List<? extends InfrastructureElement> getElements() {
		return Collections.unmodifiableList(
				new ArrayList<InfrastructureElement>());
	}

	@Override
	public double getExportRevenue() {
		return 0;
	}

	@Override
	public List<? extends InfrastructureElement> getExternalElements() {
		return Collections.unmodifiableList(
				new ArrayList<InfrastructureElement>());
	}

	@Override
	public double getFoodConsumption() {
		return foodDemandModel.getDemand(getSociety());
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
	public double getImportExpense() {
		return 0;
	}

	@Override
	public List<? extends InfrastructureElement> getInternalElements() {
		return Collections.unmodifiableList(
				new ArrayList<InfrastructureElement>());
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
		return petroleumDemandModel.getDemand(getSociety());
	}

	@Override
	public long getPopulation() {
		return populationModel.getPopulation();
	}

	@Override
	public double getSalesRevenue() {
		return 0;
	}

	@Override
	public double getWaterConsumption() {
		return waterDemandModel.getDemand(getSociety());
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
		populationModel.initialize(time);
		electricityDemandModel.initialize(time);
		foodDemandModel.initialize(time);
		waterDemandModel.initialize(time);
		petroleumDemandModel.initialize(time);

	}

	@Override
	public void tick() {
		populationModel.tick();
		electricityDemandModel.tick();
		foodDemandModel.tick();
		waterDemandModel.tick();
		petroleumDemandModel.tick();
	}

	@Override
	public void tock() {
		populationModel.tock();
		electricityDemandModel.tock();
		foodDemandModel.tock();
		waterDemandModel.tock();
		petroleumDemandModel.tock();
	}
}
