package edu.mit.isos3.hla;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
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
import hla.rti1516e.exceptions.TimeConstrainedAlreadyEnabled;
import hla.rti1516e.exceptions.TimeConstrainedIsNotEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationIsNotEnabled;
import hla.rti1516e.time.HLAfloat64Interval;
import hla.rti1516e.time.HLAfloat64Time;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import edu.mit.isos3.SimEntity;
import edu.mit.isos3.element.ElectElement;
import edu.mit.isos3.element.PetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.element.WaterElement;

public class ISOSambassador extends NullFederateAmbassador {
	public static final String OHLA_RTI = "OHLA";
	public static final String PORTICO_RTI = "portico";

	protected static Logger logger = Logger.getLogger(ISOSambassador.class);
	protected final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private HLAfloat64TimeFactory timeFactory;
	private volatile HLAfloat64Time logicalTime;
	private HLAfloat64Interval lookaheadInterval;
	private int numIterations;
	private long timeStep;
	private volatile AtomicBoolean timeConstrained = new AtomicBoolean(false);
	private volatile AtomicBoolean timeRegulating = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncRegSuccess = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncRegFailure = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncAnnounce = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncComplete = new AtomicBoolean(false);
	private volatile AtomicBoolean timeAdvanceGranted =  new AtomicBoolean(false);
	private final Map<ObjectInstanceHandle, HLAobject> objectInstanceHandleMap = 
			Collections.synchronizedMap(
					new HashMap<ObjectInstanceHandle, HLAobject>());
	private final Map<SimEntity, HLAobject> localObjects = 
			Collections.synchronizedMap(
					new HashMap<SimEntity, HLAobject>());

	public ISOSambassador() throws RTIexception {
		this(PORTICO_RTI);
	}
	
	public Collection<HLAobject> getObjects() {
		return objectInstanceHandleMap.values();
	}
	
	public ISOSambassador(String rtiName) throws RTIexception {
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory(rtiName);
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	public void connect(String federationName, String fomPath, 
			String federateName, String federateType) {
		logger.debug("Connecting to the RTI.");
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_EVOKED);
			logger.info("Connected to the RTI.");
		} catch(AlreadyConnected ignored) {
		} catch (RTIexception e) {
			logger.error(e);
		}
		
		logger.debug("Creating the federation execution.");
		try {
			rtiAmbassador.createFederationExecution(federationName, 
					new URL[]{new File(fomPath).toURI().toURL()},
					"HLAfloat64Time");
			logger.info("Federation execution " 
					+ federationName + " created.");
		} catch(FederationExecutionAlreadyExists ignored) {
			logger.trace("Federation execution already exists.");
		} catch (RTIexception | MalformedURLException e) {
			logger.error(e);
		}

