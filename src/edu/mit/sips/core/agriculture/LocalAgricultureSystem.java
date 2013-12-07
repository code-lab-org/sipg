package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.LocalInfrastructureSystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class Local.
 */
public class LocalAgricultureSystem extends LocalInfrastructureSystem implements AgricultureSystem.Local {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;

	private final DomesticProductionModel domesticProductionModel;
	private final PriceModel domesticPriceModel, importPriceModel, exportPriceModel;
	private final double arableLandArea;	
	private final double laborParticipationRate;
	private final List<AgricultureElement> elements = 
			Collections.synchronizedList(new ArrayList<AgricultureElement>());

	/**
	 * Instantiates a new local.
	 */
	public LocalAgricultureSystem() {
		super("Agriculture");
		this.domesticProductionModel = new DefaultDomesticProductionModel();
		this.domesticPriceModel = new DefaultPriceModel();
		this.importPriceModel = new DefaultPriceModel();
		this.exportPriceModel = new DefaultPriceModel();
		this.arableLandArea = 0;
		this.laborParticipationRate = 1;
	}
	
	/**
	 * Instantiates a new local.
	 *
	 * @param arableLandArea the arable land area
	 * @param laborParticipationRate the labor participation rate
	 * @param elements the elements
	 * @param domesticProductionModel the domestic production model
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 * @param exportPriceModel the export price model
	 */
	public LocalAgricultureSystem(double arableLandArea, double laborParticipationRate,
			Collection<? extends AgricultureElement> elements, 
			DomesticProductionModel domesticProductionModel,
			PriceModel domesticPriceModel, 
			PriceModel importPriceModel, 
			PriceModel exportPriceModel) {
		super("Agriculture");

		// Validate arable land area.
		if(arableLandArea < 0) {
			throw new IllegalArgumentException(
					"Arable land area cannot be negative.");
		}
		this.arableLandArea = arableLandArea;

		// Validate labor participation rate.
		if(laborParticipationRate < 0 || laborParticipationRate > 1) {
			throw new IllegalArgumentException(
					"Labor participation rate must be between 0 and 1.");
		}
		this.laborParticipationRate = laborParticipationRate;

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
		return getWaterConsumption() * DefaultUnits.convert(
				getSociety().getWaterSystem().getWaterDomesticPrice(),
				getSociety().getWaterSystem().getCurrencyUnits(), 
				getSociety().getWaterSystem().getWaterUnits(),
				getCurrencyUnits(), getWaterUnits());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
	 */
	@Override
	public double getDistributionExpense() {
		return getFoodDomesticPrice() * getFoodInDistribution();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
	 */
	@Override
	public double getDistributionRevenue() {
		return getFoodDomesticPrice() * (getFoodOutDistribution() 
				- getFoodOutDistributionLosses());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return domesticProductionModel.getDomesticProduction(this);
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
		return getFoodExportPrice() * getFoodExport();
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
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodDomesticPrice()
	 */
	@Override
	public double getFoodDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.AgricultureSystem#getFoodExport()
	 */
	@Override
	public double getFoodExport() {
		return Math.max(0, getFoodProduction() 
				+ getFoodInDistribution()
				- getFoodOutDistribution()
				- getSocietyDemand());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodExportPrice()
	 */
	@Override
	public double getFoodExportPrice() {
		return exportPriceModel.getUnitPrice();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.AgricultureSystem#getFoodImport()
	 */
	@Override
	public double getFoodImport() {
		return Math.max(0, getSocietyDemand()
				+ getFoodOutDistribution() 
				- getFoodInDistribution()
				- getFoodProduction());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodImportPrice()
	 */
	@Override
	public double getFoodImportPrice() {
		return importPriceModel.getUnitPrice();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.AgricultureSystem#getFoodInDistribution()
	 */
	@Override
	public double getFoodInDistribution() {
		double distribution = 0;
		for(AgricultureElement e : getExternalElements()) {
			distribution += FoodUnits.convertFlow(e.getFoodOutput(), e, this);
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
				distribution += FoodUnits.convertFlow(e.getFoodInput(), e, this);
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
			distribution += FoodUnits.convertFlow(e.getFoodInput() - e.getFoodOutput(), e, this);
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
			foodProduction += FoodUnits.convertFlow(e.getFoodProduction(), e, this);
		}
		return foodProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
	 */
	@Override
	public double getImportExpense() {
		return getFoodImportPrice() * getFoodImport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	@Override
	public List<AgricultureElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLaborParticipationRate()
	 */
	@Override
	public double getLaborParticipationRate() {
		return laborParticipationRate;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getLaborUsed()
	 */
	@Override
	public long getLaborUsed() {
		long value = 0;
		for(AgricultureElement e : getInternalElements()) {
			value += e.getLaborIntensityOfLandUsed() * e.getLandArea();
		}
		return value;
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
		if(getSocietyDemand() > 0) {
			return Math.min(1, getFoodProduction() 
					/ getSocietyDemand());
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
		return getFoodDomesticPrice() * getSocietyDemand();
	}

	/**
	 * Gets the society demand.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return FoodUnits.convertFlow(getSociety().getTotalFoodDemand(), getSociety(), this);
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
			waterConsumption += WaterUnits.convertFlow(e.getWaterConsumption(), e, this);
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
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
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getNumeratorUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}
}