package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.Society;

public class RegionalAgricultureSystem extends DefaultAgricultureSystem.Local {
	
	/**
	 * Instantiates a new regional agriculture system.
	 */
	public RegionalAgricultureSystem() {
		super("Regional Agriculture");
	}
	
	/**
	 * Instantiates a new regional agriculture system.
	 *
	 * @param name the name
	 */
	protected RegionalAgricultureSystem(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureSystem.Local#getArableLandArea()
	 */
	@Override
	public double getArableLandArea() {
		double value = 0;
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				value += ((AgricultureSystem.Local)
						society.getAgricultureSystem()).getArableLandArea();
			}
		}
		return value;
	}
}
