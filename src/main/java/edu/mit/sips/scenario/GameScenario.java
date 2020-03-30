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
package edu.mit.sips.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.LocalAgricultureSystem;
import edu.mit.sips.core.agriculture.RecordedAgricultureSystem;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.LocalElectricitySystem;
import edu.mit.sips.core.electricity.RecordedElectricitySystem;
import edu.mit.sips.core.petroleum.LocalPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.RecordedPetroleumSystem;
import edu.mit.sips.core.price.ConstantPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.LocalSocialSystem;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.demand.LogisticTimeDemandModel;
import edu.mit.sips.core.social.population.LogisticGrowthModel;
import edu.mit.sips.core.water.LocalWaterSystem;
import edu.mit.sips.core.water.RecordedWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.FoodUnits;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;

/**
 * A scenario implementation for the sustainable infrastructure planning game.
 * 
 * @author Paul T. Grogan
 */
public final class GameScenario extends DefaultScenario {
	public static final String INDUSTRIAL = "Industrial", URBAN = "Urban", RURAL = "Rural";
	private static DemandModel 
			foodDemandModel = new LogisticTimeDemandModel(1975, 
					FoodUnits.convertFlow(2300, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year), 0.20, 
					FoodUnits.convertFlow(1800, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year), 
					FoodUnits.convertFlow(5800, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year)),
			waterDemandModel = new LogisticTimeDemandModel(1965, 
					WaterUnits.convertFlow(175, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year), 0.08, 
					WaterUnits.convertFlow(25, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year), 
					WaterUnits.convertFlow(325, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year)), 
			electricityDemandModel = new LogisticTimeDemandModel(1950, 
					ElectricityUnits.convertFlow(0.25, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year), 0.09, 
					ElectricityUnits.convertFlow(0, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year), 
					ElectricityUnits.convertFlow(40, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year)),
			petroleumDemandModel = new LogisticTimeDemandModel(1970, 1, 0.07, 0, 9);
	private static PriceModel foodDomesticPriceModel = new ConstantPriceModel(60), 
			foodImportPriceModel = new ConstantPriceModel(70), 
			foodExportPriceModel = new ConstantPriceModel(50),
			waterDomesticPriceModel = new ConstantPriceModel(0.05), 
			waterImportPriceModel = new ConstantPriceModel(10), 
			electricityDomesticPriceModel = new ConstantPriceModel(4), 
			petroleumDomesticPriceModel = new ConstantPriceModel(8), 
			petroleumImportPriceModel = new ConstantPriceModel(35), 
			petroleumExportPriceModel = new ConstantPriceModel(30);

