package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnits.DenominatorUnits;
import edu.mit.sips.sim.util.FoodUnits.NumeratorUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class DefaultSocialSoS.
 */
public class DefaultSocialSoS extends DefaultInfrastructureSoS implements SocialSoS {

	/**
	 * Instantiates a new default social so s.
	 */
	public DefaultSocialSoS() {
		super("Society");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduct() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getDomesticProduct();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsDenominator()
	 */
	@Override
	public ElectricityUnits.DenominatorUnits getElectricityUnitsDenominator() {
		return ElectricityUnits.DenominatorUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnitsNumerator()
	 */
	@Override
	public ElectricityUnits.NumeratorUnits getElectricityUnitsNumerator() {
		return ElectricityUnits.NumeratorUnits.MWh;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumption()
	 */
	@Override
	public double getFoodConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getFoodConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
	 */
	@Override
	public DenominatorUnits getFoodUnitsDenominator() {
		return FoodUnits.DenominatorUnits.day;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
	 */
	@Override
	public NumeratorUnits getFoodUnitsNumerator() {
		return FoodUnits.NumeratorUnits.kcal;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<SocialSystem> getNestedSystems() {
		List<SocialSystem> systems = new ArrayList<SocialSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getSocialSystem());
		}
		return Collections.unmodifiableList(systems);
	}


	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getPopulation()
	 */
	@Override
	public long getPopulation() {
		long value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getPopulation();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(SocialSystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsDenominator()
	 */
	@Override
	public WaterUnits.DenominatorUnits getWaterUnitsDenominator() {
		return WaterUnits.DenominatorUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnitsNumerator()
	 */
	@Override
	public WaterUnits.NumeratorUnits getWaterUnitsNumerator() {
		return WaterUnits.NumeratorUnits.m3;
	}
}
