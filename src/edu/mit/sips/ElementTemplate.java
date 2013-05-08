package edu.mit.sips;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.SimpleLifecycleModel;
import edu.mit.sips.core.agriculture.AgricultureProduct;
import edu.mit.sips.core.agriculture.DefaultAgricultureElement;
import edu.mit.sips.core.energy.DefaultElectricityElement;
import edu.mit.sips.core.energy.DefaultPetroleumElement;
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
	WATER_PIPELINE_1(Sector.WATER, 			true, 	"Small Water Pipeline", 			1950,	200),
	WATER_PIPELINE_2(Sector.WATER, 			true, 	"Medium Water Pipeline", 			1950,	200),
	WATER_PIPELINE_3(Sector.WATER, 			true, 	"Large Water Pipeline", 			1950,	200),
	PETRO_WELL_1	(Sector.ENERGY, 		true, 	"Small Petroleum Well", 			1940,	200),
	PETRO_WELL_2	(Sector.ENERGY, 		true, 	"Medium Petroleum Well", 			1960,	200),
	PETRO_WELL_3	(Sector.ENERGY, 		true, 	"Large Petroleum Well", 			1980,	200),
	PETRO_PIPELINE_1(Sector.ENERGY, 		true, 	"Small Petroleum Pipeline", 		1940,	200),
	PETRO_PIPELINE_2(Sector.ENERGY, 		true, 	"Medium Petroleum Pipeline", 		1940,	200),
	PETRO_PIPELINE_3(Sector.ENERGY, 		true, 	"Large Petroleum Pipeline", 		1940,	200),
	POWER_PLANT_1	(Sector.ENERGY, 		true, 	"Small Oil Power Plant", 			1940,	40),
	POWER_PLANT_2	(Sector.ENERGY, 		true, 	"Large Oil Power Plant", 			1970,	40),
	PV_PLANT_1		(Sector.ENERGY, 		true, 	"Solar Photovoltaics Power Plant", 	1970,	25),
	CSP_PLANT_1		(Sector.ENERGY, 		true, 	"Concentraed Solar Power Plant", 	1980,	25),
	WIND_PLANT_1	(Sector.ENERGY, 		true, 	"Wind Power Plant", 				1980,	15),
	NUCLEAR_PLANT_1	(Sector.ENERGY, 		true, 	"Nuclear Power Plant", 				2020,	40),
	POWER_LINE_1	(Sector.ENERGY, 		true, 	"Low-capacity Power Line", 			1940,	200),
	POWER_LINE_2	(Sector.ENERGY, 		true, 	"High-capacity Power Line", 		1940,	200),
	;
	
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
							maxOperations, maxOperations, 1, 
							62.5e3, 6.25e3, 12.5e3, true), 
					500, 500, AgricultureProduct.LIVESTOCK);
		case LIVESTOCK_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							220.0e3, 22.0e3, 44.0e3, true), 
					2000, 2000, AgricultureProduct.LIVESTOCK);
		case DATES_1:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							12.5e6, 1.25e6, 2.5e6, true), 
					250, 250, AgricultureProduct.DATES);
		case DATES_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							49.0e6, 4.9e6, 9.8e6, true), 
					1000, 1000, AgricultureProduct.DATES);
		case GRAINS_1:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							50e6, 5e6, 10e6, true), 
					500, 500, AgricultureProduct.GRAINS);
		case GRAINS_2:
			return DefaultAgricultureElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							237.5e6, 2.375e6, 47.5e6, true), 
					2500, 2500, AgricultureProduct.GRAINS);
		case FOOD_TRANSPORT_1:
			return DefaultAgricultureElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							100e6, 10e6, 20e6, true), 
					0.95, 1e9, 0, 25);
		case AQUIFER_PUMP_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							30e6, 3e6, 6e6, true), 
					1, 1e6, 1e6, 0.0053, 3);
		case AQUIFER_PUMP_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 1, 
							125e6, 12.5e6, 25e6, true), 
					1, 5e6, 5e6, 0.0053, 2.75);
		case AQUIFER_PUMP_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							125e6, 12.5e6, 25e6, true), 
					1, 25e6, 25e6, 0.0053, 2.5);
		case TD_DESAL_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							4e9, 400e6, 800e6, true), 
					0, 10e6, 10e6, 0.0533, 15);
		case TD_DESAL_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							7.5e9, 750e6, 1.5e9, true), 
					0, 25e6, 25e6, 0.048, 12);
		case TD_DESAL_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							10e9, 1e9, 2e9, true), 
					0, 50e6, 50e6, 0.04, 10);
		case RO_DESAL_1:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							7e9, 700e6, 1.4e9, true), 
					0, 20e6, 20e6, 0.04, 25);
		case RO_DESAL_2:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							12.5e9, 1.25e9, 2.5e9, true), 
					0, 50e6, 50e6, 0.032, 20);
		case RO_DESAL_3:
			return DefaultWaterElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							15e9, 1.5e9, 3e9, true), 
					0, 100e6, 100e6, 0.0267, 18);
		case WATER_PIPELINE_1:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							500e6, 50e6, 100e6, true), 
					0.75, 25e6, 0, 0.01, 2);
		case WATER_PIPELINE_2:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							1e9, 100e6, 200e6, true), 
					0.8, 50e6, 0, 0.01, 1.75);
		case WATER_PIPELINE_3:
			return DefaultWaterElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							2e9, 200e6, 400e6, true), 
					0.85, 100e6, 0, 0.01, 1.5);
		case PETRO_WELL_1:
			return DefaultPetroleumElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 2, 
							50e6, 10e6, 20e6, true), 
					1, 1e6, 1e6, 10);
		case PETRO_WELL_2:
			return DefaultPetroleumElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 2, 
							125e6, 25e6, 50e6, true), 
					1, 3e6, 3e6, 10);
		case PETRO_WELL_3:
			return DefaultPetroleumElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 1, 
							maxOperations, maxOperations, 2, 
							300e6, 75e6, 125e6, true), 
					1, 10e6, 10e6, 8);
		case PETRO_PIPELINE_1:
			return DefaultPetroleumElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							100e6, 10e6, 10e6, true), 
					0.95, 100e3, 0, 0.1, 10);
		case PETRO_PIPELINE_2:
			return DefaultPetroleumElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							200e6, 20e6, 40e6, true), 
					0.975, 1e6, 0, 0.1, 8);
		case PETRO_PIPELINE_3:
			return DefaultPetroleumElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							400e6, 40e6, 80e6, true), 
					0.99, 10e6, 0, 0.1, 6);
		case POWER_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							75e6, 7.5e6, 18.75e6, true), 
					250e3, 250e3, 1.5, 0.05, 0.05);
		case POWER_PLANT_2:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							275e6, 27.5e6, 68.75e6, true), 
					1e6, 1e6, 1.4, 0.05, 0.05);
		case PV_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							375e6, 37.5e6, 75e6, true), 
					250e3, 250e3, 0, 0, 0);
		case CSP_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							300e6, 45e6, 90e6, true), 
					250e3, 250e3, 0, 0.05, 2);
		case WIND_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 5, 
							maxOperations, maxOperations, 2, 
							250e6, 25e6, 50e6, true), 
					500e3, 500e3, 0, 0, 1);
		case NUCLEAR_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					this, name + " " + ++instanceId, location, location, 
					new SimpleLifecycleModel(timeAvailable, year, 10, 
							maxOperations, maxOperations, 5, 
							3e9, 24e6, 240e6, true), 
					1e6, 1e6, 0, 0.1667, 0.01);
		case POWER_LINE_1:
			return DefaultElectricityElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							100e6, 10e6, 10e6, true), 
					0.80, 500e3, 0, 0);
		case POWER_LINE_2:
			return DefaultElectricityElement.createDistributionElement(
					this, name + " " + ++instanceId, location, destination, 
					new SimpleLifecycleModel(timeAvailable, year, 2, 
							maxOperations, maxOperations, 1, 
							1e9, 100e6, 100e6, true), 
					0.85, 5e6, 0, 0);
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
	
	/**
	 * Gets the templates.
	 *
	 * @param sectors the sectors
	 * @return the templates
	 */
	public static List<ElementTemplate> getTemplates(Collection<Sector> sectors) {
		List<ElementTemplate> templates = new ArrayList<ElementTemplate>();
		for(ElementTemplate template : values()) {
			if(sectors.contains(template.getSector())) {
				templates.add(template);
			}
		}
		return templates;
	}
}