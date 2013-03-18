package edu.mit.sips.core.social;

import edu.mit.sips.core.Society;

/**
 * The Class RegionalSocialSystem.
 */
public class RegionalSocialSystem extends DefaultSocialSystem.Local {
	
	/**
	 * Instantiates a new regional social system.
	 */
	public RegionalSocialSystem() {
		super("Regional Society");
	}
	
	/**
	 * Instantiates a new regional social system.
	 *
	 * @param name the name
	 */
	protected RegionalSocialSystem(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduct() {
		long value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getSocialSystem() instanceof SocialSystem.Local) {
				value += ((SocialSystem.Local)
						society.getSocialSystem()).getDomesticProduct();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.SocialSystem#getPopulation()
	 */
	@Override
	public long getPopulation() {
		long value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getSocialSystem() instanceof SocialSystem.Local) {
				value += ((SocialSystem.Local)
						society.getSocialSystem()).getPopulation();
			}
		}
		return value;
	}
}
