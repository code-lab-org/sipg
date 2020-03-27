package edu.mit.sips.sim.hla;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.sim.Simulator;
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
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyEnabled;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIexception;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.TimeConstrainedAlreadyEnabled;
import hla.rti1516e.exceptions.TimeConstrainedIsNotEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationIsNotEnabled;
import hla.rti1516e.time.HLAinteger64Interval;
import hla.rti1516e.time.HLAinteger64Time;
import hla.rti1516e.time.HLAinteger64TimeFactory;

/**
 * The Class SimAmbassador.
 */
public class HlaFederateAmbassador extends NullFederateAmbassador {
	private static Logger logger = Logger.getLogger(HlaFederateAmbassador.class);
	
	private final int unitsPerYear;
	private final int numberIterations; // must be a factor of unitsPerYear
	
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
	
	private final Map<ObjectInstanceHandle, HlaInfrastructureSystem> hlaObjects = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, HlaInfrastructureSystem>());

	private final Map<InfrastructureSystem, HlaInfrastructureSystem> localObjects = 
			Collections.synchronizedMap(new HashMap<InfrastructureSystem, HlaInfrastructureSystem>());
	
	private final Map<ObjectInstanceHandle, AttributeHandleSet> attributeUpdateRequests = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, AttributeHandleSet>());
	
	/**
	 * Instantiates a new sim ambassador.
	 * @throws RTIinternalError 
	 */
	public HlaFederateAmbassador(Simulator simulator, int unitsPerYear, int numberIterations) throws RTIexception {
		this.simulator = simulator;
		this.unitsPerYear = unitsPerYear;
		this.numberIterations = numberIterations;
		
		logger.trace("Making the RTI factory.");
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();

		logger.trace("Getting the RTI ambassador and encoder factory.");
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	/**
	 * Advance.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public void advance() throws RTIexception {
		logger.trace("Advancing to the next timestep.");
		
		synchronized(attributeUpdateRequests) {
			for(ObjectInstanceHandle theObject : attributeUpdateRequests.keySet()) {
				HlaObject object = hlaObjects.get(theObject);
				if(object != null) {
					object.provideAttributes(attributeUpdateRequests.get(theObject));
				}
			}
			attributeUpdateRequests.clear();
		}
		
		for(int i = 0; i < numberIterations; i++) {
			logger.trace("Performing iteration number " + (i+1) 
					+ " of " + numberIterations + ".");
			simulator.runAutoOptimization();
			synchronized(localObjects) {
				for(InfrastructureSystem system : localObjects.keySet()) {
					localObjects.get(system).setAttributes(system);
					localObjects.get(system).updateAllAttributes();
				}
			}

			if(!connected.get()) {
				logger.warn("Not connected - continuing in offline mode.");
				return;
			}

			logger.debug("Requesting time advance to " 
					+ logicalTime.add(lookaheadInterval));
			rtiAmbassador.timeAdvanceRequest(
					logicalTime.add(lookaheadInterval));
			
			logger.debug("Waiting for time advance grant.");
			while(!timeAdvanceGranted.get()) {
				Thread.yield();
			}
			timeAdvanceGranted.set(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#announceSynchronizationPoint(java.lang.String, byte[])
	 */
	@Override
	public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
			throws FederateInternalError {
		logger.info("Synchronization point announced (label = " 
				+ synchronizationPointLabel + ").");
		syncAnnounced.set(true);
	}

	/**
	 * Connect.
	 *
	 * @throws RTIexception the RTI exception
	 * @throws MalformedURLException the malformed url exception
	 */
	public void connect() throws RTIexception, MalformedURLException {
		logger.info("Connecting to the RTI.");
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE);
		} catch(AlreadyConnected ignored) { 
			logger.warn("Already connected.");
		}
		simulator.getConnection().setConnected(true);
		logger.info("Connected to the RTI.");
		
		joinFederation();
		
		connected.set(true);
		initialized.set(false);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#connectionLost(java.lang.String)
	 */
	@Override
	public void connectionLost(String faultDescription) throws FederateInternalError  {
		logger.warn("RTI connection has been lost (fault = " 
				+ faultDescription + ").");
		
		connected.set(false);
		simulator.getConnection().setConnected(false);
	}
	
	/**
	 * Disconnect.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public void disconnect() throws RTIexception {
		logger.info("Disconnecting from the RTI.");

		resignFederation();
		
		rtiAmbassador.disconnect();
		logger.trace("Disconnected from the RTI.");
		connected.set(false);
		simulator.getConnection().setConnected(false);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#discoverObjectInstance(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.ObjectClassHandle, java.lang.String)
	 */
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass, String objectName) {
		logger.trace("Redirecting to common callback method.");
		discoverObjectInstance(theObject, theObjectClass, objectName, null);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#discoverObjectInstance(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.ObjectClassHandle, java.lang.String)
	 */
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass, String objectName,
			FederateHandle producingFederate) {
		logger.info("Discovering object instance " + objectName 
				+ " with name " + objectName + ".");
		
		if(hlaObjects.containsKey(theObject)) {
			logger.warn("Already discovered object instance, skipping.");
			return;
		}
		
		logger.trace("Searching for correct subclass.");
		HlaInfrastructureSystem remoteSystem = null;
		try {
			if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HlaAgricultureSystem.CLASS_NAME))) {
				logger.trace("Creating a remote agriculture system.");
				remoteSystem = HlaAgricultureSystem.createRemoteAgricultureSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HlaWaterSystem.CLASS_NAME))) {
				logger.trace("Creating a remote water system.");
				remoteSystem = HlaWaterSystem.createRemoteWaterSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HlaElectricitySystem.CLASS_NAME))) {
				logger.trace("Creating a remote electricity system.");
				remoteSystem = HlaElectricitySystem.createRemoteElectricitySystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HlaPetroleumSystem.CLASS_NAME))) {
				logger.trace("Creating a remote petroleum system.");
				remoteSystem = HlaPetroleumSystem.createRemotePetroleumSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HlaSocialSystem.CLASS_NAME))) {
				logger.trace("Creating a remote social system.");
				remoteSystem = HlaSocialSystem.createRemoteSocialSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else {
				logger.warn("Unknown object class, skipping.");
				return;
			}
			hlaObjects.put(theObject, remoteSystem);
		} catch (RTIexception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationNotSaved(hla.rti1516e.SaveFailureReason)
	 */
	@Override
	public void federationNotSaved(SaveFailureReason reason)
			throws FederateInternalError {
		logger.warn("Federation was not saved (reason = " 
			+ reason + ").");
		saveCompleted.set(false);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationRestoreBegun()
	 */
	@Override
	public void federationRestoreBegun() throws FederateInternalError {
		logger.info("Federation restore has begun.");
		restorationBegun.set(true);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationRestored()
	 */
	@Override
	public void federationRestored() throws FederateInternalError {
		logger.info("Federation has been restored.");
		restorationCompleted.set(true);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationSaved()
	 */
	@Override
	public void federationSaved() throws FederateInternalError {
		logger.info("Federation has been saved.");
		saveCompleted.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#federationSynchronized(java.lang.String, hla.rti1516e.FederateHandleSet)
	 */
	@Override
	public void federationSynchronized(String synchronizationPointLabel, 
			FederateHandleSet failedToSyncSet)
					throws FederateInternalError {
		logger.info("Federation is synchronized (label = " 
				+ synchronizationPointLabel + ").");
		syncAchieved.set(true);
	}
	
	/**
	 * Initialize.
	 *
	 * @param startTime the start time
	 * @throws RTIexception the rT iexception
	 */
	public void initialize(long startTime) throws RTIexception {
		logger.info("Initializing federate to time " + startTime);
		
		HLAinteger64TimeFactory timeFactory = 
				(HLAinteger64TimeFactory) rtiAmbassador.getTimeFactory();
		logicalTime = timeFactory.makeInitial();
		lookaheadInterval = timeFactory.makeInterval(
				unitsPerYear/numberIterations);

		logger.trace("Enabling asynchronous delivery.");
		try {
			rtiAmbassador.enableAsynchronousDelivery();
			logger.info("Asynchronous delivery enabled.");
		} catch (AsynchronousDeliveryAlreadyEnabled ignored) {
			logger.warn("Asynchronous delivery already enabled.");
		}

		logger.trace("Enabling time constrained behavior.");
		try {
			rtiAmbassador.enableTimeConstrained();
		} catch (TimeConstrainedAlreadyEnabled ignored) { 
			logger.warn("Time constrained behavior already enabled.");
		}
		logger.info("Waiting for time constrained callback...");
		while(!timeConstrained.get()) {
			Thread.yield();
		}
		logger.trace("Time constrained behavior enabled.");

		logger.trace("Enabling time regulation with lookahead " 
				+ lookaheadInterval.getValue());
		try {
			rtiAmbassador.enableTimeRegulation(lookaheadInterval);
		} catch (TimeRegulationAlreadyEnabled ignored) { 
			logger.warn("Time regulation already enabled.");
		}
		logger.info("Waiting for time regulation callback...");
		while(!timeRegulating.get()) {
			Thread.yield();
		}
		logger.trace("Time regulation enabled.");

		logger.trace("Announcing synchronization point (label = initialized).");
		rtiAmbassador.registerFederationSynchronizationPoint("initialized", new byte[0]);

		logger.trace("Waiting for sync. point registration callback...");
		while(!syncRegistered.get()) {
			Thread.yield();
		}
		
		boolean firstFederate = false;
		if(syncRegisterSuccess.get()) {
			logger.info("Sync. point was registered: " 
					+ "this is the first joining federate.");
			firstFederate = true;
		} else {
			logger.info("Sync. point was not registered: " 
					+ "this is NOT the first joining federate.");
		}
		syncRegistered.set(false);
		syncRegisterSuccess.set(false);
		
		HLAinteger64Time initialTime = timeFactory.makeTime(
				(startTime-2)*unitsPerYear);
		logger.info("Requesting time advance to " 
				+ initialTime.getValue() + ".");
		rtiAmbassador.timeAdvanceRequest(initialTime);
		logger.trace("Waiting for time request grant...");
		while(!timeAdvanceGranted.get()) {
			Thread.yield();
		}
		timeAdvanceGranted.set(false);
		
		logger.trace("Waiting for synchronization point announcement...");
		while(!syncAnnounced.get()) {
			Thread.yield();
		}
		syncAnnounced.set(false);
		
		if(simulator.getScenario().getCountry().getAgricultureSystem() 
				instanceof AgricultureSystem.Local) {
			logger.trace("Country contains a local agriculture " + 
					"system: publish its attributes.");
			HlaAgricultureSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
					logger.trace("Creating HLA agriculture system for local " +
							"system " + city.getAgricultureSystem() + ".");
					AgricultureSystem.Local localSystem = 
							(AgricultureSystem.Local) city.getAgricultureSystem();
					HlaAgricultureSystem hlaObject = 
							HlaAgricultureSystem.createLocalAgricultureSystem(
									rtiAmbassador, encoderFactory, localSystem);
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
				}
			}
		}
		logger.trace("Subscribing to agriculture system attributes.");
		HlaAgricultureSystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getScenario().getCountry().getWaterSystem() 
				instanceof WaterSystem.Local) {
			logger.trace("Country contains a local water " + 
					"system: publish its attributes.");
			HlaWaterSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getWaterSystem() instanceof WaterSystem.Local) {
					logger.trace("Creating HLA water system for local " +
							"system " + city.getWaterSystem() + ".");
					WaterSystem.Local localSystem = 
							(WaterSystem.Local) city.getWaterSystem();
					HlaWaterSystem hlaObject = 
							HlaWaterSystem.createLocalWaterSystem(
									rtiAmbassador, encoderFactory, localSystem);
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
				}
			}
		}
		logger.trace("Subscribing to water system attributes.");
		HlaWaterSystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getScenario().getCountry().getElectricitySystem() 
				instanceof ElectricitySystem.Local) {
			logger.trace("Country contains a local electricity " + 
					"system: publish its attributes.");
			HlaElectricitySystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getElectricitySystem() instanceof ElectricitySystem.Local) {
					logger.trace("Creating HLA electricity system for local " +
							"system " + city.getElectricitySystem() + ".");
					ElectricitySystem.Local localSystem = 
							(ElectricitySystem.Local) city.getElectricitySystem();
					HlaElectricitySystem hlaObject = 
							HlaElectricitySystem.createLocalElectricitySystem(
									rtiAmbassador, encoderFactory, localSystem);
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
				}
			}
		}
		logger.trace("Subscribing to electricity system attributes.");
		HlaElectricitySystem.subscribeAll(rtiAmbassador);
		
		if(simulator.getScenario().getCountry().getPetroleumSystem() 
				instanceof PetroleumSystem.Local) {
			logger.trace("Country contains a local petroleum " + 
					"system: publish its attributes.");
			HlaPetroleumSystem.publishAll(rtiAmbassador);
			for(City city : simulator.getScenario().getCountry().getCities()) {
				if(city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
					logger.trace("Creating HLA petroleum system for local " +
							"system " + city.getElectricitySystem() + ".");
					PetroleumSystem.Local localSystem = 
							(PetroleumSystem.Local) city.getPetroleumSystem();
					HlaPetroleumSystem hlaObject = 
							HlaPetroleumSystem.createLocalPetroleumSystem(
									rtiAmbassador, encoderFactory, localSystem);
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
				}
			}
		}
		logger.trace("Subscribing to petroleum system attributes.");
		HlaPetroleumSystem.subscribeAll(rtiAmbassador);

		logger.trace("Publishing social system attributes.");
		HlaSocialSystem.publishAll(rtiAmbassador);
		for(City city : simulator.getScenario().getCountry().getCities()) {
			if(city.getSocialSystem() instanceof SocialSystem.Local) {
				SocialSystem.Local localSystem = 
						(SocialSystem.Local) city.getSocialSystem();
				HlaSocialSystem hlaObject = HlaSocialSystem.
						createLocalSocialSystem(rtiAmbassador, encoderFactory, 
								localSystem);
				hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
				localObjects.put(localSystem, hlaObject);
			}
		}
		logger.trace("Subscribing to social system attributes.");
		HlaSocialSystem.subscribeAll(rtiAmbassador);

		logger.trace("Achieving synchronization point (label = initialized).");
		rtiAmbassador.synchronizationPointAchieved("initialized");
		logger.trace("Waiting for federation synchronized callback...");
		while(!syncAchieved.get()) {
			Thread.yield();
		}
		syncAchieved.set(false);
		
		// once all federates are joined, save initial state
		
		if(firstFederate) {
			logger.debug("Requesting federation save.");
			rtiAmbassador.requestFederationSave("initialState");
		}

		logger.debug("Waiting for federation save to be initiated...");
		while(!saveInitiated.get()) {
			Thread.yield();
		}
		saveInitiated.set(false);

		logger.debug("Announcing federate save begun.");
		rtiAmbassador.federateSaveBegun();

		logger.debug("Announcing federate save complete.");
		rtiAmbassador.federateSaveComplete();

		logger.debug("Waiting for federation save complete...");
		while(!saveCompleted.get()) {
			Thread.yield();
		}
		
		advance(); // advance to start time - 1
		
		initialized.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateRestore(java.lang.String, java.lang.String, hla.rti1516e.FederateHandle)
	 */
	@Override
	public void initiateFederateRestore(String label, String federateName, FederateHandle federateHandle)
			throws FederateInternalError {
		logger.info("Initiate federate restore (label = " 
				+ label + ").");
		restorationInitiated.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateSave(java.lang.String)
	 */
	@Override
	public void initiateFederateSave(String label)
			throws FederateInternalError {
		logger.trace("Redirecting to common callback method.");
		initiateFederateSave(label, null);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#initiateFederateSave(java.lang.String, hla.rti1516e.LogicalTime)
	 */
	@Override
	public void initiateFederateSave(String label, LogicalTime time)
			throws FederateInternalError  {
		logger.info("Initiate federate save (label = " 
				+ label + ").");
		saveInitiated.set(true);
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
	 * @throws RTIexception the rT iexception
	 * @throws MalformedURLException 
	 */
	private void joinFederation() throws RTIexception, MalformedURLException {
		HlaConnection connection = (HlaConnection) simulator.getConnection();
		
		try {
			rtiAmbassador.createFederationExecution(connection.getFederationName(), 
					new URL[]{new File(connection.getFomPath()).toURI().toURL()},
					"HLAinteger64Time");
		} catch(FederationExecutionAlreadyExists ignored) { }
		
		try {
			rtiAmbassador.joinFederationExecution(connection.getFederateName(), 
					connection.getFederateType(), connection.getFederationName());
		} catch(FederateAlreadyExecutionMember ignored) { }
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#provideAttributeValueUpdate(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.AttributeHandleSet, byte[])
	 */
	public void provideAttributeValueUpdate(ObjectInstanceHandle theObject,
			AttributeHandleSet theAttributes, byte[] userSuppliedTag) {
		
		System.out.println("Adding attribute update request for " 
				+ theObject);
		if(attributeUpdateRequests.containsKey(theObject)) {
			attributeUpdateRequests.get(theObject).addAll(theAttributes);
		} else {
			attributeUpdateRequests.put(theObject,  theAttributes);
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
		
		if(hlaObjects.containsKey(theObject)) {
			try {
				HlaInfrastructureSystem system = hlaObjects.get(theObject);
				system.setAllAttributes(theAttributes);
			
				if(theAttributes.containsKey(rtiAmbassador.getAttributeHandle(
						rtiAmbassador.getObjectClassHandle(HlaInfrastructureSystem.CLASS_NAME), 
						HlaInfrastructureSystem.SOCIETY_NAME_ATTRIBUTE))) {
					for(Society society : simulator.getScenario().getCountry().getSocieties()) {
						if(society.getName().equals(system.getSocietyName())) {
							if(!(society.getAgricultureSystem() instanceof AgricultureSystem.Local)
									&& system instanceof AgricultureSystem) {
								society.setAgricultureSystem((AgricultureSystem)system);
							} else if(!(society.getWaterSystem() instanceof WaterSystem.Local)
									&& system instanceof WaterSystem) {
								society.setWaterSystem((WaterSystem)system);
							} else if(!(society.getPetroleumSystem() instanceof PetroleumSystem.Local)
									&& system instanceof PetroleumSystem) {
								society.setPetroleumSystem((PetroleumSystem)system);
							} else if(!(society.getElectricitySystem() instanceof ElectricitySystem.Local)
									&& system instanceof ElectricitySystem) {
								society.setElectricitySystem((ElectricitySystem)system);
							} else if(!(society.getSocialSystem() instanceof SocialSystem.Local)
									&& system instanceof SocialSystem) {
								society.setSocialSystem((SocialSystem)system);
							} else {
								logger.warn("Unknown infrastructure class, skipping.");
							}
							break;
						}
					}
				}
			} catch (RTIexception | DecoderException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
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
		logger.trace("Redirecting to common callback method.");
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag, 
				sentOrdering, theTransport, theTime, receivedOrdering,
				null, reflectInfo);
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
		logger.trace("Redirecting to common callback method.");
		reflectAttributeValues(theObject, theAttributes, userSuppliedTag, 
				sentOrdering, theTransport, null, null, reflectInfo);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#requestFederationRestoreFailed(java.lang.String)
	 */
	@Override
	public void requestFederationRestoreFailed(String label)
			throws FederateInternalError {
		logger.warn("Request for federation restore (label = " 
				+ label + ") failed.");
		restorationConfirmed.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#requestFederationRestoreSucceeded(java.lang.String)
	 */
	@Override
	public void requestFederationRestoreSucceeded(String label)
			throws FederateInternalError {
		logger.info("Request for federation restore (label = " 
			+ label + ") was successful.");
		restorationConfirmed.set(true);
	}

	/**
	 * Resign federation.
	 *
	 * @throws RTIexception the rT iexception
	 */
	private void resignFederation() throws RTIexception {
		logger.info("Terminating the federation execution.");
		try {
			rtiAmbassador.disableTimeConstrained();
			logger.trace("Time constrained behavior disabled.");
		} catch (FederateNotExecutionMember ignored) {
		} catch (TimeConstrainedIsNotEnabled ignored) {
		} catch(NotConnected ignored) {
		}
		timeConstrained.set(false);
		
		try {
			rtiAmbassador.disableTimeRegulation();
			logger.trace("Time regulation behavior disabled.");
		} catch (FederateNotExecutionMember ignored) {
		} catch (TimeRegulationIsNotEnabled ignored) {
		} catch(NotConnected ignored) {
		}
		timeRegulating.set(false);
		
		try {
			rtiAmbassador.resignFederationExecution(
					ResignAction.DELETE_OBJECTS_THEN_DIVEST);
			logger.trace("Resigned from the federation execution.");
		} catch (FederateNotExecutionMember ignored) {
		} catch (NotConnected ignored) { }
		
		try {
			rtiAmbassador.destroyFederationExecution(
					simulator.getConnection().getFederationName());
			logger.trace("Destroyed the federation execution.");
		} catch (FederatesCurrentlyJoined ignored) {
		} catch (FederationExecutionDoesNotExist ignored) {
		} catch (NotConnected ignored) {
		}
		
		logger.trace("Clearing local and remote HLA objects.");
		hlaObjects.clear();
		localObjects.clear();
		
		initialized.set(false);
	}

	/**
	 * Restore initial conditions.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public void restoreInitialConditions() throws RTIexception {

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

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#synchronizationPointRegistrationFailed(java.lang.String, hla.rti1516e.SynchronizationPointFailureReason)
	 */
	@Override
	public void synchronizationPointRegistrationFailed(String synchronizationPointLabel,
			SynchronizationPointFailureReason reason)
					throws FederateInternalError {
		logger.info("Synchronization point (label = " 
				+ synchronizationPointLabel 
				+ ") registration failed (reason = " + reason + ").");
		syncRegistered.set(true);
		syncRegisterSuccess.set(false);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#synchronizationPointRegistrationSucceeded(java.lang.String)
	 */
	@Override
	public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
			throws FederateInternalError {
		logger.info("Synchronization point (label = " 
				+ synchronizationPointLabel 
				+ ") registration successful.");
		syncRegistered.set(true);
		syncRegisterSuccess.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeAdvanceGrant(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeAdvanceGrant(LogicalTime theTime)
			throws FederateInternalError {
		if(theTime instanceof HLAinteger64Time) {
			logicalTime = (HLAinteger64Time) theTime;
		} else {
			logger.fatal("Incompatible time type: expected " 
					+ HLAinteger64Time.class + " but received " 
					+ theTime.getClass() + ".");
		}
		logger.info("Time advance granted (time = " + logicalTime.getValue() + ").");
		timeAdvanceGranted.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeConstrainedEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeConstrainedEnabled(LogicalTime time)
			throws FederateInternalError {
		if(time instanceof HLAinteger64Time) {
			logicalTime = (HLAinteger64Time) time;
		} else {
			logger.fatal("Incompatible time type: expected " 
					+ HLAinteger64Time.class + " but received " 
					+ time.getClass() + ".");
		}
		logger.info("Time constrained is enabled (time = " 
				+ logicalTime.getValue() + ").");
		timeConstrained.set(true);
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeRegulationEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeRegulationEnabled(LogicalTime time)
			throws FederateInternalError {
		if(time instanceof HLAinteger64Time) {
			logicalTime = (HLAinteger64Time) time;
		} else {
			logger.fatal("Incompatible time type: expected " 
					+ HLAinteger64Time.class + " but received " 
					+ time.getClass() + ".");
		}
		logger.info("Time regulation is enabled (time = " 
				+ logicalTime.getValue() + ").");
		timeRegulating.set(true);
	}
}
