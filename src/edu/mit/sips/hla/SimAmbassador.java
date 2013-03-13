package edu.mit.sips.hla;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

public class SimAmbassador extends NullFederateAmbassador {
	
	private final Country country;
	private final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	
	private final Map<ObjectInstanceHandle, HLAobject> objects = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, HLAobject>());
	private final List<ObjectInstanceHandle> deletedRemoteObjects = 
			new ArrayList<ObjectInstanceHandle>();
	
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
					synchronized(objects) {
						objects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
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
					synchronized(objects) {
						objects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
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
					synchronized(objects) {
						objects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
					}	
				}
			}
		}
		HLAenergySystem.subscribeAll(rtiAmbassador);

		// always publish social system attributes
		HLAsocialSystem.publishAll(rtiAmbassador);
		for(Society society : country.getSocieties()) {
			if(society.getSocialSystem() instanceof SocialSystem.Local) {
				SocialSystem.Local localSystem = 
						(SocialSystem.Local) society.getSocialSystem();
				HLAsocialSystem hlaObject = HLAsocialSystem.
						createLocalSocialSystem(rtiAmbassador, encoderFactory, 
								localSystem);
				synchronized(objects) {
					objects.put(hlaObject.getObjectInstanceHandle(), hlaObject);
				}	
			}
		}
		HLAsocialSystem.subscribeAll(rtiAmbassador);
		
		connection.setConnected(true);
	}
	
	public void resignAndDisconnect(FederationConnection connection) throws InvalidResignAction, OwnershipAcquisitionPending, 
			FederateOwnsAttributes, CallNotAllowedFromWithinCallback, 
			RTIinternalError, FederateIsExecutionMember {
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
		
		synchronized(objects) {
			for(HLAobject object : objects.values()) {
				if(!object.isLocal()) {
					// TODO fireElementActionEvent(ElementAction.REMOVE, new ElementActionEvent(this, element.getElement()));
				}
			}
			objects.clear();
		}
		deletedRemoteObjects.clear();
		
		connection.setConnected(false);
	}
}
