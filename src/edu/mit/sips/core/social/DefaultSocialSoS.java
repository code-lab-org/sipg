package edu.mit.sips.core.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.Society;

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
	 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProductPerCapita()
	 */
	@Override
	public double getDomesticProductPerCapita() {
		return getDomesticProduct() / getPopulation();
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
	 * @see edu.mit.sips.core.social.SocialSystem#getElectricityConsumptionPerCapita()
	 */
	@Override
	public double getElectricityConsumptionPerCapita() {
		return getElectricityConsumption() / getPopulation();
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
	 * @see edu.mit.sips.core.social.SocialSystem#getFoodConsumptionPerCapita()
	 */
	@Override
	public double getFoodConsumptionPerCapita() {
		return getFoodConsumption() / getPopulation();
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
	 * @see edu.mit.sips.core.social.SocialSystem#getWaterConsumptionPerCapita()
	 */
	@Override
	public double getWaterConsumptionPerCapita() {
		return getWaterConsumption() / getPopulation();
	}
}
