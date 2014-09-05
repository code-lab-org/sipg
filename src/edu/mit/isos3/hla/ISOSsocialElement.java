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
import edu.mit.isos3.element.ElectElement;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.PetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.element.WaterElement;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceType;

/**
 * ISOSsocialElement is the HLA object class implementing the {@link SocialElement} 
 * interface for communication with the RTI.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public class ISOSsocialElement extends ISOSelement implements SocialElement {
	private static Logger logger = Logger.getLogger(ISOSsocialElement.class);
	public static final String CLASS_NAME = "HLAobjectRoot.Element.SocialElement";
	
	public static final String NAME_ATTRIBUTE = "Name",
			LOCATION_ATTRIBUTE = "Location",
			ELECT_RECEIVED_ATTRIBUTE = "ElectReceived",
			PETROL_RECEIVED_ATTRIBUTE = "PetrolReceived",
			WATER_RECEIVED_ATTRIBUTE = "WaterReceived";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		LOCATION_ATTRIBUTE,
		ELECT_RECEIVED_ATTRIBUTE,
		PETROL_RECEIVED_ATTRIBUTE,
		WATER_RECEIVED_ATTRIBUTE
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
	private final HLAfloat64BE petrolReceived;
	private final HLAfloat64BE waterReceived;
	
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
	public ISOSsocialElement(RTIambassador rtiAmbassador, 
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
		
		logger.trace("Creating the petrol received data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		petrolReceived = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE),  petrolReceived);
		sendOrderMap.put(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the water received data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		waterReceived = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(WATER_RECEIVED_ATTRIBUTE),  waterReceived);
		sendOrderMap.put(getAttributeHandle(WATER_RECEIVED_ATTRIBUTE), 
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
		if(object instanceof SocialElement) {
			SocialElement element = (SocialElement) object;
			electReceived.setValue(element.getElectReceived());
			petrolReceived.setValue(element.getPetrolReceived());
			waterReceived.setValue(element.getWaterReceived());
		} else {
			logger.warn("Incompatible object passed: expected " 
					+ SocialElement.class + " but received "
					+ object.getClass() + ".");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.fss.hla.HLAobject#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ISOSsocialElement { name: ").append(getName())
				.append(", location: ").append(getLocation())
				.append(", electReceived: ").append(getElectReceived())
				.append(", petrolReceived: ").append(getPetrolReceived())
				.append(", waterReceived: ").append(getWaterReceived())
				.append("}").toString();
	}

	@Override
	public double getElectReceived() {
		return electReceived.getValue();
	}

	@Override
	public double getPetrolReceived() {
		return petrolReceived.getValue();
	}

	@Override
	public double getWaterReceived() {
		return waterReceived.getValue();
	}

	@Override
	public void updatePeriodicAttributes(RTIambassador rtiAmbassador) throws RTIexception {
		AttributeHandleSet ahs = rtiAmbassador.getAttributeHandleSetFactory().create();
		ahs.add(getAttributeHandle(ELECT_RECEIVED_ATTRIBUTE));
		ahs.add(getAttributeHandle(PETROL_RECEIVED_ATTRIBUTE));
		ahs.add(getAttributeHandle(WATER_RECEIVED_ATTRIBUTE));
		updateAttributes(rtiAmbassador, ahs);
	}

	@Override
	public Resource getNetExchange(Element element, long duration) {
		Resource exchange = ResourceFactory.create();
		if(element instanceof ElectElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.subtract(ResourceFactory.create(
					ResourceType.ELECTRICITY, getElectReceived()));
		}
		if(element instanceof PetrolElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.subtract(ResourceFactory.create(
					ResourceType.OIL, getPetrolReceived()));
		}
		if(element instanceof WaterElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.subtract(ResourceFactory.create(
					ResourceType.WATER, getWaterReceived()));
		}
		return exchange;
	}
}
