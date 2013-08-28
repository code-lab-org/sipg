package edu.mit.sips.hla;

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
import hla.rti1516e.SaveFailureReason;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.EncoderFactory;
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
import hla.rti1516e.exceptions.FederateHasNotBegunSave;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateUnableToUseTime;
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
import hla.rti1516e.exceptions.RestoreNotRequested;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.SaveNotInitiated;
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
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.sim.Simulator;

public class SimAmbassador extends NullFederateAmbassador {
	private final int unitsPerYear = 1000;
	private final int numberIterations = 4; // must be a factor of unitsPerYear
	
	private final Simulator simulator;
	private final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private HLAinteger64Time logicalTime;
	private HLAinteger64Interval lookaheadInterval;
	private volatile AtomicBoolean connected = new AtomicBoolean(false);
	private volatile AtomicBoolean timeConstrained = new AtomicBoolean(false);
	private volatile AtomicBoolean timeRegulating = new AtomicBoolean(false);
	private volatile AtomicBoolean timeAdvanceGranted =  new AtomicBoolean(false);
	private volatile AtomicBoolean syncRegistered = new AtomicBoolean(false);
	private volatile AtomicBoolean syncRegisterSuccess = new AtomicBoolean(false);
	private volatile AtomicBoolean syncAnnounced = new AtomicBoolean(false);
	private volatile AtomicBoolean syncAchieved = new AtomicBoolean(false);
	private volatile AtomicBoolean saveInitiated = new AtomicBoolean(false);
	private volatile AtomicBoolean saveCompleted = new AtomicBoolean(false);
	private volatile AtomicBoolean restorationConfirmed = new AtomicBoolean(false);
	private volatile AtomicBoolean restorationBegun = new AtomicBoolean(false);
	private volatile AtomicBoolean restorationInitiated = new AtomicBoolean(false);
	private volatile AtomicBoolean restorationCompleted = new AtomicBoolean(false);
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
		for(int i = 0; i < numberIterations; i++) {
			for(City city : simulator.getScenario().getCountry().getCities()) {
				// fire attribute change events to manually update hla data objects
				if(city.getSocialSystem() instanceof SocialSystem.Local) {
					city.getSocialSystem().fireAttributeChangeEvent(
							Arrays.asList(SocialSystem.POPULATION_ATTRIBUTE, 
									SocialSystem.DOMESTIC_PRODUCT_ATTRIBUTE, 
									SocialSystem.DOMESTIC_PRODUCTION_ATTRIBUTE,
									SocialSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE,
									SocialSystem.WATER_CONSUMPTION_ATTRIBUTE,
									SocialSystem.FOOD_CONSUMPTION_ATTRIBUTE,
									SocialSystem.CASH_FLOW_ATTRIBUTE));
				}
				if(city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
					city.getAgricultureSystem().fireAttributeChangeEvent(
							Arrays.asList(AgricultureSystem.DOMESTIC_PRODUCTION_ATTRIBUTE,
									AgricultureSystem.CASH_FLOW_ATTRIBUTE,
									AgricultureSystem.WATER_CONSUMPTION_ATTRIBUTE));
				}
				if(city.getWaterSystem() instanceof WaterSystem.Local) {
					city.getWaterSystem().fireAttributeChangeEvent(
							Arrays.asList(WaterSystem.DOMESTIC_PRODUCTION_ATTRIBUTE,
								WaterSystem.CASH_FLOW_ATTRIBUTE,
								WaterSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE));
				}
				if(city.getElectricitySystem() instanceof ElectricitySystem.Local) {
					city.getElectricitySystem().fireAttributeChangeEvent(
							Arrays.asList(ElectricitySystem.DOMESTIC_PRODUCTION_ATTRIBUTE,
									ElectricitySystem.CASH_FLOW_ATTRIBUTE,
									ElectricitySystem.PETROLEUM_CONSUMPTION_ATTRIBUTE,
									ElectricitySystem.WATER_CONSUMPTION_ATTRIBUTE));
				}
				if(city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
					city.getPetroleumSystem().fireAttributeChangeEvent(
							Arrays.asList(PetroleumSystem.DOMESTIC_PRODUCTION_ATTRIBUTE,
									PetroleumSystem.CASH_FLOW_ATTRIBUTE,
									PetroleumSystem.ELECTRICITY_CONSUMPTION_ATTRIBUTE));
				}
			}
			synchronized(hlaObjects) {
				for(HLAinfrastructureSystem system : hlaObjects.values()) {
					if(system.isLocal()) {
						// initiate attribute update service
						system.updateAllAttributes();
					}
				}
			}

			if(!connected.get()) {
				return;
			}

			System.out.println("Requesting time advance to " 
					+ logicalTime.add(lookaheadInterval));
			rtiAmbassador.timeAdvanceRequest(logicalTime.add(lookaheadInterval));
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
		}
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
		
