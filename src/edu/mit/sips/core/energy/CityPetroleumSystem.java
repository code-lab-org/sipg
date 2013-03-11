package edu.mit.sips.core.energy;

/**
 * The Class CityWaterSystem.
 */
public class CityPetroleumSystem extends DefaultPetroleumSystem {
	private final double maxPetroleumReservoirVolume;
	private final double initialPetroleumReservoirVolume;
	private double petroleumReservoirVolume, nextPetroleumReservoirVolume;

	/**
	 * Instantiates a new city agriculture system.
	 */
	protected CityPetroleumSystem() {
		this.maxPetroleumReservoirVolume = 0;
		this.initialPetroleumReservoirVolume = 0;
	}
	
	/**
	 * Instantiates a new city water system.
	 *
	 * @param coastal the coastal
	 * @param maxWaterReservoirVolume the max water reservoir volume
	 * @param initialWaterReservoirVolume the initial water reservoir volume
	 * @param waterReservoirRechargeRate the water reservoir recharge rate
	 * @param initialWaterSupplyPerCapita the initial water supply per capita
	 */
	public CityPetroleumSystem(double maxPetroleumReservoirVolume,
			double initialPetroleumReservoirVolume) {

		// Validate max petroleum reservoir.
		if(maxPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Max petroleum reservoir volume cannot be negative.");
		}
		this.maxPetroleumReservoirVolume = maxPetroleumReservoirVolume;
		
		// Validate initial petroleum reservoir.
		if(initialPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot be negative.");
		} else if(initialPetroleumReservoirVolume > maxPetroleumReservoirVolume) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot exceed maximum.");
		}
		this.initialPetroleumReservoirVolume = initialPetroleumReservoirVolume;
	}



	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getMaxPetroleumReservoirVolume()
	 */
	@Override
	public double getMaxPetroleumReservoirVolume() {
		return maxPetroleumReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		return petroleumReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		petroleumReservoirVolume = initialPetroleumReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextPetroleumReservoirVolume = Math.min(maxPetroleumReservoirVolume, 
				petroleumReservoirVolume - getPetroleumWithdrawals());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		petroleumReservoirVolume = nextPetroleumReservoirVolume;
	}
}
