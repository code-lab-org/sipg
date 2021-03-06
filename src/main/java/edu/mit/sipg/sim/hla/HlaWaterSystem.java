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
import edu.mit.sipg.core.water.WaterSystem;
import edu.mit.sipg.units.ElectricityUnits;
import edu.mit.sipg.units.TimeUnits;
import edu.mit.sipg.units.WaterUnits;

/**
 * A HLA infrastructure system implementation for the water sector.
 * 
 * @author Paul T. Grogan
 */
public class HlaWaterSystem extends HlaInfrastructureSystem implements WaterSystem {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.WaterSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	WATER_DOMESTIC_PRICE_ATTRIBUTE = "WaterDomesticPrice",
	WATER_IMPORT_PRICE_ATTRIBUTE = "WaterImportPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		SUSTAINABILITY_NUMERATOR_ATTRIBUTE,
		SUSTAINABILITY_DENOMINATOR_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		WATER_DOMESTIC_PRICE_ATTRIBUTE,
		WATER_IMPORT_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local water system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param waterSystem the water system
	 * @return the HLA water system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaWaterSystem createLocalWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			WaterSystem.Local waterSystem) throws RTIexception {
		HlaWaterSystem hlaSystem = new HlaWaterSystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(waterSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote water system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the HLA water system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaWaterSystem createRemoteWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		return new HlaWaterSystem(rtiAmbassador, encoderFactory, instanceName);
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
	
	private transient final HLAfloat64BE electricityConsumption;
	private transient final HLAfloat64BE waterDomesticPrice;
	private transient final HLAfloat64BE waterImportPrice;

	/**
	 * Instantiates a new HLA water system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	protected HlaWaterSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		electricityConsumption = encoderFactory.createHLAfloat64BE();
		waterDomesticPrice = encoderFactory.createHLAfloat64BE();
		waterImportPrice = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(WATER_DOMESTIC_PRICE_ATTRIBUTE), 
				waterDomesticPrice);
		attributeValues.put(getAttributeHandle(WATER_IMPORT_PRICE_ATTRIBUTE), 
				waterImportPrice);
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
	public double getElectricityConsumption() {
		return electricityConsumption.getValue();
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
	public double getWaterDomesticPrice() {
		return waterDomesticPrice.getValue();
	}

	@Override
	public double getWaterImportPrice() {
		return waterImportPrice.getValue();
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
		if(system instanceof WaterSystem) {
			WaterSystem waterSystem = (WaterSystem) system;
			electricityConsumption.setValue(waterSystem.getElectricityConsumption());
			waterDomesticPrice.setValue(waterSystem.getWaterDomesticPrice());
			waterImportPrice.setValue(waterSystem.getWaterImportPrice());
			sustainabilityMetricNumerator.setValue(waterSystem.getWaterReservoirVolume());
			sustainabilityMetricDenominator.setValue(waterSystem.getAquiferWithdrawals());
		}
	}

	@Override
	public double getAquiferLifetime() {
		return getAquiferWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getWaterReservoirVolume() / getAquiferWithdrawals());
	}

	@Override
	public double getWaterReservoirVolume() {
		return sustainabilityMetricNumerator.getValue();
	}

	@Override
	public double getAquiferWithdrawals() {
		return sustainabilityMetricDenominator.getValue();
	}
}