	/**
	 * Creates the industrial city.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	private static City createIndustrialCity(boolean assigned, Collection<Sector> sectors) {
		return new City(INDUSTRIAL, 
				sectors.contains(Sector.AGRICULTURE)?
						new LocalAgricultureSystem(8e3, 0.04,
								Arrays.asList(
										(AgricultureElement) GameElementTemplate.WHEAT_1.createElement(1940, INDUSTRIAL, INDUSTRIAL)
										),
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new RecordedAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new LocalWaterSystem(true, 200e9, 200e9, 0.1e9, 0.9e-3, 1,
								Arrays.asList(
										(WaterElement) GameElementTemplate.RO_PLANT_1.createElement(1978, INDUSTRIAL, INDUSTRIAL)
										),
								waterDomesticPriceModel, waterImportPriceModel):
							new RecordedWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new LocalPetroleumSystem(65e9, 65e9,
								Arrays.asList(
										(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1940, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_PIPELINE_1.createElement(1940, 1978-1940, INDUSTRIAL, URBAN)
										,(PetroleumElement) GameElementTemplate.OIL_PIPELINE_1.createElement(1940, INDUSTRIAL, RURAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1940, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1955, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1962, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1964, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1966, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1968, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_1.createElement(1970, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_2.createElement(1970, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_2.createElement(1972, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_WELL_2.createElement(1976, INDUSTRIAL, INDUSTRIAL)
										,(PetroleumElement) GameElementTemplate.OIL_PIPELINE_2.createElement(1976, INDUSTRIAL, URBAN)
										,(PetroleumElement) GameElementTemplate.OIL_PIPELINE_1.createElement(1976, INDUSTRIAL, RURAL)
										),
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new RecordedPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new LocalElectricitySystem(0.5,
								Arrays.asList(
										(ElectricityElement) GameElementTemplate.POWER_PLANT_1.createElement(1960, INDUSTRIAL, INDUSTRIAL)
										,(ElectricityElement) GameElementTemplate.POWER_PLANT_2.createElement(1974, INDUSTRIAL, INDUSTRIAL)
										),
								electricityDomesticPriceModel):
							new RecordedElectricitySystem(),
				assigned?
						new LocalSocialSystem(
								new LogisticGrowthModel(1980, (long) 3e6, 0.07, (long) 17.5e6),
								electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel):
							new DefaultSocialSystem());
	}

	/**
	 * Creates the rural city.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	private static City createRuralCity(boolean assigned, Collection<Sector> sectors) {
		return new City(RURAL, 
				sectors.contains(Sector.AGRICULTURE)?
						new LocalAgricultureSystem(15e3, 0.40,
								Arrays.asList(
										(AgricultureElement) GameElementTemplate.WHEAT_1.createElement(1940, RURAL, RURAL)
										,(AgricultureElement) GameElementTemplate.FOOD_TRANSPORT_1.createElement(1940, RURAL, INDUSTRIAL)
										,(AgricultureElement) GameElementTemplate.FOOD_TRANSPORT_1.createElement(1940, RURAL, URBAN)
										,(AgricultureElement) GameElementTemplate.WHEAT_1.createElement(1962, RURAL, RURAL)
										),
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new RecordedAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new LocalWaterSystem(false, 250e9, 250e9, 1.2e9, 0.9e-3, 1,
								new ArrayList<WaterElement>(),
								waterDomesticPriceModel, waterImportPriceModel):
							new RecordedWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new LocalPetroleumSystem(0, 0, new ArrayList<PetroleumElement>(),
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new RecordedPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new LocalElectricitySystem(0.5,
								Arrays.asList(
										(ElectricityElement) GameElementTemplate.POWER_PLANT_1.createElement(1966, RURAL, RURAL)
										),
								electricityDomesticPriceModel):
							new RecordedElectricitySystem(),
				assigned?
						new LocalSocialSystem(
								new LogisticGrowthModel(1980, (long) 0.75e6, 0.05, (long) 4e6),
								electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel):
							new DefaultSocialSystem());
	}
	
	/**
	 * Creates the urban city.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	private static City createUrbanCity(boolean assigned, Collection<Sector> sectors) {
		return new City(URBAN, 
				sectors.contains(Sector.AGRICULTURE)?
						new LocalAgricultureSystem(10e3, 0.04,
								Arrays.asList(
										(AgricultureElement) GameElementTemplate.WHEAT_1.createElement(1940, URBAN, URBAN)
										),
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new RecordedAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new LocalWaterSystem(true, 150e9, 150e9, 2.2e9, 0.9e-3, 1,
								new ArrayList<WaterElement>(),
								waterDomesticPriceModel, waterImportPriceModel):
							new RecordedWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new LocalPetroleumSystem(0, 0, 
								new ArrayList<PetroleumElement>(),
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new RecordedPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new LocalElectricitySystem(0.5,
								Arrays.asList(
										(ElectricityElement) GameElementTemplate.POWER_PLANT_1.createElement(1950, URBAN, URBAN)
										,(ElectricityElement) GameElementTemplate.POWER_PLANT_2.createElement(1972, URBAN, URBAN)
										),
								electricityDomesticPriceModel):
							new RecordedElectricitySystem(),
				assigned?
						new LocalSocialSystem(
								new LogisticGrowthModel(1980, (long) 6e6, 0.06, (long) 20e6),
								electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel):
							new DefaultSocialSystem());
	}

	/**
	 * Instantiates a new game scenario.
	 *
	 * @param assignedCityNames the assigned city names
	 * @param assignedSectors the assigned sectors
	 */
	public GameScenario(Collection<String> assignedCityNames, 
			Collection<Sector> assignedSectors, boolean isTeamScoreDisplayed) {
		super(Country.buildCountry("Idas Abara", 25e9, 4e9, Arrays.asList(
				createIndustrialCity(assignedCityNames.contains(INDUSTRIAL), assignedSectors), 
				createRuralCity(assignedCityNames.contains(RURAL), assignedSectors), 
				createUrbanCity(assignedCityNames.contains(URBAN), assignedSectors))),
				Arrays.asList(GameElementTemplate.values()), 
				1950, 1980, 2010, isTeamScoreDisplayed, false);
	}

	@Override
	public List<? extends ElementTemplate> getTemplates(Collection<Sector> sectors) {
		ArrayList<ElementTemplate> filteredTemplates = new ArrayList<ElementTemplate>(super.getTemplates(sectors));
		filteredTemplates.remove(getTemplate(GameElementTemplate.FOOD_TRANSPORT_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.OIL_PIPELINE_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.OIL_WELL_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.POWER_LINE_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.POWER_LINE_2.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.POWER_PLANT_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.PV_PLANT_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.RO_PLANT_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.WATER_PIPELINE_1.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.WATER_PIPELINE_2.getName()));
		filteredTemplates.remove(getTemplate(GameElementTemplate.WHEAT_1.getName()));
		return filteredTemplates;
	}
}
