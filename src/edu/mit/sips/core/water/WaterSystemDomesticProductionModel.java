package edu.mit.sips.core.water;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class WaterSystemDomesticProductionModel extends DefaultDomesticProductionModel {
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
	 * @see edu.mit.sips.core.DefaultDomesticProductionModel#getDomesticProduction(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem) {
		if(infrastructureSystem instanceof WaterSystem.Local) {
			return super.getDomesticProduction(infrastructureSystem) 
					+ privateConsumptionFromWaterProduction
					* (((WaterSystem.Local)infrastructureSystem).getWaterProduction()
							+ ((WaterSystem.Local)infrastructureSystem).getWaterFromArtesianWell());
		} else {
			return super.getDomesticProduction(infrastructureSystem);
		}
	}
}
