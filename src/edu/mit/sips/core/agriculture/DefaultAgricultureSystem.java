package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSystem;

/**
 * The Class DefaultAgricultureSystem.
 */
public abstract class DefaultAgricultureSystem implements AgricultureSystem {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements AgricultureSystem.Local {
		private final double arableLandArea;	
		private final List<AgricultureElement> elements = 
				Collections.synchronizedList(new ArrayList<AgricultureElement>());
		
		/**
		 * Instantiates a new local.
		 */
		public Local() {
			super("Agriculture");
			this.arableLandArea = 0;
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param arableLandArea the arable land area
		 */
		public Local(double arableLandArea) {
			super("Agriculture");
			
			// Validate arable land area.
			if(arableLandArea < 0) {
				throw new IllegalArgumentException(
						"Arable land area cannot be negative.");
			}
			this.arableLandArea = arableLandArea;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#addElement(edu.mit.sips.core.agriculture.AgricultureElement)
		 */
		@Override
		public synchronized boolean addElement(AgricultureElement element) {
			return elements.add(element);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getArableLandArea()
		 */
		@Override
		public double getArableLandArea() {
			return arableLandArea;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return getSociety().getGlobals().getWaterDomesticPrice()
					* getWaterConsumption();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return getSociety().getGlobals().getFoodDomesticPrice()
					* getFoodInDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getSociety().getGlobals().getFoodDomesticPrice()
					* (getFoodOutDistribution() - getFoodOutDistributionLosses());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			// add private consumption to base domestic production
			return super.getDomesticProduction() 
					+ getSociety().getGlobals().getPrivateConsumptionFromFoodProduction()
					* getFoodProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<AgricultureElement> getElements() {
			List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return getSociety().getGlobals().getFoodExportPrice() * getFoodExport();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		@Override
		public List<AgricultureElement> getExternalElements() {
			List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
			
			// see if country system is also local
			if(getSociety().getCountry().getAgricultureSystem() 
					instanceof AgricultureSystem.Local) {
				AgricultureSystem.Local system = (AgricultureSystem.Local)
						getSociety().getCountry().getAgricultureSystem();
				for(AgricultureElement element : system.getElements()) {
					City origin = getSociety().getCountry().getCity(
							element.getOrigin());
					City dest = getSociety().getCountry().getCity(
							element.getDestination());
					// add element if origin is outside this society but 
					// destination is within this society
					if(!getSociety().getCities().contains(origin)
							&& getSociety().getCities().contains(dest)) {
						elements.add(element);
					}
				}
			}
			
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodExport()
		 */
		@Override
		public double getFoodExport() {
			return Math.max(0, getFoodProduction() 
					+ getFoodInDistribution()
					- getFoodOutDistribution()
					- getSociety().getTotalFoodDemand());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodImport()
		 */
		@Override
		public double getFoodImport() {
			return Math.max(0, getSociety().getTotalFoodDemand() 
					+ getFoodOutDistribution() 
					- getFoodInDistribution()
					- getFoodProduction());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodInDistribution()
		 */
		@Override
		public double getFoodInDistribution() {
			double distribution = 0;
			for(AgricultureElement e : getExternalElements()) {
				distribution += e.getFoodOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodOutDistribution()
		 */
		@Override
		public double getFoodOutDistribution() {
			double distribution = 0;
			for(AgricultureElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					distribution += e.getFoodInput();
				}
			}
			return distribution;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getFoodOutDistributionLosses()
		 */
		@Override
		public double getFoodOutDistributionLosses() {
			double distribution = 0;
			for(AgricultureElement e : getInternalElements()) {
				distribution += e.getFoodInput() - e.getFoodOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getFoodProduction()
		 */
		@Override
		public double getFoodProduction() {
			double foodProduction = 0;
			for(AgricultureElement e : getInternalElements()) {
				foodProduction += e.getFoodProduction();
			}
			return foodProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return getSociety().getGlobals().getFoodImportPrice() * getFoodImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<AgricultureElement> getInternalElements() {
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getLandAreaUsed()
		 */
		@Override
		public double getLandAreaUsed() {
			double landAreaUsed = 0;
			for(AgricultureElement e : getInternalElements()) {
				landAreaUsed += e.getLandArea();
			}
			return landAreaUsed;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLocalFoodFraction()
		 */
		@Override
		public double getLocalFoodFraction() {
			if(getSociety().getTotalFoodDemand() > 0) {
				return Math.min(1, getFoodProduction() 
						/ getSociety().getTotalFoodDemand());
			} 
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getLocalFoodSupply()
		 */
		@Override
		public double getLocalFoodSupply() {
			return getFoodProduction() 
					+ getFoodInDistribution() 
					- getFoodOutDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getSociety().getGlobals().getFoodDomesticPrice() 
					* getSociety().getTotalFoodDemand();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getTotalFoodSupply()
		 */
		@Override
		public double getTotalFoodSupply() {
			return getLocalFoodSupply() + getFoodImport() - getFoodExport();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getUnitProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getFoodProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getFoodProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#geUnitSupplyCost()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalFoodSupply() > 0) {
				return getCashFlow() / getTotalFoodSupply();
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.AgricultureSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			double waterConsumption = 0;
			for(AgricultureElement e : getInternalElements()) {
				waterConsumption += e.getWaterConsumption();
			}
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) { }

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#removeElement(edu.mit.sips.core.agriculture.AgricultureElement)
		 */
		@Override
		public synchronized boolean removeElement(AgricultureElement element) {
			return elements.remove(element);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tick()
		 */
		@Override
		public void tick() { }
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tock()
		 */
		@Override
		public void tock() { }
	}
	
	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements AgricultureSystem.Remote {
		private double waterConsumption;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Remote#setWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
		}
	}
}
