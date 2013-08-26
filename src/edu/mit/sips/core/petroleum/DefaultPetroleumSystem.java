package edu.mit.sips.core.petroleum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;

/**
 * The Class DefaultPetroleumSystem.
 */
public abstract class DefaultPetroleumSystem extends DefaultInfrastructureSystem implements PetroleumSystem {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements PetroleumSystem.Local {
		private final PriceModel domesticPriceModel, importPriceModel, exportPriceModel;
		private final List<PetroleumElement> elements = 
				Collections.synchronizedList(new ArrayList<PetroleumElement>());
		private final double maxPetroleumReservoirVolume;
		private final double initialPetroleumReservoirVolume;
		private double petroleumReservoirVolume;
		private transient double nextPetroleumReservoirVolume;
		
		/**
		 * Instantiates a new default petroleum system.
		 *
		 */
		public Local() {
			super("Petroleum");
			this.domesticPriceModel = new DefaultPriceModel();
			this.importPriceModel = new DefaultPriceModel();
			this.exportPriceModel = new DefaultPriceModel();
			this.maxPetroleumReservoirVolume = 0;
			this.initialPetroleumReservoirVolume = 0;
		}
		
		/**
		 * Instantiates a new default petroleum system.
		 *
		 * @param maxPetroleumReservoirVolume the max petroleum reservoir volume
		 * @param initialPetroleumReservoirVolume the initial petroleum reservoir volume
		 * @param elements the elements
		 * @param domesticPriceModel the domestic price model
		 * @param importPriceModel the import price model
		 * @param exportPriceModel the export price model
		 */
		public Local(double maxPetroleumReservoirVolume,
				double initialPetroleumReservoirVolume,
				Collection<? extends PetroleumElement> elements,
				PriceModel domesticPriceModel, 
				PriceModel importPriceModel, 
				PriceModel exportPriceModel) {
			super("Petroleum");
			
			// Validate max petroleum reservoir.
			if(maxPetroleumReservoirVolume < 0) {
				throw new IllegalArgumentException(
						"Max petroleum reservoir volume cannot be negative.");
			}
			this.maxPetroleumReservoirVolume = maxPetroleumReservoirVolume;
			
			// Validate initial petroleum reservoir.
			if(initialPetroleumReservoirVolume < 0) {
				throw new IllegalArgumentException(
						"Initial petroleum reservoir volume cannot be negative.");
			} else if(initialPetroleumReservoirVolume > maxPetroleumReservoirVolume) {
				throw new IllegalArgumentException(
						"Initial petroleum reservoir volume cannot exceed maximum.");
			}
			this.initialPetroleumReservoirVolume = initialPetroleumReservoirVolume;
			
			if(elements != null) {
				this.elements.addAll(elements);
			}
			
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
			
			// Validate export price model.
			if(exportPriceModel == null) {
				throw new IllegalArgumentException(
						"Export price model cannot be null.");
			}
			this.exportPriceModel = exportPriceModel;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Local#addElement(edu.mit.sips.core.petroleum.PetroleumElement)
		 */
		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		@Override
		public synchronized boolean addElement(PetroleumElement element) {
			return elements.add(element);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		/**
		 * Gets the consumption expense.
		 *
		 * @return the consumption expense
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
			return getPetroleumDomesticPrice() * getPetroleumInDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getPetroleumDomesticPrice() * (getPetroleumOutDistribution()
					- getPetroleumOutDistributionLosses());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			// add private consumption to base domestic production
			return super.getDomesticProduction() 
					+ getSociety().getGlobals().getPrivateConsumptionFromPetroleumProduction()
					* getPetroleumProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getElectricityConsumption()
		 */
		/**
		 * Gets the electricity consumption.
		 *
		 * @return the electricity consumption
		 */
		@Override
		public double getElectricityConsumption() {
			double consumption = 0;
			for(PetroleumElement e : getInternalElements()) {
				consumption += e.getElectricityConsumption();
			}
			return consumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		/**
		 * Gets the elements.
		 *
		 * @return the elements
		 */
		@Override
		public List<PetroleumElement> getElements() {
			List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getTradeRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return getPetroleumExportPrice() * getPetroleumExport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		/**
		 * Gets the external elements.
		 *
		 * @return the external elements
		 */
		@Override
		public List<PetroleumElement> getExternalElements() {
			List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
			
			// see if country system is also local
			if(getSociety().getCountry().getPetroleumSystem()
					instanceof PetroleumSystem.Local) {
				PetroleumSystem.Local system = (PetroleumSystem.Local)
						getSociety().getCountry().getPetroleumSystem();
				for(PetroleumElement element : system.getElements()) {
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
			return getPetroleumImportPrice() * getPetroleumImport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<PetroleumElement> getInternalElements() {
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Local#getLocalPetroleumFraction()
		 */
		@Override
		public double getLocalPetroleumFraction() {
			if(getSociety().getTotalPetroleumDemand() > 0) {
				return Math.min(1, getPetroleumProduction() 
						/ getSociety().getTotalPetroleumDemand());
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getMaxPetroleumReservoirVolume()
		 */
		/**
		 * Gets the max petroleum reservoir volume.
		 *
		 * @return the max petroleum reservoir volume
		 */
		@Override
		public double getMaxPetroleumReservoirVolume() {
			return maxPetroleumReservoirVolume;
		}

		/**
		 * Gets the petroleum domestic price.
		 *
		 * @return the petroleum domestic price
		 */
		@Override
		public double getPetroleumDomesticPrice() {
			return domesticPriceModel.getUnitPrice();
		}			

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getPetroleumExport()
		 */
		/**
		 * Gets the petroleum export.
		 *
		 * @return the petroleum export
		 */
		@Override
		public double getPetroleumExport() {
			return Math.max(0, getPetroleumProduction() 
					+ getPetroleumInDistribution()
					- getPetroleumOutDistribution()
					- getSociety().getTotalPetroleumDemand());
		}

		/**
		 * Gets the petroleum export price.
		 *
		 * @return the petroleum export price
		 */
		@Override
		public double getPetroleumExportPrice() {
			return exportPriceModel.getUnitPrice();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getPetroleumImport()
		 */
		/**
		 * Gets the petroleum import.
		 *
		 * @return the petroleum import
		 */
		@Override
		public double getPetroleumImport() {
			return Math.max(0, getSociety().getTotalPetroleumDemand()
					+ getPetroleumOutDistribution()
					- getPetroleumInDistribution()
					- getPetroleumProduction());
		}
		
		/**
		 * Gets the petroleum import price.
		 *
		 * @return the petroleum import price
		 */
		@Override
		public double getPetroleumImportPrice() {
			return importPriceModel.getUnitPrice();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumInDistribution()
		 */
		/**
		 * Gets the petroleum in distribution.
		 *
		 * @return the petroleum in distribution
		 */
		@Override
		public double getPetroleumInDistribution() {
			double distribution = 0;
			for(PetroleumElement e : getExternalElements()) {
				distribution += e.getPetroleumOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistribution()
		 */
		/**
		 * Gets the petroleum out distribution.
		 *
		 * @return the petroleum out distribution
		 */
		@Override
		public double getPetroleumOutDistribution() {
			double distribution = 0;
			for(PetroleumElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					distribution += e.getPetroleumInput();
				}
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistributionLosses()
		 */
		/**
		 * Gets the petroleum out distribution losses.
		 *
		 * @return the petroleum out distribution losses
		 */
		@Override
		public double getPetroleumOutDistributionLosses() {
			double distribution = 0;
			for(PetroleumElement e : getInternalElements()) {
				distribution += e.getPetroleumInput() - e.getPetroleumOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getPetroleumProduction()
		 */
		/**
		 * Gets the petroleum production.
		 *
		 * @return the petroleum production
		 */
		@Override
		public double getPetroleumProduction() {
			double petroleumProduction = 0;
			for(PetroleumElement e : getInternalElements()) {
				petroleumProduction += e.getPetroleumProduction();
			}
			return petroleumProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getPetroleumReservoirVolume()
		 */
		/**
		 * Gets the petroleum reservoir volume.
		 *
		 * @return the petroleum reservoir volume
		 */
		@Override
		public double getPetroleumReservoirVolume() {
			return petroleumReservoirVolume;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getPetroleumWithdrawals()
		 */
		/**
		 * Gets the petroleum withdrawals.
		 *
		 * @return the petroleum withdrawals
		 */
		@Override
		public double getPetroleumWithdrawals() {
			double petroleumWithdrawals = 0;
			for(PetroleumElement e : getInternalElements()) {
				petroleumWithdrawals += e.getPetroleumWithdrawals();
			}
			return petroleumWithdrawals;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getPetroleumDomesticPrice() * getSociety().getTotalPetroleumDemand();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.PetroleumSystem#getTotalPetroleumSupply()
		 */
		@Override
		public double getTotalPetroleumSupply() {
			return getPetroleumProduction() 
					+ getPetroleumInDistribution()
					- getPetroleumOutDistribution()
					+ getPetroleumImport() 
					- getPetroleumExport();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.PetroleumSystem#getProductionCost()
		 */
		/**
		 * Gets the unit production cost.
		 *
		 * @return the unit production cost
		 */
		@Override
		public double getUnitProductionCost() {
			if(getPetroleumProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getPetroleumProduction();
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.PetroleumSystem#getSupplyCost()
		 */
		/**
		 * Gets the unit supply profit.
		 *
		 * @return the unit supply profit
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalPetroleumSupply() > 0) {
				return getCashFlow() / getTotalPetroleumSupply();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#initialize(long)
		 */
		/**
		 * Initialize.
		 *
		 * @param time the time
		 */
		@Override
		public void initialize(long time) {
			petroleumReservoirVolume = initialPetroleumReservoirVolume;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Local#removeElement(edu.mit.sips.core.petroleum.PetroleumElement)
		 */
		/**
		 * Removes the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		@Override
		public synchronized boolean removeElement(PetroleumElement element) {
			return elements.remove(element);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tick()
		 */
		/**
		 * Tick.
		 */
		@Override
		public void tick() {
			nextPetroleumReservoirVolume = Math.min(maxPetroleumReservoirVolume, 
					petroleumReservoirVolume - getPetroleumWithdrawals());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.SimEntity#tock()
		 */
		/**
		 * Tock.
		 */
		@Override
		public void tock() {
			petroleumReservoirVolume = nextPetroleumReservoirVolume;
		}
	}

	/**
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements PetroleumSystem.Remote {
		private double electricityConsumption;
		private double domesticPrice, importPrice, exportPrice;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumDomesticPrice()
		 */
		@Override
		public double getPetroleumDomesticPrice() {
			return domesticPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumExportPrice()
		 */
		@Override
		public double getPetroleumExportPrice() {
			return exportPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumImportPrice()
		 */
		@Override
		public double getPetroleumImportPrice() {
			return importPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Remote#setPetroleumDomesticPrice(double)
		 */
		@Override
		public void setPetroleumDomesticPrice(double domesticPrice) {
			this.domesticPrice = domesticPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Remote#setPetroleumExportPrice(double)
		 */
		@Override
		public void setPetroleumExportPrice(double exportPrice) {
			this.exportPrice = exportPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Remote#setPetroleumImportPrice(double)
		 */
		@Override
		public void setPetroleumImportPrice(double importPrice) {
			this.importPrice = importPrice;
		}	
	}
}
