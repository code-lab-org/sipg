package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.DeletePrivilegeNotHeld;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

/**
 * The Class HLAobject.
 */
public abstract class HLAobject implements AttributeChangeListener {
	protected final RTIambassador rtiAmbassador;
	protected final ObjectClassHandle objectClassHandle;
	protected final Map<String,AttributeHandle> attributeHandles = new HashMap<String,AttributeHandle>();
	protected final AttributeHandleSet attributeHandleSet;
	protected String instanceName;
	protected ObjectInstanceHandle objectInstanceHandle;
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Instantiates a new HLA object.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @throws NameNotFound the name not found
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 */
	public HLAobject(RTIambassador rtiAmbassador) 
			throws NameNotFound, FederateNotExecutionMember, NotConnected, 
			RTIinternalError, InvalidObjectClassHandle {
		this.rtiAmbassador = rtiAmbassador;
		
		objectClassHandle = rtiAmbassador.getObjectClassHandle(getObjectClassName());
		attributeHandleSet = rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : getAttributeNames()) {
			attributeHandles.put(attributeName, rtiAmbassador.getAttributeHandle(objectClassHandle, attributeName));
			attributeHandleSet.add(getAttributeHandle(attributeName));
		}
	}
	
	/**
	 * Adds the property change listener.
	 *
	 * @param listener the listener
	 */
	public final void addAttributeChangeListener(AttributeChangeListener listener) {
		listenerList.add(AttributeChangeListener.class, listener);
	}
	
	/**
	 * Delete.
	 *
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 * @throws DeletePrivilegeNotHeld the delete privilege not held
	 * @throws ObjectInstanceNotKnown the object instance not known
	 */
	public final void delete() throws SaveInProgress, RestoreInProgress, 
			FederateNotExecutionMember, NotConnected, RTIinternalError, DeletePrivilegeNotHeld, 
			ObjectInstanceNotKnown {
		rtiAmbassador.deleteObjectInstance(getObjectInstanceHandle(), new byte[0]);
	}
	
	/**
	 * Fire property change event.
	 *
	 * @param event the event
	 */
	protected final void fireAttributeChangeEvent(AttributeChangeEvent event) {
		AttributeChangeListener[] listeners = listenerList.getListeners(AttributeChangeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].attributeChanged(event);
		}
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
	 * Local delete.
	 *
	 * @throws OwnershipAcquisitionPending the ownership acquisition pending
	 * @throws FederateOwnsAttributes the federate owns attributes
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void localDelete() throws OwnershipAcquisitionPending, 
			FederateOwnsAttributes, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, 
			FederateNotExecutionMember, NotConnected, RTIinternalError {
		rtiAmbassador.localDeleteObjectInstance(getObjectInstanceHandle());
	}

	/**
	 * Publish all attributes.
	 *
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void publishAllAttributes() throws AttributeNotDefined, 
			ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
			NotConnected, RTIinternalError {
		rtiAmbassador.publishObjectClassAttributes(
				getObjectClassHandle(), getAttributeHandleSet());
	}
	
	/**
	 * Removes the property change listener.
	 *
	 * @param listener the listener
	 */
	public final void removeAttributeChangeListener(AttributeChangeListener listener) {
		listenerList.remove(AttributeChangeListener.class, listener);
	}
	
	/**
	 * Request attribute value update.
	 *
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void requestAttributeValueUpdate() 
			throws AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, 
			RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
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
				fireAttributeChangeEvent(new AttributeChangeEvent(this, 
						getAttributeName(attributeHandle)));
			}
		}
	}
	
	/**
	 * Subscribe all attributes.
	 *
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void subscribeAllAttributes() throws AttributeNotDefined, 
			ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
			NotConnected, RTIinternalError {
		rtiAmbassador.subscribeObjectClassAttributes(getObjectClassHandle(), getAttributeHandleSet());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getInstanceName() + " (" + getObjectInstanceHandle() + ")";
	}
    
	/**
	 * Update all attributes.
	 *
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws AttributeNotOwned the attribute not owned
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void updateAllAttributes() throws FederateNotExecutionMember, 
			NotConnected, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, 
			RestoreInProgress, RTIinternalError {
		AttributeHandleValueMap attributes = rtiAmbassador.getAttributeHandleValueMapFactory().create(getAttributeValues().keySet().size());
		for(AttributeHandle attributeHandle : getAttributeHandleSet()) {
			attributes.put(attributeHandle, getAttributeValues().get(attributeHandle).toByteArray());
		}
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0]);
	}
	
	/**
	 * Update attribute.
	 *
	 * @param attributeName the attribute name
	 * @throws AttributeNotOwned the attribute not owned
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void updateAttribute(String attributeName) 
			throws AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, 
			RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		AttributeHandleValueMap attributes = rtiAmbassador.getAttributeHandleValueMapFactory().create(1);
		attributes.put(getAttributeHandle(attributeName), 
				getAttributeValues().get(getAttributeHandle(attributeName)).toByteArray());
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0]);
	}
	
	/**
	 * Update attributes.
	 *
	 * @param attributeHandleSet the attribute handle set
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws AttributeNotOwned the attribute not owned
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws RTIinternalError the RTI internal error
	 */
	public final void updateAttributes(AttributeHandleSet attributeHandleSet) 
			throws FederateNotExecutionMember, NotConnected, AttributeNotOwned, AttributeNotDefined, 
			ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, RTIinternalError {
		AttributeHandleValueMap attributes = rtiAmbassador.getAttributeHandleValueMapFactory().create(getAttributeValues().keySet().size());
		for(AttributeHandle attributeHandle : attributeHandleSet) {
			attributes.put(attributeHandle, getAttributeValues().get(attributeHandle).toByteArray());
		}
		rtiAmbassador.updateAttributeValues(
				getObjectInstanceHandle(), attributes, new byte[0]);
	}
}
