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

import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class HLAwaterSystem.
 */
public class HLAwaterSystem extends HLAinfrastructureSystem {
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.WaterSystem";
	
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
				rtiAmbassador, encoderFactory, null, waterSystem);
		waterSystem.addAttributeChangeListener(hlaSystem);
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
				rtiAmbassador, encoderFactory, instanceName,
				new DefaultWaterSystem.Remote());
		//hlaSystem.requestAttributeValueUpdate();
		hlaSystem.addAttributeChangeListener(hlaSystem);
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
	
	private final HLAfloat64BE electricityConsumption;
	private final HLAfloat64BE waterDomesticPrice, waterAgriculturalPrice, waterImportPrice;

	/**
	 * Instantiates a new hL awater system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param waterSystem the water system
	 * @throws RTIexception the rT iexception
	 */
	protected HLAwaterSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName,
			WaterSystem waterSystem) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName, waterSystem);
		electricityConsumption = encoderFactory.createHLAfloat64BE(
				waterSystem.getElectricityConsumption());
		waterDomesticPrice = encoderFactory.createHLAfloat64BE(
				waterSystem.getWaterDomesticPrice());
		waterAgriculturalPrice = encoderFactory.createHLAfloat64BE(
				waterSystem.getWaterAgriculturalPrice());
		waterImportPrice = encoderFactory.createHLAfloat64BE(
				waterSystem.getWaterImportPrice());
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
	 * @see edu.mit.sips.hla.AttributeChangeListener#attributeChanged(edu.mit.sips.hla.AttributeChangeEvent)
	 */
	@Override
	public void attributeChanged(AttributeChangeEvent evt) {
		super.attributeChanged(evt);
		if(evt.getSource().equals(getInfrastructureSystem())) {
			// object model changed values -- send updates to federation
			//try {
				//List<String> attributesToUpdate = new ArrayList<String>();
				if(evt.getAttributeNames().contains(
						WaterSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
					electricityConsumption.setValue(
							getWaterSystem().getElectricityConsumption());
					//attributesToUpdate.add(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						WaterSystem.WATER_DOMESTIC_PRICE_ATTRIBUTE)) {
					waterDomesticPrice.setValue(
							getWaterSystem().getWaterDomesticPrice());
					//attributesToUpdate.add(WATER_DOMESTIC_PRICE_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						WaterSystem.WATER_AGRICULTURAL_PRICE_ATTRIBUTE)) {
					waterAgriculturalPrice.setValue(
							getWaterSystem().getWaterAgriculturalPrice());
					//attributesToUpdate.add(WATER_AGRICULTURAL_PRICE_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						WaterSystem.WATER_IMPORT_PRICE_ATTRIBUTE)) {
					waterImportPrice.setValue(
							getWaterSystem().getWaterImportPrice());
					//attributesToUpdate.add(WATER_IMPORT_PRICE_ATTRIBUTE);
				}
				//updateAttributes(attributesToUpdate);
			//} catch(AttributeNotOwned ignored) {
			//} catch(Exception ex) {
			//	ex.printStackTrace();
			//}
		} else if(getWaterSystem() instanceof WaterSystem.Remote) {
			WaterSystem.Remote remote = (WaterSystem.Remote) getWaterSystem();
			// federation changed values -- send updates to object model
			if(evt.getAttributeNames().contains(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
				remote.setElectricityConsumption(
						electricityConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					WATER_DOMESTIC_PRICE_ATTRIBUTE)) {
				remote.setWaterDomesticPrice(
						waterDomesticPrice.getValue());
			}
			if(evt.getAttributeNames().contains(
					WATER_AGRICULTURAL_PRICE_ATTRIBUTE)) {
				remote.setWaterAgriculturalPrice(
						waterAgriculturalPrice.getValue());
			}
			if(evt.getAttributeNames().contains(
					WATER_IMPORT_PRICE_ATTRIBUTE)) {
				remote.setWaterImportPrice(
						waterImportPrice.getValue());
			}
		}
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
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem() {
		return (WaterSystem) getInfrastructureSystem();
	}

	/**
	 * Sets the water system.
	 *
	 * @param waterSystem the new water system
	 */
	public void setWaterSystem(WaterSystem.Remote waterSystem) {
		// copy attribute values to new system
		waterSystem.setElectricityConsumption(getWaterSystem().getElectricityConsumption());
		waterSystem.setWaterDomesticPrice(getWaterSystem().getWaterDomesticPrice());
		waterSystem.setWaterAgriculturalPrice(getWaterSystem().getWaterAgriculturalPrice());
		waterSystem.setWaterImportPrice(getWaterSystem().getWaterImportPrice());
		super.setInfrastructureSystem(waterSystem);
	}
}
