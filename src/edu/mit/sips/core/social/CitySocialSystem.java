package edu.mit.sips.core.social;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class CitySocialSystem.
 */
public class CitySocialSystem extends DefaultSocialSystem.Local {
	private final PopulationModel populationModel;
	private double domesticProduct, nextDomesticProduct;
	
	/**
	 * Instantiates a new local.
	 *
	 * @param populationModel the population model
	 */
	public CitySocialSystem(PopulationModel populationModel) {
		// Validate population model.
		if(populationModel == null) {
			throw new IllegalArgumentException(
					"Population model cannot be null.");
		}
		this.populationModel = populationModel;
	}
	
	/**
	 * Instantiates a new city social system.
	 */
	protected CitySocialSystem() {
		this.populationModel = new DefaultPopulationModel();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SocialSystem#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduct() {
		return domesticProduct;
	}

	/**
	 * Gets the next economic production.
	 *
	 * @return the next economic production
	 */
	private double getNextEconomicProduction() {
		double economicProduction = 0;
		for(InfrastructureSystem s : getSociety().getInfrastructureSystems()) {
			economicProduction += s.getDomesticProduction();
		}
		return economicProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SocialSystem#getPopulation()
	 */
	@Override
	public long getPopulation() {
		return populationModel.getPopulation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		populationModel.initialize(time);
		// note: initialize domestic product LAST as 
		// it depends on other initial values
		domesticProduct = getNextEconomicProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextDomesticProduct = getNextEconomicProduction();
		populationModel.tick();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		domesticProduct = nextDomesticProduct;
		populationModel.tock();
	}
}
