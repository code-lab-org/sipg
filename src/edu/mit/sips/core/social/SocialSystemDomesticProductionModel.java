package edu.mit.sips.core.social;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class SocialSystemDomesticProductionModel.
 */
public class SocialSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private SocialSystem.Local socialSystem;
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
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(socialSystem != null) {
			return super.getDomesticProduction() 
					+ (socialSystem.getSociety().getAgricultureSystem().getFoodDomesticPrice()
					+ privateConsumptionFromFoodConsumption)
					* socialSystem.getFoodConsumption()
					+ (socialSystem.getSociety().getWaterSystem().getWaterDomesticPrice()
					+ privateConsumptionFromWaterConsumption)
					* socialSystem.getWaterConsumption()
					+ (socialSystem.getSociety().getElectricitySystem().getElectricityDomesticPrice() 
					+ privateConsumptionFromElectricityConsumption)
					* socialSystem.getElectricityConsumption();
		}
		return super.getDomesticProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DomesticProductionModel#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local socialSystem) {
		super.setInfrastructureSystem(socialSystem);
		if(socialSystem instanceof SocialSystem.Local) {
			this.socialSystem = (SocialSystem.Local) socialSystem;
		} else {
			throw new IllegalArgumentException("Infrastructure sysetm must be a social system.");
		}
	}
}
