package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.encoding.HLAinteger64BE;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystem;

/**
 * The Class HLAsocialSystem.
 */
public class HLAsocialSystem extends HLAinfrastructureSystem {
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.SocialSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	FOOD_CONSUMPTION_ATTRIBUTE = "FoodConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption",
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "PetroleumConsumption",
	POPULATION_ATTRIBUTE = "Population",
	DOMESTIC_PRODUCT_ATTRIBUTE = "DomesticProduct";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		FOOD_CONSUMPTION_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE,
		PETROLEUM_CONSUMPTION_ATTRIBUTE,
		POPULATION_ATTRIBUTE,
		DOMESTIC_PRODUCT_ATTRIBUTE
	};
	
	/**
	 * Creates the local social system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param socialSystem the social system
	 * @return the hL asocial system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAsocialSystem createLocalSocialSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			SocialSystem.Local socialSystem) throws RTIexception {
		HLAsocialSystem hlaSystem = new HLAsocialSystem(
				rtiAmbassador, encoderFactory, null, socialSystem);
		socialSystem.addAttributeChangeListener(hlaSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote social system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the hL asocial system
	 * @throws RTIexception the rT iexception
	 */
	public static HLAsocialSystem createRemoteSocialSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HLAsocialSystem hlaSystem = new HLAsocialSystem(rtiAmbassador, 
				encoderFactory, instanceName, new DefaultSocialSystem.Remote());
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
	private final HLAfloat64BE foodConsumption;
	private final HLAfloat64BE waterConsumption;
	private final HLAfloat64BE oilConsumption;
	private final HLAfloat64BE domesticProduct;
	private final HLAinteger64BE population;
	
	/**
	 * Instantiates a new hL asocial system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param socialSystem the social system
	 * @throws RTIexception the rT iexception
	 */
	protected HLAsocialSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName,
			SocialSystem socialSystem) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName, socialSystem);
		electricityConsumption = encoderFactory.createHLAfloat64BE(
				socialSystem.getElectricityConsumption());
		foodConsumption = encoderFactory.createHLAfloat64BE(
				socialSystem.getFoodConsumption());
		waterConsumption = encoderFactory.createHLAfloat64BE(
				socialSystem.getWaterConsumption());
		oilConsumption = encoderFactory.createHLAfloat64BE(
				socialSystem.getPetroleumConsumption());
		domesticProduct = encoderFactory.createHLAfloat64BE(
				socialSystem.getDomesticProduct());
		population = encoderFactory.createHLAinteger64BE(
				socialSystem.getPopulation());
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(FOOD_CONSUMPTION_ATTRIBUTE), 
				foodConsumption);
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(PETROLEUM_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(DOMESTIC_PRODUCT_ATTRIBUTE), 
				domesticProduct);
		attributeValues.put(getAttributeHandle(POPULATION_ATTRIBUTE), 
				population);
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
						SocialSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
					electricityConsumption.setValue(
							getSocialSystem().getElectricityConsumption());
					//attributesToUpdate.add(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
				} 
				if(evt.getAttributeNames().contains(
						SocialSystem.FOOD_CONSUMPTION_ATTRIBUTE)) {
					foodConsumption.setValue(
							getSocialSystem().getFoodConsumption());
					//attributesToUpdate.add(FOOD_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						SocialSystem.WATER_CONSUMPTION_ATTRIBUTE)) {
					waterConsumption.setValue(
							getSocialSystem().getWaterConsumption());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						SocialSystem.PETROLEUM_CONSUMPTION_ATTRIBUTE)) {
					oilConsumption.setValue(
							getSocialSystem().getWaterConsumption());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						SocialSystem.DOMESTIC_PRODUCT_ATTRIBUTE)) {
					domesticProduct.setValue(
							getSocialSystem().getDomesticProduct());
					//attributesToUpdate.add(DOMESTIC_PRODUCT_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						SocialSystem.POPULATION_ATTRIBUTE)) {
					population.setValue(
							getSocialSystem().getPopulation());
					//attributesToUpdate.add(POPULATION_ATTRIBUTE);
				}
				//updateAttributes(attributesToUpdate);
			//} catch(AttributeNotOwned ignored) {
			//} catch(Exception ex) {
			//	ex.printStackTrace();
			//}
		} else if(getSocialSystem() instanceof SocialSystem.Remote) {
			SocialSystem.Remote remote = (SocialSystem.Remote) getSocialSystem();
			// federation changed values -- send updates to object model
			if(evt.getAttributeNames().contains(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
				remote.setElectricityConsumption(
						electricityConsumption.getValue());
			} 
			if(evt.getAttributeNames().contains(
					FOOD_CONSUMPTION_ATTRIBUTE)) {
				remote.setFoodConsumption(
						foodConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					WATER_CONSUMPTION_ATTRIBUTE)) {
				remote.setWaterConsumption(
						waterConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					PETROLEUM_CONSUMPTION_ATTRIBUTE)) {
				remote.setPetroleumConsumption(
						oilConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					DOMESTIC_PRODUCT_ATTRIBUTE)) {
				remote.setDomesticProduct(
						domesticProduct.getValue());
			}
			if(evt.getAttributeNames().contains(
					POPULATION_ATTRIBUTE)) {
				remote.setPopulation(
						population.getValue());
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
	public SocialSystem getSocialSystem() {
		return (SocialSystem) getInfrastructureSystem();
	}

	/**
	 * Sets the social system.
	 *
	 * @param socialSystem the new social system
	 */
	public void setSocialSystem(SocialSystem.Remote socialSystem) {
		// copy attribute values to new system
		socialSystem.setDomesticProduct(getSocialSystem().getDomesticProduct());
		socialSystem.setElectricityConsumption(getSocialSystem().getElectricityConsumption());
		socialSystem.setFoodConsumption(getSocialSystem().getFoodConsumption());
		socialSystem.setPopulation(getSocialSystem().getPopulation());
		socialSystem.setWaterConsumption(getSocialSystem().getWaterConsumption());
		socialSystem.setPetroleumConsumption(getSocialSystem().getPetroleumConsumption());
		super.setInfrastructureSystem(socialSystem);
	}
}
