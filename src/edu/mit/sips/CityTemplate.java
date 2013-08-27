package edu.mit.sips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.mit.sips.core.City;
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
import edu.mit.sips.core.social.demand.ConstantDemandModel;
import edu.mit.sips.core.social.demand.LinearBoundedProductDemandModel;
import edu.mit.sips.core.social.demand.LinearTimeDemandModel;
import edu.mit.sips.core.social.demand.LogisticTimeDemandModel;
import edu.mit.sips.core.social.population.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystemDomesticProductionModel;


/**
 * The Enum CityTemplate.
 */
public enum CityTemplate { 
	RURAL("Rural"), 
	URBAN("Urban"), 
	INDUSTRIAL("Industrial"),
	RURAL_VALIDATED("Rural"),
	URBAN_VALIDATED("Urban"),
	INDUSTRIAL_VALIDATED("Industrial");
	
	private final String name;
	
	/**
	 * Instantiates a new city template.
	 *
	 * @param name the name
	 */
	private CityTemplate(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}	
	
	/**
	 * Creates the city.
	 *
	 * @param assigned the assigned
	 * @param sectors the sectors
	 * @return the city
	 */
	public City createCity(boolean assigned, Collection<Sector> sectors) {
		switch(this) {
		case INDUSTRIAL:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(3000, 0.5,
									Arrays.asList(
											(AgricultureElement) ElementTemplate.DATES_2.createElement(1920, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1945, name, name),
											(AgricultureElement) ElementTemplate.GRAINS_1.createElement(1980, name, name)
											),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6,
									Arrays.asList(
											(WaterElement) ElementTemplate.AQUIFER_PUMP_1.createElement(1945, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_2.createElement(1958, name, name),
											(WaterElement) ElementTemplate.TD_DESAL_1.createElement(1970, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_2.createElement(1980, name, name),
											(WaterElement) ElementTemplate.TD_DESAL_2.createElement(1990, name, name)
											),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									Arrays.asList(
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1945, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1955, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1960, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1980, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1985, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1990, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1995, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(2000, name, name)
											),
									new ElectricitySystemDomesticProductionModel(0),
									new ConstantPriceModel(375)):
								new DefaultElectricitySystem.Remote(),
					sectors.contains(Sector.PETROLEUM)?
							new DefaultPetroleumSystem.Local(10e9, 10e9,
									Arrays.asList(
											(PetroleumElement) ElementTemplate.PETRO_WELL_1.createElement(1940, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_1.createElement(1945, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_1.createElement(1945, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_2.createElement(1960, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_2.createElement(1965, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_2.createElement(1970, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_2.createElement(1970, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_3.createElement(1980, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_3.createElement(1982, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_3.createElement(1984, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_3.createElement(1986, name, name),
											(PetroleumElement) ElementTemplate.PETRO_WELL_3.createElement(1988, name, name),
											(PetroleumElement) ElementTemplate.PETRO_PIPELINE_2.createElement(1950, name, CityTemplate.URBAN.name),
											(PetroleumElement) ElementTemplate.PETRO_PIPELINE_3.createElement(1970, name, CityTemplate.URBAN.name),
											(PetroleumElement) ElementTemplate.PETRO_PIPELINE_1.createElement(1960, name, CityTemplate.RURAL.name),
											(PetroleumElement) ElementTemplate.PETRO_PIPELINE_2.createElement(1985, name, CityTemplate.RURAL.name)
											),
									new PetroleumSystemDomesticProductionModel(100),
									new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
								new DefaultPetroleumSystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(10000,
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1950, 50000, 0.08, 20000000),
									new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
									new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
									new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
								new DefaultSocialSystem.Remote());
		case RURAL:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(10000, 0.5,
									Arrays.asList(
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1930, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_2.createElement(1940, name, name),
											(AgricultureElement) ElementTemplate.GRAINS_1.createElement(1975, name, name),
											(AgricultureElement) ElementTemplate.GRAINS_2.createElement(1985, name, name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1968, name, URBAN.name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1968, name, INDUSTRIAL.name),
											(AgricultureElement) ElementTemplate.FOOD_TRANSPORT_1.createElement(1978, name, INDUSTRIAL.name)
											),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6,
									Arrays.asList(
											(WaterElement) ElementTemplate.AQUIFER_PUMP_1.createElement(1955, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_2.createElement(1973, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_2.createElement(1965, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_3.createElement(1984, name, name)
											),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									Arrays.asList(
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1945, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1975, name, name)
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
							new DefaultSocialSystem.Local(8000, 
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1950, 10000, 0.05, 750000),
									new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
									new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
									new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
								new DefaultSocialSystem.Remote());
		case URBAN:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(4000, 0.5,
									Arrays.asList(
											(AgricultureElement) ElementTemplate.DATES_2.createElement(1900, name, name),
											(AgricultureElement) ElementTemplate.LIVESTOCK_1.createElement(1920, name, name)
											),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6,
									Arrays.asList(
											(WaterElement) ElementTemplate.AQUIFER_PUMP_1.createElement(1920, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_1.createElement(1940, name, name),
											(WaterElement) ElementTemplate.AQUIFER_PUMP_2.createElement(1960, name, name),
											(WaterElement) ElementTemplate.RO_DESAL_1.createElement(1970, name, name),
											(WaterElement) ElementTemplate.RO_DESAL_1.createElement(1990, name, name)
											),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									Arrays.asList(
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1945, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1955, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_1.createElement(1960, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1970, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1980, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(1990, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(2000, name, name),
											(ElectricityElement) ElementTemplate.POWER_PLANT_2.createElement(2005, name, name)
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
							new DefaultSocialSystem.Local(20000,
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1950, 100000, 0.07, 10000000),
									new LinearBoundedProductDemandModel(8000, 1, 100000, 10),
									new LinearBoundedProductDemandModel(8000, 2, 100000, 4.5),
									new LinearBoundedProductDemandModel(8000, 10, 100000, 100)):
								new DefaultSocialSystem.Remote());
		case INDUSTRIAL_VALIDATED:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(3000, 0.5,
									new ArrayList<AgricultureElement>(),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6,
									new ArrayList<WaterElement>(),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									new ArrayList<ElectricityElement>(),
									new ElectricitySystemDomesticProductionModel(0),
									new ConstantPriceModel(375)):
								new DefaultElectricitySystem.Remote(),
					sectors.contains(Sector.PETROLEUM)?
							new DefaultPetroleumSystem.Local(10e9, 10e9,
									new ArrayList<PetroleumElement>(),
									new PetroleumSystemDomesticProductionModel(100),
									new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
								new DefaultPetroleumSystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(10000,
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1980, 3000000, 0.07, 17500000),
									new LinearTimeDemandModel(1965, 3.1507, 0.0781),
									new LogisticTimeDemandModel(1970, 1950.0/654., 0.15, 1700.0/654., 3100.0/654.),
									new ConstantDemandModel(85.)):
								new DefaultSocialSystem.Remote());
		case RURAL_VALIDATED:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(10000, 0.5,
									new ArrayList<AgricultureElement>(),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6,
									new ArrayList<WaterElement>(),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									new ArrayList<ElectricityElement>(),
									new ElectricitySystemDomesticProductionModel(0),
									new ConstantPriceModel(375)):
								new DefaultElectricitySystem.Remote(),
					sectors.contains(Sector.PETROLEUM)?
							new DefaultPetroleumSystem.Local(0, 0, new ArrayList<PetroleumElement>(),
									new PetroleumSystemDomesticProductionModel(100),
									new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
								new DefaultPetroleumSystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(8000, 
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1980, 750000, 0.05, 4000000),
									new LinearTimeDemandModel(1965, 3.1507, 0.0781),
									new LogisticTimeDemandModel(1970, 1950.0/654., 0.15, 1700.0/654., 3100.0/654.),
									new ConstantDemandModel(85.)):
								new DefaultSocialSystem.Remote());
		case URBAN_VALIDATED:
			return new City(name, 
					sectors.contains(Sector.AGRICULTURE)?
							new DefaultAgricultureSystem.Local(4000, 0.5,
									new ArrayList<AgricultureElement>(),
									new AgricultureSystemDomesticProductionModel(0),
									new ConstantPriceModel(150), new ConstantPriceModel(200), new ConstantPriceModel(150)):
								new DefaultAgricultureSystem.Remote(),
					sectors.contains(Sector.WATER)?
							new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6,
									new ArrayList<WaterElement>(),
									new WaterSystemDomesticProductionModel(0),
									new ConstantPriceModel(6), new ConstantPriceModel(40)):
								new DefaultWaterSystem.Remote(),
					sectors.contains(Sector.ELECTRICITY)?
							new DefaultElectricitySystem.Local(0.300,
									new ArrayList<ElectricityElement>(),
									new ElectricitySystemDomesticProductionModel(0),
									new ConstantPriceModel(375)):
								new DefaultElectricitySystem.Remote(),
					sectors.contains(Sector.PETROLEUM)?
							new DefaultPetroleumSystem.Local(0, 0, 
									new ArrayList<PetroleumElement>(),
									new PetroleumSystemDomesticProductionModel(100),
									new ConstantPriceModel(50), new ConstantPriceModel(375), new ConstantPriceModel(300)):
								new DefaultPetroleumSystem.Remote(),
					assigned?
							new DefaultSocialSystem.Local(20000,
									new SocialSystemDomesticProductionModel(5000, 100, 2000),
									new LogisticGrowthModel(1980, 6000000, 0.06, 20000000),
									new LinearTimeDemandModel(1965, 3.1507, 0.0781),
									new LogisticTimeDemandModel(1970, 1950.0/654., 0.15, 1700.0/654., 3100.0/654.),
									new ConstantDemandModel(85.)):
								new DefaultSocialSystem.Remote());
		default:
			throw new IllegalArgumentException("Unknown city template.");
		}
	}
};