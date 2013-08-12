package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.social.demand.DefaultDemandModel;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.PopulationModel;

/**
 * The Class DefaultSocialSystem.
 */
public abstract class DefaultSocialSystem implements SocialSystem {

	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements SocialSystem.Local {
		private final PopulationModel populationModel;
		private final DemandModel electricityDemandModel, foodDemandModel;
		private final double initialDomesticProductPerCapita;
		private double domesticProduct;
		private transient double nextDomesticProduct;
		
		/**
		 * Instantiates a new local.
		 *
		 */
		public Local() {
			super("Society");
			this.populationModel = new DefaultPopulationModel();
			this.electricityDemandModel = new DefaultDemandModel();
			this.foodDemandModel = new DefaultDemandModel();
			this.initialDomesticProductPerCapita = 0;
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param initialDomesticProductPerCapita the initial domestic product per capita
		 * @param populationModel the population model
		 * @param electricityDemandModel the electricity demand model
		 * @param foodDemandModel the food demand model
		 */
		public Local(double initialDomesticProductPerCapita, 
				PopulationModel populationModel, 
				DemandModel electricityDemandModel, 
				DemandModel foodDemandModel) {
			super("Society");
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
			this.electricityDemandModel.setSocialSystem(this);
			
			// Validate food demand model.
			if(foodDemandModel == null) {
				throw new IllegalArgumentException(
						"Food demand model cannot be null.");
			}
			this.foodDemandModel = foodDemandModel;
			this.foodDemandModel.setSocialSystem(this);

			// No need to validate initial domestic product
			this.initialDomesticProductPerCapita = initialDomesticProductPerCapita;
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
			// add private consumption to base domestic production
			return super.getDomesticProduction()
					+ (getSociety().getGlobals().getFoodDomesticPrice()
					+ getSociety().getGlobals().getPrivateConsumptionFromFoodConsumption())
					* getFoodConsumption()
					+ (getSociety().getGlobals().getWaterDomesticPrice()
					+ getSociety().getGlobals().getPrivateConsumptionFromWaterConsumption())
					* getWaterConsumption()
					+ (getSociety().getGlobals().getElectricityDomesticPrice() 
					+ getSociety().getGlobals().getPrivateConsumptionFromElectricityConsumption())
					* getElectricityConsumption();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProductPerCapita()
		 */
		@Override
		public double getDomesticProductPerCapita() {
			if(getPopulation() > 0) {
				return getDomesticProduct() / getPopulation();
			} 
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return getElectricityConsumptionPerCapita() * getPopulation();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyElectricityConsumptionPerCapita()
		 */
		@Override
		public double getElectricityConsumptionPerCapita() {
			return electricityDemandModel.getDemand();
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
			return getFoodConsumptionPerCapita() * getPopulation();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyFoodConsumptionPerCapita()
		 */
		@Override
		public double getFoodConsumptionPerCapita() {
			return foodDemandModel.getDemand();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Local#getInitialDomesticProductPerCapita()
		 */
		@Override
		public double getInitialDomesticProductPerCapita() {
			return initialDomesticProductPerCapita;
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
		 * Gets the next economic production.
		 *
		 * @return the next economic production
		 */
		private double getNextEconomicProduction() {
			double economicProduction = 0;
			for(InfrastructureSystem s : getSociety().getInfrastructureSystems()) {
				economicProduction += s.getDomesticProduction();
			}
			return economicProduction;
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
			return getWaterConsumptionPerCapita() * getPopulation();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SocialSystem#getSocietyWaterConsumptionPerCapita()
		 */
		@Override
		public double getWaterConsumptionPerCapita() {
			return Math.min(getSociety().getGlobals().getMaxWaterDemandPerCapita(), 
					Math.max(getSociety().getGlobals().getMinWaterDemandPerCapita(), 
							getSociety().getWaterSystem().getWaterSupplyPerCapita()));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) {
			populationModel.initialize(time);
			domesticProduct = initialDomesticProductPerCapita*populationModel.getPopulation();
			
			// initialize demand models after domestic product
			electricityDemandModel.initialize(time);
			foodDemandModel.initialize(time);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		@Override
		public void tick() {
			nextDomesticProduct = getNextEconomicProduction();
			populationModel.tick();
			electricityDemandModel.tick();
			foodDemandModel.tick();
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
		}
	}
	
	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements SocialSystem.Remote {
		private double domesticProduct;
		private long population;
		private double electricityConsumption;
		private double foodConsumption;
		private double waterConsumption;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProduct()
		 */
		@Override
		public double getDomesticProduct() {
			return domesticProduct;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProductPerCapita()
		 */
		@Override
		public double getDomesticProductPerCapita() {
			if(population > 0) {
				return domesticProduct / population;
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumptionPerCapita()
		 */
		@Override
		public double getElectricityConsumptionPerCapita() {
			if(population > 0) {
				return electricityConsumption / population;
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumption()
		 */
		@Override
		public double getFoodConsumption() {
			return foodConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumptionPerCapita()
		 */
		@Override
		public double getFoodConsumptionPerCapita() {
			if(population > 0) {
				return foodConsumption / population;
			}
			return 0;
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
		 * @see edu.mit.sips.core.social.SocialSystem#getWaterConsumptionPerCapita()
		 */
		@Override
		public double getWaterConsumptionPerCapita() {
			if(population > 0) {
				return waterConsumption / population;
			} 
			return 0;
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
