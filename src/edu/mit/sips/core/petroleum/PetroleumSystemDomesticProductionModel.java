package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class PetroleumSystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private PetroleumSystem.Local petroleumSystem;
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
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(petroleumSystem != null) {
			return super.getDomesticProduction() 
					+ privateConsumptionFromPetroleumProduction
					* petroleumSystem.getPetroleumProduction();
		}
		return super.getDomesticProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#setInfrastructureSystem(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local petroleumSystem) {
		super.setInfrastructureSystem(petroleumSystem);
		if(petroleumSystem instanceof PetroleumSystem.Local) {
			this.petroleumSystem = (PetroleumSystem.Local) petroleumSystem;
		} else {
			throw new IllegalArgumentException("Infrastructure system must be a petroleum system.");
		}
	}
}
