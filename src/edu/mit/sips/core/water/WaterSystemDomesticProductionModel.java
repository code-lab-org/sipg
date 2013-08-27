package edu.mit.sips.core.water;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class WaterSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private WaterSystem.Local waterSystem;
	private final double privateConsumptionFromWaterProduction;
	
	/**
	 * Instantiates a new water system domestic production model.
	 */
	protected WaterSystemDomesticProductionModel() {
		privateConsumptionFromWaterProduction = 0;
	}
	
	/**
	 * Instantiates a new water system domestic production model.
	 *
	 * @param privateConsumptionFromWaterProduction the private consumption from water production
	 */
	public WaterSystemDomesticProductionModel(
			double privateConsumptionFromWaterProduction) {
		this.privateConsumptionFromWaterProduction = privateConsumptionFromWaterProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(waterSystem != null) {
			return super.getDomesticProduction() 
					+ privateConsumptionFromWaterProduction
							* (waterSystem.getWaterProduction() 
									+ waterSystem.getWaterFromArtesianWell());
		}
		return super.getDomesticProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#setInfrastructureSystem(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local waterSystem) {
		super.setInfrastructureSystem(waterSystem);
		if(waterSystem instanceof WaterSystem.Local) {
			this.waterSystem = (WaterSystem.Local) waterSystem;
		} else {
			throw new IllegalArgumentException("Infrastructure system must be a water system.");
		}
	}
}
