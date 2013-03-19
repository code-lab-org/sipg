package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;

/**
 * The Class DefaultSocialSystem.
 */
public abstract class DefaultSocialSystem implements SocialSystem {

	/**
	 * The Class Local.
	 */
	public static abstract class Local extends DefaultInfrastructureSystem.Local implements SocialSystem.Local {
		
		/**
		 * Instantiates a new local.
		 *
		 * @param name the name
		 */
		public Local() {
			super("Society");
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
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return getSociety().getGlobals().getEconomicIntensityOfElectricityConsumption() 
					* getElectricityConsumption()
					+ getExportRevenue() - getImportExpense();
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
			if(getPopulation() > 0) {
				return getSociety().getGlobals().getElectricityDemand(
						getDomesticProduct()/getPopulation());
			} 
			return getSociety().getGlobals().getElectricityDemand(0);
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
			if(getPopulation() > 0) {
				return getSociety().getGlobals().getFoodDemand(
						getDomesticProduct()/getPopulation());
			}
			return getSociety().getGlobals().getFoodDemand(0);
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
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) {
			fireAttributeChanges();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tick()
		 */
		@Override
		public void tick() {
			
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tock()
		 */
		@Override
		public void tock() {
			fireAttributeChanges();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#fireAttributeChanges()
		 */
		@Override
		public void fireAttributeChanges() {
			fireAttributeChangeEvent(WATER_CONSUMPTION_ATTRIBUTE);
			fireAttributeChangeEvent(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
			fireAttributeChangeEvent(FOOD_CONSUMPTION_ATTRIBUTE);
			fireAttributeChangeEvent(POPULATION_ATTRIBUTE);
			fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
			fireAttributeChangeEvent(DOMESTIC_PRODUCT_ATTRIBUTE);
			fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#fireAttributeChanges(edu.mit.sips.core.InfrastructureElement)
		 */
		@Override
		public void fireAttributeChanges(InfrastructureElement element) {
			Set<Society> affectedSocieties = new HashSet<Society>();
			affectedSocieties.addAll(getAffectedSocietiesRecursive(
					getSociety().getCountry().getSociety(element.getOrigin())));
			affectedSocieties.addAll(getAffectedSocietiesRecursive(
					getSociety().getCountry().getSociety(element.getDestination())));
			
			for(Society society : affectedSocieties) {
				if(society.getSocialSystem() instanceof SocialSystem.Local) {
					((SocialSystem.Local)society.getSocialSystem()).fireAttributeChanges();
				}
			}
		}
		
		/**
		 * Gets the affected societies recursive.
		 *
		 * @param society the society
		 * @return the affected societies recursive
		 */
		private static Set<Society> getAffectedSocietiesRecursive(Society society) {
			Set<Society> affectedSocieties = new HashSet<Society>();
			affectedSocieties.add(society);
			if(society.getSociety() != null) {
				affectedSocieties.addAll(getAffectedSocietiesRecursive(society.getSociety()));
			}
			return affectedSocieties;
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
			fireAttributeChangeEvent(DOMESTIC_PRODUCT_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
			fireAttributeChangeEvent(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getFoodConsumption(double)
		 */
		@Override
		public void setFoodConsumption(double foodConsumption) {
			this.foodConsumption = foodConsumption;
			fireAttributeChangeEvent(FOOD_CONSUMPTION_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#setPopulation(long)
		 */
		@Override
		public void setPopulation(long population) {
			this.population = population;
			fireAttributeChangeEvent(POPULATION_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.social.SocialSystem.Remote#getWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
			fireAttributeChangeEvent(WATER_CONSUMPTION_ATTRIBUTE);
		}
	}
}
