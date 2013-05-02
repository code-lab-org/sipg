package edu.mit.sips;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;
import edu.mit.sips.core.water.DefaultWaterElement;

/**
 * The Enum ElementTemplate.
 */
public enum ElementTemplate {
	LIVESTOCK_1		(Sector.AGRICULTURE,	false, 	"Small Livestock Field", 			1900,	200),
	LIVESTOCK_2		(Sector.AGRICULTURE,	false, 	"Large Livestock Field", 			1900,	200),
	DATES_1			(Sector.AGRICULTURE,	false, 	"Small Date Field", 				1900,	200),
	DATES_2			(Sector.AGRICULTURE,	false, 	"Large Date Field",					1900,	200),
	GRAINS_1		(Sector.AGRICULTURE,	false, 	"Small Grain Field", 				1970,	200),
	GRAINS_2		(Sector.AGRICULTURE,	false, 	"Large Grain Field", 				1970,	200),
	FOOD_TRANSPORT_1(Sector.AGRICULTURE,	true, 	"Default Food Transport", 			1900,	200),
	AQUIFER_PUMP_1	(Sector.WATER, 			false, 	"Small Aquifer Pump", 				1900,	50),
	AQUIFER_PUMP_2	(Sector.WATER, 			false, 	"Medium Aquifer Pump", 				1900,	50),
	AQUIFER_PUMP_3	(Sector.WATER, 			false, 	"Large Aquifer Pump", 				1900,	50),
	TD_DESAL_1		(Sector.WATER, 			false, 	"Thermal Distillation Desal. Plant",1960,	40),
	TD_DESAL_2		(Sector.WATER, 			false, 	"Improved TD Desal. Plant", 		1980,	40),
	TD_DESAL_3		(Sector.WATER, 			false, 	"Advanced TD Desal. Plant", 		2000,	40),
	RO_DESAL_1		(Sector.WATER, 			false, 	"Reverse Osmosis Desal. Plant",		1970,	40),
	RO_DESAL_2		(Sector.WATER, 			false, 	"Improved RO Desal. Plant", 		1990,	40),
	RO_DESAL_3		(Sector.WATER, 			false, 	"Advanced RO Desal. Plant", 		2010,	40),
	WATER_PIPELINE_1(Sector.WATER, 			true, 	"Small Water Pipeline", 			1950,	50),
	WATER_PIPELINE_2(Sector.WATER, 			true, 	"Medium Water Pipeline", 			1950,	50),
	WATER_PIPELINE_3(Sector.WATER, 			true, 	"Large Water Pipeline", 			1950,	50);
	
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
	private ElementTemplate(Sector sector, boolean transport, 
			String name, long yearAvailable, long maxOperations) {
		this.sector = sector;
		this.transport = transport;
		this.name = name;
		this.timeAvailable = yearAvailable;
		this.maxOperations = maxOperations;
	}
	
	/**
	 * Creates the element.
	 *
	 * @param year the year
	 * @param location the location
	 * @param destination the destination
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long year, 
			String location, String destination) {
		if(year < timeAvailable) {
			throw new IllegalArgumentException(
					"Element not available before " + timeAvailable + ".");
		}
		switch(this) {
		case LIVESTOCK_1:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							62.5e3, 6.25e3, 12.5e3, true), 
					250, 0, AgricultureProduct.LIVESTOCK);
		case LIVESTOCK_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							220.0e3, 22.0e3, 44.0e3, true), 
					1000, 0, AgricultureProduct.LIVESTOCK);
		case DATES_1:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							12.5e6, 1.25e6, 2.5e6, true), 
					250, 0, AgricultureProduct.DATES);
		case DATES_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							49.0e6, 4.9e6, 9.8e6, true), 
					1000, 0, AgricultureProduct.DATES);
		case GRAINS_1:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							50e6, 5e6, 10e6, true), 
					500, 0, AgricultureProduct.GRAINS);
		case GRAINS_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 200, 1, 
							237.5e6, 2.375e6, 47.5e6, true), 
					2500, 0, AgricultureProduct.GRAINS);
		case FOOD_TRANSPORT_1:
			return DefaultAgricultureElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 200, 2, 
							100e6, 10e6, 20e6, true), 
					0.95, 1e9, 0, 25);
		case AQUIFER_PUMP_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 50, 1, 
							30e6, 3e6, 6e6, true), 
					1, 1e6, 0, 0.0053, 3);
		case AQUIFER_PUMP_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, 50, 1, 
							125e6, 12.5e6, 25e6, true), 
					1, 5e6, 0, 0.0053, 2.75);
		case AQUIFER_PUMP_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, 50, 1, 
							125e6, 12.5e6, 25e6, true), 
					1, 25e6, 0, 0.0053, 2.5);
		case TD_DESAL_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							4e9, 400e6, 800e6, true), 
					0, 10e6, 0, 0.0533, 15);
		case TD_DESAL_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							7.5e9, 750e6, 1.5e9, true), 
					0, 25e6, 0, 0.048, 12);
		case TD_DESAL_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							10e9, 1e9, 2e9, true), 
					0, 50e6, 0, 0.04, 10);
		case RO_DESAL_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							7e9, 700e6, 1.4e9, true), 
					0, 20e6, 0, 0.04, 25);
		case RO_DESAL_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							12.5e9, 1.25e9, 2.5e9, true), 
					0, 50e6, 0, 0.032, 20);
		case RO_DESAL_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, 40, 2, 
							15e9, 1.5e9, 3e9, true), 
					0, 100e6, 0, 0.0267, 18);
		case WATER_PIPELINE_1:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, 50, 1, 
							500e6, 50e6, 100e6, true), 
					0.75, 25e6, 0, 0.01, 2);
		case WATER_PIPELINE_2:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, 50, 1, 
							1e9, 100e6, 200e6, true), 
					0.8, 50e6, 0, 0.01, 1.75);
		case WATER_PIPELINE_3:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, 50, 1, 
							2e9, 200e6, 400e6, true), 
					0.85, 100e6, 0, 0.01, 1.5);
			
		default:
			throw new IllegalArgumentException(
					"Unknown element template.");
		}
	}
	
	/**
	 * Gets the max operations.
	 *
	 * @return the max operations
	 */
	public long getMaxOperations() {
		return maxOperations;
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
	 * Gets the time available.
	 *
	 * @return the time available
	 */
	public long getTimeAvailable() {
		return timeAvailable;
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