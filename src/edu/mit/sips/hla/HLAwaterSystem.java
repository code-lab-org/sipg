package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class HLAwaterSystem.
 */
public class HLAwaterSystem extends HLAinfrastructureSystem implements WaterSystem {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.WaterSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	WATER_DOMESTIC_PRICE_ATTRIBUTE = "WaterDomesticPrice",
	WATER_AGRICULTURAL_PRICE_ATTRIBUTE = "WaterAgriculturalPrice",
	WATER_IMPORT_PRICE_ATTRIBUTE = "WaterImportPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		WATER_DOMESTIC_PRICE_ATTRIBUTE,
		WATER_AGRICULTURAL_PRICE_ATTRIBUTE,
		WATER_IMPORT_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local water system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param waterSystem the water system
	 * @return the hL awater system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAwaterSystem createLocalWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			WaterSystem.Local waterSystem) throws RTIexception {
		HLAwaterSystem hlaSystem = new HLAwaterSystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(waterSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote water system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the hL awater system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAwaterSystem createRemoteWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HLAwaterSystem hlaSystem = new HLAwaterSystem(
				rtiAmbassador, encoderFactory, instanceName);
		//hlaSystem.requestAttributeValueUpdate();
		return hlaSystem;
	}
	
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
	
	private transient final HLAfloat64BE electricityConsumption;
	private transient final HLAfloat64BE waterDomesticPrice;
	private transient final HLAfloat64BE waterAgriculturalPrice;
	private transient final HLAfloat64BE waterImportPrice;

	/**
	 * Instantiates a new hL awater system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the rT iexception
	 */
	protected HLAwaterSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		electricityConsumption = encoderFactory.createHLAfloat64BE();
		waterDomesticPrice = encoderFactory.createHLAfloat64BE();
		waterAgriculturalPrice = encoderFactory.createHLAfloat64BE();
		waterImportPrice = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(WATER_DOMESTIC_PRICE_ATTRIBUTE), 
				waterDomesticPrice);
		attributeValues.put(getAttributeHandle(WATER_AGRICULTURAL_PRICE_ATTRIBUTE), 
				waterAgriculturalPrice);
		attributeValues.put(getAttributeHandle(WATER_IMPORT_PRICE_ATTRIBUTE), 
				waterImportPrice);
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
	 * @see edu.mit.sips.core.water.WaterSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		return electricityConsumption.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterAgriculturalPrice()
	 */
	@Override
	public double getWaterAgriculturalPrice() {
		return waterAgriculturalPrice.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterDomesticPrice()
	 */
	@Override
	public double getWaterDomesticPrice() {
		return waterDomesticPrice.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.water.WaterSystem#getWaterImportPrice()
	 */
	@Override
	public double getWaterImportPrice() {
		return waterImportPrice.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAinfrastructureSystem#setAttributes(edu.mit.sips.core.InfrastructureSystem)
	 */
	@Override
	public void setAttributes(InfrastructureSystem system) {
		super.setAttributes(system);
		if(system instanceof WaterSystem) {
			WaterSystem waterSystem = (WaterSystem) system;
			electricityConsumption.setValue(waterSystem.getElectricityConsumption());
			waterDomesticPrice.setValue(waterSystem.getWaterDomesticPrice());
			waterAgriculturalPrice.setValue(waterSystem.getWaterAgriculturalPrice());
			waterImportPrice.setValue(waterSystem.getWaterImportPrice());
		}
	}
}
