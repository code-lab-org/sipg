package edu.mit.sips.core.electricity;

import edu.mit.sips.core.DefaultDomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class WaterSystemDomesticProductionModel.
 */
public class ElectricitySystemDomesticProductionModel extends DefaultDomesticProductionModel {
	private ElectricitySystem.Local electricitySystem;
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
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(electricitySystem != null) {
			return super.getDomesticProduction() 
					+ privateConsumptionFromElectricityProduction
					* (electricitySystem.getElectricityProduction() 
							+ electricitySystem.getElectricityFromBurningPetroleum());
		}
		return super.getDomesticProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DefaultDomesticProductionModel#setInfrastructureSystem(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local electricitySystem) {
		super.setInfrastructureSystem(electricitySystem);
		if(electricitySystem instanceof ElectricitySystem.Local) {
			this.electricitySystem = (ElectricitySystem.Local) electricitySystem;
		} else {
			throw new IllegalArgumentException("Infrastructure system must be a electricity system.");
		}
	}
}
