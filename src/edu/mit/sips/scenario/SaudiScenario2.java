package edu.mit.sips.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystemDomesticProductionModel;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.electricity.DefaultElectricitySystem;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySystemDomesticProductionModel;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSystemDomesticProductionModel;
import edu.mit.sips.core.price.ConstantPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystemDomesticProductionModel;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.demand.LogisticTimeDemandModel;
import edu.mit.sips.core.social.population.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystemDomesticProductionModel;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class SaudiScenario2.
 */
public final class SaudiScenario2 extends DefaultScenario {
	public static final String INDUSTRIAL = "Industrial", URBAN = "Urban", RURAL = "Rural";
	private static DomesticProductionModel agricultureSystemDomesticProductionModel = 
			new AgricultureSystemDomesticProductionModel(0),
			waterSystemDomesticProductionModel = new WaterSystemDomesticProductionModel(0),
			electricitySystemDomesticProductionModel = new ElectricitySystemDomesticProductionModel(0),
			petroleumSystemDomesticProductionModel = new PetroleumSystemDomesticProductionModel(100),
			socialSystemDomesticProductionModel = new SocialSystemDomesticProductionModel(5000, 100, 2000);
	private static DemandModel 
			foodDemandModel = new LogisticTimeDemandModel(1970, 
					FoodUnits.convertFlow(1950, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year), 0.15, 
					FoodUnits.convertFlow(1700, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year), 
					FoodUnits.convertFlow(3100, FoodUnits.kcal, TimeUnits.day, FoodUnits.GJ, TimeUnits.year)),
			waterDemandModel = new LogisticTimeDemandModel(1965, 
					WaterUnits.convertFlow(175, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year), 0.08, 
					WaterUnits.convertFlow(25, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year), 
					WaterUnits.convertFlow(325, WaterUnits.L, TimeUnits.day, WaterUnits.m3, TimeUnits.year)), 
			electricityDemandModel = new LogisticTimeDemandModel(1950, 
					ElectricityUnits.convertFlow(0.25, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year), 0.09, 
					ElectricityUnits.convertFlow(0, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year), 
					ElectricityUnits.convertFlow(40, ElectricityUnits.kWh, TimeUnits.day, ElectricityUnits.MWh, TimeUnits.year)),
			petroleumDemandModel = new LogisticTimeDemandModel(1970, 1, 0.07, 0, 9);
	private static PriceModel foodDomesticPriceModel = new ConstantPriceModel(50), 
			foodImportPriceModel = new ConstantPriceModel(75), 
			foodExportPriceModel = new ConstantPriceModel(40),
			waterDomesticPriceModel = new ConstantPriceModel(0.05), 
			waterImportPriceModel = new ConstantPriceModel(10), 
			electricityDomesticPriceModel = new ConstantPriceModel(4), 
			petroleumDomesticPriceModel = new ConstantPriceModel(8), 
			petroleumImportPriceModel = new ConstantPriceModel(45), 
			petroleumExportPriceModel = new ConstantPriceModel(40);

	/**
	 * Instantiates a new saudi scenario2.
	 *
	 * @param assignedCityNames the assigned city names
	 * @param assignedSectors the assigned sectors
	 */
	public SaudiScenario2(Collection<String> assignedCityNames, 
			List<Sector> assignedSectors) {
		super(Country.buildCountry("Kingdom", 25e9, Arrays.asList(
				createIndustrialCity(assignedCityNames.contains(INDUSTRIAL), assignedSectors), 
				createRuralCity(assignedCityNames.contains(RURAL), assignedSectors), 
				createUrbanCity(assignedCityNames.contains(URBAN), assignedSectors))),
				Arrays.asList(SaudiElementTemplate2.values()));
	}

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
						new DefaultAgricultureSystem.Local(8e3, 0.04,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(0, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(1976, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1986, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1990, 1996, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1990, 1996, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1990, 1996, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1990, 1996, INDUSTRIAL, INDUSTRIAL)
										),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 200e9, 200e9, 0.1e9, 0.9e-3, 1,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate2.RO_PLANT_1.createElement(1978, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(1980, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(1986, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_1.createElement(1994, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(2000, INDUSTRIAL, INDUSTRIAL)
										),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(65e9, 65e9,
								Arrays.asList(
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1940, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_1.createElement(1940, 1978, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_1.createElement(1940, 1990, INDUSTRIAL, RURAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1950, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1955, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1962, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1964, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1966, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1968, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_1.createElement(1970, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1970, 1981, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1972, 1982, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1976, 1983, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_2.createElement(1976, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_1.createElement(1976, INDUSTRIAL, RURAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1984, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1988, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_1.createElement(1988, INDUSTRIAL, RURAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(1990, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_2.createElement(1992, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate2.OIL_WELL_2.createElement(2002, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_2.createElement(2004, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate2.OIL_PIPELINE_1.createElement(2008, INDUSTRIAL, RURAL)
										),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.5,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(1960, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_LINE_1.createElement(1970, INDUSTRIAL, RURAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1974, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1984, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1992, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1998, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2000, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2002, INDUSTRIAL, INDUSTRIAL),
										//(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2003, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2004, INDUSTRIAL, INDUSTRIAL),
										//(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2005, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2006, INDUSTRIAL, INDUSTRIAL)
										),
								electricitySystemDomesticProductionModel,
								electricityDomesticPriceModel):
							new DefaultElectricitySystem(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
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
						new DefaultAgricultureSystem.Local(15e3, 0.40,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(0, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.FOOD_TRANSPORT_1.createElement(0, RURAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.FOOD_TRANSPORT_1.createElement(0, RURAL, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(1962, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1982, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1982, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1982, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.FOOD_TRANSPORT_2.createElement(1982, RURAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate2.FOOD_TRANSPORT_2.createElement(1982, RURAL, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1984, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1984, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1984, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1984, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(2002, 2008, RURAL, RURAL)
										),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(false, 250e9, 250e9, 1.2e9, 0.9e-3, 1,
								new ArrayList<WaterElement>(),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.5,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(1966, RURAL, RURAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(1986, RURAL, RURAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(1996, RURAL, RURAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(2000, RURAL, RURAL)
										),
								electricitySystemDomesticProductionModel,
								electricityDomesticPriceModel):
							new DefaultElectricitySystem(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
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
						new DefaultAgricultureSystem.Local(10e3, 0.04,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(0, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_1.createElement(1974, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1984, 1996, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1986, 1996, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1986, 1996, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1988, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(1988, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate2.WHEAT_2.createElement(2002, URBAN, URBAN)
										),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 150e9, 150e9, 2.2e9, 0.9e-3, 1,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate2.RO_PLANT_1.createElement(1980, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_1.createElement(1982, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(1986, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(1990, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate2.RO_PLANT_2.createElement(2000, URBAN, URBAN)
										),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, 
								new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.5,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_1.createElement(1950, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_LINE_1.createElement(1972, URBAN, RURAL),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1972, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1982, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1988, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1990, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1994, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1996, URBAN, URBAN),
										//(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(1998, URBAN, URBAN),
										//(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2000, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2002, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2004, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2006, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate2.POWER_PLANT_2.createElement(2008, URBAN, URBAN)
										),
								electricitySystemDomesticProductionModel,
								electricityDomesticPriceModel):
							new DefaultElectricitySystem(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1980, (long) 6e6, 0.06, (long) 20e6),
								electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel):
							new DefaultSocialSystem());
	}
}
