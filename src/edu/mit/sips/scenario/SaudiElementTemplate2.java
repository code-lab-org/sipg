package edu.mit.sips.scenario;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;

public enum SaudiElementTemplate2 implements ElementTemplate {
	WHEAT_1			(Sector.AGRICULTURE,	false, 	"Small Wheat Field", 			0,	9999),
	WHEAT_2			(Sector.AGRICULTURE,	false, 	"Large Wheat Field", 			0,	9999),
	FOOD_TRANSPORT_1(Sector.AGRICULTURE,	true, 	"Low-volume Food Transport", 	0,	9999),
	FOOD_TRANSPORT_2(Sector.AGRICULTURE,	true, 	"High-volume Food Transport", 	0,	9999);
	
	private final Sector sector;
	private final boolean transport;
	private final String name;
	private final long timeAvailable;
	private final long maxOperations;
	private static int instanceId = 0;

	/**
	 * Instantiates a new element template.
	 *
	 * @param sector the sector
	 * @param transport the transport
	 * @param name the name
	 * @param yearAvailable the year available
	 * @param maxOperations the max operations
	 */
	private SaudiElementTemplate2(Sector sector, boolean transport, 
			String name, long yearAvailable, long maxOperations) {
		this.sector = sector;
		this.transport = transport;
		this.name = name;
		this.timeAvailable = yearAvailable;
		this.maxOperations = maxOperations;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.ElementTemplate#createElement(long, java.lang.String, java.lang.String)
	 */
	@Override
	public InfrastructureElement createElement(long year, 
			String location, String destination) {
		return createElement(year, year+maxOperations, location, destination);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElementTemplate#getMaxOperations()
	 */
	@Override
	public long getMaxOperations() {
		return maxOperations;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElementTemplate#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElementTemplate#getSector()
	 */
	@Override
	public Sector getSector() {
		return sector;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElementTemplate#getTimeAvailable()
	 */
	@Override
	public long getTimeAvailable() {
		return timeAvailable;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.scenario.ElementTemplate#isTransport()
	 */
	@Override
	public boolean isTransport() {
		return transport;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public InfrastructureElement createElement(long year,
			long endYear, String location, String destination) {
		if(year < timeAvailable) {
			throw new IllegalArgumentException(
					"Element not available before " + timeAvailable + ".");
		}
		if(endYear-year > maxOperations) {
			throw new IllegalArgumentException(
					"Element max operations is " + maxOperations + ".");
		}
		switch(this) {
		case WHEAT_1:
			return DefaultAgricultureElement.createProductionElement(
					name, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					500, 500, 5.5/365*239005736, 0, 1.1e6, 65);
		case WHEAT_2:
			return DefaultAgricultureElement.createProductionElement(
					name, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					1000, 1000, 5.5/365*239005736, 0, 1.1e6, 65);
		case FOOD_TRANSPORT_1:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					0.90, 2./365*239005736*1000, 0, 0);
		case FOOD_TRANSPORT_2:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year,  0, 
							0, 0, 0, true), 
					0.90, 10./365*239005736*1000, 0, 0);
		default:
			throw new IllegalArgumentException(
					"Unknown element template.");
		}
	}
}