		connected.set(true);
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
		
		connected.set(false);
		
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
		
		System.out.println("Discovering object instance " + objectName + ".");
		
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
					HLAelectricitySystem.CLASS_NAME))) {
				HLAelectricitySystem remoteSystem = 
						HLAelectricitySystem.createRemoteElectricitySystem(
								rtiAmbassador, encoderFactory, objectName);
				synchronized(hlaObjects) {
					hlaObjects.put(theObject, remoteSystem);
				}
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLApetroleumSystem.CLASS_NAME))) {
				HLApetroleumSystem remoteSystem = 
						HLApetroleumSystem.createRemotePetroleumSystem(
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
	 * @throws FederateUnableToUseTime 
	 * @throws SaveNotInitiated 
	 * @throws FederateHasNotBegunSave 
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
			SynchronizationPointLabelNotAnnounced, FederateUnableToUseTime, SaveNotInitiated, FederateHasNotBegunSave {
		HLAinteger64TimeFactory timeFactory = 
				(HLAinteger64TimeFactory) rtiAmbassador.getTimeFactory();
		logicalTime = timeFactory.makeInitial();
		lookaheadInterval = timeFactory.makeInterval(unitsPerYear/numberIterations);

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
		
		// announce synchronization point
		rtiAmbassador.registerFederationSynchronizationPoint("initialized", new byte[0]);
		
		while(!syncRegistered.get()) {
			Thread.yield();
		}
		boolean firstFederate = syncRegisterSuccess.get();
		syncRegistered.set(false);
		syncRegisterSuccess.set(false);
		
		if(firstFederate) {
			System.out.println("Registered sync. point: requesting ta to " 
					+ timeFactory.makeTime((startTime-2)*unitsPerYear));
			rtiAmbassador.timeAdvanceRequest(
					timeFactory.makeTime((startTime-2)*unitsPerYear));
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
		} else {
			/* use for non-time regulating federates
			TimeQueryReturn query = rtiAmbassador.queryGALT();
			System.out.println("GALT is " + ((HLAinteger64Time)query.time).getValue());
			rtiAmbassador.timeAdvanceRequest(query.time);
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
			*/
			rtiAmbassador.timeAdvanceRequest(
					timeFactory.makeTime((startTime-2)*unitsPerYear));
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
		}
		
		while(!syncAnnounced.get()) {
			Thread.yield();
		}
		syncAnnounced.set(false);
		
		if(simulator.getScenario().getCountry().getAgricultureSystem() instanceof AgricultureSystem.Local) {
			// if country includes a local national agriculture system
			// publish its attributes
			HLAagricultureSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
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
		
		if(simulator.getScenario().getCountry().getWaterSystem() instanceof WaterSystem.Local) {
			// if country includes a local national water system
			// publish its attributes
			HLAwaterSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
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
		
		if(simulator.getScenario().getCountry().getElectricitySystem() instanceof ElectricitySystem.Local) {
			// if country includes a local national electricity system
			// publish its attributes
			HLAelectricitySystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getElectricitySystem() instanceof ElectricitySystem.Local) {
					ElectricitySystem.Local localSystem = 
							(ElectricitySystem.Local) city.getElectricitySystem();
					HLAelectricitySystem hlaObject = HLAelectricitySystem.
							createLocalElectricitySystem(rtiAmbassador, encoderFactory, 
									localSystem);
					synchronized(hlaObjects) {
						hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLAelectricitySystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getScenario().getCountry().getPetroleumSystem() instanceof PetroleumSystem.Local) {
			// if country includes a local national petroleum system
			// publish its attributes
			HLApetroleumSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
					PetroleumSystem.Local localSystem = 
							(PetroleumSystem.Local) city.getPetroleumSystem();
					HLApetroleumSystem hlaObject = HLApetroleumSystem.
							createLocalPetroleumSystem(rtiAmbassador, encoderFactory, 
									localSystem);
					synchronized(hlaObjects) {
						hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLApetroleumSystem.subscribeAll(rtiAmbassador);

		// always publish city social system attributes
		HLAsocialSystem.publishAll(rtiAmbassador);
		for(City city : simulator.getScenario().getCountry().getCities()) {
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
		
		rtiAmbassador.synchronizationPointAchieved("initialized");
		while(!syncAchieved.get()) {
			Thread.yield();
		}
		syncAchieved.set(false);
		
		// once all federates are joined, advance to start time - 1
		
		advance(); // advance to start time - 1
		
		if(firstFederate) {
			rtiAmbassador.requestFederationSave("initialState");
		}
		
		while(!saveInitiated.get()) {
			Thread.yield();
		}
		saveInitiated.set(false);

		rtiAmbassador.federateSaveBegun();
		
		rtiAmbassador.federateSaveComplete();
		
		while(!saveCompleted.get()) {
			Thread.yield();
		}
		saveCompleted.set(false);
		
		advance(); // advance to start time

		initialized.set(true);
	}
	
	/**
	 * Restore initial conditions.
	 *
	 * @throws SaveInProgress the save in progress
	 * @throws RestoreInProgress the restore in progress
	 * @throws FederateNotExecutionMember the federate not execution member
	 * @throws NotConnected the not connected
	 * @throws RTIinternalError the rT iinternal error
	 * @throws RestoreNotRequested the restore not requested
	 * @throws SynchronizationPointLabelNotAnnounced the synchronization point label not announced
	 * @throws LogicalTimeAlreadyPassed the logical time already passed
	 * @throws InvalidLogicalTime the invalid logical time
	 * @throws InTimeAdvancingState the in time advancing state
	 * @throws RequestForTimeRegulationPending the request for time regulation pending
	 * @throws RequestForTimeConstrainedPending the request for time constrained pending
	 * @throws IllegalTimeArithmetic the illegal time arithmetic
	 * @throws AttributeNotOwned the attribute not owned
	 * @throws AttributeNotDefined the attribute not defined
	 * @throws ObjectInstanceNotKnown the object instance not known
	 * @throws InvalidLogicalTimeInterval the invalid logical time interval
	 * @throws TimeRegulationIsNotEnabled the time regulation is not enabled
	 */
	public void restoreInitialConditions() 
			throws SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
			NotConnected, RTIinternalError, RestoreNotRequested, 
			SynchronizationPointLabelNotAnnounced, LogicalTimeAlreadyPassed, 
			InvalidLogicalTime, InTimeAdvancingState, RequestForTimeRegulationPending, 
			RequestForTimeConstrainedPending, IllegalTimeArithmetic, AttributeNotOwned, 
			AttributeNotDefined, ObjectInstanceNotKnown, InvalidLogicalTimeInterval, 
			TimeRegulationIsNotEnabled {

		rtiAmbassador.registerFederationSynchronizationPoint("reset", new byte[0]);
		
		while(!syncRegistered.get()) {
			Thread.yield();
		}
		syncRegistered.set(false);
		
		while(!syncAnnounced.get()) {
			Thread.yield();
		}
		syncAnnounced.set(false);

		rtiAmbassador.synchronizationPointAchieved("reset");
		while(!syncAchieved.get()) {
			Thread.yield();
		}
		syncAchieved.set(false);
		
		if(syncRegisterSuccess.get()) {
			// if this federate registered the sync point, it also
			// requests the federation restore
			try {
				rtiAmbassador.requestFederationRestore("initialState");
				
				while(!restorationConfirmed.get()) {
					Thread.yield();
				}
				restorationConfirmed.set(false);
			} catch(RestoreInProgress ignored) {}
		}
		syncRegisterSuccess.set(false);
		
		while(!restorationBegun.get()) {
			Thread.yield();
		}
		restorationBegun.set(false);

		while(!restorationInitiated.get()) {
			Thread.yield();
		}
		restorationInitiated.set(false);
		
		rtiAmbassador.federateRestoreComplete();
		
		while(!restorationCompleted.get()) {
			Thread.yield();
		}
		restorationCompleted.set(false);
		
		logicalTime = (HLAinteger64Time) rtiAmbassador.queryLogicalTime(); // should be start time - 1
		
		advance(); // advance to start time
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
		
		System.out.println("Providing attribute updates for " 
				+ hlaObjects.get(theObject));
		
		HLAinfrastructureSystem localSystem;
		synchronized(hlaObjects) {
			localSystem = hlaObjects.get(theObject);
		}
		if(localSystem != null) {
			try {
				localSystem.provideAttributes(theAttributes);
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
		// long time = (long) Math.ceil(((HLAinteger64Time)theTime).getValue()/(double)unitsPerYear) - 1;
		
		System.out.println("Reflecting attributes with timestamp " 
				+ theTime + " for " + hlaObjects.get(theObject));
		
		try {
			synchronized(hlaObjects) {
				if(hlaObjects.containsKey(theObject)) {
					if(hlaObjects.get(theObject) instanceof HLAagricultureSystem) {
						HLAagricultureSystem system = (HLAagricultureSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null
								&& !system.getSocietyName().isEmpty()) {
							system.setAgricultureSystem((AgricultureSystem.Remote)
									simulator.getScenario().getCountry().getSociety(
											system.getSocietyName()).getAgricultureSystem());
						}
						// simulator.fireUpdateEvent(time);
					} else if(hlaObjects.get(theObject) instanceof HLAwaterSystem) {
						HLAwaterSystem system = (HLAwaterSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null
								&& !system.getSocietyName().isEmpty()) {
							system.setWaterSystem((WaterSystem.Remote)
									simulator.getScenario().getCountry().getSociety(
											system.getSocietyName()).getWaterSystem());
						}
						// simulator.fireUpdateEvent(time);
					} else if(hlaObjects.get(theObject) instanceof HLAelectricitySystem) {
						HLAelectricitySystem system = (HLAelectricitySystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null
								&& !system.getSocietyName().isEmpty()) {
							system.setElectricitySystem((ElectricitySystem.Remote)
									simulator.getScenario().getCountry().getSociety(
											system.getSocietyName()).getElectricitySystem());
						}
						// simulator.fireUpdateEvent(time);
					} else if(hlaObjects.get(theObject) instanceof HLApetroleumSystem) {
						HLApetroleumSystem system = (HLApetroleumSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null
								&& !system.getSocietyName().isEmpty()) {
							system.setPetroleumSystem((PetroleumSystem.Remote)
									simulator.getScenario().getCountry().getSociety(
											system.getSocietyName()).getPetroleumSystem());
						}
						// simulator.fireUpdateEvent(time);
					} else if(hlaObjects.get(theObject) instanceof HLAsocialSystem) {
						HLAsocialSystem system = (HLAsocialSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null
								&& !system.getSocietyName().isEmpty()) {
							system.setSocialSystem((SocialSystem.Remote)
									simulator.getScenario().getCountry().getSociety(
											system.getSocietyName()).getSocialSystem());
						}
						// simulator.fireUpdateEvent(time);
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
		syncRegisterSuccess.set(true);
		System.out.println(synchronizationPointLabel + " registration succeeded");
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#synchronizationPointRegistrationFailed(java.lang.String, hla.rti1516e.SynchronizationPointFailureReason)
	 */
	@Override
	public void synchronizationPointRegistrationFailed(String synchronizationPointLabel,
			SynchronizationPointFailureReason reason)
					throws FederateInternalError {
		syncRegistered.set(true);
		syncRegisterSuccess.set(false);
		System.out.println(synchronizationPointLabel + " registration failed: " + reason);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#announceSynchronizationPoint(java.lang.String, byte[])
	 */
	@Override
	public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
			throws FederateInternalError {
		System.out.println("Synchronization Point Announced");
		syncAnnounced.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationSynchronized(java.lang.String, hla.rti1516e.FederateHandleSet)
	 */
	@Override
	public void federationSynchronized(String synchronizationPointLabel, FederateHandleSet failedToSyncSet)
			throws FederateInternalError {
		System.out.println("Federation Synchronized");
		syncAchieved.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateSave(java.lang.String)
	 */
	@Override
	public void initiateFederateSave(String label)
			throws FederateInternalError {
		initiateFederateSave(label, null);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateSave(java.lang.String, hla.rti1516e.LogicalTime)
	 */
	@Override
	public void initiateFederateSave(String label, LogicalTime time)
			throws FederateInternalError  {
		System.out.println("Initiate Federate Save");
		saveInitiated.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationSaved()
	 */
	@Override
	public void federationSaved() throws FederateInternalError {
		System.out.println("Federation Saved");
		saveCompleted.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationNotSaved(hla.rti1516e.SaveFailureReason)
	 */
	@Override
	public void federationNotSaved(SaveFailureReason reason)
			throws FederateInternalError {
		System.out.println("Federation Not Saved");
		saveCompleted.set(false);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#requestFederationRestoreSucceeded(java.lang.String)
	 */
	@Override
	public void requestFederationRestoreSucceeded(String label)
			throws FederateInternalError {
		System.out.println("Request Federation Restore Succeeded");
		restorationConfirmed.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#requestFederationRestoreFailed(java.lang.String)
	 */
	@Override
	public void requestFederationRestoreFailed(String label)
			throws FederateInternalError {
		System.out.println("Request Federation Restore Failed");
		restorationConfirmed.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationRestoreBegun()
	 */
	@Override
	public void federationRestoreBegun() throws FederateInternalError {
		System.out.println("Federate Restore Begun");
		restorationBegun.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateRestore(java.lang.String, java.lang.String, hla.rti1516e.FederateHandle)
	 */
	@Override
	public void initiateFederateRestore(String label, String federateName, FederateHandle federateHandle)
			throws FederateInternalError {
		System.out.println("Federate Restore to " + label + " Initiated");
		restorationInitiated.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationRestored()
	 */
	@Override
	public void federationRestored() throws FederateInternalError {
		System.out.println("Federation Restored");
		restorationCompleted.set(true);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#connectionLost(java.lang.String)
	 */
	@Override
	public void connectionLost(String faultDescription) throws FederateInternalError  {
		connected.set(false);
		
		simulator.getConnection().setConnected(false);
	}
}
