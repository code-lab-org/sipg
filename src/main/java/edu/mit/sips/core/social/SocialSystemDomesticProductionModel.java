package edu.mit.sips.core.social;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class SocialSystemDomesticProductionModel.
 */
public class SocialSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private final double privateConsumptionFromFoodConsumption;
	private final double privateConsumptionFromWaterConsumption;
	private final double privateConsumptionFromElectricityConsumption;
	
	/**
	 * Instantiates a new social system domestic production model.
	 */
	protected SocialSystemDomesticProductionModel() {
		privateConsumptionFromFoodConsumption = 0;
		privateConsumptionFromWaterConsumption = 0;
		privateConsumptionFromElectricityConsumption = 0;
	}
	
	/**
	 * Instantiates a new social system domestic production model.
	 *
	 * @param privateConsumptionFromFoodConsumption the private consumption from food consumption
	 * @param privateConsumptionFromWaterConsumption the private consumption from water consumption
	 * @param privateConsumptionFromElectricityConsumption the private consumption from electricity consumption
	 */
	public SocialSystemDomesticProductionModel(
			double privateConsumptionFromFoodConsumption,
			double privateConsumptionFromWaterConsumption,
			double privateConsumptionFromElectricityConsumption) {
		this.privateConsumptionFromFoodConsumption = privateConsumptionFromFoodConsumption;
		this.privateConsumptionFromWaterConsumption = privateConsumptionFromWaterConsumption;
		this.privateConsumptionFromElectricityConsumption = privateConsumptionFromElectricityConsumption;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultDomesticProductionModel#getDomesticProduction(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem) {
		if(infrastructureSystem instanceof SocialSystem.Local) {
			return super.getDomesticProduction(infrastructureSystem) 
					+ (((SocialSystem.Local)infrastructureSystem).getSociety().getAgricultureSystem().getFoodDomesticPrice()
					+ privateConsumptionFromFoodConsumption)
					* ((SocialSystem.Local)infrastructureSystem).getFoodConsumption()
					+ (((SocialSystem.Local)infrastructureSystem).getSociety().getWaterSystem().getWaterDomesticPrice()
					+ privateConsumptionFromWaterConsumption)
					* ((SocialSystem.Local)infrastructureSystem).getWaterConsumption()
					+ (((SocialSystem.Local)infrastructureSystem).getSociety().getElectricitySystem().getElectricityDomesticPrice() 
					+ privateConsumptionFromElectricityConsumption)
					* ((SocialSystem.Local)infrastructureSystem).getElectricityConsumption();
		} else {
			return super.getDomesticProduction(infrastructureSystem);
		}
	}
}
