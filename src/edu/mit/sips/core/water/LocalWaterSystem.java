package edu.mit.sips.core.water;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.LocalInfrastructureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

public class LocalWaterSystem extends LocalInfrastructureSystem implements WaterSystem.Local {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;

	private final List<WaterElement> elements = 
			Collections.synchronizedList(new ArrayList<WaterElement>());
	private final DomesticProductionModel domesticProductionModel;
	private final PriceModel domesticPriceModel, importPriceModel;
	private final double maxWaterReservoirVolume;
	private final double initialWaterReservoirVolume;
	private final double waterReservoirRechargeRate;
	private final boolean coastalAccess;
	private final double electricalIntensityOfPrivateProduction;
	private final double reservoirIntensityOfPrivateProduction;

	private double waterReservoirVolume;
	private transient double nextWaterReservoirVolume;

	/**
	 * Instantiates a new local.
	 */
	protected LocalWaterSystem() {
		super("Water");
		domesticProductionModel = new DefaultDomesticProductionModel();
		this.domesticPriceModel = new DefaultPriceModel();
		this.importPriceModel = new DefaultPriceModel();
		this.maxWaterReservoirVolume = 0;
		this.initialWaterReservoirVolume = 0;
		this.waterReservoirRechargeRate = 0;
		this.coastalAccess = false;
		this.electricalIntensityOfPrivateProduction = 0;
		this.reservoirIntensityOfPrivateProduction = 1;
	}

