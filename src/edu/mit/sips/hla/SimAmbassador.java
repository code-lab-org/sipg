/*
 * 
 */
package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TimeQueryReturn;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger64BE;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyEnabled;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InTimeAdvancingState;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLogicalTime;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;
import hla.rti1516e.exceptions.InvalidLookahead;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.LogicalTimeAlreadyPassed;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RequestForTimeConstrainedPending;
import hla.rti1516e.exceptions.RequestForTimeRegulationPending;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.SynchronizationPointLabelNotAnnounced;
import hla.rti1516e.exceptions.TimeConstrainedAlreadyEnabled;
import hla.rti1516e.exceptions.TimeConstrainedIsNotEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationIsNotEnabled;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;
import hla.rti1516e.time.HLAinteger64Interval;
import hla.rti1516e.time.HLAinteger64Time;
import hla.rti1516e.time.HLAinteger64TimeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.sim.Simulator;

public class SimAmbassador extends NullFederateAmbassador {
	
	private final Simulator simulator;
	private final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private HLAinteger64Time logicalTime, startTime;
	private HLAinteger64Interval lookaheadInterval, tickInterval;
	private volatile AtomicBoolean timeConstrained = new AtomicBoolean(false);
	private volatile AtomicBoolean timeRegulating = new AtomicBoolean(false);
	private volatile AtomicBoolean timeAdvanceGranted =  new AtomicBoolean(false);
	private volatile AtomicBoolean syncRegistered = new AtomicBoolean(false);
	private volatile AtomicBoolean syncAnnounced = new AtomicBoolean(false);
	private volatile AtomicBoolean syncAchieved = new AtomicBoolean(false);
	private volatile AtomicBoolean initialized =  new AtomicBoolean(false);
	
