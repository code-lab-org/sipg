package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.PopulationModel;
import edu.mit.sips.core.water.DefaultWaterSystem;

public class City extends DefaultSociety implements Society {
	/**
	 * Instantiates a new city.
	 */
	protected City() {
		
	}
	
	/**
	 * Instantiates a new city.
	 *
	 * @param name the name
	 * @param populationModel the population model
	 * @param initialWaterConsumptionPerCapita the initial water consumption per capita
	 * @param coastal the coastal
	 * @param arableLandArea the arable land area
	 * @param maxWaterReservoir the max water reservoir
	 * @param initialWaterReservoir the initial water reservoir
	 * @param waterReservoirRechargeRate the water reservoir recharge rate
	 * @param maxPetroleumReservoir the max petroleum reservoir
	 * @param initialPetroleumReservoir the initial petroleum reservoir
	 */
	public City(String name, PopulationModel populationModel, 
			double initialWaterConsumptionPerCapita, boolean coastal, 
			double arableLandArea, double maxWaterReservoir, 
			double initialWaterReservoir, double waterReservoirRechargeRate, 
			double maxPetroleumReservoir, double initialPetroleumReservoir) {
		super(name, new ArrayList<Society>(), 
				new DefaultAgricultureSystem.Local(arableLandArea),
				new DefaultWaterSystem.Local(coastal, maxWaterReservoir,
						initialWaterReservoir, waterReservoirRechargeRate, 
						initialWaterConsumptionPerCapita),
						new DefaultEnergySystem.Local(maxPetroleumReservoir,
								initialPetroleumReservoir),
								new DefaultSocialSystem.Local(populationModel));
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultSociety#getCities()
	 */
	@Override
	public List<City> getCities() {
		return Collections.unmodifiableList(Arrays.asList(this));
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getCountry()
	 */
	@Override
	public Country getCountry() {
		if(getSociety() == null) {
			throw new IllegalStateException("Society cannot be null.");
		}
		return getSociety().getCountry();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getGlobals()
	 */
	@Override
	public Globals getGlobals() {
		return getCountry().getGlobals();
	}
}
