package edu.mit.sips.core;

/**
 * The Interface SimEntity.
 */
public interface SimEntity {
	
	/**
	 * Initialize.
	 *
	 * @param time the time
	 */
	public void initialize(long time);
	
	/**
	 * Tick (calculates state updates which are stored in temporary variables).
	 */
	public void tick();
	
	/**
	 * Tock (commits the updates calculated during the tick phase).
	 */
	public void tock();
}
