package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class AgricultureSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private AgricultureSystem.Local agricultureSystem;
	private final double privateConsumptionFromFoodProduction;
	
	/**
	 * Instantiates a new water system domestic production model.
	 */
	protected AgricultureSystemDomesticProductionModel() {
		privateConsumptionFromFoodProduction = 0;
	}
	
	/**
	 * Instantiates a new water system domestic production model.
	 *
	 * @param privateConsumptionFromFoodProduction the private consumption from food production
	 */
	public AgricultureSystemDomesticProductionModel(
			double privateConsumptionFromFoodProduction) {
		this.privateConsumptionFromFoodProduction = privateConsumptionFromFoodProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(agricultureSystem != null) {
			return super.getDomesticProduction() 
					+ privateConsumptionFromFoodProduction
					* agricultureSystem.getFoodProduction();
		}
		return super.getDomesticProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#setInfrastructureSystem(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local agricultureSystem) {
		super.setInfrastructureSystem(agricultureSystem);
		if(agricultureSystem instanceof AgricultureSystem.Local) {
			this.agricultureSystem = (AgricultureSystem.Local) agricultureSystem;
		} else {
			throw new IllegalArgumentException("Infrastructure system must be a agriculture system.");
		}
	}
}
