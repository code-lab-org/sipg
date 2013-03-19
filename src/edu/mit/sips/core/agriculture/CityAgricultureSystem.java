package edu.mit.sips.core.agriculture;

/**
 * The Class CityAgricultureSystem.
 */
public class CityAgricultureSystem extends DefaultAgricultureSystem.Local {
	private final double arableLandArea;

	/**
	 * Instantiates a new city agriculture system.
	 */
	protected CityAgricultureSystem() {
		this.arableLandArea = 0;
	}
	
	/**
	 * Instantiates a new city agriculture system.
	 *
	 * @param arableLandArea the arable land area
	 */
	public CityAgricultureSystem(double arableLandArea) {
		// Validate arable land area.
		if(arableLandArea < 0) {
			throw new IllegalArgumentException(
					"Arable land area cannot be negative.");
		}
		this.arableLandArea = arableLandArea;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.AgricultureSystem#getArableLandArea()
	 */
	@Override
	public double getArableLandArea() {
		return arableLandArea;
	}
}
