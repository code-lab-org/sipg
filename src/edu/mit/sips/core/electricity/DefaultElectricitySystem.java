
package edu.mit.sips.core.electricity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;


/**
 * The Class DefaultElectricitySystem.
 */
public abstract class DefaultElectricitySystem implements ElectricitySystem {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements ElectricitySystem.Local {
		
		private final double petroleumIntensityOfPrivateProduction;
		private final DomesticProductionModel domesticProductionModel;
		private final PriceModel domesticPriceModel;
		private final List<ElectricityElement> elements = 
				Collections.synchronizedList(new ArrayList<ElectricityElement>());
		
		/**
		 * Instantiates a new default electricity system.
		 */
		public Local() {
			super("Electricity");
			this.petroleumIntensityOfPrivateProduction = 0;
			this.domesticProductionModel = new DefaultDomesticProductionModel();
			this.domesticPriceModel = new DefaultPriceModel();
		}
		
		/**
		 * Instantiates a new default electricity system.
		 *
		 * @param petroleumIntensityOfPrivateProduction the electrical intensity of burning petroleum
		 * @param elements the elements
		 * @param domesticProductionModel the domestic production model
		 * @param domesticPriceModel the domestic price model
		 */
		public Local(double petroleumIntensityOfPrivateProduction,
				Collection<? extends ElectricityElement> elements,
				DomesticProductionModel domesticProductionModel,
				PriceModel domesticPriceModel) {
			super("Electricity");
			
			// Validate electrical intensity of burning petroleum.
			if(petroleumIntensityOfPrivateProduction < 0){ 
				throw new IllegalArgumentException(
						"Electrical intensity of burning petrleum cannot be negative.");
			}
			this.petroleumIntensityOfPrivateProduction = petroleumIntensityOfPrivateProduction;
			
			if(elements != null) {
				this.elements.addAll(elements);
			}

			// Validate domestic production model.
			if(domesticProductionModel == null) {
				throw new IllegalArgumentException(
						"Domestic production model cannot be null.");
			}
			this.domesticProductionModel = domesticProductionModel;
			
			// Validate domestic price model.
			if(domesticPriceModel == null) {
				throw new IllegalArgumentException(
						"Domestic price model cannot be null.");
			}
			this.domesticPriceModel = domesticPriceModel;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#addElement(edu.mit.sips.core.electricity.ElectricityElement)
		 */
		@Override
		public synchronized boolean addElement(ElectricityElement element) {
			return elements.add(element);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return getSociety().getPetroleumSystem().getPetroleumDomesticPrice()
					* getPetroleumConsumptionFromPublicProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return getElectricityDomesticPrice() * getElectricityInDistribution();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return getElectricityDomesticPrice() * (getElectricityOutDistribution() 
					- getElectricityOutDistributionLosses());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return domesticProductionModel.getDomesticProduction(this);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#getElectricalIntensityOfBurningPetroleum()
		 */
		@Override
		public double getPetroleumIntensityOfPrivateProduction() {
			return petroleumIntensityOfPrivateProduction;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem#getElectricityDomesticPrice()
		 */
		@Override
		public double getElectricityDomesticPrice() {
			return domesticPriceModel.getUnitPrice();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.EnergySystem#getEnergyFromBurningPetroleum()
		 */
		@Override
		public double getElectricityFromPrivateProduction() {
			return Math.max(0, getSociety().getTotalElectricityDemand()  
					+ getElectricityOutDistribution()
					- getElectricityInDistribution()
					- getElectricityProduction());
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityInDistribution()
		 */
		@Override
		public double getElectricityInDistribution() {
			double distribution = 0;
			for(ElectricityElement e : getExternalElements()) {
				distribution += e.getElectricityOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistribution()
		 */
		@Override
		public double getElectricityOutDistribution() {
			double distribution = 0;
			for(ElectricityElement e : getInternalElements()) {
				if(!getSociety().getCities().contains(
						getSociety().getCountry().getCity(e.getDestination()))) {
					distribution += e.getElectricityInput();
				}
			}
			return distribution;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistributionLosses()
		 */
		@Override
		public double getElectricityOutDistributionLosses() {
			double distribution = 0;
			for(ElectricityElement e : getInternalElements()) {
				distribution += e.getElectricityInput() - e.getElectricityOutput();
			}
			return distribution;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.EnergySystem#getEnergyProduction()
		 */
		@Override
		public double getElectricityProduction() {
			double energyProduction = 0;
			for(ElectricityElement e : getInternalElements()) {
				energyProduction += e.getElectricityProduction();
			}
			return energyProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
		 */
		@Override
		public TimeUnits getElectricityTimeUnits() {
			return electricityTimeUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
		 */
		@Override
		public ElectricityUnits getElectricityUnits() {
			return electricityUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.EnergySystem#getEnergyWasted()
		 */
		@Override
		public double getElectricityWasted() {
			// Energy is wasted if supply exceeds maximum demand.
			return Math.max(0, getElectricityProduction() 
					+ getElectricityInDistribution()
					- getElectricityOutDistribution()
					- getSociety().getTotalElectricityDemand());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		@Override
		public List<ElectricityElement> getElements() {
			List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
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
		public List<ElectricityElement> getExternalElements() {
			List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
			
			// see if country system is also local
			if(getSociety().getCountry().getElectricitySystem()
					instanceof ElectricitySystem.Local) {
				ElectricitySystem.Local system = (ElectricitySystem.Local)
						getSociety().getCountry().getElectricitySystem();
				for(ElectricityElement element : system.getElements()) {
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
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		@Override
		public List<ElectricityElement> getInternalElements() {
			return Collections.unmodifiableList(elements);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getLocalElectricityFraction()
		 */
		@Override
		public double getLocalElectricityFraction() {
			if(getSociety().getTotalElectricityDemand() > 0) {
				return Math.min(1, (getElectricityProduction() 
						+ getElectricityFromPrivateProduction())
						/ getSociety().getTotalElectricityDemand());
			}
			return 0;
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
		 * @see edu.mit.sips.EnergySystem#getPetroleumConsumed()
		 */
		@Override
		public double getPetroleumConsumption() {
			return getPetroleumConsumptionFromPrivateProduction() 
					+ getPetroleumConsumptionFromPublicProduction();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.ElectricitySystem#getPetroleumBurned()
		 */
		@Override
		public double getPetroleumConsumptionFromPrivateProduction() {
			// Petroleum is burned to meet shortfall in energy demand.
			return getElectricityFromPrivateProduction()
					* getPetroleumIntensityOfPrivateProduction();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#getPetroleumConsumptionFromPublicProduction()
		 */
		@Override
		public double getPetroleumConsumptionFromPublicProduction() {
			double petroleumConsumption = 0;
			for(ElectricityElement e : getInternalElements()) {
				petroleumConsumption += e.getPetroleumConsumption();
			}
			return petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityFraction()
		 */
		@Override
		public double getRenewableElectricityFraction() {
			if(getSociety().getTotalElectricityDemand() > 0) {
				return getRenewableElectricityProduction() / 
						getSociety().getTotalElectricityDemand();
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.ElectricitySystem#getRenewableEnergyProduction()
		 */
		@Override
		public double getRenewableElectricityProduction() {
			double production = 0;
			for(ElectricityElement element : getInternalElements()) {
				if(element.isRenewableElectricity()) {
					production += element.getElectricityProduction();
				}
			}
			return production;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return getElectricityDomesticPrice() * (getSociety().getTotalElectricityDemand() 
					- getElectricityFromPrivateProduction());
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.EnergySystem#getNetEnergySupply()
		 */
		@Override
		public double getTotalElectricitySupply() {
			return getElectricityProduction() 
					+ getElectricityInDistribution()
					- getElectricityOutDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitProductionCost()
		 */
		@Override
		public double getUnitProductionCost() {
			if(getElectricityProduction() > 0) {
				return (getLifecycleExpense() + getConsumptionExpense()) 
						/ getElectricityProduction();
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitSupplyCost()
		 */
		@Override
		public double getUnitSupplyProfit() {
			if(getTotalElectricitySupply() > 0) {
				return getCashFlow() / getTotalElectricitySupply();
			}
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.EnergySystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			double waterConsumption = 0;
			for(ElectricityElement e : getInternalElements()) {
				waterConsumption += e.getWaterConsumption();
			}
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
		 */
		@Override
		public TimeUnits getWaterTimeUnits() {
			return waterTimeUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
		 */
		@Override
		public WaterUnits getWaterUnits() {
			return waterUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) { }

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#removeElement(edu.mit.sips.core.electricity.ElectricityElement)
		 */
		@Override
		public synchronized boolean removeElement(ElectricityElement element) {
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
	public static class Remote extends DefaultInfrastructureSystem.Remote implements ElectricitySystem.Remote {		
		private double waterConsumption;
		private double petroleumConsumption;
		private double domesticPrice;
		
		/**
		 * Instantiates a new remote.
		 */
		public Remote() {
			setName("Electricity");
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem#getElectricityDomesticPrice()
		 */
		@Override
		public double getElectricityDomesticPrice() {
			return domesticPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
		 */
		@Override
		public TimeUnits getElectricityTimeUnits() {
			return electricityTimeUnits;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
		 */
		@Override
		public ElectricityUnits getElectricityUnits() {
			return electricityUnits;
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
		 * @see edu.mit.sips.core.electricity.ElectricitySystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			return petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
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
			return waterTimeUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
		 */
		@Override
		public WaterUnits getWaterUnits() {
			return waterUnits;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Remote#setElectricityDomesticPrice(double)
		 */
		@Override
		public void setElectricityDomesticPrice(double domesticPrice) {
			this.domesticPrice = domesticPrice;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.electricity.ElectricitySystem.Remote#setPetroleumConsumption(double)
		 */
		@Override
		public void setPetroleumConsumption(double petroleumConsumption) {
			this.petroleumConsumption = petroleumConsumption;
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
