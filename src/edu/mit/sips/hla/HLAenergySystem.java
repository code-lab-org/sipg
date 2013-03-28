package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.AttributeNotDefined;
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

import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.energy.EnergySystem;

/**
 * The Class HLAenergySystem.
 */
public class HLAenergySystem extends HLAinfrastructureSystem {
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.EnergySystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "PetroleumConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		DOMESTIC_PRODUCTION_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		PETROLEUM_CONSUMPTION_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE
	};

	/**
	 * Creates the local energy system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param energySystem the energy system
	 * @return the hL aenergy system
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
	public static HLAenergySystem createLocalEnergySystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			EnergySystem.Local energySystem) 
					throws NameNotFound, FederateNotExecutionMember, 
					NotConnected, RTIinternalError, InvalidObjectClassHandle, 
					ObjectClassNotPublished, ObjectClassNotDefined, 
					SaveInProgress, RestoreInProgress, ObjectInstanceNotKnown {
		HLAenergySystem hlaSystem = new HLAenergySystem(
				rtiAmbassador, encoderFactory, null, energySystem);
		energySystem.addAttributeChangeListener(hlaSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote energy system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param energySystem the energy system
	 * @return the hL aenergy system
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
	public static HLAenergySystem createRemoteEnergySystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) 
					throws NameNotFound, FederateNotExecutionMember, 
					NotConnected, RTIinternalError, InvalidObjectClassHandle, 
					ObjectInstanceNotKnown, AttributeNotDefined, SaveInProgress, 
					RestoreInProgress, ObjectClassNotPublished, ObjectClassNotDefined {
		HLAenergySystem hlaSystem = new HLAenergySystem(
				rtiAmbassador, encoderFactory, instanceName, 
				new DefaultEnergySystem.Remote());
		//hlaSystem.requestAttributeValueUpdate();
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

	private final HLAfloat64BE petroleumConsumption;
	private final HLAfloat64BE waterConsumption;
	/**
	 * Instantiates a new hL aenergy system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param energySystem the energy system
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
	protected HLAenergySystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName,
			EnergySystem energySystem) throws NameNotFound, 
			FederateNotExecutionMember, NotConnected, RTIinternalError, 
			InvalidObjectClassHandle, ObjectInstanceNotKnown, 
			ObjectClassNotPublished, ObjectClassNotDefined, 
			SaveInProgress, RestoreInProgress {
		super(rtiAmbassador, encoderFactory, instanceName, energySystem);
		electricityConsumption = encoderFactory.createHLAfloat64BE(
				energySystem.getElectricityConsumption());
		petroleumConsumption = encoderFactory.createHLAfloat64BE(
				energySystem.getPetroleumConsumption());
		waterConsumption = encoderFactory.createHLAfloat64BE(
				energySystem.getWaterConsumption());
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(PETROLEUM_CONSUMPTION_ATTRIBUTE), 
				petroleumConsumption);
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.AttributeChangeListener#attributeChanged(edu.mit.sips.hla.AttributeChangeEvent)
	 */
	@Override
	public void attributeChanged(AttributeChangeEvent evt) {
		super.attributeChanged(evt);
		if(evt.getSource().equals(getInfrastructureSystem())) {
			// object model changed values -- send updates to federation
			//try {
				//List<String> attributesToUpdate = new ArrayList<String>();
				if(evt.getAttributeNames().contains(
						EnergySystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
					electricityConsumption.setValue(
							getEnergySystem().getElectricityConsumption());
					//attributesToUpdate.add(ELECTRICITY_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						EnergySystem.PETROLEUM_CONSUMPTION_ATTRIBUTE)) {
					petroleumConsumption.setValue(
							getEnergySystem().getPetroleumConsumption());
					//attributesToUpdate.add(PETROLEUM_CONSUMPTION_ATTRIBUTE);
				}
				if(evt.getAttributeNames().contains(
						EnergySystem.WATER_CONSUMPTION_ATTRIBUTE)) {
					waterConsumption.setValue(
							getEnergySystem().getWaterConsumption());
					//attributesToUpdate.add(WATER_CONSUMPTION_ATTRIBUTE);
				}
				//updateAttributes(attributesToUpdate);
			//} catch(AttributeNotOwned ignored) {
			//} catch(Exception ex) {
			//	ex.printStackTrace();
			//}
		} else if(getEnergySystem() instanceof EnergySystem.Remote) {
			EnergySystem.Remote remote = (EnergySystem.Remote) getEnergySystem();
			// federation changed values -- send updates to object model
			if(evt.getAttributeNames().contains(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE)) {
				remote.setElectricityConsumption(
						electricityConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					PETROLEUM_CONSUMPTION_ATTRIBUTE)) {
				remote.setPetroleumConsumption(
						petroleumConsumption.getValue());
			}
			if(evt.getAttributeNames().contains(
					WATER_CONSUMPTION_ATTRIBUTE)) {
				remote.setWaterConsumption(
						waterConsumption.getValue());
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

	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public EnergySystem getEnergySystem() {
		return (EnergySystem) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.HLAobject#getObjectClassName()
	 */
	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	/**
	 * Sets the energy system.
	 *
	 * @param energySystem the new energy system
	 */
	public void setEnergySystem(EnergySystem.Remote energySystem) {
		// copy attribute values to new system
		energySystem.setElectricityConsumption(getEnergySystem().getElectricityConsumption());
		energySystem.setPetroleumConsumption(getEnergySystem().getPetroleumConsumption());
		energySystem.setWaterConsumption(getEnergySystem().getWaterConsumption());
		super.setInfrastructureSystem(energySystem);
	}
}
