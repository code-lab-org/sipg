package edu.mit.sips.core.social;

import edu.mit.sips.core.Society;

/**
 * The Class RegionalSocialSystem.
 */
public class RegionalSocialSystem extends DefaultSocialSystem.Local {

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

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {

	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {

	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {

	}
}
