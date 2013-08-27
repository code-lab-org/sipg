package edu.mit.sips.core;


/**
 * The Interface DomesticProductionModel.
 */
public interface DomesticProductionModel {
	
	/**
	 * Gets the domestic production.
	 *
	 * @return the domestic production
	 */
	public double getDomesticProduction();
	
	/**
	 * Sets the infrastructure system.
	 *
	 * @param infrastructureSystem the new infrastructure system
	 */
	public void setInfrastructureSystem(InfrastructureSystem.Local infrastructureSystem);
}
