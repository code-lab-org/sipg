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
import edu.mit.sips.core.social.demand.LinearBoundedProductDemandModel;
import edu.mit.sips.core.social.population.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystemDomesticProductionModel;

/**
 * The Class SaudiScenario1.
 */
public final class SaudiScenario1 extends DefaultScenario {
	public static final String INDUSTRIAL = "Industrial", URBAN = "Urban", RURAL = "Rural";
	private static DomesticProductionModel agricultureSystemDomesticProductionModel = 
			new AgricultureSystemDomesticProductionModel(0),
			waterSystemDomesticProductionModel = new WaterSystemDomesticProductionModel(0),
			electricitySystemDomesticProductionModel = new ElectricitySystemDomesticProductionModel(0),
			petroleumSystemDomesticProductionModel = new PetroleumSystemDomesticProductionModel(100),
			socialSystemDomesticProductionModel = new SocialSystemDomesticProductionModel(5000, 100, 2000);
	private static DemandModel foodDemandModel = new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
			waterDemandModel = new LinearBoundedProductDemandModel(8000, 10, 100000, 100), 
			electricityDemandModel = new LinearBoundedProductDemandModel(8000, 1, 100000, 10);
	private static PriceModel foodDomesticPriceModel = new ConstantPriceModel(150), 
			foodImportPriceModel = new ConstantPriceModel(200), 
			foodExportPriceModel = new ConstantPriceModel(150),
			waterDomesticPriceModel = new ConstantPriceModel(6), 
			waterImportPriceModel = new ConstantPriceModel(40), 
			electricityDomesticPriceModel = new ConstantPriceModel(375), 
			petroleumDomesticPriceModel = new ConstantPriceModel(50), 
			petroleumImportPriceModel = new ConstantPriceModel(375), 
			petroleumExportPriceModel = new ConstantPriceModel(300);
	
	/**
	 * Instantiates a new saudi scenario1.
	 *
	 * @param assignedCityNames the assigned city names
	 * @param assignedSectors the assigned sectors
	 */
	public SaudiScenario1(Collection<String> assignedCityNames, 
			List<Sector> assignedSectors) {
		super(Country.buildCountry("KSA", 10000L, Arrays.asList(
				createIndustrialCity(assignedCityNames.contains(INDUSTRIAL), assignedSectors), 
				createRuralCity(assignedCityNames.contains(RURAL), assignedSectors), 
				createUrbanCity(assignedCityNames.contains(URBAN), assignedSectors))),
				 Arrays.asList(SaudiElementTemplate.values()));
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
						new DefaultAgricultureSystem.Local(3000, 0.5,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate.DATES_2.createElement(1920, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate.LIVESTOCK_1.createElement(1945, INDUSTRIAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate.GRAINS_1.createElement(1980, INDUSTRIAL, INDUSTRIAL)
										),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_1.createElement(1945, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1958, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate.TD_DESAL_1.createElement(1970, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1980, INDUSTRIAL, INDUSTRIAL),
										(WaterElement) SaudiElementTemplate.TD_DESAL_2.createElement(1990, INDUSTRIAL, INDUSTRIAL)
										),
										waterSystemDomesticProductionModel,
										waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1945, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1955, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1960, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1980, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1985, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1990, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1995, INDUSTRIAL, INDUSTRIAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(2000, INDUSTRIAL, INDUSTRIAL)
										),
										electricitySystemDomesticProductionModel,
										electricityDomesticPriceModel):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(10e9, 10e9,
								Arrays.asList(
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_1.createElement(1940, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_1.createElement(1945, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_1.createElement(1945, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_2.createElement(1960, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_2.createElement(1965, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_2.createElement(1970, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_2.createElement(1970, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_3.createElement(1980, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_3.createElement(1982, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_3.createElement(1984, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_3.createElement(1986, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_WELL_3.createElement(1988, INDUSTRIAL, INDUSTRIAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_PIPELINE_2.createElement(1950, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate.PETRO_PIPELINE_3.createElement(1970, INDUSTRIAL, URBAN),
										(PetroleumElement) SaudiElementTemplate.PETRO_PIPELINE_1.createElement(1960, INDUSTRIAL, RURAL),
										(PetroleumElement) SaudiElementTemplate.PETRO_PIPELINE_2.createElement(1985, INDUSTRIAL, RURAL)
										),
										petroleumSystemDomesticProductionModel,
										petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1950, 50000, 0.08, 20000000),
								electricityDemandModel, foodDemandModel, waterDemandModel):
							new DefaultSocialSystem.Remote());
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
						new DefaultAgricultureSystem.Local(10000, 0.5,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate.LIVESTOCK_1.createElement(1930, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate.LIVESTOCK_2.createElement(1940, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate.GRAINS_1.createElement(1975, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate.GRAINS_2.createElement(1985, RURAL, RURAL),
										(AgricultureElement) SaudiElementTemplate.FOOD_TRANSPORT_1.createElement(1968, RURAL, URBAN),
										(AgricultureElement) SaudiElementTemplate.FOOD_TRANSPORT_1.createElement(1968, RURAL, INDUSTRIAL),
										(AgricultureElement) SaudiElementTemplate.FOOD_TRANSPORT_1.createElement(1978, RURAL, INDUSTRIAL)
										),
										agricultureSystemDomesticProductionModel,
										foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_1.createElement(1955, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1973, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1965, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_3.createElement(1984, RURAL, RURAL)
										),
										waterSystemDomesticProductionModel,
										waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1945, RURAL, RURAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1975, RURAL, RURAL)
										),
										electricitySystemDomesticProductionModel,
										electricityDomesticPriceModel):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1950, 10000, 0.05, 750000),
								electricityDemandModel, foodDemandModel, waterDemandModel):
							new DefaultSocialSystem.Remote());
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
						new DefaultAgricultureSystem.Local(4000, 0.5,
								Arrays.asList(
										(AgricultureElement) SaudiElementTemplate.DATES_2.createElement(1900, URBAN, URBAN),
										(AgricultureElement) SaudiElementTemplate.LIVESTOCK_1.createElement(1920, URBAN, URBAN)
										),
										agricultureSystemDomesticProductionModel,
										foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_1.createElement(1920, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_1.createElement(1940, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1960, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate.RO_DESAL_1.createElement(1970, URBAN, URBAN),
										(WaterElement) SaudiElementTemplate.RO_DESAL_1.createElement(1990, URBAN, URBAN)
										),
										waterSystemDomesticProductionModel,
										waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1945, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1955, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1960, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1970, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1980, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1990, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(2000, URBAN, URBAN),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(2005, URBAN, URBAN)
										),
										electricitySystemDomesticProductionModel,
										electricityDomesticPriceModel):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1950, 100000, 0.07, 10000000),
								electricityDemandModel, foodDemandModel, waterDemandModel):
							new DefaultSocialSystem.Remote());
	}
}
