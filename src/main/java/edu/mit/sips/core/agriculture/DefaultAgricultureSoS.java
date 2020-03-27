package edu.mit.sips.core.agriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultAgricultureSoS.
 */
public class DefaultAgricultureSoS extends DefaultInfrastructureSoS implements AgricultureSoS {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	private List<Double> foodSecurityHistory = new ArrayList<Double>();
	
	/**
	 * Instantiates a new default agriculture so s.
	 */
	public DefaultAgricultureSoS() {
		super("Agriculture");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodDomesticPrice()
	 */
	@Override
	public double getFoodDomesticPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodDomesticPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodExportPrice()
	 */
	@Override
	public double getFoodExportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodExportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodImportPrice()
	 */
	@Override
	public double getFoodImportPrice() {
		if(!getNestedSystems().isEmpty()) {
			double value = 0;
			for(AgricultureSystem system : getNestedSystems()) {
				value += DefaultUnits.convert(system.getFoodImportPrice(), 
						system.getCurrencyUnits(), system.getFoodUnits(),
						getCurrencyUnits(), getFoodUnits());
			}
			return value / getNestedSystems().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodProduction()
	 */
	@Override
	public double getFoodProduction() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getFoodProduction();
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodSecurity()
	 */
	@Override
	public double getFoodSecurity() {
		return getTotalFoodSupply() == 0 ? 1 
				: (getFoodProduction() / getTotalFoodSupply());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<AgricultureSystem> getNestedSystems() {
		List<AgricultureSystem> systems = new ArrayList<AgricultureSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getAgricultureSystem());
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getTotalFoodSupply()
	 */
	@Override
	public double getTotalFoodSupply() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getTotalFoodSupply();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(AgricultureSystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
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
	
	@Override
	public void initialize(long time) {
		super.initialize(time);
		foodSecurityHistory.clear();
	}
	
	@Override
	public void tick() {
		super.tick();
		this.foodSecurityHistory.add(1000 / 0.75 * Math.max(Math.min(this.getFoodSecurity(), 0.75), 0));
	}

	@Override
	public double getFoodSecurityScore() {
		double value = 0;
		for(double item : foodSecurityHistory) {
			value += item;
		}
		return value / foodSecurityHistory.size();
	}
}
