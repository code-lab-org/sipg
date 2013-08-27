package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class AgricultureSystemDomesticProductionModel extends DefaultDomesticProductionModel {
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
	 * @see edu.mit.sips.core.DefaultDomesticProductionModel#getDomesticProduction(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem) {
		if(infrastructureSystem instanceof AgricultureSystem.Local) {
			return super.getDomesticProduction(infrastructureSystem) 
					+ privateConsumptionFromFoodProduction
					* ((AgricultureSystem.Local)infrastructureSystem).getFoodProduction();
		} else {
			return super.getDomesticProduction(infrastructureSystem);
		}
	}
}
