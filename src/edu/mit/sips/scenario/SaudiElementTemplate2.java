package edu.mit.sips.scenario;

import java.util.Arrays;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;
import edu.mit.sips.core.water.DefaultWaterElement;

public enum SaudiElementTemplate2 implements ElementTemplate {
	WHEAT_1			(Sector.AGRICULTURE,	false, 	"Small Wheat Field", 			0,		9999),
	WHEAT_2			(Sector.AGRICULTURE,	false, 	"Large Wheat Field", 			0,		9999),
	FOOD_TRANSPORT_1(Sector.AGRICULTURE,	true, 	"Low-volume Food Transport", 	0,		9999),
	FOOD_TRANSPORT_2(Sector.AGRICULTURE,	true, 	"High-volume Food Transport", 	0,		9999),
	RO_PLANT_1		(Sector.WATER,			false,	"Small RO Plant",				1970,	50),
	RO_PLANT_2		(Sector.WATER,			false,	"Large RO Plant",				1980,	50),
	WATER_PIPELINE_1(Sector.WATER,			true, 	"Low-volume Water Pipeline", 	1950,	100),
	WATER_PIPELINE_2(Sector.WATER,			true, 	"High-volume Water Pipeline", 	1950,	100);
	
	private final Sector sector;
	private final boolean transport;
	private final String name;
	private final long timeAvailable;
	private final long maxOperations;
	private static int[] instanceId = new int[SaudiElementTemplate2.values().length];
	
	/**
	 * Gets the instance id.
	 *
	 * @param template the template
	 * @return the instance id
	 */
	private static int getInstanceId(SaudiElementTemplate2 template) {
		return ++instanceId[Arrays.asList(SaudiElementTemplate2.values()).indexOf(template)];
	}

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
					name, name + " " + getInstanceId(WHEAT_1), location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					500, 500, 5./365*239005736, 0, 1.e6, 60);
		case WHEAT_2:
			return DefaultAgricultureElement.createProductionElement(
					name, name + " " + getInstanceId(WHEAT_2), location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					1000, 1000, 5./365*239005736, 0, 1.e6, 60);
		case FOOD_TRANSPORT_1:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + getInstanceId(FOOD_TRANSPORT_1), location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					0.90, 2./365*239005736*1000, 0, 0);
		case FOOD_TRANSPORT_2:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + getInstanceId(FOOD_TRANSPORT_2), location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year,  0, 
							0, 0, 0, true), 
					0.95, 10./365*239005736*1000, 0, 0);
		case RO_PLANT_1:
			return DefaultWaterElement.createProductionElement(
					name, name + " " + getInstanceId(RO_PLANT_1), location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, endYear-year, 1, 
							0, 0, 0, true), 
					0.0, 50e6, 50e6, 5.5e-3, 0, true);
		case RO_PLANT_2:
			return DefaultWaterElement.createProductionElement(
					name, name + " " + getInstanceId(RO_PLANT_2), location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, endYear-year, 1, 
							0, 0, 0, true), 
					0.0, 150e6, 150e6, 4.5e-3, 0, true);
		case WATER_PIPELINE_1:
			return DefaultWaterElement.createDistributionElement(
					name, name + " " + getInstanceId(WATER_PIPELINE_1), location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					0.85, 50e6, 50e6, 2.0e-3, 0);
		case WATER_PIPELINE_2:
			return DefaultWaterElement.createDistributionElement(
					name, name + " " + getInstanceId(WATER_PIPELINE_2), location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, endYear-year, 0, 
							0, 0, 0, true), 
					0.90, 150e6, 150e6, 2.0e-3, 0);
		default:
			throw new IllegalArgumentException(
					"Unknown element template.");
		}
	}
}

