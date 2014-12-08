/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos.app.hla;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

import org.apache.log4j.Logger;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.hla.ISOSelement;
import edu.mit.isos.core.sim.SimEntity;

/**
 * ISOSwaterElement is the HLA object class implementing the {@link WaterElement} 
 * interface for communication with the RTI.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public class ISOSelectElement extends ISOSelement implements ElectElement {
	private static Logger logger = Logger.getLogger(ISOSelectElement.class);
	public static final String CLASS_NAME = "HLAobjectRoot.Element.ElectElement";
	
	public static final String NAME_ATTRIBUTE = "Name",
			LOCATION_ATTRIBUTE = "Location",
			PETROL_RECEIVED_ATTRIBUTE = "PetrolReceived",
			ELECT_SENT_TO_PETROL_ATTRIBUTE = "ElectSentToPetrol",
			ELECT_SENT_TO_WATER_ATTRIBUTE = "ElectSentToWater",
			ELECT_SENT_TO_SOCIAL_ATTRIBUTE = "ElectSentToSocial";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		LOCATION_ATTRIBUTE,
		PETROL_RECEIVED_ATTRIBUTE,
		ELECT_SENT_TO_PETROL_ATTRIBUTE,
		ELECT_SENT_TO_WATER_ATTRIBUTE,
		ELECT_SENT_TO_SOCIAL_ATTRIBUTE
	};

	/**
	 * Publishes all of this object class's attributes.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void publishAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet handles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			handles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.publishObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), handles);
	}
	

	/**
	 * Subscribes to all of this object class's attributes.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void subscribeAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet handles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			handles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.subscribeObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), handles);
	}

	private final HLAfloat64BE petrolReceived;
	private final HLAfloat64BE electSentToPetrol;
	private final HLAfloat64BE electSentToSocial;
	private final HLAfloat64BE electSentToWater;
	
	/**
	 * Instantiates a new ISOS element. The object is interpreted as local
	 * if {@link instanceName} is null and is interpreted as remote if 
	 * {@link instanceName} is not null.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	public ISOSelectElement(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) 
					throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		
		logger.trace("Creating the petrol received data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		petrolReceived = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE),  petrolReceived);
		sendOrderMap.put(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the elect sent to petrol data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		electSentToPetrol = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECT_SENT_TO_PETROL_ATTRIBUTE),  electSentToPetrol);
		sendOrderMap.put(getAttributeHandle(ELECT_SENT_TO_PETROL_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the elect sent to social data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		electSentToSocial = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECT_SENT_TO_SOCIAL_ATTRIBUTE),  electSentToSocial);
		sendOrderMap.put(getAttributeHandle(ELECT_SENT_TO_SOCIAL_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the elect sent to water data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		electSentToWater = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECT_SENT_TO_WATER_ATTRIBUTE),  electSentToWater);
		sendOrderMap.put(getAttributeHandle(ELECT_SENT_TO_WATER_ATTRIBUTE), 
				OrderType.TIMESTAMP);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getAttributeNames()
	 */
	@Override
	public String[] getAttributeNames() {
		return ATTRIBUTES;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	/* (non-Javadoc)
	 * @see edu.mit.fss.hla.HLAobject#setAttributes(java.lang.Object)
	 */
	@Override
	public void setAttributes(SimEntity object) {
		super.setAttributes(object);
		if(object instanceof ElectElement) {
			ElectElement element = (ElectElement) object;
			petrolReceived.setValue(element.getPetrolReceived());
			electSentToPetrol.setValue(element.getElectSentToPetrol());
			electSentToSocial.setValue(element.getElectSentToSocial());
			electSentToWater.setValue(element.getElectSentToWater());
		} else {
			logger.warn("Incompatible object passed: expected " 
					+ ElectElement.class + " but received "
					+ object.getClass() + ".");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.fss.hla.HLAobject#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ISOSelectElement { name: ").append(getName())
				.append(", location: ").append(getLocation())
				.append(", petrolReceived: ").append(getPetrolReceived())
				.append(", electSentToPetrol: ").append(getElectSentToPetrol())
				.append(", electSentToSocial: ").append(getElectSentToSocial())
				.append(", electSentToWater: ").append(getElectSentToWater())
				.append("}").toString();
	}

	@Override
	public double getPetrolReceived() {
		return petrolReceived.getValue();
	}

	@Override
	public double getElectSentToPetrol() {
		return electSentToPetrol.getValue();
	}

	@Override
	public double getElectSentToSocial() {
		return electSentToSocial.getValue();
	}

	@Override
	public double getElectSentToWater() {
		return electSentToWater.getValue();
	}

	@Override
	public void updatePeriodicAttributes(RTIambassador rtiAmbassador) throws RTIexception {
		AttributeHandleSet ahs = rtiAmbassador.getAttributeHandleSetFactory().create();
		ahs.add(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE));
		ahs.add(getAttributeHandle(ELECT_SENT_TO_PETROL_ATTRIBUTE));
		ahs.add(getAttributeHandle(ELECT_SENT_TO_WATER_ATTRIBUTE));
		ahs.add(getAttributeHandle(ELECT_SENT_TO_SOCIAL_ATTRIBUTE));
		updateAttributes(rtiAmbassador, ahs);
	}

	@Override
	public Resource getNetExchange(Element element, long duration) {
		Resource exchange = ResourceFactory.create();
		if(element instanceof PetrolElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.add(ResourceFactory.create(
					ResourceType.ELECTRICITY, getElectSentToPetrol()));
			exchange = exchange.subtract(ResourceFactory.create(
					ResourceType.OIL, getPetrolReceived()));
		}
		if(element instanceof SocialElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.add(ResourceFactory.create(
					ResourceType.ELECTRICITY, getElectSentToSocial()));
		}
		if(element instanceof WaterElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.add(ResourceFactory.create(
					ResourceType.ELECTRICITY, getElectSentToWater()));
		}
		return exchange;
	}
}
