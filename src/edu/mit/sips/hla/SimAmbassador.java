package edu.mit.sips.hla;

import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateHandle;
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
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

public class SimAmbassador extends NullFederateAmbassador {
	
	private final Country country;
	private final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	
	private final Map<ObjectInstanceHandle, HLAinfrastructureSystem> hlaObjects = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, HLAinfrastructureSystem>());
	
	/**
	 * Instantiates a new sim ambassador.
	 * @throws RTIinternalError 
	 */
	public SimAmbassador(Country country) throws RTIinternalError {
		this.country = country;
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	public void connectAndJoin(FederationConnection connection) 
			throws ConnectionFailed, InvalidLocalSettingsDesignator, 
			UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, 
			RTIinternalError, CouldNotCreateLogicalTimeFactory, 
			FederateNameAlreadyInUse, InconsistentFDD, ErrorReadingFDD, 
			CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, 
			FederationExecutionDoesNotExist, MalformedURLException, FederateNotExecutionMember, 
			NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, 
			IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, 
			ObjectInstanceNotKnown, InterruptedException {
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE, 
					connection.getLocalSettingsDesignator());
		} catch(AlreadyConnected ignored) { }
		
		try {
			rtiAmbassador.createFederationExecution(connection.getFederationName(), 
					new File(connection.getFomPath()).toURI().toURL());
		} catch(FederationExecutionAlreadyExists ignored) { }
		
		try {
			rtiAmbassador.joinFederationExecution(connection.getFederateName(), 
					connection.getFederateType(), connection.getFederationName());
		} catch(FederateAlreadyExecutionMember ignored) { }

		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			// if country includes a local national agriculture system
			// publish its attributes
			HLAagricultureSystem.publishAll(rtiAmbassador);
			for(Society society : country.getSocieties()) {
				if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
					AgricultureSystem.Local localSystem = 
							(AgricultureSystem.Local) society.getAgricultureSystem();
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
		
		if(country.getWaterSystem() instanceof WaterSystem.Local) {
			// if country includes a local national water system
			// publish its attributes
			HLAwaterSystem.publishAll(rtiAmbassador);
			for(Society society : country.getSocieties()) {
				if(society.getWaterSystem() instanceof WaterSystem.Local) {
					WaterSystem.Local localSystem = 
							(WaterSystem.Local) society.getWaterSystem();
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
		
		if(country.getEnergySystem() instanceof EnergySystem.Local) {
			// if country includes a local national energy system
			// publish its attributes
			HLAenergySystem.publishAll(rtiAmbassador);
			for(Society society : country.getSocieties()) {
				if(society.getEnergySystem() instanceof EnergySystem.Local) {
					EnergySystem.Local localSystem = 
							(EnergySystem.Local) society.getEnergySystem();
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
		for(City city : country.getCities()) {
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
		
		connection.setConnected(true);
	}
	
	public void resignAndDisconnect(FederationConnection connection) throws InvalidResignAction, OwnershipAcquisitionPending, 
			FederateOwnsAttributes, CallNotAllowedFromWithinCallback, 
			RTIinternalError, FederateIsExecutionMember {
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
			rtiAmbassador.destroyFederationExecution(connection.getFederationName());
		} catch (FederatesCurrentlyJoined ignored) {
		} catch(FederationExecutionDoesNotExist ignored) {
		} catch(NotConnected ignored) {
		}
		
		rtiAmbassador.disconnect();
		
		synchronized(hlaObjects) {
			hlaObjects.clear();
		}
		
		connection.setConnected(false);
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

	/* (non-Javadoc)
	 * @see hla.rti1516e.NullFederateAmbassador#discoverObjectInstance(hla.rti1516e.ObjectInstanceHandle, hla.rti1516e.ObjectClassHandle, java.lang.String)
	 */
	public void discoverObjectInstance(ObjectInstanceHandle theObject,
			ObjectClassHandle theObjectClass, String objectName) {
		discoverObjectInstance(theObject, theObjectClass, objectName, null);
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
							country.getSociety(system.getSocietyName()).setAgricultureSystem(
									(AgricultureSystem.Remote) system.getAgricultureSystem());
							// TODO fire system update event to reset attribute change listeners
						}
					} else if(hlaObjects.get(theObject) instanceof HLAwaterSystem) {
						HLAwaterSystem system = (HLAwaterSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							country.getSociety(system.getSocietyName()).setWaterSystem(
									(WaterSystem.Remote) system.getWaterSystem());
							// TODO fire system update event to reset attribute change listeners
						}
					} else if(hlaObjects.get(theObject) instanceof HLAenergySystem) {
						HLAenergySystem system = (HLAenergySystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getInfrastructureSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							country.getSociety(system.getSocietyName()).setEnergySystem(
									(EnergySystem.Remote) system.getEnergySystem());
							// TODO fire system update event to reset attribute change listeners
						}
					} else if(hlaObjects.get(theObject) instanceof HLAsocialSystem) {
						HLAsocialSystem system = (HLAsocialSystem) hlaObjects.get(theObject);
						system.setAllAttributes(theAttributes);
						if(system.getSocialSystem().getSociety() == null 
								&& !system.getSocietyName().isEmpty()) {
							country.getSociety(system.getSocietyName()).setSocialSystem(
									(SocialSystem.Remote) system.getSocialSystem());
							// TODO fire system update event to reset attribute change listeners
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
}
