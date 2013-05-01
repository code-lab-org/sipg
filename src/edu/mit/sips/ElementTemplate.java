package edu.mit.sips;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;

/**
 * The Enum ElementTemplate.
 */
public enum ElementTemplate {
	SMALL_LIVESTOCK(Sector.AGRICULTURE, false, "Small Livestock Field", 1900),
	LARGE_LIVESTOCK(Sector.AGRICULTURE, false, "Large Livestock Field", 1900),
	SMALL_DATES(Sector.AGRICULTURE, false, "Small Dates Field", 1900),
	LARGE_DATES(Sector.AGRICULTURE, false, "Large Dates Field", 1900),
	SMALL_GRAINS(Sector.AGRICULTURE, false, "Small Grains Field", 1970),
	LARGE_GRAINS(Sector.AGRICULTURE, false, "Large Grains Field", 1970);
	
	private final Sector sector;
	private final boolean transport;
	private final String name;
	private final long yearAvailable;
	private static int instanceId = 0;
	
	/**
	 * Instantiates a new element template.
	 *
	 * @param sector the sector
	 * @param transport the transport
	 * @param name the name
	 * @param yearAvailable the year available
	 */
	private ElementTemplate(Sector sector, boolean transport, 
			String name, long yearAvailable) {
		this.sector = sector;
		this.transport = transport;
		this.name = name;
		this.yearAvailable = yearAvailable;
	}
	
	/**
	 * Creates the element.
	 *
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long year, String city) {
		if(year < yearAvailable) {
			throw new IllegalArgumentException(
					"Element not available before " + yearAvailable + ".");
		}
		switch(this) {
		case SMALL_LIVESTOCK:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							62.5e3, 6.25e3, 12.5e3, true), 
					250, 0, AgricultureProduct.LIVESTOCK);
		case LARGE_LIVESTOCK:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							220.0e3, 22.0e3, 44.0e3, true), 
					1000, 0, AgricultureProduct.LIVESTOCK);
		case SMALL_DATES:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							12.5e6, 1.25e6, 2.5e6, true), 
					250, 0, AgricultureProduct.DATES);
		case LARGE_DATES:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							49.0e6, 4.9e6, 9.8e6, true), 
					1000, 0, AgricultureProduct.DATES);
		case SMALL_GRAINS:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							50e6, 5e6, 10e6, true), 
					500, 0, AgricultureProduct.GRAINS);
		case LARGE_GRAINS:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, city, city, 
					new SimpleLifecycleModel(year, 1, 100, 1, 
							237.5e6, 2.375e6, 47.5e6, true), 
					2500, 0, AgricultureProduct.GRAINS);
		default:
			throw new IllegalArgumentException(
					"Unknown element template.");
		}
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public Sector getSector() {
		return sector;
	}
	
	/**
	 * Gets the year available.
	 *
	 * @return the year available
	 */
	public long getYearAvailable() {
		return yearAvailable;
	}
	
	/**
	 * Checks if is transport.
	 *
	 * @return true, if is transport
	 */
	public boolean isTransport() {
		return transport;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
}