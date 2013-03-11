package edu.mit.sips.core;

/**
 * The Interface MutableInfrastructureElement.
 */
public interface MutableInfrastructureElement {
	
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
	 * Creates the element.
	 *
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement();
}
