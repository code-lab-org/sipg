package edu.mit.sips.hla;

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

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class HLAinfrastructureSystem.
 */
public abstract class HLAinfrastructureSystem extends HLAobject implements InfrastructureSystem {
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem";
	protected static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	protected static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	public static final String NAME_ATTRIBUTE = "Name",
			SOCIETY_NAME_ATTRIBUTE = "SocietyName",
			NET_CASH_FLOW_ATTRIBUTE = "NetCashFlow",
			DOMESTIC_PRODUCTION_ATTRIBUTE = "DomesticProduction",
			SUSTAINABILITY_NUMERATOR_ATTRIBUTE = "SustainabilityMetricNumerator",
			SUSTAINABILITY_DENOMINATOR_ATTRIBUTE = "SustainabilityMetricDenominator";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		SUSTAINABILITY_NUMERATOR_ATTRIBUTE,
		SUSTAINABILITY_DENOMINATOR_ATTRIBUTE
	};

	/**
	 * Publish all.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @throws RTIexception the rT iexception
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
	 * @param rtiAmbassador the rti ambassador
	 * @throws RTIexception the rT iexception
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
	private transient final HLAfloat64BE domesticProduction;
	protected transient final HLAfloat64BE sustainabilityMetricNumerator;
	protected transient final HLAfloat64BE sustainabilityMetricDenominator;
	protected transient final Map<AttributeHandle,DataElement> attributeValues = 
			new HashMap<AttributeHandle,DataElement>();
	
	/**
	 * Instantiates a new HLA infrastructure system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the rT iexception
	 */
	protected HLAinfrastructureSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, instanceName);
		name = encoderFactory.createHLAunicodeString();
		societyName = encoderFactory.createHLAunicodeString();
		netCashFlow = encoderFactory.createHLAfloat64BE();
		domesticProduction = encoderFactory.createHLAfloat64BE();
		sustainabilityMetricNumerator = encoderFactory.createHLAfloat64BE();
		sustainabilityMetricDenominator = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(NAME_ATTRIBUTE), 
				name);
		attributeValues.put(getAttributeHandle(SOCIETY_NAME_ATTRIBUTE), 
				societyName);
		attributeValues.put(getAttributeHandle(NET_CASH_FLOW_ATTRIBUTE), 
				netCashFlow);
		attributeValues.put(getAttributeHandle(DOMESTIC_PRODUCTION_ATTRIBUTE), 
				domesticProduction);
		attributeValues.put(getAttributeHandle(
				SUSTAINABILITY_NUMERATOR_ATTRIBUTE), 
				sustainabilityMetricNumerator);
		attributeValues.put(getAttributeHandle(
				SUSTAINABILITY_DENOMINATOR_ATTRIBUTE), 
				sustainabilityMetricDenominator);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getAttributeNames()
	 */
	@Override
	public String[] getAttributeNames() {
		return ATTRIBUTES;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getAttributeValues()
	 */
	@Override
	public Map<AttributeHandle, DataElement> getAttributeValues() {
		return new HashMap<AttributeHandle,DataElement>(attributeValues);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		return netCashFlow.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return domesticProduction.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		return name.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
	 */
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
		domesticProduction.setValue(infrastructureSystem.getCapitalExpense()); // TODO using existing variable for capex
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		return domesticProduction.getValue(); // TODO using existing variable for capex
	}
}