		logger.debug("Joining the federation execution.");
		try {
			rtiAmbassador.joinFederationExecution(federateName, 
					federateType, federationName);
			logger.info("Joined federation execution " 
					+ federationName + " as federate " 
					+ federateName + " of type " 
					+ federateType + ".");
		} catch(FederateAlreadyExecutionMember ignored) { 
			logger.trace("Already joined to the federation execution.");
		} catch (RTIexception e) {
			logger.error(e);
		}
	}
	
	public void initialize(long initialTime, int numIterations, long timeStep, 
			Collection<? extends SimEntity> entities) {
		this.numIterations = numIterations;
		this.timeStep = timeStep;
		
		logger.debug("Creating the time factory and making values.");
		try {
			logger.trace("Getting the time factory.");
			if(rtiAmbassador.getTimeFactory() instanceof HLAfloat64TimeFactory) {
				timeFactory = (HLAfloat64TimeFactory) rtiAmbassador.getTimeFactory();
			} else {
				String message = "Time factory is not compatible. Expected " 
						+ HLAfloat64TimeFactory.class + " but received " 
						+ rtiAmbassador.getTimeFactory().getClass() + ".";
				logger.fatal(message);
			}
		} catch (RTIexception e) {
			logger.error(e);
		}
		logger.trace("Making the lookahead interval.");
		lookaheadInterval = timeFactory.makeInterval(timeStep/((double)numIterations));
		logger.trace("Making the initial time.");
		HLAfloat64Time initTime = timeFactory.makeTime(initialTime);
		
		logger.debug("Enabling asynchronous message delivery.");
		try {
			rtiAmbassador.enableAsynchronousDelivery();
			logger.info("Asynchronous message delivery enabled.");
		} catch (AsynchronousDeliveryAlreadyEnabled ignored) {
		} catch (RTIexception e) {
			logger.error(e);
		}
		
		logger.debug("Enabling time constrained behavior.");
		try {
			rtiAmbassador.enableTimeConstrained();
		} catch (TimeConstrainedAlreadyEnabled ignored) { 
		} catch (RTIexception e) {
			logger.error(e);
		}
		logger.trace("Waiting for time constrained callback service.");
		while(!timeConstrained.get()) {
			try {
				rtiAmbassador.evokeCallback(0.1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Time constrained behavior enabled.");

		logger.debug("Enabling time regulating behavior.");
		try {
			rtiAmbassador.enableTimeRegulation(lookaheadInterval);
		} catch (TimeRegulationAlreadyEnabled ignored) { 
		} catch (RTIexception e) {
			logger.error(e);
		}
		logger.trace("Waiting for time regulating callback service.");
		while(!timeRegulating.get()) {
			try {
				rtiAmbassador.evokeCallback(0.1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Time regulating behavior enabled.");
		
		logger.debug("Publishing and subscribing all objects and interactions.");
		try {
			ISOSelectElement.publishAll(rtiAmbassador);
			ISOSsocialElement.publishAll(rtiAmbassador);
			ISOSpetrolElement.publishAll(rtiAmbassador);
			ISOSwaterElement.publishAll(rtiAmbassador);
			
			ISOSelectElement.subscribeAll(rtiAmbassador);
			ISOSsocialElement.subscribeAll(rtiAmbassador);
			ISOSpetrolElement.subscribeAll(rtiAmbassador);
			ISOSwaterElement.subscribeAll(rtiAmbassador);
			logger.info("Published and subscribed all objects and interactions.");
		} catch (RTIexception e) {
			logger.error(e);
		}

		logger.debug("Registering object instantiations.");
		for(SimEntity entity : entities) {
			entity.initialize((long)initialTime);
			if(!localObjects.containsKey(entity)) {
				try {
					logger.trace("Searching for the correct object subclass.");
					HLAobject hlaObject = null;
					if(entity instanceof ElectElement) {
						logger.debug("Registering an elect element.");
						hlaObject = new ISOSelectElement(
								rtiAmbassador, encoderFactory, null);
						hlaObject.setAttributes(entity);
					} else if(entity instanceof PetrolElement) {
						logger.debug("Registering a petrol element.");
						hlaObject = new ISOSpetrolElement(
								rtiAmbassador, encoderFactory, null);
						hlaObject.setAttributes(entity);
					} else if(entity instanceof SocialElement) {
						logger.debug("Registering a social element.");
						hlaObject = new ISOSsocialElement(
								rtiAmbassador, encoderFactory, null);
						hlaObject.setAttributes(entity);
					} else if(entity instanceof WaterElement) {
						logger.debug("Registering a water element.");
						hlaObject = new ISOSwaterElement(
								rtiAmbassador, encoderFactory, null);
						hlaObject.setAttributes(entity);
					} else {
						logger.warn("Unknown HLA object type for class " 
								+ entity.getClass() + ", skipping");
					}
					logger.trace("Adding " + entity.getName() 
							+ " to local objects.");
					localObjects.put(entity, hlaObject);
					if(hlaObject != null) {
						logger.trace("Adding " + entity.getName() 
								+ " to known instances.");
						objectInstanceHandleMap.put(
								hlaObject.getObjectInstanceHandle(), hlaObject);
					}
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		}
		
		logger.debug("Registering `init' synchronization point.");
		try {
			rtiAmbassador.registerFederationSynchronizationPoint("init", new byte[0]);
		} catch (RTIexception e) {
			logger.error(e);
		}
		logger.trace("Waiting for synchronization registration confirmation callback service.");
		while(!initSyncRegSuccess.get() && !initSyncRegFailure.get()) {
			try {
				rtiAmbassador.evokeCallback(0.1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Synchronization point confirmed (" + (initSyncRegSuccess.get()?"Success":"Failure") + ").");
		logger.trace("Waiting for synchronization announce callback service.");
		while(!initSyncAnnounce.get()) {
			try {
				rtiAmbassador.evokeCallback(0.1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Synchronization point announced.");

		logger.debug("Achieving `init' synchronization point.");
		try {
			rtiAmbassador.synchronizationPointAchieved("init");
		} catch (RTIexception e) {
			logger.error(e);
		}
		logger.info("Synchronization point achieved.");
		logger.trace("Waiting for synchronization complete callback service.");
		while(!initSyncComplete.get()) {
			try {
				rtiAmbassador.evokeCallback(0.1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Synchronization point complete.");
		
		logger.info("Updating attributes for all local objects.");
		for(HLAobject object : localObjects.values()) {
			if(object != null) {
				try {
					object.updateAllAttributes(rtiAmbassador);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		}
		
		if(initTime.compareTo(logicalTime) > 0) {
			logger.debug("Requesting time advance to initial time " + initTime);
			try {
				rtiAmbassador.timeAdvanceRequest(initTime);
			} catch (RTIexception e) {
				logger.error(e);
			}
			
			logger.debug("Waiting for time advance grant.");
			while(!timeAdvanceGranted.get()) {
				try {
					rtiAmbassador.evokeCallback(0.1);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
			timeAdvanceGranted.set(false);
		}
	}
	
	public void advance() {
		for(int i = 0; i < numIterations; i++) {
			for(SimEntity entity : localObjects.keySet()) {
				entity.iterateTick(timeStep);
			}
			for(SimEntity entity : localObjects.keySet()) {
				entity.iterateTock();
				if(localObjects.get(entity) != null) {
					localObjects.get(entity).setAttributes(entity);
					try {
						localObjects.get(entity).updateAllAttributes(rtiAmbassador);
					} catch (RTIexception e) {
						logger.error(e);
					}
				}
			}
			try {
				HLAfloat64Time nextTime = logicalTime.add(lookaheadInterval);
				logger.debug("Requesting time advance to initial time " + nextTime);
				rtiAmbassador.timeAdvanceRequest(nextTime);
			} catch (RTIexception e) {
				logger.error(e);
			}
			
			logger.debug("Waiting for time advance grant.");
			while(!timeAdvanceGranted.get()) {
				try {
					rtiAmbassador.evokeCallback(0.1);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
			timeAdvanceGranted.set(false);
		}
	}
	
	public void disconnect(String federationName) {
		logger.debug("Disabling time constrained behavior.");
		try {
			rtiAmbassador.disableTimeConstrained();
		} catch (FederateNotExecutionMember ignored) {
			logger.trace("Federate is not an execution member.");
		} catch (TimeConstrainedIsNotEnabled ignored) {
			logger.trace("Time constrained is not enabled.");
		} catch(NotConnected ignored) {
			logger.trace("Federate is not connected.");
		} catch (RTIexception e) {
			logger.error(e);
		}
		timeConstrained.set(false);

		logger.debug("Disabling time regulation.");
		try {
			rtiAmbassador.disableTimeRegulation();
		} catch (FederateNotExecutionMember ignored) {
			logger.trace("Federate is not an execution member.");
		} catch (TimeRegulationIsNotEnabled ignored) {
			logger.trace("Time regulation is not enabled.");
		} catch(NotConnected ignored) {
			logger.trace("Federate is not connected.");
		} catch (RTIexception e) {
			logger.error(e);
		}
		timeRegulating.set(false);

		logger.debug("Resigning from the federation execution.");
		try {
			rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
		} catch (FederateNotExecutionMember ignored) {
			logger.trace("Federate is not an execution member.");
		} catch (NotConnected ignored) { 
			logger.trace("Federate is not connected.");
		} catch (RTIexception e) {
			logger.error(e);
		}

		logger.debug("Destroying the federation execution.");
		try {
			rtiAmbassador.destroyFederationExecution(federationName);
		} catch (FederatesCurrentlyJoined ignored) {
			logger.trace("Other federates are currently joined.");
		} catch (FederationExecutionDoesNotExist ignored) {
			logger.trace("Federation execution does not exist.");
		} catch (NotConnected ignored) {
			logger.trace("Federate is not connected.");
		} catch (RTIexception e) {
			logger.error(e);
		}		
	}
	
	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeConstrainedEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeConstrainedEnabled(LogicalTime time)
			throws FederateInternalError {
		if(time instanceof HLAfloat64Time) {
			logicalTime = (HLAfloat64Time) time;
		} else {
			String message = "Incompatible time. Expected " 
					+ HLAfloat64Time.class + " but received " 
					+ time.getClass() + ".";
			logger.fatal(message);
		}
		logger.info("Time constrained enabled with logical time " 
				+ logicalTime.getValue() + ".");
		timeConstrained.set(true);
	}

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#timeRegulationEnabled(hla.rti1516e.LogicalTime)
	 */
	@Override
	public void timeRegulationEnabled(LogicalTime time)
			throws FederateInternalError {
		if(time instanceof HLAfloat64Time) {
			logicalTime = (HLAfloat64Time) time;
		} else {
			String message = "Incompatible time. Expected " 
					+ HLAfloat64Time.class + " but received " 
					+ time.getClass() + ".";
			logger.fatal(message);
		}
		logger.info("Time regulation enabled with logical time " 
				+ logicalTime.getValue() + ".");
		timeRegulating.set(true);
	}

	@Override
	public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
			throws FederateInternalError {
		if(synchronizationPointLabel.equals("init")) {
			initSyncRegSuccess.set(true);
		}
	}

	@Override
	public void synchronizationPointRegistrationFailed(String synchronizationPointLabel,
			SynchronizationPointFailureReason reason)
					throws FederateInternalError {
		if(synchronizationPointLabel.equals("init")) {
			initSyncRegFailure.set(true);
		}
	}

	@Override
	public void announceSynchronizationPoint(String synchronizationPointLabel, 
			byte[] userSuppliedTag) throws FederateInternalError {
		if(synchronizationPointLabel.equals("init")) {
			initSyncAnnounce.set(true);
		}
	}

	@Override
	public void federationSynchronized(String synchronizationPointLabel, 
			FederateHandleSet failedToSyncSet) throws FederateInternalError {
		if(synchronizationPointLabel.equals("init")) {
			initSyncComplete.set(true);
		}
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass, String objectName) {
		logger.info("Discovering object instance " + theObject + ".");

		if(objectInstanceHandleMap.containsKey(theObject)) {
			logger.warn("Already discovered object instance " 
					+ theObject + ", skipping.");
			return;
		}

		logger.trace("Searching for the correct object subclass.");
		HLAobject hlaObject = null;
		try {
			if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					ISOSelectElement.CLASS_NAME))) {
				logger.debug("Creating an elect element.");
				hlaObject = new ISOSelectElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSsocialElement.CLASS_NAME))) {
				logger.debug("Creating a social element.");
				hlaObject = new ISOSsocialElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSpetrolElement.CLASS_NAME))) {
				logger.debug("Creating a petrol element.");
				hlaObject = new ISOSpetrolElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSwaterElement.CLASS_NAME))) {
				logger.debug("Creating a water element.");
				hlaObject = new ISOSwaterElement(
						rtiAmbassador, encoderFactory, objectName);
			} else {
				logger.warn("Unknown object class " + theObjectClass + ", skipping.");
				return;
			}
			
			logger.trace("Adding object to known instances.");
			objectInstanceHandleMap.put(theObject, hlaObject);
		} catch (RTIexception e) {
			logger.error(e);
		}
	}
	
	@Override
	public void timeAdvanceGrant(LogicalTime theTime)
			throws FederateInternalError {
		if(theTime instanceof HLAfloat64Time) {
			logicalTime = (HLAfloat64Time) theTime;
		} else {
			String message = "Incompatible time. Expected " 
					+ HLAfloat64Time.class + " but received " 
					+ theTime.getClass() + ".";
			logger.fatal(message);
		}
		logger.info("Time advance granted to logical time " 
				+ logicalTime.getValue());
		timeAdvanceGranted.set(true);
	}
	
	@Override
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
				sentOrdering, theTransport, reflectInfo);
	}
	
	@Override
	public void reflectAttributeValues(ObjectInstanceHandle theObject,
			AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag,
			OrderType sentOrdering,
			TransportationTypeHandle theTransport,
			SupplementalReflectInfo reflectInfo) {
		logger.debug("Reflect attributes for object " + theObject);
		try {
			if(objectInstanceHandleMap.containsKey(theObject)) {
				logger.trace("Reflecting attributes for known object " 
						+ objectInstanceHandleMap.get(theObject));
				HLAobject object = objectInstanceHandleMap.get(theObject);
				object.setAllAttributes(theAttributes);
			} 
		} catch (DecoderException e) {
			logger.error(e);
		}
	}
}