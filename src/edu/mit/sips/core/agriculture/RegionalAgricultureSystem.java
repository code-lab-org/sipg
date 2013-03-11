package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.Society;

public class RegionalAgricultureSystem extends DefaultAgricultureSystem.Local {

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
