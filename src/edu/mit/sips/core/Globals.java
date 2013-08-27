package edu.mit.sips.core;

/**
 * The Class Globals.
 */
public class Globals {
	private double agricultureLaborParticipationRate = 0.50;			// -
	private double electricalIntensityOfBurningPetroleum = 0.300;		// MWh/bbl
	
	/**
	 * Instantiates a new globals.
	 */
	public Globals() {
		
	}
	
	/**
	 * Gets the agriculture labor participation rate.
	 *
	 * @return the agriculture labor participation rate
	 */
	public double getAgricultureLaborParticipationRate() {
		return agricultureLaborParticipationRate;
	}

	/**
	 * Gets the electrical intensity of burning petroleum.
	 *
	 * @return the electrical intensity of burning petroleum
	 */
	public double getElectricalIntensityOfBurningPetroleum() {
		return electricalIntensityOfBurningPetroleum;
	}

	/**
	 * Sets the agriculture labor participation rate.
	 *
	 * @param agricultureLaborParticipationRate the new agriculture labor participation rate
	 */
	public void setAgricultureLaborParticipationRate(
			double agricultureLaborParticipationRate) {
		this.agricultureLaborParticipationRate = agricultureLaborParticipationRate;
	}

	/**
	 * Sets the electrical intensity of burning petroleum.
	 *
	 * @param electricalIntensityOfBurningPetroleum the new electrical intensity of burning petroleum
	 */
	public void setElectricalIntensityOfBurningPetroleum(
			double electricalIntensityOfBurningPetroleum) {
		this.electricalIntensityOfBurningPetroleum = electricalIntensityOfBurningPetroleum;
	}
}
