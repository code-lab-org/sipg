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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

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

/**
 * The Class SimAmbassador.
 */
public class SimAmbassador extends NullFederateAmbassador {
	private static Logger logger = Logger.getLogger(SimAmbassador.class);
	
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

	private final Map<InfrastructureSystem, HLAinfrastructureSystem> localObjects = 
			Collections.synchronizedMap(new HashMap<InfrastructureSystem, HLAinfrastructureSystem>());
	
	/**
	 * Instantiates a new sim ambassador.
	 * @throws RTIinternalError 
	 */
	public SimAmbassador(Simulator simulator) throws RTIexception {
		this.simulator = simulator;
		
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
		
		for(int i = 0; i < numberIterations; i++) {
			logger.trace("Performing iteration number " + (i+1) 
					+ " of " + numberIterations + ".");
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
	
	/**
	 * Connect.
	 *
	 * @throws RTIexception the RTI exception
	 * @throws MalformedURLException the malformed url exception
	 */
	public void connect() throws RTIexception, MalformedURLException {
		logger.info("Connecting to the RTI with settings designator: " 
			+ simulator.getConnection().getLocalSettingsDesignator());
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE, 
					simulator.getConnection().getLocalSettingsDesignator());
		} catch(AlreadyConnected ignored) { 
			logger.warn("Already connected.");
		}
		simulator.getConnection().setConnected(true);
		logger.info("Connected to the RTI.");
		
		joinFederation();
		
		connected.set(true);
	}
	
	/**
	 * Disconnect.
	 *
	 * @throws RTIexception the rT iexception
	 */
	public void disconnect() throws RTIexception {
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
		
		logger.info("Discovering object instance " + objectName 
				+ " with name " + objectName + ".");
		
		if(hlaObjects.containsKey(theObject)) {
			logger.warn("Already discovered object instance, skipping.");
			return;
		}
		
		logger.trace("Searching for correct subclass.");
		HLAinfrastructureSystem remoteSystem = null;
		try {
			if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAagricultureSystem.CLASS_NAME))) {
				logger.trace("Creating a remote agriculture system.");
				remoteSystem = HLAagricultureSystem.createRemoteAgricultureSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAwaterSystem.CLASS_NAME))) {
				logger.trace("Creating a remote water system.");
				remoteSystem = HLAwaterSystem.createRemoteWaterSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAelectricitySystem.CLASS_NAME))) {
				logger.trace("Creating a remote electricity system.");
				remoteSystem = HLAelectricitySystem.createRemoteElectricitySystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLApetroleumSystem.CLASS_NAME))) {
				logger.trace("Creating a remote petroleum system.");
				remoteSystem = HLApetroleumSystem.createRemotePetroleumSystem(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					HLAsocialSystem.CLASS_NAME))) {
				logger.trace("Creating a remote social system.");
				remoteSystem = HLAsocialSystem.createRemoteSocialSystem(
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
	
	/**
	 * Initialize.
	 *
	 * @param startTime the start time
	 * @throws RTIexception the rT iexception
	 */
	public void initialize(long startTime) throws RTIexception {
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
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
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
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
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
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
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
					hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					localObjects.put(localSystem, hlaObject);
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
				hlaObjects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
				localObjects.put(localSystem, hlaObject);
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
		
		if(hlaObjects.containsKey(theObject)) {
			try {
				HLAinfrastructureSystem system = hlaObjects.get(theObject);
				system.setAllAttributes(theAttributes);
			
				if(theAttributes.containsKey(rtiAmbassador.getAttributeHandle(
						rtiAmbassador.getObjectClassHandle(HLAinfrastructureSystem.CLASS_NAME), 
						HLAinfrastructureSystem.SOCIETY_NAME_ATTRIBUTE))) {
					for(Society society : simulator.getScenario().getCountry().getSocieties()) {
						if(society.getName().equals(system.getSocietyName())) {
							if(system instanceof AgricultureSystem) {
								society.setAgricultureSystem((AgricultureSystem)system);
							} else if(system instanceof WaterSystem) {
								society.setWaterSystem((WaterSystem)system);
							} else if(system instanceof PetroleumSystem) {
								society.setPetroleumSystem((PetroleumSystem)system);
							} else if(system instanceof ElectricitySystem) {
								society.setElectricitySystem((ElectricitySystem)system);
							} else if(system instanceof SocialSystem) {
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
	 * @throws RTIexception the rT iexception
	 */
	private void resignFederation() throws RTIexception {
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
