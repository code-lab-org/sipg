package edu.mit.sips.core;


/**
 * The Interface DomesticProductionModel.
 */
public interface DomesticProductionModel {
	
	/**
	 * Gets the domestic production.
	 *
	 * @param infrastructureSystem the infrastructure system
	 * @return the domestic production
	 */
	public double getDomesticProduction(InfrastructureSystem.Local infrastructureSystem);
}