	/**
	 * Instantiates a new local.
	 *
	 * @param coastalAccess the coastal access
	 * @param maxWaterReservoirVolume the max water reservoir volume
	 * @param initialWaterReservoirVolume the initial water reservoir volume
	 * @param waterReservoirRechargeRate the water reservoir recharge rate
	 * @param electricalIntensityOfProduction the electrical intensity of production
	 * @param aquiferIntensityOfProduction the aquifer intensity of production
	 * @param elements the elements
	 * @param domesticProductionModel the domestic production model
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 */
	public LocalWaterSystem(boolean coastalAccess, double maxWaterReservoirVolume,
			double initialWaterReservoirVolume, 
			double waterReservoirRechargeRate,
			double electricalIntensityOfProduction, 
			double aquiferIntensityOfProduction,
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

		// Validate electrical intensity of production.
		if(electricalIntensityOfProduction < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity of production cannot be negative.");
		}
		this.electricalIntensityOfPrivateProduction = electricalIntensityOfProduction;

		// Validate aquifer intensity of production.
		if(aquiferIntensityOfProduction < 0) {
			throw new IllegalArgumentException(
					"Aquifer intensity of production cannot be negative.");
		}
		this.reservoirIntensityOfPrivateProduction = aquiferIntensityOfProduction;

		// No need to validate coastal access.
		this.coastalAccess = coastalAccess;

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
		return getElectricityConsumptionFromPublicProduction() * DefaultUnits.convert(
				getSociety().getElectricitySystem().getElectricityDomesticPrice(),
				getSociety().getElectricitySystem().getCurrencyUnits(), 
				getSociety().getElectricitySystem().getElectricityUnits(),
				getCurrencyUnits(), getElectricityUnits());
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
		return domesticProductionModel.getDomesticProduction(this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#getEnergyConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		return getElectricityConsumptionFromPrivateProduction() + 
				getElectricityConsumptionFromPublicProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getElectricityConsumptionFromPrivateProduction()
	 */
	@Override
	public double getElectricityConsumptionFromPrivateProduction() {
		return getWaterFromPrivateProduction() * electricalIntensityOfPrivateProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getElectricityConsumptionFromPublicProduction()
	 */
	@Override
	public double getElectricityConsumptionFromPublicProduction() {
		double energyConsumption = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				energyConsumption += 0;
			} else {
				energyConsumption += ElectricityUnits.convertFlow(
						e.getElectricityConsumption(), e, this);
			}
		}
		return energyConsumption;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsDenominator()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsNumerator()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
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
		if(getSocietyDemand() > 0) {
			return Math.min(1, (getWaterProduction() + getWaterFromPrivateProduction())
					/ getSocietyDemand());
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
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getOperationsExpense()
	 */
	@Override
	public double getOperationsExpense() {
		double value = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				value += CurrencyUnits.convertFlow(e.getFixedOperationsExpense(), e, this);
			} else {
				value += CurrencyUnits.convertFlow(e.getTotalOperationsExpense(), e, this);
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getRenewableWaterFraction()
	 */
	@Override
	public double getRenewableWaterFraction() {
		if(getSocietyDemand() > 0) {
			return getRenewableWaterProduction() 
					/ getSocietyDemand();
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
				renewableProduction += WaterUnits.convertFlow(e.getWaterProduction(), e, this)
						* (1 - e.getReservoirIntensityOfWaterProduction());
			}
		}
		renewableProduction += Math.min(getWaterReservoirRechargeRate(), 
				getWaterProduction() + getWaterFromPrivateProduction() 
				- renewableProduction);
		return renewableProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#getWaterWithdrawals()
	 */
	@Override
	public double getReservoirWithdrawals() {
		return getReservoirWithdrawalsFromPublicProduction() + 
				getReservoirWithdrawalsFromPrivateProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWithdrawalsFromPrivateProduction()
	 */
	@Override
	public double getReservoirWithdrawalsFromPrivateProduction() {
		return getWaterFromPrivateProduction() *
				reservoirIntensityOfPrivateProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getReservoirWithdrawalsFromPublicProduction()
	 */
	@Override
	public double getReservoirWithdrawalsFromPublicProduction() {
		double waterWithdrawals = 0;
		for(WaterElement e : getInternalElements()) {
			waterWithdrawals += WaterUnits.convertFlow(e.getWaterWithdrawals(), e, this);
		}
		return waterWithdrawals;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
	 */
	@Override
	public double getSalesRevenue() {
		return (getSocietyDemand() - getWaterFromPrivateProduction()) * getWaterDomesticPrice();
		/*
			double privateProduction = getWaterFromPrivateProduction();
			double publicProduction = getWaterProduction() - privateProduction;
			double agConsumption = WaterUnits.convertFlow(
					getSociety().getAgricultureSystem().getWaterConsumption(),
					getSociety().getAgricultureSystem(), this);
			double nonAgConsumption = getWaterProduction() - agConsumption;

			if(publicProduction <= nonAgConsumption) {
				return publicProduction * getWaterDomesticPrice();
			} else {
				return (publicProduction - nonAgConsumption) * getWaterAgriculturalPrice();
			}
		 */
	}

	/**
	 * Gets the society demand.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return WaterUnits.convertFlow(getSociety().getTotalWaterDemand(), getSociety(), this);
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
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterAgriculturalPrice()
	 */
	@Override
	public double getWaterAgriculturalPrice() {
		ElectricitySystem electSys = getSociety().getElectricitySystem();
		return electricalIntensityOfPrivateProduction 
				* DefaultUnits.convert(electSys.getElectricityDomesticPrice(),
						electSys.getCurrencyUnits(), electSys.getElectricityUnits(),
						getCurrencyUnits(), getElectricityUnits());
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
	public double getWaterFromPrivateProduction() {
		// Artesian water used to meet shortfall in reaching minimum demand.
		return Math.min(getWaterReservoirVolume() - getReservoirWithdrawalsFromPublicProduction(), 
				Math.max(0, getSocietyDemand()
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
		return Math.max(0, getSocietyDemand()
				+ getWaterOutDistribution()
				- getWaterInDistribution()
				- getWaterProduction()
				- getWaterFromPrivateProduction());
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
			distribution += WaterUnits.convertFlow(e.getWaterOutput(), e, this);
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
				distribution += WaterUnits.convertFlow(e.getWaterInput(), e, this);
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
			distribution += WaterUnits.convertFlow(e.getWaterInput() - e.getWaterOutput(), e, this);
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
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				waterProduction += 0;
			} else {
				waterProduction += WaterUnits.convertFlow(e.getWaterProduction(), e, this);
			}
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
	 * @see edu.mit.sips.WaterSystem#getWaterWasted()
	 */
	@Override
	public double getWaterWasted() {
		// Water is wasted if supply exceeds maximum demand.
		return Math.max(0, getWaterProduction() 
				+ getWaterFromPrivateProduction()
				+ getWaterInDistribution()
				- getWaterOutDistribution()
				- getSocietyDemand());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		waterReservoirVolume = initialWaterReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#isCoastalAccess()
	 */
	@Override
	public boolean isCoastalAccess() {
		return coastalAccess;
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
				- getReservoirWithdrawals());
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