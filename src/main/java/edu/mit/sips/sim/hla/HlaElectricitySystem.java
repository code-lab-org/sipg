package edu.mit.sips.sim.hla;

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
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The Class HLA electricity system.
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
	 * @param rtiAmbassador the rti ambassador
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
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the HLA electricity system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaElectricitySystem createRemoteElectricitySystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HlaElectricitySystem hlaSystem = new HlaElectricitySystem(
				rtiAmbassador, encoderFactory, instanceName);
		//hlaSystem.requestAttributeValueUpdate();
		return hlaSystem;
	}
	
	/**
	 * Publish all.
	 *
	 * @param rtiAmbassador the rti ambassador
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
	 * @param rtiAmbassador the rti ambassador
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
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param energySystem the energy system
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAinfrastructureSystem#setAttributes(edu.mit.sips.core.InfrastructureSystem)
	 */
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
