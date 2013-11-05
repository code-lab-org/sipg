package edu.mit.sips.core;

import edu.mit.sips.sim.util.CurrencyUnitsOutput;


/**
 * The Interface MutableInfrastructureElement.
 */
public interface MutableInfrastructureElement extends CurrencyUnitsOutput {
	
	/**
	 * Creates the element.
	 *
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement();
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public String getDestination();
	
	/**
	 * Gets the lifecycle model.
	 *
	 * @return the lifecycle model
	 */
	public MutableLifecycleModel getLifecycleModel();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public String getOrigin();
	
	/**
	 * Gets the template name.
	 *
	 * @return the template name
	 */
	public String getTemplateName();
	
	/**
	 * Sets the destination.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(String destination);
	
	/**
	 * Sets the lifecycle model.
	 *
	 * @param lifecycleModel the new lifecycle model
	 */
	public void setLifecycleModel(MutableLifecycleModel lifecycleModel);
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name);
	
	/**
	 * Sets the origin.
	 *
	 * @param origin the new origin
	 */
	public void setOrigin(String origin);

	/**
	 * Sets the template name.
	 *
	 * @param templateName the new template name
	 */
	public void setTemplateName(String templateName);
}
