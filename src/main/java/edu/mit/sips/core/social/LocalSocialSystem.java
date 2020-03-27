package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.LocalInfrastructureSystem;
import edu.mit.sips.core.social.demand.DefaultDemandModel;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.DefaultPopulationModel;
import edu.mit.sips.core.social.population.PopulationModel;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

public class LocalSocialSystem extends LocalInfrastructureSystem implements SocialSystem.Local {
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	
	private final PopulationModel populationModel;
	private final DemandModel electricityDemandModel, foodDemandModel, waterDemandModel, petroleumDemandModel;

	/**
	 * Instantiates a new local.
	 */
	public LocalSocialSystem() {
		super("Society");
		this.populationModel = new DefaultPopulationModel();
		this.electricityDemandModel = new DefaultDemandModel();
		this.foodDemandModel = new DefaultDemandModel();
		this.waterDemandModel = new DefaultDemandModel();
		this.petroleumDemandModel = new DefaultDemandModel();
	}

	/**
	 * Instantiates a new local.
	 *
	 * @param populationModel the population model
	 * @param electricityDemandModel the electricity demand model
	 * @param foodDemandModel the food demand model
	 * @param waterDemandModel the water demand model
	 * @param petroleumDemandModel the petroleum demand model
	 */
	public LocalSocialSystem(PopulationModel populationModel, 
			DemandModel electricityDemandModel, 
			DemandModel foodDemandModel,
			DemandModel waterDemandModel, 
			DemandModel petroleumDemandModel) {
		super("Society");

		// Validate population model.
		if(populationModel == null) {
			throw new IllegalArgumentException(
					"Population model cannot be null.");
		}
		this.populationModel = populationModel;

		// Validate electricity demand model.
		if(electricityDemandModel == null) {
			throw new IllegalArgumentException(
					"Electricity demand model cannot be null.");
		}
		this.electricityDemandModel = electricityDemandModel;

		// Validate food demand model.
		if(foodDemandModel == null) {
			throw new IllegalArgumentException(
					"Food demand model cannot be null.");
		}
		this.foodDemandModel = foodDemandModel;

		// Validate water demand model.
		if(waterDemandModel == null) {
			throw new IllegalArgumentException(
					"Water demand model cannot be null.");
		}
		this.waterDemandModel = waterDemandModel;

		// Validate petroleum demand model.
		if(petroleumDemandModel == null) {
			throw new IllegalArgumentException(
					"Petroleum demand model cannot be null.");
		}
		this.petroleumDemandModel = petroleumDemandModel;
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
	 * @see edu.mit.sips.SocialSystem#getSocietyElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		return electricityDemandModel.getDemand(getSociety());
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
		return foodDemandModel.getDemand(getSociety());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
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
	 * @see edu.mit.sips.core.social.SocialSystem#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		return petroleumDemandModel.getDemand(getSociety());
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
		return waterDemandModel.getDemand(getSociety());
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
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		populationModel.initialize(time);

		// initialize demand models after domestic product
		electricityDemandModel.initialize(time);
		foodDemandModel.initialize(time);
		waterDemandModel.initialize(time);
		petroleumDemandModel.initialize(time);

	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		populationModel.tick();
		electricityDemandModel.tick();
		foodDemandModel.tick();
		waterDemandModel.tick();
		petroleumDemandModel.tick();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		populationModel.tock();
		electricityDemandModel.tock();
		foodDemandModel.tock();
		waterDemandModel.tock();
		petroleumDemandModel.tock();
	}
}
