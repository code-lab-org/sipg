package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.social.demand.DefaultDemandModel;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.PopulationModel;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultSocialSystem.
 */
public abstract class DefaultSocialSystem implements SocialSystem {

	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements SocialSystem.Local {
		private static final OilUnits oilUnits = OilUnits.toe;
		private static final TimeUnits oilTimeUnits = TimeUnits.year;
		
		private final DomesticProductionModel domesticProductionModel;
		private final PopulationModel populationModel;
		private final DemandModel electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel;
		private double domesticProduct;
		private transient double nextDomesticProduct;
		
		/**
		 * Instantiates a new local.
		 */
		public Local() {
			super("Society");
			this.domesticProductionModel = new DefaultDomesticProductionModel();
			this.populationModel = new DefaultPopulationModel();
			this.electricityDemandModel = new DefaultDemandModel();
			this.foodDemandModel = new DefaultDemandModel();
			this.waterDemandModel = new DefaultDemandModel();
			this.petroleumDemandModel = new DefaultDemandModel();
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param domesticProductionModel the domestic production model
		 * @param populationModel the population model
		 * @param electricityDemandModel the electricity demand model
		 * @param foodDemandModel the food demand model
		 * @param waterDemandModel the water demand model
		 * @param petroleumDemandModel the petroleum demand model
		 */
		public Local(DomesticProductionModel domesticProductionModel,
				PopulationModel populationModel, 
				DemandModel electricityDemandModel, 
				DemandModel foodDemandModel,
				DemandModel waterDemandModel, 
				DemandModel petroleumDemandModel) {
			super("Society");

			// Validate domestic production model.
			if(domesticProductionModel == null) {
				throw new IllegalArgumentException(
						"Domestic production model cannot be null.");
			}
			this.domesticProductionModel = domesticProductionModel;
			
			// Validate population model.
			if(populationModel == null) {
				throw new IllegalArgumentException(
						"Population model cannot be null.");
			}
			this.populationModel = populationModel;
			
			// Validate electricity demand model.
			if(electricityDemandModel == null) {
				throw new IllegalArgumentException(
						"Electricity demand model cannot be null.");
			}
			this.electricityDemandModel = electricityDemandModel;
			
			// Validate food demand model.
			if(foodDemandModel == null) {
				throw new IllegalArgumentException(
						"Food demand model cannot be null.");
			}
			this.foodDemandModel = foodDemandModel;
			
			// Validate water demand model.
			if(waterDemandModel == null) {
				throw new IllegalArgumentException(
						"Water demand model cannot be null.");
			}
			this.waterDemandModel = waterDemandModel;
			
			// Validate petroleum demand model.
			if(petroleumDemandModel == null) {
				throw new IllegalArgumentException(
						"Petroleum demand model cannot be null.");
			}
			this.petroleumDemandModel = petroleumDemandModel;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return 0;
			/* Government does not pay for its citizens' consumption
			return getElectricityConsumption() * city.getGlobals().getElectricityDomesticPrice()
					 + getWaterConsumption() * city.getGlobals().getWaterDomesticPrice()
					 + getFoodConsumption() * city.getGlobals().getFoodDomesticPrice();
			 */
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getDomesticProduct()
		 */
		@Override
		public double getDomesticProduct() {
			return domesticProduct;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return domesticProductionModel.getDomesticProduction(this);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityDemandModel.getDemand(this);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsDenominator()
		 */
		@Override
		public TimeUnits getElectricityTimeUnits() {
			return TimeUnits.year;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsNumerator()
		 */
		@Override
		public ElectricityUnits getElectricityUnits() {
			return ElectricityUnits.MWh;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<? extends InfrastructureElement> getElements() {
			return Collections.unmodifiableList(
					new ArrayList<InfrastructureElement>());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		@Override
		public List<? extends InfrastructureElement> getExternalElements() {
			return Collections.unmodifiableList(
					new ArrayList<InfrastructureElement>());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyFoodConsumption()
		 */
		@Override
		public double getFoodConsumption() {
			return foodDemandModel.getDemand(this);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
		 */
		@Override
		public TimeUnits getFoodTimeUnits() {
			return TimeUnits.day;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
		 */
		@Override
		public FoodUnits getFoodUnits() {
			return FoodUnits.kcal;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<? extends InfrastructureElement> getInternalElements() {
			return Collections.unmodifiableList(
					new ArrayList<InfrastructureElement>());
		}

		/**
		 * Gets the next domestic product.
		 *
		 * @return the next domestic product
		 */
		private double getNextDomesticProduct() {
			double nextDomesticProduct = 0;
			for(InfrastructureSystem s : getSociety().getInfrastructureSystems()) {
				nextDomesticProduct += s.getDomesticProduction();
			}
			return nextDomesticProduct;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
		 */
		@Override
		public TimeUnits getOilTimeUnits() {
			return oilTimeUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
		 */
		@Override
		public OilUnits getOilUnits() {
			return oilUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			return petroleumDemandModel.getDemand(this);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getPopulation()
		 */
		@Override
		public long getPopulation() {
			return populationModel.getPopulation();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterDemandModel.getDemand(this);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
		 */
		@Override
		public TimeUnits getWaterTimeUnits() {
			return TimeUnits.year;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
		 */
		@Override
		public WaterUnits getWaterUnits() {
			return WaterUnits.m3;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) {
			populationModel.initialize(time);
			
			// initialize demand models after domestic product
			electricityDemandModel.initialize(time);
			foodDemandModel.initialize(time);
			waterDemandModel.initialize(time);
			petroleumDemandModel.initialize(time);
			
			domesticProduct = getDomesticProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		@Override
		public void tick() {
			nextDomesticProduct = getNextDomesticProduct();
			populationModel.tick();
			electricityDemandModel.tick();
			foodDemandModel.tick();
			waterDemandModel.tick();
			petroleumDemandModel.tick();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tock()
		 */
		@Override
		public void tock() {
			domesticProduct = nextDomesticProduct;
			populationModel.tock();
			electricityDemandModel.tock();
			foodDemandModel.tock();
			waterDemandModel.tock();
			petroleumDemandModel.tock();
		}
	}
	
	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements SocialSystem.Remote {
		private static final OilUnits oilUnits = OilUnits.toe;
		private static final TimeUnits oilTimeUnits = TimeUnits.year;
		
		private double domesticProduct;
		private long population;
		private double electricityConsumption;
		private double foodConsumption;
		private double waterConsumption;
		private double petroleumConsumption;

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProduct()
		 */
		@Override
		public double getDomesticProduct() {
			return domesticProduct;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsDenominator()
		 */
		@Override
		public TimeUnits getElectricityTimeUnits() {
			return TimeUnits.year;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsNumerator()
		 */
		@Override
		public ElectricityUnits getElectricityUnits() {
			return ElectricityUnits.MWh;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumption()
		 */
		@Override
		public double getFoodConsumption() {
			return foodConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
		 */
		@Override
		public TimeUnits getFoodTimeUnits() {
			return TimeUnits.day;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
		 */
		@Override
		public FoodUnits getFoodUnits() {
			return FoodUnits.kcal;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
		 */
		@Override
		public TimeUnits getOilTimeUnits() {
			return oilTimeUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
		 */
		@Override
		public OilUnits getOilUnits() {
			return oilUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			return petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getPopulation()
		 */
		@Override
		public long getPopulation() {
			return population;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
		 */
		@Override
		public TimeUnits getWaterTimeUnits() {
			return TimeUnits.year;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
		 */
		@Override
		public WaterUnits getWaterUnits() {
			return WaterUnits.m3;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setDomesticProduct(double)
		 */
		@Override
		public void setDomesticProduct(double domesticProduct) {
			this.domesticProduct = domesticProduct;
			fireAttributeChangeEvent(Arrays.asList(
					DOMESTIC_PRODUCT_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getFoodConsumption(double)
		 */
		@Override
		public void setFoodConsumption(double foodConsumption) {
			this.foodConsumption = foodConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					FOOD_CONSUMPTION_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setPetroleumConsumption(double)
		 */
		@Override
		public void setPetroleumConsumption(double petroleumConsumption) {
			this.petroleumConsumption = petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setPopulation(long)
		 */
		@Override
		public void setPopulation(long population) {
			this.population = population;
			fireAttributeChangeEvent(Arrays.asList(
					POPULATION_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					WATER_CONSUMPTION_ATTRIBUTE));
		}
	}
}
