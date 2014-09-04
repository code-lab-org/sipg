/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos3.hla;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

import org.apache.log4j.Logger;

import edu.mit.isos3.SimEntity;
import edu.mit.isos3.element.WaterElement;

/**
 * ISOSwaterElement is the HLA object class implementing the {@link WaterElement} 
 * interface for communication with the RTI.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public class ISOSwaterElement extends ISOSelement implements WaterElement {
	private static Logger logger = Logger.getLogger(ISOSwaterElement.class);
	public static final String CLASS_NAME = "HLAobjectRoot.Element.WaterElement";
	
	public static final String NAME_ATTRIBUTE = "Name",
			LOCATION_ATTRIBUTE = "Location",
			ELECT_RECEIVED_ATTRIBUTE = "ElectReceived",
			WATER_SENT_TO_SOCIAL_ATTRIBUTE = "WaterSentToSocial";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		LOCATION_ATTRIBUTE,
		ELECT_RECEIVED_ATTRIBUTE,
		WATER_SENT_TO_SOCIAL_ATTRIBUTE
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

	private final HLAfloat64BE electReceived;
	private final HLAfloat64BE waterSentToSocial;
	
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
	public ISOSwaterElement(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) 
					throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		
		logger.trace("Creating the elect received data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		electReceived = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECT_RECEIVED_ATTRIBUTE),  electReceived);
		sendOrderMap.put(getAttributeHandle(ELECT_RECEIVED_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the water sent to social data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		waterSentToSocial = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(WATER_SENT_TO_SOCIAL_ATTRIBUTE),  waterSentToSocial);
		sendOrderMap.put(getAttributeHandle(WATER_SENT_TO_SOCIAL_ATTRIBUTE), 
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
		if(object instanceof WaterElement) {
			WaterElement element = (WaterElement) object;
			electReceived.setValue(element.getElectReceived());
			waterSentToSocial.setValue(element.getWaterSentToSocial());
		} else {
			logger.warn("Incompatible object passed: expected " 
					+ WaterElement.class + " but received "
					+ object.getClass() + ".");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.fss.hla.HLAobject#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ISOSsocialElement { name: ")
				.append(getName()).append(", location: ").append(getLocation().toString())
				.append(getName()).append(", electReceived: ").append(getElectReceived())
				.append(getName()).append(", waterSentToSocial: ").append(getWaterSentToSocial())
				.append("}").toString();
	}

	@Override
	public double getElectReceived() {
		return electReceived.getValue();
	}

	@Override
	public double getWaterSentToSocial() {
		return waterSentToSocial.getValue();
	}
}
