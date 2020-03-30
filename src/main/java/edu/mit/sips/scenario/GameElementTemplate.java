/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.scenario;

import java.util.Arrays;

import edu.mit.sips.core.agriculture.DefaultAgricultureElement;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.electricity.DefaultElectricityElement;
import edu.mit.sips.core.lifecycle.DefaultSimpleLifecycleModel;
import edu.mit.sips.core.petroleum.DefaultPetroleumElement;
import edu.mit.sips.core.water.DefaultWaterElement;

/**
 * Enumeration of element templates used for the sustainable infrastructure planning game.
 * 
 * @author Paul T. Grogan
 */
public enum GameElementTemplate implements ElementTemplate {
	WHEAT_1			(Sector.AGRICULTURE,	false, 	"Small Wheat Field", 			1900,	100),
	WHEAT_2			(Sector.AGRICULTURE,	false, 	"Large Wheat Field", 			1950,	100),
	FOOD_TRANSPORT_1(Sector.AGRICULTURE,	true, 	"Low-volume Food Transport", 	1900,	100),
	FOOD_TRANSPORT_2(Sector.AGRICULTURE,	true, 	"High-volume Food Transport", 	1950,	100),
	RO_PLANT_1		(Sector.WATER,			false,	"Small RO Plant",				1970,	50),
	RO_PLANT_2		(Sector.WATER,			false,	"Large RO Plant",				1980,	50),
	RO_PLANT_3		(Sector.WATER,			false,	"Huge RO Plant",				1980,	50),
	WATER_PIPELINE_1(Sector.WATER,			true, 	"Low-volume Water Pipeline", 	1950,	100),
	WATER_PIPELINE_2(Sector.WATER,			true, 	"High-volume Water Pipeline", 	1950,	100),
	OIL_WELL_1		(Sector.PETROLEUM,		false,	"Small Oil Well",				1940,	100),
	OIL_WELL_2		(Sector.PETROLEUM,		false,	"Large Oil Well",				1970,	100),
	OIL_PIPELINE_1	(Sector.PETROLEUM,		true, 	"Low-volume Oil Pipeline", 		1940,	50),
	OIL_PIPELINE_2	(Sector.PETROLEUM,		true, 	"High-volume Oil Pipeline", 	1970,	50),
	POWER_PLANT_1	(Sector.ELECTRICITY,	false,	"Small Thermal Power Plant",	1950,	50),
	POWER_PLANT_2	(Sector.ELECTRICITY,	false,	"Large Thermal Power Plant",	1970,	50),
	PV_PLANT_1		(Sector.ELECTRICITY,	false,	"Small Solar PV Plant",			1980,	30),
	PV_PLANT_2		(Sector.ELECTRICITY,	false,	"Large Solar PV Plant",			1980,	30),
	POWER_LINE_1	(Sector.ELECTRICITY,	true, 	"Low-capacity Power Line",		1950,	50),
	POWER_LINE_2	(Sector.ELECTRICITY,	true, 	"High-capacity Power Line", 	1980,	50);
	
	private static int[] instanceId = new int[GameElementTemplate.values().length];
	
	/**
	 * Gets the instance id.
	 *
	 * @param template the template
	 * @return the instance id
	 */
	private static int getInstanceId(GameElementTemplate template) {
		return ++instanceId[Arrays.asList(GameElementTemplate.values()).indexOf(template)];
	}
	
	private final Sector sector;
	private final boolean transport;
	private final String name;
	private final long timeAvailable;
	private final long maxOperations;

	/**
	 * Instantiates a new element template.
	 *
	 * @param sector the sector
	 * @param transport the transport
	 * @param name the name
	 * @param yearAvailable the year available
	 * @param maxOperations the max operations
	 */
	private GameElementTemplate(Sector sector, boolean transport, 
			String name, long yearAvailable, long maxOperations) {
		this.sector = sector;
		this.transport = transport;
		this.name = name;
		this.timeAvailable = yearAvailable;
		this.maxOperations = maxOperations;
	}
	
