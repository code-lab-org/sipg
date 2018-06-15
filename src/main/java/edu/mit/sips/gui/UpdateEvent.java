package edu.mit.sips.gui;

import java.util.EventObject;

import edu.mit.sips.core.Country;

/**
 * The Class UpdateEvent.
 */
public class UpdateEvent extends EventObject {
	private static final long serialVersionUID = 5248912198162523521L;
	
	private final long time;
	private final Country country;
	
	/**
	 * Instantiates a new update event.
	 *
	 * @param source the source
	 * @param country the country
	 */
	public UpdateEvent(Object source, long time, Country country) {
		super(source);
		this.time = time;
		this.country = country;
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}
}
