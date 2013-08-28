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
import edu.mit.sips.core.social.demand.ConstantDemandModel;
import edu.mit.sips.core.social.demand.LogisticTimeDemandModel;
import edu.mit.sips.core.social.population.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystemDomesticProductionModel;

/**
 * The Class SaudiScenario2.
 */
public final class SaudiScenario2 extends DefaultScenario {
	public static final String INDUSTRIAL = "Industrial", URBAN = "Urban", RURAL = "Rural";

	/**
	 * Instantiates a new saudi scenario2.
	 *
	 * @param assignedCityNames the assigned city names
	 * @param assignedSectors the assigned sectors
	 */
	public SaudiScenario2(Collection<String> assignedCityNames, 
			List<Sector> assignedSectors) {
		super(Country.buildCountry("KSA", 10000L, Arrays.asList(
				createIndustrialCity(assignedCityNames.contains(INDUSTRIAL), assignedSectors), 
				createRuralCity(assignedCityNames.contains(RURAL), assignedSectors), 
				createUrbanCity(assignedCityNames.contains(URBAN), assignedSectors))),
				new ArrayList<ElementTemplate>());
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
						new DefaultElectricitySystem.Local(0.10,
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
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1980, 3000000, 0.07, 17500000),
								new LogisticTimeDemandModel(1990, 0.346, 0.095, 0, 0.84),
								new LogisticTimeDemandModel(1970, 1950.0, 0.15, 1700.0, 3100.0),
								new ConstantDemandModel(85.)):
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
						new DefaultElectricitySystem.Local(0.10,
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
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1980, 750000, 0.05, 4000000),
								new LogisticTimeDemandModel(1990, 0.346, 0.095, 0, 0.84),
								new LogisticTimeDemandModel(1970, 1950.0, 0.15, 1700.0, 3100.0),
								new ConstantDemandModel(85.)):
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
						new DefaultElectricitySystem.Local(0.10,
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
						new DefaultSocialSystem.Local(
								new SocialSystemDomesticProductionModel(5000, 100, 2000),
								new LogisticGrowthModel(1980, 6000000, 0.06, 20000000),
								new LogisticTimeDemandModel(1990, 0.346, 0.095, 0, 0.84),
								new LogisticTimeDemandModel(1970, 1950.0, 0.15, 1700.0, 3100.0),
								new ConstantDemandModel(85.)):
							new DefaultSocialSystem.Remote());
	}
}
