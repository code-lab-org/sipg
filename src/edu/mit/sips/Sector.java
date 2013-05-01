package edu.mit.sips;

/**
 * The Enum Sector.
 */
public enum Sector { 
	AGRICULTURE("Agriculture"), 
	WATER("Water"),
	ENERGY("Energy");
	
	private final String name;
	
	/**
	 * Instantiates a new sector.
	 *
	 * @param name the name
	 */
	private Sector(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
};