	@Override
	public InfrastructureElement createElement(long year,
			long operationsDuration, String location, String destination) {
		if(year < timeAvailable) {
			throw new IllegalArgumentException(
					"Element not available before " + timeAvailable + ".");
		}
		if(operationsDuration > maxOperations) {
			throw new IllegalArgumentException(
					"Element max operations is " + maxOperations + ".");
		}
		switch(this) {
		case WHEAT_1:
			return DefaultAgricultureElement.createProductionElement(
					name, name + " " + getInstanceId(WHEAT_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration, 0, 
							100e6, 5e6, 0, true), 
					500, 500, 5000, 50e3, 1.5e6, 60);
		case WHEAT_2:
			return DefaultAgricultureElement.createProductionElement(
					name, name + " " + getInstanceId(WHEAT_2), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration, 0, 
							180e6, 9e6, 0, true), 
					1000, 1000, 5000, 45e3, 1.5e6, 60);
		case FOOD_TRANSPORT_1:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + getInstanceId(FOOD_TRANSPORT_1), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration, 0, 
							50e6, 2.5e6, 0, true), 
							0.92, 2e6, 0, 2);
		case FOOD_TRANSPORT_2:
			return DefaultAgricultureElement.createDistributionElement(
					name, name + " " + getInstanceId(FOOD_TRANSPORT_2), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration,  0, 
							300e6, 15e6, 0, true), 
							0.94, 15e6, 0, 2);
		case RO_PLANT_1:
			return DefaultWaterElement.createProductionElement(
					name, name + " " + getInstanceId(RO_PLANT_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							200e6*1.5, 1e6, 5e6, true), 
					0.0, 50e6, 50e6, 5.5e-3, 0.014, true);
		case RO_PLANT_2:
			return DefaultWaterElement.createProductionElement(
					name, name + " " + getInstanceId(RO_PLANT_2), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							500e6*1.5, 2.5e6, 12.5e6, true), 
					0.0, 150e6, 150e6, 4.5e-3, 0.012, true);
		case RO_PLANT_3:
			return DefaultWaterElement.createProductionElement(
					name, name + " " + getInstanceId(RO_PLANT_3), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							2e9*1.5, 10e6, 50e6, true), 
					0.0, 600e6, 600e6, 4.5e-3, 0.012, true);
		case WATER_PIPELINE_1:
			return DefaultWaterElement.createDistributionElement(
					name, name + " " + getInstanceId(WATER_PIPELINE_1), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration, 0, 
							10e6, 400e3, 0, true), 
					0.85, 50e6, 50e6, 2.0e-3, 0.008);
		case WATER_PIPELINE_2:
			return DefaultWaterElement.createDistributionElement(
					name, name + " " + getInstanceId(WATER_PIPELINE_2), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 0, 
							maxOperations, operationsDuration, 0, 
							50e6, 1e6, 0, true), 
					0.90, 150e6, 150e6, 2.0e-3, 0.008);
		case OIL_WELL_1:
			return DefaultPetroleumElement.createProductionElement(
					name, name + " " + getInstanceId(OIL_WELL_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 1+1, 
							maxOperations, operationsDuration, 0, 
							500e6*2.0, 25e6, 25e6, true), 
					1.0, 25e6, 25e6, 6.00);
		case OIL_WELL_2:
			return DefaultPetroleumElement.createProductionElement(
					name, name + " " + getInstanceId(OIL_WELL_2), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							1750e6*1.5, 87.5e6, 87.5e6, true), 
					1.0, 100e6, 100e6, 5.75);
		case OIL_PIPELINE_1:
			return DefaultPetroleumElement.createDistributionElement(
					name, name + " " + getInstanceId(OIL_PIPELINE_1), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 1+1, 
							maxOperations, operationsDuration, 0, 
							100e6*2.0, 2e6, 0, true), 
					0.98, 10e6, 10e6, 2.0e-3, 0.10);
		case OIL_PIPELINE_2:
			return DefaultPetroleumElement.createDistributionElement(
					name, name + " " + getInstanceId(OIL_PIPELINE_2), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 0, 
							450e6*2.0, 9e6, 0, true), 
					0.99, 50e6, 50e6, 2.0e-3, 0.10);
		case POWER_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					name, name + " " + getInstanceId(POWER_PLANT_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 1+1, 
							maxOperations, operationsDuration, 1, 
							25e6*2.0, 250e3, 1.25e6, true), 
					2e6, 2e6, 0.3, 0, 0);
		case POWER_PLANT_2:
			return DefaultElectricityElement.createProductionElement(
					name, name + " " + getInstanceId(POWER_PLANT_2), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							150e6*1.5, 1.5e6, 7.5e6, true), 
					10e6, 10e6, 0.25, 0, 0);
		case PV_PLANT_1:
			return DefaultElectricityElement.createProductionElement(
					name, name + " " + getInstanceId(PV_PLANT_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							200e6*1.5, 3e6, 2e6, true), 
					2e6, 2e6, 0, 0, 0);
		case PV_PLANT_2:
			return DefaultElectricityElement.createProductionElement(
					name, name + " " + getInstanceId(PV_PLANT_1), location, location, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 2+1, 
							maxOperations, operationsDuration, 1, 
							900e6*1.5, 13.5e6, 9e6, true), 
					10e6, 10e6, 0, 0, 0);
		case POWER_LINE_1:
			return DefaultElectricityElement.createDistributionElement(
					name, name + " " + getInstanceId(POWER_LINE_1), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 1+1, 
							maxOperations, operationsDuration, 0, 
							50e6*2.0, 2e6, 0, true), 
					0.94, 10e6, 10e6, 0);
		case POWER_LINE_2:
			return DefaultElectricityElement.createDistributionElement(
					name, name + " " + getInstanceId(POWER_LINE_2), location, destination, 
					new DefaultSimpleLifecycleModel(timeAvailable, year, 1+1, 
							maxOperations, operationsDuration, 0, 
							225e6*2.0, 9e6, 0, true), 
					0.96, 50e6, 50e6, 0);
		default:
			throw new IllegalArgumentException(
					"Unknown element template.");
		}
	}

	@Override
	public InfrastructureElement createElement(long year, 
			String location, String destination) {
		return createElement(year, maxOperations, location, destination);
	}

	@Override
	public long getMaxOperations() {
		return maxOperations;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Sector getSector() {
		return sector;
	}

	@Override
	public long getTimeAvailable() {
		return timeAvailable;
	}
	
	@Override
	public boolean isTransport() {
		return transport;
	}

	@Override
	public String toString() {
		return name;
	}
}