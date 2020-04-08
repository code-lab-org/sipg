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
package edu.mit.sipg.sim.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sipg.core.base.InfrastructureSystem;
import edu.mit.sipg.core.electricity.ElectricitySystem;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.OilUnits;
import edu.mit.sipg.units.TimeUnits;
import edu.mit.sipg.units.WaterUnits;

/**
 * A HLA infrastructure system implementation for the electricity sector.
 * 
 * @author Paul T. Grogan
 */
public class HlaElectricitySystem extends HlaInfrastructureSystem implements ElectricitySystem {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.ElectricitySystem";
	
	public static final String 
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "PetroleumConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption",
	ELECTRICITY_DOMESTIC_PRICE_ATTRIBUTE = "ElectricityDomesticPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		PETROLEUM_CONSUMPTION_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE,
		ELECTRICITY_DOMESTIC_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local electricity system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param electricitySystem the electricity system
	 * @return the HLA electricity system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaElectricitySystem createLocalElectricitySystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			ElectricitySystem.Local electricitySystem) throws RTIexception {
		HlaElectricitySystem hlaSystem = new HlaElectricitySystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(electricitySystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote electricity system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the HLA electricity system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaElectricitySystem createRemoteElectricitySystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		return new HlaElectricitySystem(rtiAmbassador, encoderFactory, instanceName);
	}
	
	/**
	 * Publish all.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void publishAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet attributeHandleSet = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			attributeHandleSet.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.publishObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
				attributeHandleSet);
	}

	/**
	 * Subscribe all.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void subscribeAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet attributeHandleSet = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			attributeHandleSet.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.subscribeObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
				attributeHandleSet);
	}

	private transient final HLAfloat64BE petroleumConsumption;
	private transient final HLAfloat64BE waterConsumption;
	private transient final HLAfloat64BE electricityDomesticPrice;
	
	/**
	 * Instantiates a new HLA electricity system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	protected HlaElectricitySystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		petroleumConsumption = encoderFactory.createHLAfloat64BE();
		waterConsumption = encoderFactory.createHLAfloat64BE();
		electricityDomesticPrice = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(PETROLEUM_CONSUMPTION_ATTRIBUTE), 
				petroleumConsumption);
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(ELECTRICITY_DOMESTIC_PRICE_ATTRIBUTE), 
				electricityDomesticPrice);
	}
	
	@Override
	public String[] getAttributeNames() {
		return ATTRIBUTES;
	}
	
	@Override
	public Map<AttributeHandle, DataElement> getAttributeValues() {
		return new HashMap<AttributeHandle,DataElement>(attributeValues);
	}

	@Override
	public double getElectricityDomesticPrice() {
		return electricityDomesticPrice.getValue();
	}

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public double getPetroleumConsumption() {
		return petroleumConsumption.getValue();
	}

	@Override
	public double getWaterConsumption() {
		return waterConsumption.getValue();
	}

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	@Override
	public void setAttributes(InfrastructureSystem system) {
		super.setAttributes(system);
		if(system instanceof ElectricitySystem) {
			ElectricitySystem electricitySystem = (ElectricitySystem) system;
			petroleumConsumption.setValue(electricitySystem.getPetroleumConsumption());
			waterConsumption.setValue(electricitySystem.getWaterConsumption());
			electricityDomesticPrice.setValue(electricitySystem.getElectricityDomesticPrice());
		}
	}
}