	private final Map<ObjectInstanceHandle, HLAinfrastructureSystem> hlaObjects = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, HLAinfrastructureSystem>());
	
	/**
	 * Instantiates a new sim ambassador.
	 * @throws RTIinternalError 
	 */
	public SimAmbassador(Simulator simulator)
			throws RTIinternalError {
		this.simulator = simulator;
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	/**
	 * Advance.
	 *
	 * @throws LogicalTimeAlreadyPassed the logical time already passed
	 * @throws InvalidLogicalTime the invalid logical time
	 * @throws InTimeAdvancingState the in time advancing state
	 * @throws RequestForTimeRegulationPending the request for time regulation pending
	 * @throws RequestForTimeConstrainedPending the request for time constrained pending
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws IllegalTimeArithmetic the illegal time arithmetic
	 * @throws TimeRegulationIsNotEnabled 
	 * @throws InvalidLogicalTimeInterval 
	 * @throws ObjectInstanceNotKnown 
	 * @throws AttributeNotDefined 
	 * @throws AttributeNotOwned 
	 */
	public void advance() 
			throws LogicalTimeAlreadyPassed, InvalidLogicalTime, 
			InTimeAdvancingState, RequestForTimeRegulationPending, 
			RequestForTimeConstrainedPending, SaveInProgress, 
			RestoreInProgress, FederateNotExecutionMember, NotConnected, 
			RTIinternalError, IllegalTimeArithmetic, AttributeNotOwned, 
			AttributeNotDefined, ObjectInstanceNotKnown, 
			InvalidLogicalTimeInterval, TimeRegulationIsNotEnabled {
		for(HLAinfrastructureSystem system : hlaObjects.values()) {
			if(system.isLocal()) {
				system.updateAllAttributes();
			}
		}
		
		for(City city : simulator.getCountry().getCities()) {
			if(city.getSocialSystem() instanceof SocialSystem.Local) {
				// TODO incomplete
				city.getSocialSystem().fireAttributeChangeEvent(
						Arrays.asList(SocialSystem.POPULATION_ATTRIBUTE));
			}
		}
		
		rtiAmbassador.timeAdvanceRequest(logicalTime.add(tickInterval));
		while(!timeAdvanceGranted.get()) {
			Thread.yield();
		}
		timeAdvanceGranted.set(false);
	}
	
	/**
	 * Connect.
	 *
	 * @throws ConnectionFailed the connection failed
	 * @throws InvalidLocalSettingsDesignator the invalid local settings designator
	 * @throws UnsupportedCallbackModel the unsupported callback model
	 * @throws CallNotAllowedFromWithinCallback the call not allowed from within callback
	 * @throws RTIinternalError the rT iinternal error
	 * @throws ObjectInstanceNotKnown 
	 * @throws ObjectClassNotPublished 
	 * @throws ObjectClassNotDefined 
	 * @throws AttributeNotDefined 
	 * @throws InvalidObjectClassHandle 
	 * @throws NameNotFound 
	 * @throws RequestForTimeRegulationPending 
	 * @throws InvalidLookahead 
	 * @throws RequestForTimeConstrainedPending 
	 * @throws InTimeAdvancingState 
	 * @throws FederateNotExecutionMember 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws FederationExecutionDoesNotExist 
	 * @throws FederateNameAlreadyInUse 
	 * @throws MalformedURLException 
	 * @throws NotConnected 
	 * @throws CouldNotOpenFDD 
	 * @throws ErrorReadingFDD 
	 * @throws InconsistentFDD 
	 * @throws CouldNotCreateLogicalTimeFactory 
	 */
	public void connect() 
			throws ConnectionFailed, InvalidLocalSettingsDesignator, 
			UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, 
			RTIinternalError, CouldNotCreateLogicalTimeFactory, 
			InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, 
			NotConnected, MalformedURLException, FederateNameAlreadyInUse, 
			FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, 
			FederateNotExecutionMember, InTimeAdvancingState, 
			RequestForTimeConstrainedPending, InvalidLookahead, 
			RequestForTimeRegulationPending, NameNotFound, InvalidObjectClassHandle, 
			AttributeNotDefined, ObjectClassNotDefined, ObjectClassNotPublished, 
			ObjectInstanceNotKnown {
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE, 
					simulator.getConnection().getLocalSettingsDesignator());
		} catch(AlreadyConnected ignored) { }
		simulator.getConnection().setConnected(true);
		
		joinFederation();
	}
	
	/**
	 * Disconnect.
	 * @throws RTIinternalError 
	 * @throws CallNotAllowedFromWithinCallback 
	 * @throws FederateIsExecutionMember 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws FederateOwnsAttributes 
	 * @throws OwnershipAcquisitionPending 
	 * @throws InvalidResignAction 
	 */
	public void disconnect() 
			throws FederateIsExecutionMember, CallNotAllowedFromWithinCallback, 
			RTIinternalError, InvalidResignAction, OwnershipAcquisitionPending, 
			FederateOwnsAttributes, SaveInProgress, RestoreInProgress {
		resignFederation();
		
		rtiAmbassador.disconnect();
		
		simulator.getConnection().setConnected(false);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#discoverObjectInstance(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.ObjectClassHandle, java.lang.String)
	 */
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass, String objectName) {
		discoverObjectInstance(theObject, theObjectClass, objectName, null);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#discoverObjectInstance(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.ObjectClassHandle, java.lang.String)
	 */
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass,
			String objectName,
			FederateHandle producingFederate) {
		try {
			if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAagricultureSystem.CLASS_NAME))) {
				HLAagricultureSystem remoteSystem = 
						HLAagricultureSystem.createRemoteAgricultureSystem(
								rtiAmbassador, encoderFactory, objectName);
				synchronized(hlaObjects) {
					hlaObjects.put(theObject, remoteSystem);
				}
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAwaterSystem.CLASS_NAME))) {
				HLAwaterSystem remoteSystem = 
						HLAwaterSystem.createRemoteWaterSystem(
								rtiAmbassador, encoderFactory, objectName);
				synchronized(hlaObjects) {
					hlaObjects.put(theObject, remoteSystem);
				}
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAenergySystem.CLASS_NAME))) {
				HLAenergySystem remoteSystem = 
						HLAenergySystem.createRemoteEnergySystem(
								rtiAmbassador, encoderFactory, objectName);
				synchronized(hlaObjects) {
					hlaObjects.put(theObject, remoteSystem);
				}
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAsocialSystem.CLASS_NAME))) {
				HLAsocialSystem remoteSystem = 
						HLAsocialSystem.createRemoteSocialSystem(
								rtiAmbassador, encoderFactory, objectName);
				synchronized(hlaObjects) {
					hlaObjects.put(theObject, remoteSystem);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "An exception of type " 
					+ ex.getMessage() 
					+ " occurred while discovering an object."
					+ " See stack trace for more information.", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Initialize.
	 *
	 * @param startTime the start time
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws NameNotFound the name not found
	 * @throws InvalidObjectClassHandle the invalid object class handle
	 * @throws RTIinternalError the rT iinternal error
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectClassNotDefined the object class not defined
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws ObjectClassNotPublished the object class not published
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws LogicalTimeAlreadyPassed the logical time already passed
	 * @throws InvalidLogicalTime the invalid logical time
	 * @throws InTimeAdvancingState the in time advancing state
	 * @throws RequestForTimeRegulationPending the request for time regulation pending
	 * @throws RequestForTimeConstrainedPending the request for time constrained pending
	 * @throws InvalidLookahead 
	 * @throws CallNotAllowedFromWithinCallback 
	 * @throws FederationExecutionDoesNotExist 
	 * @throws FederateNameAlreadyInUse 
	 * @throws MalformedURLException 
	 * @throws CouldNotOpenFDD 
	 * @throws ErrorReadingFDD 
	 * @throws InconsistentFDD 
	 * @throws CouldNotCreateLogicalTimeFactory 
	 * @throws IllegalTimeArithmetic 
	 * @throws TimeRegulationIsNotEnabled 
	 * @throws InvalidLogicalTimeInterval 
	 * @throws AttributeNotOwned 
	 * @throws SynchronizationPointLabelNotAnnounced 
	 */
	public void initialize(long startTime) 
			throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
			NotConnected, RTIinternalError, LogicalTimeAlreadyPassed, 
			InvalidLogicalTime, InTimeAdvancingState, 
			RequestForTimeRegulationPending, RequestForTimeConstrainedPending, 
			CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD,
			CouldNotOpenFDD, MalformedURLException, FederateNameAlreadyInUse, 
			FederationExecutionDoesNotExist, CallNotAllowedFromWithinCallback, 
			InvalidLookahead, NameNotFound, InvalidObjectClassHandle, 
			AttributeNotDefined, ObjectClassNotDefined, ObjectClassNotPublished, 
			ObjectInstanceNotKnown, IllegalTimeArithmetic, AttributeNotOwned, 
			InvalidLogicalTimeInterval, TimeRegulationIsNotEnabled, 
			SynchronizationPointLabelNotAnnounced {
		HLAinteger64TimeFactory timeFactory = 
				(HLAinteger64TimeFactory) rtiAmbassador.getTimeFactory();
		logicalTime = timeFactory.makeInitial();
		lookaheadInterval = timeFactory.makeInterval(1);
		tickInterval = timeFactory.makeInterval(1);

		try {
			rtiAmbassador.enableAsynchronousDelivery();
		} catch (AsynchronousDeliveryAlreadyEnabled ignored) { }
		
		try {
			rtiAmbassador.enableTimeConstrained();
		} catch (TimeConstrainedAlreadyEnabled ignored) { }
		while(!timeConstrained.get()) {
			Thread.yield();
		}
		
		try {
			rtiAmbassador.enableTimeRegulation(lookaheadInterval);
		} catch (TimeRegulationAlreadyEnabled ignored) { }
		while(!timeRegulating.get()) {
			Thread.yield();
		}
		
		if(simulator.getCountry().getAgricultureSystem() instanceof AgricultureSystem.Local) {
			// if country includes a local national agriculture system
			// publish its attributes
			HLAagricultureSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getCountry().getCities()) {
				if(city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
					AgricultureSystem.Local localSystem = 
							(AgricultureSystem.Local) city.getAgricultureSystem();
					HLAagricultureSystem hlaObject = HLAagricultureSystem.
							createLocalAgricultureSystem(rtiAmbassador, encoderFactory, 
									localSystem);
					synchronized(hlaObjects) {
						hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLAagricultureSystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getCountry().getWaterSystem() instanceof WaterSystem.Local) {
			// if country includes a local national water system
			// publish its attributes
			HLAwaterSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getCountry().getCities()) {
				if(city.getWaterSystem() instanceof WaterSystem.Local) {
					WaterSystem.Local localSystem = 
							(WaterSystem.Local) city.getWaterSystem();
					HLAwaterSystem hlaObject = HLAwaterSystem.
							createLocalWaterSystem(rtiAmbassador, encoderFactory, 
									localSystem);
					synchronized(hlaObjects) {
						hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLAwaterSystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getCountry().getEnergySystem() instanceof EnergySystem.Local) {
			// if country includes a local national energy system
			// publish its attributes
			HLAenergySystem.publishAll(rtiAmbassador);
			for(City city : simulator.getCountry().getCities()) {
				if(city.getEnergySystem() instanceof EnergySystem.Local) {
					EnergySystem.Local localSystem = 
							(EnergySystem.Local) city.getEnergySystem();
					HLAenergySystem hlaObject = HLAenergySystem.
							createLocalEnergySystem(rtiAmbassador, encoderFactory, 
									localSystem);
					synchronized(hlaObjects) {
						hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLAenergySystem.subscribeAll(rtiAmbassador);

		// always publish city social system attributes
		HLAsocialSystem.publishAll(rtiAmbassador);
		for(City city : simulator.getCountry().getCities()) {
			if(city.getSocialSystem() instanceof SocialSystem.Local) {
				SocialSystem.Local localSystem = 
						(SocialSystem.Local) city.getSocialSystem();
				HLAsocialSystem hlaObject = HLAsocialSystem.
						createLocalSocialSystem(rtiAmbassador, encoderFactory, 
								localSystem);
				synchronized(hlaObjects) {
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
				}	
			}
		}
		HLAsocialSystem.subscribeAll(rtiAmbassador);
		
		this.startTime = timeFactory.makeTime(startTime);
		
		TimeQueryReturn query = rtiAmbassador.queryGALT();
		if(!query.timeIsValid) {
			// first federate
			
			// announce synchronization point
			rtiAmbassador.registerFederationSynchronizationPoint("initialized", new byte[0]);
			
			while(!syncRegistered.get()) {
				Thread.yield();
			}
			syncRegistered.set(false);
			
			System.out.println("GALT not valid: requesting ta to " + this.startTime.subtract(lookaheadInterval).getValue());
			rtiAmbassador.timeAdvanceRequest(this.startTime.subtract(lookaheadInterval));
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
		} else {
			/* use for non-time regulating federates
			System.out.println("GALT is " + ((HLAinteger64Time)query.time).getValue());
			rtiAmbassador.timeAdvanceRequest(query.time);
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
			*/
		}
		
		while(!syncAnnounced.get()) {
			Thread.yield();
		}
		syncAnnounced.set(false);
		
		rtiAmbassador.synchronizationPointAchieved("initialized");
		while(!syncAchieved.get()) {
			Thread.yield();
		}
		syncAchieved.set(false);
		
		// once all federates are joined, advance to start time
		
		advance();

		initialized.set(true);
	}

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public boolean isInitialized() {
		return initialized.get();
	}
	
	/**
	 * Join federation.
	 *
	 * @throws CouldNotCreateLogicalTimeFactory the could not create logical time factory
	 * @throws InconsistentFDD the inconsistent fdd
	 * @throws ErrorReadingFDD the error reading fdd
	 * @throws CouldNotOpenFDD the could not open fdd
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws MalformedURLException the malformed url exception
	 * @throws FederateNameAlreadyInUse the federate name already in use
	 * @throws FederationExecutionDoesNotExist the federation execution does not exist
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws CallNotAllowedFromWithinCallback the call not allowed from within callback
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws InTimeAdvancingState the in time advancing state
	 * @throws RequestForTimeConstrainedPending the request for time constrained pending
	 * @throws InvalidLookahead the invalid lookahead
	 * @throws RequestForTimeRegulationPending the request for time regulation pending
	 * @throws ObjectClassNotDefined 
	 * @throws AttributeNotDefined 
	 * @throws InvalidObjectClassHandle 
	 * @throws NameNotFound 
	 * @throws ObjectInstanceNotKnown 
	 * @throws ObjectClassNotPublished 
	 */
	private void joinFederation() 
			throws CouldNotCreateLogicalTimeFactory, InconsistentFDD, 
			ErrorReadingFDD, CouldNotOpenFDD, NotConnected, RTIinternalError, 
			MalformedURLException, FederateNameAlreadyInUse, 
			FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, 
			CallNotAllowedFromWithinCallback, FederateNotExecutionMember, 
			InTimeAdvancingState, RequestForTimeConstrainedPending, 
			InvalidLookahead, RequestForTimeRegulationPending, NameNotFound, 
			InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, 
			ObjectClassNotPublished, ObjectInstanceNotKnown {
		try {
			rtiAmbassador.createFederationExecution(simulator.getConnection().getFederationName(), 
					new URL[]{new File(simulator.getConnection().getFomPath()).toURI().toURL()},
					"HLAinteger64Time");
		} catch(FederationExecutionAlreadyExists ignored) { }
		
		try {
			rtiAmbassador.joinFederationExecution(simulator.getConnection().getFederateName(), 
					simulator.getConnection().getFederateType(), simulator.getConnection().getFederationName());
		} catch(FederateAlreadyExecutionMember ignored) { }
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#provideAttributeValueUpdate(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.AttributeHandleSet, byte[])
	 */
	public void provideAttributeValueUpdate(ObjectInstanceHandle theObject,
			AttributeHandleSet theAttributes, byte[] userSuppliedTag) {
		HLAinfrastructureSystem localSystem;
		synchronized(hlaObjects) {
			localSystem = hlaObjects.get(theObject);
		}
		if(localSystem != null) {
			try {
				localSystem.updateAttributes(theAttributes);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "An exception of type " 
						+ ex.getMessage() 
						+ " occurred while providing attribute updates."
						+ " See stack trace for more information.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#reflectAttributeValues(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.AttributeHandleValueMap, byte[], hla.rti1516e.OrderType, hla.rti1516e.TransportationTypeHandle, hla.rti1516e.LogicalTime, hla.rti1516e.OrderType, hla.rti1516e.MessageRetractionHandle, hla.rti1516e.FederateAmbassador.SupplementalReflectInfo)
	 */
	public void reflectAttributeValues(ObjectInstanceHandle theObject,
			AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag,
			OrderType sentOrdering,
			TransportationTypeHandle theTransport,
			LogicalTime theTime,
			OrderType receivedOrdering,
			MessageRetractionHandle retractionHandle,
			SupplementalReflectInfo reflectInfo) {
		try {
			synchronized(hlaObjects) {
				if(hlaObjects.containsKey(theObject)) {
					if(hlaObjects.get(theObject) instanceof HLAagricultureSystem) {
						HLAagricultureSystem system = (HLAagricultureSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							simulator.getCountry().getSociety(system.getSocietyName()).setAgricultureSystem(
									(AgricultureSystem.Remote) system.getAgricultureSystem());
							// TODO fire system update event to reset attribute change listeners
							simulator.fireUpdateEvent();
						}
					} else if(hlaObjects.get(theObject) instanceof HLAwaterSystem) {
						HLAwaterSystem system = (HLAwaterSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							simulator.getCountry().getSociety(system.getSocietyName()).setWaterSystem(
									(WaterSystem.Remote) system.getWaterSystem());
							// TODO fire system update event to reset attribute change listeners
							simulator.fireUpdateEvent();
						}
					} else if(hlaObjects.get(theObject) instanceof HLAenergySystem) {
						HLAenergySystem system = (HLAenergySystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							simulator.getCountry().getSociety(system.getSocietyName()).setEnergySystem(
									(EnergySystem.Remote) system.getEnergySystem());
							// TODO fire system update event to reset attribute change listeners
							simulator.fireUpdateEvent();
						}
					} else if(hlaObjects.get(theObject) instanceof HLAsocialSystem) {
						HLAsocialSystem system = (HLAsocialSystem) hlaObjects.get(theObject);
						System.out.println("Receiving social system attribute updates...");
						for(AttributeHandle ah : theAttributes.keySet()) {
							HLAinteger64BE data = encoderFactory.createHLAinteger64BE();
							data.decode(theAttributes.get(ah));
							System.out.println(
									rtiAmbassador.getAttributeName(system.getObjectClassHandle(), ah) 
									+ ": " + theAttributes.get(ah) + " (" 
									+ data.getValue() + ")");
						}
						system.setAllAttributes(theAttributes);
						if(system.getSocialSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							simulator.getCountry().getSociety(system.getSocietyName()).setSocialSystem(
									(SocialSystem.Remote) system.getSocialSystem());
							// TODO fire system update event to reset attribute change listeners
							simulator.fireUpdateEvent();
						}
					}
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "An exception of type " + ex.getMessage() 
					+ " occurred while decoding an attribute update. See stack trace for more information.", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#reflectAttributeValues(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.AttributeHandleValueMap, byte[], hla.rti1516e.OrderType, hla.rti1516e.TransportationTypeHandle, hla.rti1516e.LogicalTime, hla.rti1516e.OrderType, hla.rti1516e.FederateAmbassador.SupplementalReflectInfo)
	 */
	public void reflectAttributeValues(ObjectInstanceHandle theObject,
			AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag,
			OrderType sentOrdering,
			TransportationTypeHandle theTransport,
			LogicalTime theTime,
			OrderType receivedOrdering,
			SupplementalReflectInfo reflectInfo) {
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, theTime, receivedOrdering, null, reflectInfo);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#reflectAttributeValues(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.AttributeHandleValueMap, byte[], hla.rti1516e.OrderType, hla.rti1516e.TransportationTypeHandle, hla.rti1516e.FederateAmbassador.SupplementalReflectInfo)
	 */
	public void reflectAttributeValues(ObjectInstanceHandle theObject,
			AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag,
			OrderType sentOrdering,
			TransportationTypeHandle theTransport,
			SupplementalReflectInfo reflectInfo) {
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, null, null, reflectInfo);
	}

	/**
	 * Resign federation.
	 *
	 * @throws InvalidResignAction the invalid resign action
	 * @throws OwnershipAcquisitionPending the ownership acquisition pending
	 * @throws FederateOwnsAttributes the federate owns attributes
	 * @throws CallNotAllowedFromWithinCallback the call not allowed from within callback
	 * @throws RTIinternalError the rT iinternal error
	 * @throws NotConnected 
	 * @throws FederateNotExecutionMember 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws  
	 */
	private void resignFederation() 
			throws InvalidResignAction, OwnershipAcquisitionPending, 
			FederateOwnsAttributes, CallNotAllowedFromWithinCallback, 
			RTIinternalError, SaveInProgress, RestoreInProgress {
		try {
			rtiAmbassador.disableTimeConstrained();
		} catch (FederateNotExecutionMember ignored) {
		} catch (TimeConstrainedIsNotEnabled ignored) {
		} catch(NotConnected ignored) {
		}
		timeConstrained.set(false);
		
		try {
			rtiAmbassador.disableTimeRegulation();
		} catch (FederateNotExecutionMember ignored) {
		} catch (TimeRegulationIsNotEnabled ignored) {
		} catch(NotConnected ignored) {
		}
		timeRegulating.set(false);
		
		synchronized(hlaObjects) {
			for(HLAinfrastructureSystem system : hlaObjects.values()) {
				if(system.isLocal()) {
					// remove hla object as attribute change listener
					((InfrastructureSystem.Local)system.getInfrastructureSystem())
					.removeAttributeChangeListener(system);
				}
			}
		}
		
		try {
			rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
		} catch (FederateNotExecutionMember ignored) {
		} catch (NotConnected ignored) { }
		
		try {
			rtiAmbassador.destroyFederationExecution(simulator.getConnection().getFederationName());
		} catch (FederatesCurrentlyJoined ignored) {
		} catch (FederationExecutionDoesNotExist ignored) {
		} catch (NotConnected ignored) {
		}
		
		synchronized(hlaObjects) {
			hlaObjects.clear();
		}
		
		initialized.set(false);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeAdvanceGrant(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeAdvanceGrant(LogicalTime theTime)
			throws FederateInternalError {
		timeAdvanceGranted.set(true);
		logicalTime = (HLAinteger64Time) theTime;
		System.out.println("(TAG) Logical time is " + logicalTime.getValue());
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeConstrainedEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeConstrainedEnabled(LogicalTime time)
			throws FederateInternalError {
		timeConstrained.set(true);
		logicalTime = (HLAinteger64Time) time;
		System.out.println("(TCE) Logical time is " + logicalTime.getValue());
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeRegulationEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeRegulationEnabled(LogicalTime time)
			throws FederateInternalError {
		timeRegulating.set(true);
		logicalTime = (HLAinteger64Time) time;
		System.out.println("(TRE) Logical time is " + logicalTime.getValue());
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#synchronizationPointRegistrationSucceeded(java.lang.String)
	 */
	@Override
	public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
			throws FederateInternalError {
		syncRegistered.set(true);
		System.out.println(synchronizationPointLabel + " registration succeeded");
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#synchronizationPointRegistrationFailed(java.lang.String, hla.rti1516e.SynchronizationPointFailureReason)
	 */
	@Override
	public void synchronizationPointRegistrationFailed(String synchronizationPointLabel,
			SynchronizationPointFailureReason reason)
					throws FederateInternalError {
		syncRegistered.set(false);
		System.out.println(synchronizationPointLabel + " registration failed: " + reason);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#announceSynchronizationPoint(java.lang.String, byte[])
	 */
	@Override
	public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
			throws FederateInternalError {
		syncAnnounced.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationSynchronized(java.lang.String, hla.rti1516e.FederateHandleSet)
	 */
	@Override
	public void federationSynchronized(String synchronizationPointLabel, FederateHandleSet failedToSyncSet)
			throws FederateInternalError {
		syncAchieved.set(true);
	}
}
