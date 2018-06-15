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
import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.hla.ISOSelement;
import edu.mit.isos.core.sim.SimEntity;

/**
 * ISOSpetrolElement is the HLA object class implementing the {@link PetrolElement} 
 * interface for communication with the RTI.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public class ISOSpetrolElement extends ISOSelement implements PetrolElement {
	private static Logger logger = Logger.getLogger(ISOSpetrolElement.class);
	public static final String CLASS_NAME = "HLAobjectRoot.Element.PetrolElement";
	
	public static final String NAME_ATTRIBUTE = "Name",
			LOCATION_ATTRIBUTE = "Location",
			ELECT_RECEIVED_ATTRIBUTE = "ElectReceived",
			PETROL_SENT_TO_ELECT_ATTRIBUTE = "PetrolSentToElect",
			PETROL_SENT_TO_SOCIAL_ATTRIBUTE = "PetrolSentToSocial";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		LOCATION_ATTRIBUTE,
		ELECT_RECEIVED_ATTRIBUTE,
		PETROL_SENT_TO_ELECT_ATTRIBUTE,
		PETROL_SENT_TO_SOCIAL_ATTRIBUTE
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
	private final HLAfloat64BE petrolSentToElect;
	private final HLAfloat64BE petrolSentToSocial;
	
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
	public ISOSpetrolElement(RTIambassador rtiAmbassador, 
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
		
		logger.trace("Creating the petrol sent to elect data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		petrolSentToElect = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(PETROL_SENT_TO_ELECT_ATTRIBUTE),  petrolSentToElect);
		sendOrderMap.put(getAttributeHandle(PETROL_SENT_TO_ELECT_ATTRIBUTE), 
				OrderType.TIMESTAMP);
		
		logger.trace("Creating the petrol sent to social data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		petrolSentToSocial = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(PETROL_SENT_TO_SOCIAL_ATTRIBUTE),  petrolSentToSocial);
		sendOrderMap.put(getAttributeHandle(PETROL_SENT_TO_SOCIAL_ATTRIBUTE), 
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
		if(object instanceof PetrolElement) {
			PetrolElement element = (PetrolElement) object;
			electReceived.setValue(element.getElectReceived());
			petrolSentToElect.setValue(element.getPetrolSentToElect());
			petrolSentToSocial.setValue(element.getPetrolSentToSocial());
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
		return new StringBuilder().append("ISOSpetrolElement { name: ").append(getName())
				.append(", location: ").append(getLocation())
				.append(", electReceived: ").append(getElectReceived())
				.append(", petrolSentToElect: ").append(getPetrolSentToElect())
				.append(", petrolSentToSocial: ").append(getPetrolSentToSocial())
				.append("}").toString();
	}

	@Override
	public double getElectReceived() {
		return electReceived.getValue();
	}

	@Override
	public double getPetrolSentToElect() {
		return petrolSentToElect.getValue();
	}

	@Override
	public double getPetrolSentToSocial() {
		return petrolSentToSocial.getValue();
	}

	@Override
	public void updatePeriodicAttributes(RTIambassador rtiAmbassador) throws RTIexception {
		AttributeHandleSet ahs = rtiAmbassador.getAttributeHandleSetFactory().create();
		ahs.add(getAttributeHandle(ELECT_RECEIVED_ATTRIBUTE));
		ahs.add(getAttributeHandle(PETROL_SENT_TO_ELECT_ATTRIBUTE));
		ahs.add(getAttributeHandle(PETROL_SENT_TO_SOCIAL_ATTRIBUTE));
		updateAttributes(rtiAmbassador, ahs);
	}

	@Override
	public Resource getNetExchange(Element element, long duration) {
		Resource exchange = ResourceFactory.create();
		if(element instanceof ElectElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.add(ResourceFactory.create(
					ResourceType.OIL, getPetrolSentToElect()));
			exchange = exchange.subtract(ResourceFactory.create(
					ResourceType.ELECTRICITY, getElectReceived()));
		}
		if(element instanceof SocialElement && element.getLocation().equals(getLocation())) {
			exchange = exchange.add(ResourceFactory.create(
					ResourceType.OIL, getPetrolSentToSocial()));
		}
		return exchange;
	}
}
