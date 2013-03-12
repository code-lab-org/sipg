package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
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

import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class HLAwaterSystem.
 */
public class HLAwaterSystem extends HLAinfrastructureSystem {
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.WaterSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	WATER_SUPPLY_PER_CAPITA_ATTRIBUTE = "WaterSupplyPerCapita";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		WATER_SUPPLY_PER_CAPITA_ATTRIBUTE
	};
	
	/**
	 * Creates the local water system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param waterSystem the water system
	 * @return the hL awater system
	 * @throws NameNotFound the name not found
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws ObjectClassNotPublished the object class not published
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws ObjectInstanceNotKnown the object instance not known
	 */
	public static HLAwaterSystem createLocalWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			WaterSystem.Local waterSystem) 
					throws NameNotFound, FederateNotExecutionMember, 
					NotConnected, RTIinternalError, InvalidObjectClassHandle, 
					ObjectClassNotPublished, ObjectClassNotDefined, 
					SaveInProgress, RestoreInProgress, ObjectInstanceNotKnown {
		HLAwaterSystem hlaSystem = new HLAwaterSystem(
				rtiAmbassador, encoderFactory, waterSystem);
		waterSystem.addAttributeChangeListener(hlaSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote water system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param waterSystem the water system
	 * @return the hL awater system
	 * @throws NameNotFound the name not found
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws ObjectClassNotDefined 
	 * @throws ObjectClassNotPublished 
	 */
	public static HLAwaterSystem createRemoteWaterSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName, WaterSystem.Remote waterSystem) 
					throws NameNotFound, FederateNotExecutionMember, 
					NotConnected, RTIinternalError, InvalidObjectClassHandle, 
					ObjectInstanceNotKnown, AttributeNotDefined, SaveInProgress, 
					RestoreInProgress, ObjectClassNotPublished, ObjectClassNotDefined {
		HLAwaterSystem hlaSystem = new HLAwaterSystem(
				rtiAmbassador, encoderFactory, waterSystem);
		hlaSystem.requestAttributeValueUpdate();
		hlaSystem.addAttributeChangeListener(hlaSystem);
		return hlaSystem;
	}

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

	private final HLAfloat64BE electricityConsumption;
	private final HLAfloat64BE waterSupplyPerCapita;
	
	/**
	 * Instantiates a new hL awater system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param waterSystem the water system
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
	protected HLAwaterSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory,
			WaterSystem waterSystem) throws NameNotFound, 
			FederateNotExecutionMember, NotConnected, RTIinternalError, 
			InvalidObjectClassHandle, ObjectInstanceNotKnown, 
			ObjectClassNotPublished, ObjectClassNotDefined, 
			SaveInProgress, RestoreInProgress {
		super(rtiAmbassador, encoderFactory, waterSystem);
		electricityConsumption = encoderFactory.createHLAfloat64BE();
		waterSupplyPerCapita = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(WATER_SUPPLY_PER_CAPITA_ATTRIBUTE), 
				waterSupplyPerCapita);
	}
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem() {
		return (WaterSystem) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.AttributeChangeListener#attributeChanged(edu.mit.sips.hla.AttributeChangeEvent)
	 */
	@Override
	public void attributeChanged(AttributeChangeEvent evt) {
		super.attributeChanged(evt);
		if(evt.getSource().equals(getInfrastructureSystem())) {
			// object model changed values -- send updates to federation
			try {
				if(evt.getAttributeName().equals(
						WaterSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
					electricityConsumption.setValue(
							getWaterSystem().getElectricityConsumption());
					updateAttribute(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
				} else if(evt.getAttributeName().equals(
						WaterSystem.WATER_SUPPLY_PER_CAPITA_ATTRIBUTE)) {
					waterSupplyPerCapita.setValue(
							getWaterSystem().getWaterSupplyPerCapita());
					updateAttribute(WATER_SUPPLY_PER_CAPITA_ATTRIBUTE);
				}
			} catch(AttributeNotOwned ignored) {
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} else if(getWaterSystem() instanceof WaterSystem.Remote) {
			WaterSystem.Remote remote = (WaterSystem.Remote) getWaterSystem();
			// federation changed values -- send updates to object model
			if(evt.getAttributeName().equals(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
				remote.setElectricityConsumption(
						electricityConsumption.getValue());
			} else if(evt.getAttributeName().equals(
					WATER_SUPPLY_PER_CAPITA_ATTRIBUTE)) {
				remote.setWaterSupplyPerCapita(
						waterSupplyPerCapita.getValue());
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
