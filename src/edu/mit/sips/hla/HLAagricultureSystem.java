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

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;

/**
 * The Class HLAwaterSystem.
 */
public class HLAagricultureSystem extends HLAinfrastructureSystem {
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.AgricultureSystem";
	
	public static final String 
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption",
	FOOD_DOMESTIC_PRICE_ATTRIBUTE = "FoodDomesticPrice",
	FOOD_IMPORT_PRICE_ATTRIBUTE = "FoodImportPrice",
	FOOD_EXPORT_PRICE_ATTRIBUTE = "FoodExportPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE,
		FOOD_DOMESTIC_PRICE_ATTRIBUTE,
		FOOD_IMPORT_PRICE_ATTRIBUTE,
		FOOD_EXPORT_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local water system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param agricultureSystem the agriculture system
	 * @return the hL awater system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAagricultureSystem createLocalAgricultureSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			AgricultureSystem.Local agricultureSystem) throws RTIexception {
		HLAagricultureSystem hlaSystem = new HLAagricultureSystem(
				rtiAmbassador, encoderFactory, null, agricultureSystem);
		agricultureSystem.addAttributeChangeListener(hlaSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote agriculture system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the hL aagriculture system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAagricultureSystem createRemoteAgricultureSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HLAagricultureSystem hlaSystem = new HLAagricultureSystem(
				rtiAmbassador, encoderFactory, instanceName, new DefaultAgricultureSystem.Remote());
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
	
	private final HLAfloat64BE waterConsumption;
	private final HLAfloat64BE foodDomesticPrice, foodImportPrice, foodExportPrice;

	/**
	 * Instantiates a new hL aagriculture system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param agricultureSystem the agriculture system
	 * @throws RTIexception the rT iexception
	 */
	protected HLAagricultureSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName,
			AgricultureSystem agricultureSystem) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName, agricultureSystem);
		waterConsumption = encoderFactory.createHLAfloat64BE(
				agricultureSystem.getWaterConsumption());
		foodDomesticPrice = encoderFactory.createHLAfloat64BE(
				agricultureSystem.getFoodDomesticPrice());
		foodImportPrice = encoderFactory.createHLAfloat64BE(
				agricultureSystem.getFoodImportPrice());
		foodExportPrice = encoderFactory.createHLAfloat64BE(
				agricultureSystem.getFoodExportPrice());
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(FOOD_DOMESTIC_PRICE_ATTRIBUTE), 
				foodDomesticPrice);
		attributeValues.put(getAttributeHandle(FOOD_IMPORT_PRICE_ATTRIBUTE), 
				foodImportPrice);
		attributeValues.put(getAttributeHandle(FOOD_EXPORT_PRICE_ATTRIBUTE), 
				foodExportPrice);
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
			//	List<String> attributesToUpdate = new ArrayList<String>();
				if(evt.getAttributeNames().contains(
						AgricultureSystem.WATER_CONSUMPTION_ATTRIBUTE)) {
					waterConsumption.setValue(
							getAgricultureSystem().getWaterConsumption());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						AgricultureSystem.FOOD_DOMESTIC_PRICE_ATTRIBUTE)) {
					foodDomesticPrice.setValue(
							getAgricultureSystem().getFoodDomesticPrice());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						AgricultureSystem.FOOD_IMPORT_PRICE_ATTRIBUTE)) {
					foodImportPrice.setValue(
							getAgricultureSystem().getFoodImportPrice());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						AgricultureSystem.FOOD_EXPORT_PRICE_ATTRIBUTE)) {
					foodExportPrice.setValue(
							getAgricultureSystem().getFoodExportPrice());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
			//	updateAttributes(attributesToUpdate);
			//} catch(AttributeNotOwned ignored) {
			//} catch(Exception ex) {
			//	ex.printStackTrace();
			//}
		} else if(getAgricultureSystem() instanceof AgricultureSystem.Remote) {
			AgricultureSystem.Remote remote = 
					(AgricultureSystem.Remote) getAgricultureSystem();
			// federation changed values -- send updates to object model
			if(evt.getAttributeNames().contains(
					WATER_CONSUMPTION_ATTRIBUTE)) {
				remote.setWaterConsumption(
						waterConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					FOOD_DOMESTIC_PRICE_ATTRIBUTE)) {
				remote.setFoodDomesticPrice(
						foodDomesticPrice.getValue());
			}
			if(evt.getAttributeNames().contains(
					FOOD_IMPORT_PRICE_ATTRIBUTE)) {
				remote.setFoodImportPrice(
						foodImportPrice.getValue());
			}
			if(evt.getAttributeNames().contains(
					FOOD_EXPORT_PRICE_ATTRIBUTE)) {
				remote.setFoodExportPrice(
						foodExportPrice.getValue());
			}
		}
	}
	
	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem() {
		return (AgricultureSystem) getInfrastructureSystem();
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
	 * Sets the agriculture system.
	 *
	 * @param agricultureSystem the new agriculture system
	 */
	public void setAgricultureSystem(AgricultureSystem.Remote agricultureSystem) {
		// copy attribute values to new system
		agricultureSystem.setWaterConsumption(getAgricultureSystem().getWaterConsumption());
		agricultureSystem.setFoodDomesticPrice(getAgricultureSystem().getFoodDomesticPrice());
		agricultureSystem.setFoodImportPrice(getAgricultureSystem().getFoodImportPrice());
		agricultureSystem.setFoodExportPrice(getAgricultureSystem().getFoodExportPrice());
		super.setInfrastructureSystem(agricultureSystem);
	}
}
