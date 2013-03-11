package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class DefaultSocialSystem.
 */
public abstract class DefaultSocialSystem implements SocialSystem {

	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements SocialSystem.Local {
		private final PopulationModel populationModel;
		private double domesticProduct, nextDomesticProduct;
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			this.populationModel = new DefaultPopulationModel();
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param populationModel the population model
		 */
		public Local(PopulationModel populationModel) {
			super("Society");
			
			// Validate population model.
			if(populationModel == null) {
				throw new IllegalArgumentException(
						"Population model cannot be null.");
			}
			this.populationModel = populationModel;
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
			return getSociety().getGlobals().getEconomicIntensityOfElectricityConsumption() 
					* getElectricityConsumption()
					+ getExportRevenue() - getImportExpense();
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
			return getSociety().getGlobals().getElectricityDemand(
					getDomesticProduct()/getPopulation());
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
			return getSociety().getGlobals().getFoodDemand(
					getDomesticProduct()/getPopulation());
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
		public List<InfrastructureElement> getInternalElements() {
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
			// note: initialize domestic product LAST as 
			// it depends on other initial values
			domesticProduct = getNextEconomicProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		@Override
		public void tick() {
			nextDomesticProduct = getNextEconomicProduction();
			populationModel.tick();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tock()
		 */
		@Override
		public void tock() {
			domesticProduct = nextDomesticProduct;
			populationModel.tock();
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
		 * @see edu.mit.sips.core.social.SocialSystem#getPopulation()
		 */
		@Override
		public long getPopulation() {
			return population;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumption()
		 */
		@Override
		public double getFoodConsumption() {
			return foodConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setDomesticProduct(double)
		 */
		@Override
		public void setDomesticProduct(double domesticProduct) {
			this.domesticProduct = domesticProduct;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setPopulation(long)
		 */
		@Override
		public void setPopulation(long population) {
			this.population = population;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getFoodConsumption(double)
		 */
		@Override
		public void getFoodConsumption(double foodConsumption) {
			this.foodConsumption = foodConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getWaterConsumption(double)
		 */
		@Override
		public void getWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
		}
	}
}
