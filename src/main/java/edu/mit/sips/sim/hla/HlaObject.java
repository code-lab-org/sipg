package edu.mit.sips.sim.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

/**
 * The Class HLA object.
 */
public abstract class HlaObject {
	private transient final boolean local;
	private transient final RTIambassador rtiAmbassador;
	private transient final ObjectClassHandle objectClassHandle;
	private transient final Map<String,AttributeHandle> attributeHandles = new HashMap<String,AttributeHandle>();
	private transient final AttributeHandleSet attributeHandleSet;
	private transient String instanceName;
	private transient ObjectInstanceHandle objectInstanceHandle;
	private transient EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Instantiates a new HLA object.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param instanceName the instance name
	 * @throws RTIexception the rT iexception
	 */
	public HlaObject(RTIambassador rtiAmbassador, String instanceName) 
			throws RTIexception {
		this.local = instanceName == null;
		this.rtiAmbassador = rtiAmbassador;
		
		objectClassHandle = rtiAmbassador.getObjectClassHandle(getObjectClassName());
		attributeHandleSet = rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : getAttributeNames()) {
			attributeHandles.put(attributeName, 
					rtiAmbassador.getAttributeHandle(
							objectClassHandle, attributeName));
			attributeHandleSet.add(getAttributeHandle(attributeName));
		}
		
