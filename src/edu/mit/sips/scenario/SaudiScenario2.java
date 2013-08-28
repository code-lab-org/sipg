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
import edu.mit.sips.core.social.demand.ConstantDemandModel;
import edu.mit.sips.core.social.demand.DemandModel;
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
	private static DomesticProductionModel agricultureSystemDomesticProductionModel = 
			new AgricultureSystemDomesticProductionModel(0),
			waterSystemDomesticProductionModel = new WaterSystemDomesticProductionModel(0),
			electricitySystemDomesticProductionModel = new ElectricitySystemDomesticProductionModel(0),
			petroleumSystemDomesticProductionModel = new PetroleumSystemDomesticProductionModel(100),
			socialSystemDomesticProductionModel = new SocialSystemDomesticProductionModel(5000, 100, 2000);
	private static DemandModel foodDemandModel = new LogisticTimeDemandModel(1970, 1950.0, 0.15, 1700.0, 3100.0),
			waterDemandModel = new ConstantDemandModel(85.), 
			electricityDemandModel = new LogisticTimeDemandModel(1990, 0.346, 0.095, 0, 0.84);
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
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6,
								new ArrayList<WaterElement>(),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								new ArrayList<ElectricityElement>(),
								electricitySystemDomesticProductionModel,
								electricityDomesticPriceModel):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(10e9, 10e9,
								new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1980, 3000000, 0.07, 17500000),
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
								new ArrayList<AgricultureElement>(),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6,
								new ArrayList<WaterElement>(),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								new ArrayList<ElectricityElement>(),
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
								new LogisticGrowthModel(1980, 750000, 0.05, 4000000),
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
								new ArrayList<AgricultureElement>(),
								agricultureSystemDomesticProductionModel,
								foodDomesticPriceModel, foodImportPriceModel, foodExportPriceModel):
							new DefaultAgricultureSystem.Remote(),
				sectors.contains(Sector.WATER)?
						new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6,
								new ArrayList<WaterElement>(),
								waterSystemDomesticProductionModel,
								waterDomesticPriceModel, waterImportPriceModel):
							new DefaultWaterSystem.Remote(),
				sectors.contains(Sector.ELECTRICITY)?
						new DefaultElectricitySystem.Local(0.10,
								new ArrayList<ElectricityElement>(),
								electricitySystemDomesticProductionModel,
								electricityDomesticPriceModel):
							new DefaultElectricitySystem.Remote(),
				sectors.contains(Sector.PETROLEUM)?
						new DefaultPetroleumSystem.Local(0, 0, 
								new ArrayList<PetroleumElement>(),
								petroleumSystemDomesticProductionModel,
								petroleumDomesticPriceModel, petroleumImportPriceModel, petroleumExportPriceModel):
							new DefaultPetroleumSystem.Remote(),
				assigned?
						new DefaultSocialSystem.Local(
								socialSystemDomesticProductionModel,
								new LogisticGrowthModel(1980, 6000000, 0.06, 20000000),
								electricityDemandModel, foodDemandModel, waterDemandModel):
							new DefaultSocialSystem.Remote());
	}
}
