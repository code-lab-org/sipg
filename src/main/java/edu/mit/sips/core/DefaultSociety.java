package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.electricity.DefaultElectricitySystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.scenario.Sector;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultSociety.
 */
public abstract class DefaultSociety implements Society {
	private String name;
	private transient Society society;
	private final List<? extends Society> nestedSocieties;
	
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	private AgricultureSystem agricultureSystem;
	private WaterSystem waterSystem;
	private ElectricitySystem electricitySystem;
	private PetroleumSystem petroleumSystem;
	private SocialSystem socialSystem;

	private double cumulativeCashFlow;
	private transient double nextTotalCashFlow;
	private double cumulativeCapitalExpense;
	private transient double nextTotalCapitalExpense;
	private double domesticProduct;
	private transient double nextDomesticProduct;
	
	/**
	 * Instantiates a new default society.
	 */
	protected DefaultSociety() {
		this.name = "Society";
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>());
		this.agricultureSystem = new DefaultAgricultureSystem();
		agricultureSystem.setSociety(this);
		this.waterSystem = new DefaultWaterSystem();
		waterSystem.setSociety(this);
		this.petroleumSystem = new DefaultPetroleumSystem();
		petroleumSystem.setSociety(this);
		this.electricitySystem = new DefaultElectricitySystem();
		electricitySystem.setSociety(this);
		this.socialSystem = new DefaultSocialSystem();
		socialSystem.setSociety(this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getTotalCapitalExpense()
	 */
	@Override
	public double getCumulativeCapitalExpense() {
		return cumulativeCapitalExpense;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SocialSystem#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduct() {
		return domesticProduct;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getPopulation()
	 */
	@Override
	public long getPopulation() {
		return socialSystem.getPopulation();
	}
	
	/**
	 * Instantiates a new default society.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 * @param agricultureSystem the agriculture system
	 * @param waterSystem the water system
	 * @param electricitySystem the electricity system
	 * @param petroleumSystem the petroleum system
	 * @param socialSystem the social system
	 */
	public DefaultSociety(String name, List<? extends Society> nestedSocieties,
			AgricultureSystem agricultureSystem, WaterSystem waterSystem,
			PetroleumSystem petroleumSystem, ElectricitySystem electricitySystem, 
			SocialSystem socialSystem) {
		// Validate name.
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
		
		// Validate cities.
		if(nestedSocieties == null) {
			throw new IllegalArgumentException(
					"Nested societies list cannot be null.");
		}
		for(Society nestedSociety : nestedSocieties) {
			if(nestedSociety == null) {
				throw new IllegalArgumentException(
						"Nested society cannot be null.");
			}
		}
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>(nestedSocieties));
		for(Society nestedSociety : nestedSocieties) {
			nestedSociety.setSociety(this);
		}

		this.agricultureSystem = agricultureSystem;
		this.agricultureSystem.setSociety(this);

		this.waterSystem = waterSystem;
		this.waterSystem.setSociety(this);

		this.electricitySystem = electricitySystem;
		this.electricitySystem.setSociety(this);
		
		this.petroleumSystem = petroleumSystem;
		this.petroleumSystem.setSociety(this);

		this.socialSystem = socialSystem;
		this.socialSystem.setSociety(this);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getCumulativeCashFlow()
	 */
	@Override
	public double getCumulativeCashFlow() {
		return cumulativeCashFlow;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getAgricultureSystem()
	 */
	@Override
	public AgricultureSystem getAgricultureSystem() {
		return agricultureSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalCashFlow()
	 */
	@Override
	public double getTotalCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getCapitalExpense()
	 */
	@Override
	public double getTotalCapitalExpense() {
		double value = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			value += system.getCapitalExpense();
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getElectricitySystem()
	 */
	@Override
	public ElectricitySystem getElectricitySystem() {
		return electricitySystem;
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
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}	
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getInfrastructureSystems()
	 */
	@Override
	public List<? extends InfrastructureSystem> getInfrastructureSystems() {
		return Arrays.asList(getAgricultureSystem(), getWaterSystem(), 
				getPetroleumSystem(), getElectricitySystem(), getSocialSystem());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getInternalElements()
	 */
	@Override
	public List<InfrastructureElement> getInternalElements() {
		List<InfrastructureElement> elements = 
				new ArrayList<InfrastructureElement>();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				elements.addAll(((InfrastructureSystem.Local)system)
						.getInternalElements());
			}
		}
		return Collections.unmodifiableList(elements);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getLocalSectors()
	 */
	@Override
	public final Collection<Sector> getLocalSectors() {
		List<Sector> sectors = new ArrayList<Sector>();
		if(agricultureSystem instanceof AgricultureSystem.Local){
			sectors.add(Sector.AGRICULTURE);
		}
		if(waterSystem instanceof WaterSystem.Local) {
			sectors.add(Sector.WATER);
		}
		if(petroleumSystem instanceof PetroleumSystem.Local) {
			sectors.add(Sector.PETROLEUM);
		}
		if(electricitySystem instanceof ElectricitySystem.Local) {
			sectors.add(Sector.ELECTRICITY);
		}
		return sectors;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getNestedSocieties()
	 */
	@Override
	public List<? extends Society> getNestedSocieties() {
		return nestedSocieties;
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
	 * @see edu.mit.sips.core.Society#getPetroleumSystem()
	 */
	@Override
	public PetroleumSystem getPetroleumSystem() {
		return petroleumSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getSocialSystem()
	 */
	@Override
	public SocialSystem getSocialSystem() {
		return socialSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getSocieties()
	 */
	@Override
	public List<? extends Society> getSocieties() {
		List<Society> societies = new ArrayList<Society>();
		societies.add(this);
		for(Society society : getNestedSocieties()) {
			societies.addAll(society.getSocieties());
		}
		return societies;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getSociety()
	 */
	@Override
	public final Society getSociety() {
		return society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalElectricityDemand()
	 */
	@Override
	public double getTotalElectricityDemand() {
		return ElectricityUnits.convertFlow(
				getWaterSystem().getElectricityConsumption(),
				getWaterSystem(), this)
				+ ElectricityUnits.convertFlow(
						getPetroleumSystem().getElectricityConsumption(), 
						getPetroleumSystem(), this)
						+ ElectricityUnits.convertFlow(
								getSocialSystem().getElectricityConsumption(), 
								getSocialSystem(), this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalFoodDemand()
	 */
	@Override
	public double getTotalFoodDemand() {
		return FoodUnits.convertFlow(
				getSocialSystem().getFoodConsumption(), 
				getSocialSystem(), this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalPetroleumDemand()
	 */
	@Override
	public double getTotalPetroleumDemand() {
		return OilUnits.convertFlow(
				getSocialSystem().getPetroleumConsumption(),
				getSocialSystem(), this)
				+ OilUnits.convertFlow(
						getElectricitySystem().getPetroleumConsumption(),
						getElectricitySystem(), this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalWaterDemand()
	 */
	@Override
	public double getTotalWaterDemand() {
		return WaterUnits.convertFlow(
				getAgricultureSystem().getWaterConsumption(),
				getAgricultureSystem(), this)
				+ WaterUnits.convertFlow(
						getElectricitySystem().getWaterConsumption(),
						getElectricitySystem(), this)
						+ WaterUnits.convertFlow(
								getSocialSystem().getWaterConsumption(),
								getSocialSystem(), this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getWaterSystem()
	 */
	@Override
	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		cumulativeCashFlow = 0;
		cumulativeCapitalExpense = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.initialize(time);
		}
		for(Society society : getNestedSocieties()) {
			society.initialize(time);
		}
		domesticProduct = getNextDomesticProduct(); // TODO need initial value
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setAgricultureSystem(edu.mit.sips.core.agriculture.AgricultureSystem)
	 */
	@Override
	public void setAgricultureSystem(AgricultureSystem agricultureSystem) {
		agricultureSystem.setSociety(this);
		this.agricultureSystem = agricultureSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setElectricitySystem(edu.mit.sips.core.electricity.ElectricitySystem)
	 */
	@Override
	public void setElectricitySystem(ElectricitySystem electricitySystem) {
		electricitySystem.setSociety(this);
		this.electricitySystem = electricitySystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setPetroleumSystem(edu.mit.sips.core.petroleum.PetroleumSystem)
	 */
	@Override
	public void setPetroleumSystem(PetroleumSystem petroleumSystem) {
		petroleumSystem.setSociety(this);
		this.petroleumSystem = petroleumSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
		socialSystem.setSociety(this);
		this.socialSystem = socialSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setWaterSystem(edu.mit.sips.core.water.WaterSystem)
	 */
	@Override
	public void setWaterSystem(WaterSystem waterSystem) {
		waterSystem.setSociety(this);
		this.waterSystem = waterSystem;
	}

	/**
	 * Gets the next domestic product.
	 *
	 * @return the next domestic product
	 */
	private double getNextDomesticProduct() {
		double nextDomesticProduct = 0;
		for(InfrastructureSystem s : getInfrastructureSystems()) {
			nextDomesticProduct += s.getDomesticProduction();
		}
		return nextDomesticProduct;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextTotalCashFlow = getTotalCashFlow();
		nextTotalCapitalExpense = getTotalCapitalExpense();
		nextDomesticProduct = getNextDomesticProduct();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.tick();
		}
		for(Society society : getNestedSocieties()) {
			society.tick();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		cumulativeCashFlow += nextTotalCashFlow;
		cumulativeCapitalExpense += nextTotalCapitalExpense;
		domesticProduct = nextDomesticProduct;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			system.tock();
		}
		for(Society society : getNestedSocieties()) {
			society.tock();
		}
	}
}