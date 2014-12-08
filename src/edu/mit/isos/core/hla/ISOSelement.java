/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos.core.hla;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.RTIexception;

import org.apache.log4j.Logger;

import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.Node;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.sim.SimEntity;

/**
 * ISOSelement is the HLA object class implementing the {@link Element} 
 * interface for communication with the RTI.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class ISOSelement extends HLAobject implements Element {
	private static Logger logger = Logger.getLogger(ISOSelement.class);
	public static final String CLASS_NAME = "HLAobjectRoot.Element";
	
	public static final String NAME_ATTRIBUTE = "Name",
			LOCATION_ATTRIBUTE = "Location";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		LOCATION_ATTRIBUTE
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

	private final HLAunicodeString name;
	private final HLAunicodeString location;
	
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
	public ISOSelement(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) 
					throws RTIexception {
		super(rtiAmbassador, instanceName);
		
		logger.trace("Creating the name data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		name = encoderFactory.createHLAunicodeString();
		attributeValues.put(getAttributeHandle(NAME_ATTRIBUTE),  name);
		sendOrderMap.put(getAttributeHandle(NAME_ATTRIBUTE), 
				OrderType.RECEIVE);

		logger.trace("Creating the location data element, " 
				+ "adding it as an attribute, "
				+ " and setting the send order.");
		location = encoderFactory.createHLAunicodeString();
		attributeValues.put(getAttributeHandle(LOCATION_ATTRIBUTE), location);
		sendOrderMap.put(getAttributeHandle(LOCATION_ATTRIBUTE), 
				OrderType.RECEIVE);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getAttributeNames()
	 */
	@Override
	public String[] getAttributeNames() {
		return ATTRIBUTES;
	}

	/* (non-Javadoc)
	 * @see edu.mit.fss.Element#getFrame()
	 */
	@Override
	public Location getLocation() {
		String[] nodeNames = location.getValue().split("-");
		if(nodeNames.length==1) {
			return new Location(new Node(nodeNames[0]), new Node(nodeNames[0]));
		} else if(nodeNames.length == 2) {
			return new Location(new Node(nodeNames[0]), new Node(nodeNames[1]));
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.fss.Element#getName()
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
	 * @see edu.mit.fss.hla.HLAobject#setAttributes(java.lang.Object)
	 */
	@Override
	public void setAttributes(SimEntity object) {
		if(object instanceof Element) {
			Element element = (Element) object;
			name.setValue(element.getName());
			location.setValue(element.getLocation().toString());
		} else {
			logger.warn("Incompatible object passed: expected " 
					+ Element.class + " but received "
					+ object.getClass() + ".");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.fss.hla.HLAobject#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ISOSelement { name: ").append(getName())
				.append(", location: ").append(getLocation())
				.append("}").toString();
	}
	
	public final void updateStaticAttributes(RTIambassador rtiAmbassador) throws RTIexception {
		AttributeHandleSet ahs = rtiAmbassador.getAttributeHandleSetFactory().create();
		ahs.add(getAttributeHandle(NAME_ATTRIBUTE));
		ahs.add(getAttributeHandle(LOCATION_ATTRIBUTE));
		updateAttributes(rtiAmbassador, ahs);
	}
	
	public abstract void updatePeriodicAttributes(RTIambassador rtiAmbassador) throws RTIexception;
}
