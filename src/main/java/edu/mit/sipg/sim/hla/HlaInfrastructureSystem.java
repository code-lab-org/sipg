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
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.base.InfrastructureSystem;
import edu.mit.sipg.units.CurrencyUnits;
import edu.mit.sipg.units.TimeUnits;

/**
 * A generic HLA object conforming to the infrastructure system interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class HlaInfrastructureSystem extends HlaObject implements InfrastructureSystem {
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem";
	protected static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	protected static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	public static final String NAME_ATTRIBUTE = "Name",
			SOCIETY_NAME_ATTRIBUTE = "SocietyName",
			NET_CASH_FLOW_ATTRIBUTE = "NetCashFlow",
			CAPITAL_EXPENSE_ATTRIBUTE = "CapitalExpense",
			SUSTAINABILITY_NUMERATOR_ATTRIBUTE = "SustainabilityMetricNumerator",
			SUSTAINABILITY_DENOMINATOR_ATTRIBUTE = "SustainabilityMetricDenominator";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		SUSTAINABILITY_NUMERATOR_ATTRIBUTE,
		SUSTAINABILITY_DENOMINATOR_ATTRIBUTE
	};

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

	private transient Society society;
	private transient final HLAunicodeString name;
	private transient final HLAunicodeString societyName;
	private transient final HLAfloat64BE netCashFlow;
	private transient final HLAfloat64BE capitalExpense;
	protected transient final HLAfloat64BE sustainabilityMetricNumerator;
	protected transient final HLAfloat64BE sustainabilityMetricDenominator;
	protected transient final Map<AttributeHandle,DataElement> attributeValues = 
			new HashMap<AttributeHandle,DataElement>();
	
	/**
	 * Instantiates a new HLA infrastructure system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	protected HlaInfrastructureSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, instanceName);
		name = encoderFactory.createHLAunicodeString();
		societyName = encoderFactory.createHLAunicodeString();
		netCashFlow = encoderFactory.createHLAfloat64BE();
		capitalExpense = encoderFactory.createHLAfloat64BE();
		sustainabilityMetricNumerator = encoderFactory.createHLAfloat64BE();
		sustainabilityMetricDenominator = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(NAME_ATTRIBUTE), 
				name);
		attributeValues.put(getAttributeHandle(SOCIETY_NAME_ATTRIBUTE), 
				societyName);
		attributeValues.put(getAttributeHandle(NET_CASH_FLOW_ATTRIBUTE), 
				netCashFlow);
		attributeValues.put(getAttributeHandle(CAPITAL_EXPENSE_ATTRIBUTE), 
				capitalExpense);
		attributeValues.put(getAttributeHandle(
				SUSTAINABILITY_NUMERATOR_ATTRIBUTE), 
				sustainabilityMetricNumerator);
		attributeValues.put(getAttributeHandle(
				SUSTAINABILITY_DENOMINATOR_ATTRIBUTE), 
				sustainabilityMetricDenominator);
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
	public double getCapitalExpense() {
		return capitalExpense.getValue();
	}

	@Override
	public double getCashFlow() {
		return netCashFlow.getValue();
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	@Override
	public Society getSociety() {
		return society;
	}

	/**
	 * Gets the society name.
	 *
	 * @return the society name
	 */
	public String getSocietyName() {
		return societyName.getValue();
	}

	@Override
	public void initialize(long time) { }
	
	/**
	 * Sets the infrastructure system.
	 *
	 * @param infrastructureSystem the new infrastructure system
	 */
	public void setAttributes(InfrastructureSystem infrastructureSystem) {
		name.setValue(infrastructureSystem.getName());
		societyName.setValue(infrastructureSystem.getSociety()==null ? "" 
				: infrastructureSystem.getSociety().getName());
		netCashFlow.setValue(infrastructureSystem.getCashFlow());
		// TODO domesticProduction.setValue(infrastructureSystem.getDomesticProduction());
		capitalExpense.setValue(infrastructureSystem.getCapitalExpense()); // TODO using existing variable for capex
	}
	
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	@Override
	public void tick() { }

	@Override
	public void tock() { }
}
