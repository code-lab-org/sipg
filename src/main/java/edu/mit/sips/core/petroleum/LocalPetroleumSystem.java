package edu.mit.sips.core.petroleum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.LocalInfrastructureSystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class LocalPetroleumSystem.
 */
public class LocalPetroleumSystem  extends LocalInfrastructureSystem implements PetroleumSystem.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;

	private final DomesticProductionModel domesticProductionModel;
	private final PriceModel domesticPriceModel, importPriceModel, exportPriceModel;
	private final List<PetroleumElement> elements = 
			Collections.synchronizedList(new ArrayList<PetroleumElement>());
	private final double maxPetroleumReservoirVolume;
	private final double initialPetroleumReservoirVolume;
	private double petroleumReservoirVolume;
	private transient double nextPetroleumReservoirVolume;

	private transient final Map<Long, Double> electricityConsumptionMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumReservoirVolumeMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumWithdrawalsMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumDomesticPriceMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumImportPriceMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumExportPriceMap = 
			new HashMap<Long, Double>();
	

	/**
	 * Instantiates a new default petroleum system.
	 *
	 */
	public LocalPetroleumSystem() {
		super("Petroleum");
		this.domesticProductionModel = new DefaultDomesticProductionModel();
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
	 * @param domesticProductionModel the domestic production model
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 * @param exportPriceModel the export price model
	 */
	public LocalPetroleumSystem(double maxPetroleumReservoirVolume,
			double initialPetroleumReservoirVolume,
			Collection<? extends PetroleumElement> elements,
			DomesticProductionModel domesticProductionModel,
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
	@Override
	public double getConsumptionExpense() {
		return getElectricityConsumption() * DefaultUnits.convert(
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
		return domesticProductionModel.getDomesticProduction(this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double consumption = 0;
		for(PetroleumElement e : getInternalElements()) {
			consumption += ElectricityUnits.convertFlow(e.getElectricityConsumption(), e, this);
		}
		return consumption;
	}

	/**
	 * Gets the electricity consumption map.
	 *
	 * @return the electricity consumption map
	 */
	public Map<Long, Double> getElectricityConsumptionMap() {
		return new HashMap<Long, Double>(electricityConsumptionMap);
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
	 * @see edu.mit.sips.InfrastructureSystem#getElements()
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
		if(getSocietyDemand() > 0) {
			return Math.min(1, getPetroleumProduction() 
					/ getSocietyDemand());
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getMaxPetroleumReservoirVolume()
	 */
	@Override
	public double getMaxPetroleumReservoirVolume() {
		return maxPetroleumReservoirVolume;
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
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumDomesticPrice()
	 */
	@Override
	public double getPetroleumDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum domestic price map.
	 *
	 * @return the petroleum domestic price map
	 */
	public Map<Long, Double> getPetroleumDomesticPriceMap() {
		return new HashMap<Long, Double>(petroleumDomesticPriceMap);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumExport()
	 */
	@Override
	public double getPetroleumExport() {
		return Math.max(0, getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution()
				- getSocietyDemand());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumExportPrice()
	 */
	@Override
	public double getPetroleumExportPrice() {
		return exportPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum export price map.
	 *
	 * @return the petroleum export price map
	 */
	public Map<Long, Double> getPetroleumExportPriceMap() {
		return new HashMap<Long, Double>(petroleumExportPriceMap);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumImport()
	 */
	@Override
	public double getPetroleumImport() {
		return Math.max(0, getSocietyDemand()
				+ getPetroleumOutDistribution()
				- getPetroleumInDistribution()
				- getPetroleumProduction());
	}			

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getPetroleumImportPrice()
	 */
	@Override
	public double getPetroleumImportPrice() {
		return importPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum import price map.
	 *
	 * @return the petroleum import price map
	 */
	public Map<Long, Double> getPetroleumImportPriceMap() {
		return new HashMap<Long, Double>(petroleumImportPriceMap);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumInDistribution()
	 */
	@Override
	public double getPetroleumInDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getExternalElements()) {
			distribution += OilUnits.convertFlow(e.getPetroleumOutput(), e, this);
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistribution()
	 */
	@Override
	public double getPetroleumOutDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += OilUnits.convertFlow(e.getPetroleumInput(), e, this);
			}
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistributionLosses()
	 */
	@Override
	public double getPetroleumOutDistributionLosses() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			distribution += OilUnits.convertFlow(e.getPetroleumInput(), e, this)
					- OilUnits.convertFlow(e.getPetroleumOutput(), e, this);
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumProduction()
	 */
	@Override
	public double getPetroleumProduction() {
		double petroleumProduction = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumProduction += OilUnits.convertFlow(e.getPetroleumProduction(), e, this);
		}
		return petroleumProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		return petroleumReservoirVolume;
	}

	/**
	 * Gets the petroleum reservoir volume map.
	 *
	 * @return the petroleum reservoir volume map
	 */
	public Map<Long, Double> getPetroleumReservoirVolumeMap() {
		return new HashMap<Long, Double>(petroleumReservoirVolumeMap);
	}


	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumWithdrawals()
	 */
	@Override
	public double getPetroleumWithdrawals() {
		double petroleumWithdrawals = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumWithdrawals += OilUnits.convertFlow(e.getPetroleumWithdrawals(), e, this);
		}
		return petroleumWithdrawals;
	}

	/**
	 * Gets the petroleum withdrawals map.
	 *
	 * @return the petroleum withdrawals map
	 */
	public Map<Long, Double> getPetroleumWithdrawalsMap() {
		return new HashMap<Long, Double>(petroleumWithdrawalsMap);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem#getReservoirLifetime()
	 */
	public double getReservoirLifetime() {
		return getPetroleumWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getPetroleumReservoirVolume() / getPetroleumWithdrawals());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
	 */
	@Override
	public double getSalesRevenue() {
		return getPetroleumDomesticPrice() * getSocietyDemand();
	}

	/**
	 * Gets the society demand.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return OilUnits.convertFlow(getSociety().getTotalPetroleumDemand(), getSociety(), this);
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
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalPetroleumSupply() > 0) {
			return getCashFlow() / getTotalPetroleumSupply();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.LocalInfrastructureSystem#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		super.initialize(time);
		petroleumReservoirVolume = initialPetroleumReservoirVolume;
		electricityConsumptionMap.clear();
		petroleumReservoirVolumeMap.clear();
		petroleumWithdrawalsMap.clear();
		petroleumDomesticPriceMap.clear();
		petroleumImportPriceMap.clear();
		petroleumExportPriceMap.clear();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.petroleum.PetroleumSystem.Local#removeElement(edu.mit.sips.core.petroleum.PetroleumElement)
	 */
	@Override
	public synchronized boolean removeElement(PetroleumElement element) {
		return elements.remove(element);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.LocalInfrastructureSystem#tick()
	 */
	@Override
	public void tick() {
		super.tick();
		nextPetroleumReservoirVolume = Math.min(maxPetroleumReservoirVolume, 
				petroleumReservoirVolume - getPetroleumWithdrawals());
		electricityConsumptionMap.put(time, getElectricityConsumption());
		petroleumReservoirVolumeMap.put(time, getPetroleumReservoirVolume());
		petroleumWithdrawalsMap.put(time, getPetroleumWithdrawals());
		petroleumDomesticPriceMap.put(time, getPetroleumDomesticPrice());
		petroleumImportPriceMap.put(time, getPetroleumImportPrice());
		petroleumExportPriceMap.put(time, getPetroleumExportPrice());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		super.tock();
		petroleumReservoirVolume = nextPetroleumReservoirVolume;
	}
}