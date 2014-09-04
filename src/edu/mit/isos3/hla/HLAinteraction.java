/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos3.hla;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.mit.isos2.SimInteraction;

/**
 * An HLAinteraction is the base class for communicating transient interaction
 * classes with the HLA RTI. It performs some low-level functions related to 
 * setting parameters for local or remote interactions.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class HLAinteraction implements SimInteraction {
	private static Logger logger = Logger.getLogger(HLAinteraction.class);
	
	private final RTIambassador rtiAmbassador;
	private final InteractionClassHandle interactionClassHandle;
	private final Map<String,ParameterHandle> parameterHandles = 
			new HashMap<String,ParameterHandle>();
	
	/**
	 * Instantiates a new HLA interaction.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @throws RTIexception the RTI exception
	 */
	public HLAinteraction(RTIambassador rtiAmbassador) throws RTIexception {
		this.rtiAmbassador = rtiAmbassador;
		interactionClassHandle = rtiAmbassador.getInteractionClassHandle(
				getInteractionClassName());
		for(String parameterName : getParameterNames()) {
			parameterHandles.put(parameterName, 
					rtiAmbassador.getParameterHandle(
							interactionClassHandle, parameterName));
		}
	}
	
	/**
	 * Gets this object's FOM interaction class name.
	 *
	 * @return the interaction class name
	 */
	public abstract String getInteractionClassName();
	
	/**
	 * Gets this object's RTI-assigned parameter handle for a given FOM 
	 * parameter name. Returns null if the attribute handle does not exist.
	 *
	 * @param parameterName the parameter name
	 * @return the parameter handle
	 */
	public final ParameterHandle getParameterHandle(String parameterName)  {
		return parameterHandles.get(parameterName);
	}
	
	/**
	 * Gets this object's FOM parameter names as an array of strings.
	 *
	 * @return the parameter names
	 */
	public abstract String[] getParameterNames();
	
	/**
	 * Gets this object's data element value for a given parameter handle.
	 *
	 * @param parameterHandle the parameter handle
	 * @return the parameter value
	 */
	public abstract DataElement getParameterValue(ParameterHandle parameterHandle);
	
	/**
	 * Gets this object's FOM send order.
	 *
	 * @return the send order
	 */
	public abstract OrderType getSendOrder();

	/**
	 * Sends this interaction to the RTI.
	 *
	 * @throws RTIexception the RTI exception
	 */
	public void send() throws RTIexception {
		ParameterHandleValueMap values = 
				rtiAmbassador.getParameterHandleValueMapFactory()
				.create(getParameterNames().length);
		
		for(String name : getParameterNames()) {
			values.put(parameterHandles.get(name), 
					getParameterValue(parameterHandles.get(name)).toByteArray());
		}
		
		if(getSendOrder()==OrderType.TIMESTAMP) {
			LogicalTime timestamp = rtiAmbassador.queryLogicalTime().add(
					rtiAmbassador.queryLookahead());
			logger.debug("Sending interaction " + this 
					+ " with timestamp " + timestamp.toString());
			rtiAmbassador.sendInteraction(interactionClassHandle, 
					values, null, timestamp);
		} else {
			logger.debug("Sending interaction " + this + ".");
			rtiAmbassador.sendInteraction(interactionClassHandle, 
					values, null);
		}
	}
	
	/**
	 * Sets this object's parameter values from a local interaction object.
	 *
	 * @param interaction the new parameters
	 */
	public abstract void setParameters(SimInteraction interaction);
	
	/**
	 * Sets all of this object's parameter values from an RTI-provided
	 * parameter handle value map.
	 *
	 * @param parameterValues the new parameters
	 * @throws DecoderException 
	 */
	public void setParameters(ParameterHandleValueMap parameterValues) 
			throws DecoderException {
		for(ParameterHandle handle : parameterValues.keySet()) {
			getParameterValue(handle).decode(parameterValues.get(handle));
		}
	}
}