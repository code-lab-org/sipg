package edu.mit.sips.core.electricity;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class ElectricitySystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private final double privateConsumptionFromElectricityProduction;
	
	/**
	 * Instantiates a new water system domestic production model.
	 */
	protected ElectricitySystemDomesticProductionModel() {
		privateConsumptionFromElectricityProduction = 0;
	}
	
	/**
	 * Instantiates a new water system domestic production model.
	 *
	 * @param privateConsumptionFromElectricityProduction the private consumption from electricity production
	 */
	public ElectricitySystemDomesticProductionModel(
			double privateConsumptionFromElectricityProduction) {
		this.privateConsumptionFromElectricityProduction = privateConsumptionFromElectricityProduction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultDomesticProductionModel#getDomesticProduction(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem) {
		if(infrastructureSystem instanceof ElectricitySystem.Local) {
			return super.getDomesticProduction(infrastructureSystem) 
					+ privateConsumptionFromElectricityProduction
					* (((ElectricitySystem.Local)infrastructureSystem).getElectricityProduction() 
							+ ((ElectricitySystem.Local)infrastructureSystem).getElectricityFromBurningPetroleum());
		} else {
			return super.getDomesticProduction(infrastructureSystem);
		}
	}
}
