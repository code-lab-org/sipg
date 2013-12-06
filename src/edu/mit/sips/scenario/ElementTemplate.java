package edu.mit.sips.scenario;

import edu.mit.sips.core.InfrastructureElement;

/**
 * The Interface ElementTemplate.
 */
public interface ElementTemplate {
	
	/**
	 * Creates the element.
	 *
	 * @param timeInitialized the time initialized
	 * @param origin the origin
	 * @param destination the destination
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long timeInitialized,
			String origin, String destination);
	
	/**
	 * Creates the element.
	 *
	 * @param timeInitialized the time initialized
	 * @param operationsDuration the operations duration
	 * @param origin the origin
	 * @param destination the destination
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long timeInitialized,
			long operationsDuration, String origin, String destination);
	
	/**
	 * Gets the max operations.
	 *
	 * @return the max operations
	 */
	public long getMaxOperations();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public Sector getSector();
	
	/**
	 * Gets the time available.
	 *
	 * @return the time available
	 */
	public long getTimeAvailable();

	/**
	 * Checks if is transport.
	 *
	 * @return true, if is transport
	 */
	public boolean isTransport();
}
