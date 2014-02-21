package edu.mit.sips.core.electricity;

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
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class LocalElectricitySystem.
 */
public class LocalElectricitySystem extends LocalInfrastructureSystem implements ElectricitySystem.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	private final double petroleumIntensityOfPrivateProduction;
	private final DomesticProductionModel domesticProductionModel;
	private final PriceModel domesticPriceModel;
	private final List<ElectricityElement> elements = 
			Collections.synchronizedList(new ArrayList<ElectricityElement>());
	
	private transient final Map<Long, Double> petroleumConsumptionMap = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> waterConsumptionMap = 
			new HashMap<Long, Double>();

	/**
	 * Instantiates a new default electricity system.
	 */
	public LocalElectricitySystem() {
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
	public LocalElectricitySystem(double petroleumIntensityOfPrivateProduction,
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
		return DefaultUnits.convert(getSociety().getPetroleumSystem().getPetroleumDomesticPrice(),
				getSociety().getPetroleumSystem().getCurrencyUnits(),
				getSociety().getPetroleumSystem().getOilUnits(),
				getCurrencyUnits(), getOilUnits())
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
		return Math.max(0, getSocietyDemand()
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
			distribution += ElectricityUnits.convertFlow(e.getElectricityOutput(), e, this);
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
				distribution += ElectricityUnits.convertFlow(e.getElectricityInput(), e, this);
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
			distribution += ElectricityUnits.convertFlow(e.getElectricityInput(), e, this)
					- ElectricityUnits.convertFlow(e.getElectricityOutput(), e, this);
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
			energyProduction += ElectricityUnits.convertFlow(e.getElectricityProduction(), e, this);
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
				- getSocietyDemand());
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
		if(getSocietyDemand() > 0) {
			return Math.min(1, (getElectricityProduction() 
					+ getElectricityFromPrivateProduction())
					/ getSocietyDemand());
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
			petroleumConsumption += OilUnits.convertFlow(e.getPetroleumConsumption(), e, this);
		}
		return petroleumConsumption;
	}

	/**
	 * Gets the petroleum consumption map.
	 *
	 * @return the petroleum consumption map
	 */
	public Map<Long, Double> getPetroleumConsumptionMap() {
		return new HashMap<Long, Double>(petroleumConsumptionMap);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#getElectricalIntensityOfBurningPetroleum()
	 */
	@Override
	public double getPetroleumIntensityOfPrivateProduction() {
		return petroleumIntensityOfPrivateProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityFraction()
	 */
	@Override
	public double getRenewableElectricityFraction() {
		if(getSocietyDemand() > 0) {
			return getRenewableElectricityProduction() / 
					getSocietyDemand();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElectricitySystem#getRenewableEnergyProduction()
	 */
	@Override
	public double getRenewableElectricityProduction() {
		double production = 0;
		for(ElectricityElement e : getInternalElements()) {
			if(e.isRenewableElectricity()) {
				production += ElectricityUnits.convertFlow(e.getElectricityProduction(), e, this);
			}
		}
		return production;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
	 */
	@Override
	public double getSalesRevenue() {
		return getElectricityDomesticPrice() * (getSocietyDemand()
				- getElectricityFromPrivateProduction());
	}

	/**
	 * Gets the society demand.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return ElectricityUnits.convertFlow(getSociety().getTotalElectricityDemand(), getSociety(), this);
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
			waterConsumption += WaterUnits.convertFlow(e.getWaterConsumption(), e, this);
		}
		return waterConsumption;
	}

	/**
	 * Gets the water consumption map.
	 *
	 * @return the water consumption map
	 */
	public Map<Long, Double> getWaterConsumptionMap() {
		return new HashMap<Long, Double>(waterConsumptionMap);
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
	 * @see edu.mit.sips.core.LocalInfrastructureSystem#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		super.initialize(time);
		petroleumConsumptionMap.clear();
		waterConsumptionMap.clear();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.electricity.ElectricitySystem.Local#removeElement(edu.mit.sips.core.electricity.ElectricityElement)
	 */
	@Override
	public synchronized boolean removeElement(ElectricityElement element) {
		return elements.remove(element);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.LocalInfrastructureSystem#tick()
	 */
	@Override
	public void tick() {
		super.tick();
		petroleumConsumptionMap.put(time, getPetroleumConsumption());
		waterConsumptionMap.put(time, getWaterConsumption());
	}
}