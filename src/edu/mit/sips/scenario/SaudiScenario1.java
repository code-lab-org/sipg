package edu.mit.sips.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
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
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystemDomesticProductionModel;
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
								new AgricultureSystemDomesticProductionModel(0),
								new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
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
								new WaterSystemDomesticProductionModel(0),
								new ConstantPriceModel(6), new ConstantPriceModel(40)):
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
								new ElectricitySystemDomesticProductionModel(0),
								new ConstantPriceModel(375)):
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
								new PetroleumSystemDomesticProductionModel(100),
								new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1950, 50000, 0.08, 20000000),
								new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
								new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
								new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
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
								new AgricultureSystemDomesticProductionModel(0),
								new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6,
								Arrays.asList(
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_1.createElement(1955, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1973, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_2.createElement(1965, RURAL, RURAL),
										(WaterElement) SaudiElementTemplate.AQUIFER_PUMP_3.createElement(1984, RURAL, RURAL)
										),
								new WaterSystemDomesticProductionModel(0),
								new ConstantPriceModel(6), new ConstantPriceModel(40)):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								Arrays.asList(
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_1.createElement(1945, RURAL, RURAL),
										(ElectricityElement) SaudiElementTemplate.POWER_PLANT_2.createElement(1975, RURAL, RURAL)
										),
								new ElectricitySystemDomesticProductionModel(0),
								new ConstantPriceModel(375)):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
								new PetroleumSystemDomesticProductionModel(100),
								new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1950, 10000, 0.05, 750000),
								new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
								new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
								new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
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
								new AgricultureSystemDomesticProductionModel(0),
								new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
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
								new WaterSystemDomesticProductionModel(0),
								new ConstantPriceModel(6), new ConstantPriceModel(40)):
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
								new ElectricitySystemDomesticProductionModel(0),
								new ConstantPriceModel(375)):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
								new PetroleumSystemDomesticProductionModel(100),
								new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1950, 100000, 0.07, 10000000),
								new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
								new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
								new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
							new DefaultSocialSystem.Remote());
	}
}
