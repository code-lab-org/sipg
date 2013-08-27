package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class PetroleumSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private final double privateConsumptionFromPetroleumProduction;
	
	/**
	 * Instantiates a new water system domestic production model.
	 */
	protected PetroleumSystemDomesticProductionModel() {
		privateConsumptionFromPetroleumProduction = 0;
	}
	
	/**
	 * Instantiates a new water system domestic production model.
	 *
	 * @param privateconsumptionFromPetroleumProduction the private consumption from petroleum production
	 */
	public PetroleumSystemDomesticProductionModel(
			double privateConsumptionFromPetroleumProduction) {
		this.privateConsumptionFromPetroleumProduction = privateConsumptionFromPetroleumProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultDomesticProductionModel#getDomesticProduction(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem) {
		if(infrastructureSystem instanceof PetroleumSystem.Local) {
			return super.getDomesticProduction(infrastructureSystem) 
					+ privateConsumptionFromPetroleumProduction
					* ((PetroleumSystem.Local)infrastructureSystem).getPetroleumProduction();
		} else {
			return super.getDomesticProduction(infrastructureSystem);
		}
	}
}