		if(local) {
			objectInstanceHandle = rtiAmbassador.registerObjectInstance(getObjectClassHandle());
			this.instanceName = rtiAmbassador.getObjectInstanceName(objectInstanceHandle);
		} else {
			this.instanceName = instanceName;
			objectInstanceHandle = rtiAmbassador.getObjectInstanceHandle(instanceName);
		}
	}
	
	/**
	 * Delete.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void delete() throws RTIexception {
		rtiAmbassador.deleteObjectInstance(getObjectInstanceHandle(), new byte[0]);
	}
	
	/**
	 * Gets the attribute handle.
	 *
	 * @param attributeName the attribute name
	 * @return the attribute handle
	 */
	public final AttributeHandle getAttributeHandle(String attributeName)  {
		return attributeHandles.get(attributeName);
	}
	
	/**
	 * Gets the attribute handle set.
	 *
	 * @return the attribute handle set
	 */
	public final AttributeHandleSet getAttributeHandleSet() {
		return attributeHandleSet;
	}
	
	/**
	 * Gets the attribute name.
	 *
	 * @param attributeHandle the attribute handle
	 * @return the attribute name
	 */
	public final String getAttributeName(AttributeHandle attributeHandle) {
		for(String attributeName : attributeHandles.keySet()) {
			if(attributeHandles.get(attributeName).equals(attributeHandle)) {
				return attributeName;
			}
		}
		return null;
	}
	
	/**
	 * Gets the attribute names.
	 *
	 * @return the attribute names
	 */
	public abstract String[] getAttributeNames();
	
	/**
	 * Gets the attribute values.
	 *
	 * @return the attribute values
	 */
	public abstract Map<AttributeHandle,DataElement> getAttributeValues();
	
	/**
	 * Gets the instance name.
	 *
	 * @return the instance name
	 */
	public final String getInstanceName() {
		return instanceName;
	}
	
	/**
	 * Gets the object class handle.
	 *
	 * @return the object class handle
	 */
	public final ObjectClassHandle getObjectClassHandle() {
		return objectClassHandle;
	}
	
	/**
	 * Gets the object class name.
	 *
	 * @return the object class name
	 */
	public abstract String getObjectClassName();

	/**
	 * Gets the object instance handle.
	 *
	 * @return the object instance handle
	 */
	public final ObjectInstanceHandle getObjectInstanceHandle() {
		return objectInstanceHandle;
	}
	
	/**
	 * Checks if is local.
	 *
	 * @return true, if is local
	 */
	public final boolean isLocal() {
		return local;
	}

	/**
	 * Local delete.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void localDelete() throws RTIexception {
		rtiAmbassador.localDeleteObjectInstance(getObjectInstanceHandle());
	}
	
	/**
	 * Provide attributes.
	 *
	 * @param attributeHandleSet the attribute handle set
	 * @throws RTIexception the rT iexception
	 */
	public final void provideAttributes(AttributeHandleSet attributeHandleSet) 
			throws RTIexception {
		AttributeHandleValueMap attributes = 
				rtiAmbassador.getAttributeHandleValueMapFactory().
				create(getAttributeValues().keySet().size());
		for(AttributeHandle attributeHandle : attributeHandleSet) {
			attributes.put(attributeHandle, getAttributeValues().get(attributeHandle).toByteArray());
		}
		System.out.println("Providing attributes without a timestamp");
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0]);
	}
	
	/**
	 * Publish all attributes.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void publishAllAttributes() throws RTIexception {
		rtiAmbassador.publishObjectClassAttributes(
				getObjectClassHandle(), getAttributeHandleSet());
	}
	
	/**
	 * Request attribute value update.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void requestAttributeValueUpdate() throws RTIexception {
		rtiAmbassador.requestAttributeValueUpdate(
				getObjectInstanceHandle(), getAttributeHandleSet(), new byte[0]);
	}
	
	/**
	 * Sets the all attributes.
	 *
	 * @param attributeHandleValueMap the new all attributes
	 * @throws DecoderException the decoder exception
	 */
	public final void setAllAttributes(AttributeHandleValueMap attributeHandleValueMap) throws DecoderException {
		for(AttributeHandle attributeHandle : getAttributeHandleSet()) {
			ByteWrapper wrapper = attributeHandleValueMap.getValueReference(attributeHandle);
			if(wrapper != null) {
				getAttributeValues().get(attributeHandle).decode(wrapper);
			}
		}
	}
    
	/**
	 * Subscribe all attributes.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void subscribeAllAttributes() throws RTIexception {
		rtiAmbassador.subscribeObjectClassAttributes(
				getObjectClassHandle(), getAttributeHandleSet());
	}
	
	@Override
	public String toString() {
		return getInstanceName() + " (" + getObjectInstanceHandle() + ")";
	}
	
	/**
	 * Update all attributes.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public final void updateAllAttributes() throws RTIexception {
		AttributeHandleValueMap attributes = 
				rtiAmbassador.getAttributeHandleValueMapFactory().
				create(getAttributeValues().keySet().size());
		for(AttributeHandle attributeHandle : getAttributeHandleSet()) {
			attributes.put(attributeHandle, getAttributeValues().get(attributeHandle).toByteArray());
		}
		System.out.println("Sending attributes with timestamp " 
				+ rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()).toString());
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0], 
				rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()));
	}
	
	/**
	 * Update attributes.
	 *
	 * @param attributeHandleSet the attribute handle set
	 * @throws RTIexception the rT iexception
	 */
	public final void updateAttributes(AttributeHandleSet attributeHandleSet) 
			throws RTIexception {
		AttributeHandleValueMap attributes = 
				rtiAmbassador.getAttributeHandleValueMapFactory().
				create(getAttributeValues().keySet().size());
		for(AttributeHandle attributeHandle : attributeHandleSet) {
			attributes.put(attributeHandle, getAttributeValues().get(attributeHandle).toByteArray());
		}
		System.out.println("Updating attributes with timestamp " 
				+ rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()).toString());
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0], 
				rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()));
		// if an InvalidLogicalTime exception is generated, this federate may
		// be in time-advancing state - this happened during testing when the 
		// second and third federates join at nearly the same time, causing 
		// the federation to synchronize and advance time before all 
		// provideAttributeValueUpdate responses had been processed
		// this has been resolved by adding a provideAttributes method
		// to update attributes without an associated timestamp value
	}
	
	/**
	 * Update attributes.
	 *
	 * @param attributeNames the attribute names
	 * @throws RTIexception the rT iexception
	 */
	public final void updateAttributes(List<String> attributeNames) 
			throws RTIexception {
		AttributeHandleValueMap attributes = 
				rtiAmbassador.getAttributeHandleValueMapFactory().
				create(attributeNames.size());
		for(String attributeName : attributeNames) {
			attributes.put(getAttributeHandle(attributeName), 
					getAttributeValues().get(
							getAttributeHandle(attributeName)).toByteArray());
		}
		System.out.println("Sending attributes with timestamp " 
				+ rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()).toString());
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0], 
				rtiAmbassador.queryLogicalTime().add(rtiAmbassador.queryLookahead()));
	}
}
