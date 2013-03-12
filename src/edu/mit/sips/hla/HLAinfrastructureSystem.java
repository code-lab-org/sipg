package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.InfrastructureSystem;

/**
 * The Class HLAinfrastructureSystem.
 */
public abstract class HLAinfrastructureSystem extends HLAobject {
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem";
	
	public static final String NAME_ATTRIBUTE = "Name",
			SOCIETY_NAME_ATTRIBUTE = "SocietyName",
			NET_CASH_FLOW_ATTRIBUTE = "NetCashFlow",
			DOMESTIC_PRODUCTION_ATTRIBUTE = "DomesticProduction";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE
	};

	/**
	 * Publish all.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws NameNotFound the name not found
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws RTIinternalError the rT iinternal error
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 */
	public static void publishAll(RTIambassador rtiAmbassador) 
			throws FederateNotExecutionMember, NotConnected, NameNotFound, 
			InvalidObjectClassHandle, RTIinternalError, AttributeNotDefined, 
			ObjectClassNotDefined, SaveInProgress, RestoreInProgress {
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
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws NameNotFound the name not found
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws RTIinternalError the rT iinternal error
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 */
	public static void subscribeAll(RTIambassador rtiAmbassador) 
			throws FederateNotExecutionMember, NotConnected, NameNotFound, 
			InvalidObjectClassHandle, RTIinternalError, AttributeNotDefined, 
			ObjectClassNotDefined, SaveInProgress, RestoreInProgress {
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

	private final InfrastructureSystem infrastructureSystem;
	private final HLAunicodeString name;
	private final HLAunicodeString societyName;
	private final HLAfloat64BE netCashFlow;
	private final HLAfloat64BE domesticProduction;
	protected final Map<AttributeHandle,DataElement> attributeValues = 
			new HashMap<AttributeHandle,DataElement>();
	
	/**
	 * Instantiates a new HLA infrastructure system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param infrastructureSystem the infrastructure system
	 * @throws NameNotFound the name not found
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws ObjectClassNotDefined 
	 * @throws ObjectClassNotPublished 
	 */
	protected HLAinfrastructureSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory,
			InfrastructureSystem infrastructureSystem) throws NameNotFound, 
			FederateNotExecutionMember, NotConnected, RTIinternalError, 
			InvalidObjectClassHandle, ObjectInstanceNotKnown, 
			ObjectClassNotPublished, ObjectClassNotDefined, SaveInProgress, 
			RestoreInProgress {
		super(rtiAmbassador, infrastructureSystem instanceof InfrastructureSystem.Local?true:false);
		this.infrastructureSystem = infrastructureSystem;
		name = encoderFactory.createHLAunicodeString();
		societyName = encoderFactory.createHLAunicodeString();
		netCashFlow = encoderFactory.createHLAfloat64BE();
		domesticProduction = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(NAME_ATTRIBUTE), name);
		attributeValues.put(getAttributeHandle(SOCIETY_NAME_ATTRIBUTE), societyName);
		attributeValues.put(getAttributeHandle(NET_CASH_FLOW_ATTRIBUTE), netCashFlow);
		attributeValues.put(getAttributeHandle(DOMESTIC_PRODUCTION_ATTRIBUTE), domesticProduction);
	}
	
	/**
	 * Gets the infrastructure system.
	 *
	 * @return the infrastructure system
	 */
	public InfrastructureSystem getInfrastructureSystem() {
		return infrastructureSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.AttributeChangeListener#attributeChanged(edu.mit.sips.hla.AttributeChangeEvent)
	 */
	@Override
	public void attributeChanged(AttributeChangeEvent evt) {
		if(evt.getSource().equals(infrastructureSystem)) {
			// object model changed values -- send updates to federation
			try {
				if(evt.getAttributeName().equals(InfrastructureSystem.NAME_ATTRIBUTE)) {
					name.setValue(infrastructureSystem.getName());
					updateAttribute(NAME_ATTRIBUTE);
				} else if(evt.getAttributeName().equals(InfrastructureSystem.SOCIETY_ATTRIBUTE)) {
					societyName.setValue(infrastructureSystem.getSociety().getName());
					updateAttribute(SOCIETY_NAME_ATTRIBUTE);
				} else if(evt.getAttributeName().equals(InfrastructureSystem.CASH_FLOW_ATTRIBUTE)) {
					netCashFlow.setValue(infrastructureSystem.getCashFlow());
					updateAttribute(NET_CASH_FLOW_ATTRIBUTE);
				} else if(evt.getAttributeName().equals(InfrastructureSystem.DOMESTIC_PRODUCTION_ATTRIBUTE)) {
					domesticProduction.setValue(infrastructureSystem.getDomesticProduction());
					updateAttribute(DOMESTIC_PRODUCTION_ATTRIBUTE);
				} 
			} catch(AttributeNotOwned ignored) {
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} else if(infrastructureSystem instanceof InfrastructureSystem.Remote) {
			InfrastructureSystem.Remote remote = (InfrastructureSystem.Remote) infrastructureSystem;
			// federation changed values -- send updates to object model
			if(evt.getAttributeName().equals(NAME_ATTRIBUTE)) {
				remote.setName(name.getValue());
			} else if(evt.getAttributeName().equals(SOCIETY_NAME_ATTRIBUTE)) {
				remote.setSociety(remote.getSociety().getCountry().getSociety(societyName.getValue()));
			} else if(evt.getAttributeName().equals(NET_CASH_FLOW_ATTRIBUTE)) {
				remote.setCashFlow(netCashFlow.getValue());
			} else if(evt.getAttributeName().equals(DOMESTIC_PRODUCTION_ATTRIBUTE)) {
				remote.setDomesticProduction(domesticProduction.getValue());
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
}
