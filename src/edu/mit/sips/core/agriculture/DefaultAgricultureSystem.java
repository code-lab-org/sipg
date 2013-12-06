package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultAgricultureSystem.
 */
public class DefaultAgricultureSystem extends DefaultInfrastructureSystem implements AgricultureSystem {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;

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

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodDomesticPrice()
	 */
	@Override
	public double getFoodDomesticPrice() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodExportPrice()
	 */
	@Override
	public double getFoodExportPrice() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getFoodImportPrice()
	 */
	@Override
	public double getFoodImportPrice() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		return 0;
	}
}
