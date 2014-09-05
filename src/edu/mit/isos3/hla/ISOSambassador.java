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

import edu.mit.isos3.Scenario;
import edu.mit.isos3.SimEntity;
import edu.mit.isos3.element.ElectElement;
import edu.mit.isos3.element.LocalElectElement;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.element.LocalPetrolElement;
import edu.mit.isos3.element.LocalSocialElement;
import edu.mit.isos3.element.LocalWaterElement;
import edu.mit.isos3.element.PetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.element.WaterElement;

public class ISOSambassador extends NullFederateAmbassador {
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
	private final Map<ObjectInstanceHandle, ISOSelement> objectInstanceHandleMap = 
			Collections.synchronizedMap(
					new HashMap<ObjectInstanceHandle, ISOSelement>());
	private final Map<LocalElement, ISOSelement> localObjects = 
			Collections.synchronizedMap(
					new HashMap<LocalElement, ISOSelement>());

	public ISOSambassador() throws RTIexception {
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	public Collection<ISOSelement> getElements() {
		return objectInstanceHandleMap.values();
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
	
	public void initialize(Scenario scenario, int numIterations, long timeStep) {
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
		HLAfloat64Time initTime = timeFactory.makeTime(scenario.getInitialTime());
		
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
				rtiAmbassador.evokeMultipleCallbacks(0,1);
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
				rtiAmbassador.evokeMultipleCallbacks(0,1);
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
		for(LocalElement entity : scenario.getElements()) {
			if(!localObjects.containsKey(entity)) {
				try {
					logger.trace("Searching for the correct object subclass.");
					ISOSelement element = null;
					if(entity instanceof ElectElement) {
						logger.debug("Registering an elect element.");
						element = new ISOSelectElement(
								rtiAmbassador, encoderFactory, null);
					} else if(entity instanceof PetrolElement) {
						logger.debug("Registering a petrol element.");
						element = new ISOSpetrolElement(
								rtiAmbassador, encoderFactory, null);
					} else if(entity instanceof SocialElement) {
						logger.debug("Registering a social element.");
						element = new ISOSsocialElement(
								rtiAmbassador, encoderFactory, null);
					} else if(entity instanceof WaterElement) {
						logger.debug("Registering a water element.");
						element = new ISOSwaterElement(
								rtiAmbassador, encoderFactory, null);
					} else {
						logger.warn("Unknown HLA object type for class " 
								+ entity.getClass() + ", skipping");
					}
					logger.trace("Adding " + entity.getName() 
							+ " to local objects.");
					localObjects.put(entity, element);
					if(element != null) {
						element.setAttributes(entity);
						logger.trace("Adding " + entity.getName() 
								+ " to known instances.");
						objectInstanceHandleMap.put(
								element.getObjectInstanceHandle(), element);
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
				rtiAmbassador.evokeMultipleCallbacks(0,1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Synchronization point confirmed (" + (initSyncRegSuccess.get()?"Success":"Failure") + ").");
		logger.trace("Waiting for synchronization announce callback service.");
		while(!initSyncAnnounce.get()) {
			try {
				rtiAmbassador.evokeMultipleCallbacks(0,1);
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
				rtiAmbassador.evokeMultipleCallbacks(0,1);
			} catch (RTIexception e) {
				logger.error(e);
			}
		}
		logger.info("Synchronization point complete.");

		for(LocalElement entity : localObjects.keySet()) {
			logger.trace("Updating name and location attributes for " 
					+ entity.getName() + ".");
			if(localObjects.get(entity) != null) {
				try {
					localObjects.get(entity).updateStaticAttributes(rtiAmbassador);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		}

		logger.debug("Setting up object links.");
		for(LocalElement entity : localObjects.keySet()) {
			while(!setUpElement(entity)) {
				try {
					rtiAmbassador.evokeMultipleCallbacks(0,1);
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
					rtiAmbassador.evokeMultipleCallbacks(0,1);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
			timeAdvanceGranted.set(false);
		}
	}
	
	private boolean setUpElement(LocalElement element) {
		if(element instanceof LocalElectElement) {
			return setUpElect((LocalElectElement)element);
		}
		if(element instanceof LocalPetrolElement) {
			return setUpPetrol((LocalPetrolElement)element);
		}
		if(element instanceof LocalSocialElement) {
			return setUpSocial((LocalSocialElement)element);
		}
		if(element instanceof LocalWaterElement) {
			return setUpWater((LocalWaterElement)element);
		}
		return true; // nothing to set up
	}
	
	private boolean setUpElect(LocalElectElement elect) {
		PetrolElement petrol = null;
		SocialElement social = null;
		WaterElement water = null;
		for(ISOSelement element : getElements()) {
			if(element instanceof PetrolElement 
					&& elect.getLocation().equals(element.getLocation())) {
				petrol = (PetrolElement) element;
				elect.setPetrolSupplier(petrol);
				elect.setCustomer(petrol);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof SocialElement 
					&& elect.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				elect.setCustomer(social);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof WaterElement
					&& elect.getLocation().equals(element.getLocation())) {
				water = (WaterElement) element;
				elect.setCustomer(water);
				break;
			}
		}
		logger.warn(elect + " missing " + (petrol==null?"petrol":"") + " " + (social==null?"social":"") + " " + (water==null?"water":""));
		return petrol != null && social != null && water != null;
	}
	
	private boolean setUpPetrol(LocalPetrolElement petrol) {
		ElectElement elect = null;
		SocialElement social = null;
		for(ISOSelement element : getElements()) {
			if(element instanceof ElectElement 
					&& petrol.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				petrol.setCustomer(elect);
				petrol.setElectSupplier(elect);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof SocialElement 
					&& petrol.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				petrol.setCustomer(social);
				break;
			}
		}
		logger.warn(petrol + " missing " + (elect==null?"elect":"") + " " + (social==null?"social":""));
		return elect != null && social != null;
	}
	
	private boolean setUpSocial(LocalSocialElement social) {
		ElectElement elect = null;
		PetrolElement petrol = null;
		WaterElement water = null;
		for(ISOSelement element : getElements()) {
			if(element instanceof ElectElement 
					&& social.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				social.setElectSupplier(elect);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof PetrolElement 
					&& social.getLocation().equals(element.getLocation())) {
				petrol = (PetrolElement) element;
				social.setPetrolSupplier(petrol);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof WaterElement 
					&& social.getLocation().equals(element.getLocation())) {
				water = (WaterElement) element;
				social.setWaterSupplier(water);
				break;
			}
		}
		logger.warn(social + " missing " + (elect==null?"elect":"") + " " + (petrol==null?"petrol":"") + " " + (water==null?"water":""));
		return elect != null && petrol != null && water != null;
	}
	
	private boolean setUpWater(LocalWaterElement water) {
		ElectElement elect = null;
		SocialElement social = null;
		for(ISOSelement element : getElements()) {
			if(element instanceof ElectElement 
					&& water.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				water.setElectSupplier(elect);
				break;
			}
		}
		for(ISOSelement element : getElements()) {
			if(element instanceof SocialElement 
					&& water.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				water.setCustomer(social);
				break;
			}
		}
		logger.warn(water + " missing " + (elect==null?"elect":"") + " " + (social==null?"social":""));
		return elect != null && social != null;
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
						localObjects.get(entity).updatePeriodicAttributes(rtiAmbassador);
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
					rtiAmbassador.evokeMultipleCallbacks(0,1);
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
		ISOSelement element = null;
		try {
			if(theObjectClass.equals(rtiAmbassador.getObjectClassHandle(
					ISOSelectElement.CLASS_NAME))) {
				logger.debug("Creating an elect element.");
				element = new ISOSelectElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSsocialElement.CLASS_NAME))) {
				logger.debug("Creating a social element.");
				element = new ISOSsocialElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSpetrolElement.CLASS_NAME))) {
				logger.debug("Creating a petrol element.");
				element = new ISOSpetrolElement(
						rtiAmbassador, encoderFactory, objectName);
			} else if(theObjectClass.equals(
					rtiAmbassador.getObjectClassHandle(
							ISOSwaterElement.CLASS_NAME))) {
				logger.debug("Creating a water element.");
				element = new ISOSwaterElement(
						rtiAmbassador, encoderFactory, objectName);
			} else {
				logger.warn("Unknown object class " + theObjectClass + ", skipping.");
				return;
			}
			
			logger.trace("Adding object to known instances.");
			objectInstanceHandleMap.put(theObject, element);
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
				ISOSelement element = objectInstanceHandleMap.get(theObject);
				element.setAllAttributes(theAttributes);
			} 
		} catch (DecoderException e) {
			logger.error(e);
		}
	}
}