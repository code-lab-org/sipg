package edu.mit.sips.core;


/**
 * The Class DefaultDomesticProductionModel.
 */
public class DefaultDomesticProductionModel implements DomesticProductionModel {
	private InfrastructureSystem.Local infrastructureSystem;

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.social.DomesticProductionModel#getDomesticProduct()
	 */
	@Override
	public double getDomesticProduction() {
		if(infrastructureSystem != null) {
			// expense method:
			// GDP = C + I + G + (X - M)
			// C: private consumption (not included here)
			// I: gross investment (capital and decommission expense)
			// G: government spending (operational expense)
			// X: exports (distribution and export revenue)
			// M: imports (distribution and import expense)
			return infrastructureSystem.getCapitalExpense() 
					+ infrastructureSystem.getDecommissionExpense()
					+ infrastructureSystem.getOperationsExpense() 
					+ infrastructureSystem.getConsumptionExpense()
					+ infrastructureSystem.getDistributionRevenue() 
					+ infrastructureSystem.getExportRevenue()
					- infrastructureSystem.getDistributionExpense() 
					- infrastructureSystem.getImportExpense();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.production.DomesticProductionModel#setInfrastructureSystem(edu.mit.sips.core.InfrastructureSystem.Local)
	 */
	@Override
	public void setInfrastructureSystem(InfrastructureSystem.Local infrastructureSystem) { 
		this.infrastructureSystem = infrastructureSystem;
	}
}
