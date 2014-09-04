package edu.mit.isos3.hla;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.SynchronizationPointFailureReason;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos2.SimEntity;

public class ISOSambassador extends NullFederateAmbassador {
	public static final String OHLA_RTI = "OHLA";
	public static final String PORTICO_RTI = "portico";

	protected static Logger logger = Logger.getLogger(ISOSambassador.class);
	protected final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private HLAfloat64TimeFactory timeFactory;
	private volatile HLAfloat64Time logicalTime;
	private HLAfloat64Interval lookaheadInterval;
	private volatile AtomicBoolean timeConstrained = new AtomicBoolean(false);
	private volatile AtomicBoolean timeRegulating = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncRegSuccess = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncRegFailure = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncAnnounce = new AtomicBoolean(false);
	private volatile AtomicBoolean initSyncComplete = new AtomicBoolean(false);
	private volatile AtomicBoolean timeAdvancing =  new AtomicBoolean(false);
	private final Map<ObjectInstanceHandle, HLAobject> objectInstanceHandleMap = 
			Collections.synchronizedMap(
					new HashMap<ObjectInstanceHandle, HLAobject>());
	private final Map<SimEntity, HLAobject> localObjects = 
			Collections.synchronizedMap(
					new HashMap<SimEntity, HLAobject>());
	
	public static void main(String[] args) throws RTIexception {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		final ISOSambassador ambA = new ISOSambassador();
		ambA.connect("ISOS Test", "isos.xml", "Test A", "Test");
		final ISOSambassador ambB = new ISOSambassador();
		ambB.connect("ISOS Test", "isos.xml", "Test B", "Test");
		
		new Thread(new Runnable() {
			public void run() {
				ambA.initialize(0.0, 0.25);
				ambA.disconnect("ISOS Test");
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				ambB.initialize(0.0, 0.25);
				ambB.disconnect("ISOS Test");
			}
		}).start();
	}

	public ISOSambassador() throws RTIexception {
		this(PORTICO_RTI);
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
	
	public void initialize(double initialTime, double lookahead) {
		logger.debug("Making the initial time and lookahead intervals.");
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
		logger.trace("Making the initial time and lookahead interval.");
		logicalTime = timeFactory.makeTime(initialTime);
		lookaheadInterval = timeFactory.makeInterval(lookahead);
		
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
	}
	
	public void advance() {
		
	}
	
	public void reset() {
		
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
}