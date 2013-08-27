package edu.mit.sips.core.water;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;

/**
 * The Class DefaultAgricultureSystem.
 */
public abstract class DefaultWaterSystem implements WaterSystem {

	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements WaterSystem.Local {
		private final List<WaterElement> elements = 
				Collections.synchronizedList(new ArrayList<WaterElement>());
		private final DomesticProductionModel domesticProductionModel;
		private final PriceModel domesticPriceModel, importPriceModel;
		private final double maxWaterReservoirVolume;
		private final double initialWaterReservoirVolume;
		private final double waterReservoirRechargeRate;
		private final boolean coastal; // TODO use for desalination elements

		private double waterReservoirVolume;
		private transient double nextWaterReservoirVolume;
		
		/**
		 * Instantiates a new city agriculture system.
		 */
		protected Local() {
			super("Water");
			domesticProductionModel = new DefaultDomesticProductionModel();
			domesticProductionModel.setInfrastructureSystem(this);
			this.domesticPriceModel = new DefaultPriceModel();
			this.importPriceModel = new DefaultPriceModel();
			this.maxWaterReservoirVolume = 0;
			this.initialWaterReservoirVolume = 0;
			this.waterReservoirRechargeRate = 0;
			this.coastal = false;
		}
		
		/**
		 * Instantiates a new city water system.
		 *
		 * @param coastal the coastal
		 * @param maxWaterReservoirVolume the max water reservoir volume
		 * @param initialWaterReservoirVolume the initial water reservoir volume
		 * @param waterReservoirRechargeRate the water reservoir recharge rate
		 * @param elements the elements
		 * @param domesticPriceModel the domestic price model
		 * @param importPriceModel the import price model
		 */
		public Local(boolean coastal, double maxWaterReservoirVolume,
				double initialWaterReservoirVolume, 
				double waterReservoirRechargeRate,
				Collection<? extends WaterElement> elements,
				DomesticProductionModel domesticProductionModel,
				PriceModel domesticPriceModel, 
				PriceModel importPriceModel) {
			super("Water");
			// Validate max water reservoir volume.
			if(maxWaterReservoirVolume < 0) {
				throw new IllegalArgumentException(
						"Max water reservoir volume cannot be negative.");
			}
			this.maxWaterReservoirVolume = maxWaterReservoirVolume;

			// Validate initial water reservoir volume.
			if(initialWaterReservoirVolume < 0) {
				throw new IllegalArgumentException(
						"Initial water reservoir volume cannot be negative.");
			} else if(initialWaterReservoirVolume > maxWaterReservoirVolume) {
				throw new IllegalArgumentException(
						"Initial water reservoir volume cannot exceed maximum.");
			}
			this.initialWaterReservoirVolume = initialWaterReservoirVolume;

			// No need to validate recharge rate.
			this.waterReservoirRechargeRate = waterReservoirRechargeRate;

			// No need to validate coastal.
			this.coastal = coastal;
			
			if(elements != null) {
				this.elements.addAll(elements);
			}

			// Validate domestic production model.
			if(domesticProductionModel == null) {
				throw new IllegalArgumentException(
						"Domestic production model cannot be null.");
			}
			this.domesticProductionModel = domesticProductionModel;
			domesticProductionModel.setInfrastructureSystem(this);
			
			// Validate domestic price model.
			if(domesticPriceModel == null) {
				throw new IllegalArgumentException(
						"Domestic price model cannot be null.");
			}
			this.domesticPriceModel = domesticPriceModel;
			
			// Validate import price model.
			if(importPriceModel == null) {
				throw new IllegalArgumentException(
						"Import price model cannot be null.");
			}
			this.importPriceModel = importPriceModel;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#addElement(edu.mit.sips.core.water.WaterElement)
		 */
		@Override
		public synchronized boolean addElement(WaterElement element) {
			return elements.add(element);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return getSociety().getElectricitySystem().getElectricityDomesticPrice()
					* getElectricityConsumption();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return getWaterDomesticPrice() * getWaterInDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getWaterDomesticPrice() * (getWaterOutDistribution() 
					- getWaterOutDistributionLosses());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return domesticProductionModel.getDomesticProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getEnergyConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			double energyConsumption = 0;
			for(WaterElement e : getInternalElements()) {
				energyConsumption += e.getElectricityConsumption();
			}
			return energyConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<WaterElement> getElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
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
		public List<WaterElement> getExternalElements() {
			List<WaterElement> elements = new ArrayList<WaterElement>();
			
			// see if country system is also local
			if(getSociety().getCountry().getWaterSystem() 
					instanceof WaterSystem.Local) {
				WaterSystem.Local system = (WaterSystem.Local)
						getSociety().getCountry().getWaterSystem();
				for(WaterElement element : system.getElements()) {
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
		 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return getWaterImportPrice() * getWaterImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<WaterElement> getInternalElements() {
			return Collections.unmodifiableList(elements);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getLocalWaterFraction()
		 */
		@Override
		public double getLocalWaterFraction() {
			if(getSociety().getTotalWaterDemand() > 0) {
				return Math.min(1, (getWaterProduction() + getWaterFromArtesianWell())
						/ getSociety().getTotalWaterDemand());
			} 
			return 0;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getMaxWaterReservoirVolume()
		 */
		@Override
		public double getMaxWaterReservoirVolume() {
			return maxWaterReservoirVolume;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getRenewableWaterFraction()
		 */
		@Override
		public double getRenewableWaterFraction() {
			if(getSociety().getTotalWaterDemand() > 0) {
				return getRenewableWaterProduction() 
						/ getSociety().getTotalWaterDemand();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getRenewableWaterProduction()
		 */
		@Override
		public double getRenewableWaterProduction() {
			double renewableProduction = 0;
			for(WaterElement e : getInternalElements()) {
				if(e.getReservoirIntensityOfWaterProduction() < 1) {
					renewableProduction += e.getWaterProduction() 
							* (1 - e.getReservoirIntensityOfWaterProduction());
				}
			}
			renewableProduction += Math.min(getWaterReservoirRechargeRate(), 
					getWaterProduction() + getWaterFromArtesianWell() 
					- renewableProduction);
			return renewableProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterWithdrawals()
		 */
		@Override
		public double getReservoirWaterWithdrawals() {
			double waterWithdrawals = 0;
			for(WaterElement e : getInternalElements()) {
				waterWithdrawals += e.getWaterWithdrawals();
			}
			return waterWithdrawals;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getWaterDomesticPrice() * (getSociety().getTotalWaterDemand() 
					- getWaterFromArtesianWell());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getTotalWaterSupply()
		 */
		public double getTotalWaterSupply() {
			return getWaterProduction() 
					+ getWaterInDistribution()
					- getWaterOutDistribution()
					+ getWaterImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getWaterProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getWaterProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getSupplyCost()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalWaterSupply() > 0) {
				return getCashFlow() / getTotalWaterSupply();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
		 */
		@Override
		public double getWaterDomesticPrice() {
			return domesticPriceModel.getUnitPrice();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterFromArtesianWell()
		 */
		@Override
		public double getWaterFromArtesianWell() {
			// Artesian water used to meet shortfall in reaching minimum demand.
			return Math.min(getWaterReservoirVolume() - getReservoirWaterWithdrawals(), 
					Math.max(0, getSociety().getTotalWaterDemand()
							+ getWaterOutDistribution()
							- getWaterInDistribution()
							- getWaterProduction()));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterImport()
		 */
		@Override
		public double getWaterImport() {
			// Water is imported to meet shortfall in reaching minimum demand.
			// Note that water cannot be exported, and is wasted if excess.
			return Math.max(0, getSociety().getTotalWaterDemand()
					+ getWaterOutDistribution()
					- getWaterInDistribution()
					- getWaterProduction()
					- getWaterFromArtesianWell());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
		 */
		@Override
		public double getWaterImportPrice() {
			return importPriceModel.getUnitPrice();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.WaterSystem#getWaterInDistribution()
		 */
		@Override
		public double getWaterInDistribution() {
			double distribution = 0;
			for(WaterElement e : getExternalElements()) {
				distribution += e.getWaterOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.WaterSystem#getWaterOutDistribution()
		 */
		@Override
		public double getWaterOutDistribution() {
			double distribution = 0;
			for(WaterElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					distribution += e.getWaterInput();
				}
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.WaterSystem#getWaterOutDistributionLosses()
		 */
		@Override
		public double getWaterOutDistributionLosses() {
			double distribution = 0;
			for(WaterElement e : getInternalElements()) {
				distribution += e.getWaterInput() - e.getWaterOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterProduction()
		 */
		@Override
		public double getWaterProduction() {
			double waterProduction = 0;
			for(WaterElement e : getInternalElements()) {
				waterProduction += e.getWaterProduction();
			}
			return waterProduction;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirRechargeRate()
		 */
		@Override
		public double getWaterReservoirRechargeRate() {
			return waterReservoirRechargeRate;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterReservoirVolume()
		 */
		@Override
		public double getWaterReservoirVolume() {
			return waterReservoirVolume;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.WaterSystem#getWaterWasted()
		 */
		@Override
		public double getWaterWasted() {
			// Water is wasted if supply exceeds maximum demand.
			return Math.max(0, getWaterProduction() 
					+ getWaterFromArtesianWell()
					+ getWaterInDistribution()
					- getWaterOutDistribution()
					- getSociety().getTotalWaterDemand());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) {
			waterReservoirVolume = initialWaterReservoirVolume;
		}

		/**
		 * Checks if is coastal.
		 *
		 * @return true, if is coastal
		 */
		public boolean isCoastal() {
			return coastal;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Local#removeElement(edu.mit.sips.core.water.WaterElement)
		 */
		@Override
		public synchronized boolean removeElement(WaterElement element) {
			return elements.remove(element);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		@Override
		public void tick() {
			nextWaterReservoirVolume = Math.min(maxWaterReservoirVolume, 
					waterReservoirVolume + waterReservoirRechargeRate 
					- getReservoirWaterWithdrawals() 
					- getWaterFromArtesianWell());
			if(nextWaterReservoirVolume < 0) {
				throw new IllegalStateException(
						"Water reservoir volume cannot be negative.");
			}
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		@Override
		public void tock() {
			waterReservoirVolume = nextWaterReservoirVolume;
		}
	}

	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements WaterSystem.Remote {
		private double electricityConsumption;
		private double domesticPrice, importPrice;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
		 */
		public double getWaterDomesticPrice() {
			return domesticPrice;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
		 */
		public double getWaterImportPrice() {
			return importPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Remote#setWaterDomesticPrice(double)
		 */
		public void setWaterDomesticPrice(double domesticPrice) {
			this.domesticPrice = domesticPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.water.WaterSystem.Remote#setWaterImportPrice(double)
		 */
		public void setWaterImportPrice(double importPrice) {
			this.importPrice = importPrice;
		}
	}
}