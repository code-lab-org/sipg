package edu.mit.sips.core.water;

/**
 * The Class CityWaterSystem.
 */
public class CityWaterSystem extends DefaultWaterSystem.Local {
	private final double maxWaterReservoirVolume;
	private final double initialWaterReservoirVolume;
	private final double waterReservoirRechargeRate;
	private final double initialWaterSupplyPerCapita;
	private final boolean coastal; // TODO use for desalination elements

	private double waterReservoirVolume, nextWaterReservoirVolume;
	private double waterSupplyPerCapita;

	/**
	 * Instantiates a new city agriculture system.
	 */
	protected CityWaterSystem() {
		this.maxWaterReservoirVolume = 0;
		this.initialWaterReservoirVolume = 0;
		this.waterReservoirRechargeRate = 0;
		this.coastal = false;
		this.initialWaterSupplyPerCapita = 0;
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
	public CityWaterSystem(boolean coastal, double maxWaterReservoirVolume,
			double initialWaterReservoirVolume, double waterReservoirRechargeRate,
			double initialWaterSupplyPerCapita) {
		// Validate max water reservoir volume.
		if(maxWaterReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Max water reservoir volume cannot be negative.");
		}
		this.maxWaterReservoirVolume = maxWaterReservoirVolume;

		// Validate initial water reservoir volume.
		if(initialWaterReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Initial water reservoir volume cannot be negative.");
		} else if(initialWaterReservoirVolume > maxWaterReservoirVolume) {
			throw new IllegalArgumentException(
					"Initial water reservoir volume cannot exceed maximum.");
		}
		this.initialWaterReservoirVolume = initialWaterReservoirVolume;

		// No need to validate recharge rate.
		this.waterReservoirRechargeRate = waterReservoirRechargeRate;

		// No need to validate coastal.
		this.coastal = coastal;

		if(initialWaterSupplyPerCapita < 0) {
			throw new IllegalArgumentException(
					"Initial water supply per capita cannot be negative.");
		}
		this.initialWaterSupplyPerCapita = 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#getMaxWaterReservoirVolume()
	 */
	@Override
	public double getMaxWaterReservoirVolume() {
		return maxWaterReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem.Local#getWaterReservoirRechargeRate()
	 */
	@Override
	public double getWaterReservoirRechargeRate() {
		return waterReservoirRechargeRate;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#getWaterReservoirVolume()
	 */
	@Override
	public double getWaterReservoirVolume() {
		return waterReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#getWaterSupplyPerCapita()
	 */
	@Override
	public double getWaterSupplyPerCapita() {
		return waterSupplyPerCapita;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		waterReservoirVolume = initialWaterReservoirVolume;
		setWaterSupplyPerCapita(initialWaterSupplyPerCapita);
	}

	/**
	 * Checks if is coastal.
	 *
	 * @return true, if is coastal
	 */
	public boolean isCoastal() {
		return coastal;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.WaterSystem#setWaterSupplyPerCapita(double)
	 */
	@Override
	public void setWaterSupplyPerCapita(double waterSupplyPerCapita) {
		this.waterSupplyPerCapita = waterSupplyPerCapita;
		fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
		fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
		fireAttributeChangeEvent(ELECTRICITY_CONSUMED_ATTRIBUTE);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextWaterReservoirVolume = Math.min(maxWaterReservoirVolume, 
				waterReservoirVolume + waterReservoirRechargeRate 
				- getReservoirWaterWithdrawals() 
				- getWaterFromArtesianWell());
		if(nextWaterReservoirVolume < 0) {
			throw new IllegalStateException(
					"Water reservoir volume cannot be negative.");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tock() {
		waterReservoirVolume = nextWaterReservoirVolume;
	}
}
