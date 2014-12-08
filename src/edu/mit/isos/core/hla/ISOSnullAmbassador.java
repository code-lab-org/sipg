package edu.mit.isos.core.hla;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.SocialElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.app.elect.LocalElectElement;
import edu.mit.isos.app.petrol.LocalPetrolElement;
import edu.mit.isos.app.social.LocalSocialElement;
import edu.mit.isos.app.water.LocalWaterElement;
import edu.mit.isos.core.context.Scenario;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;
import edu.mit.isos.core.sim.SimEntity;

public class ISOSnullAmbassador implements ISOSambassador {
	protected static Logger logger = Logger.getLogger(ISOSnullAmbassador.class);
	private int numIterations;
	private long timeStep;
	private final Set<LocalElement> localObjects = new HashSet<LocalElement>();
	
	public Collection<Element> getElements() {
		return new HashSet<Element>(localObjects);
	}
	
	public void connect(String federationName, String fomPath, 
			String federateName, String federateType) {
	}
	
	public void initialize(Scenario scenario, int numIterations, long timeStep) {
		this.numIterations = numIterations;
		this.timeStep = timeStep;
		
		logger.debug("Registering object instantiations.");
		localObjects.addAll(scenario.getElements());

		logger.debug("Setting up object links.");
		for(LocalElement entity : localObjects) {
			setUpElement(entity);
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
		for(Element element : getElements()) {
			if(element instanceof PetrolElement 
					&& elect.getLocation().equals(element.getLocation())) {
				petrol = (PetrolElement) element;
				elect.setPetrolSupplier(petrol);
				elect.setCustomer(petrol);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof SocialElement 
					&& elect.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				elect.setCustomer(social);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof WaterElement
					&& elect.getLocation().equals(element.getLocation())) {
				water = (WaterElement) element;
				elect.setCustomer(water);
				break;
			}
		}
		if(petrol==null || social==null || water==null) {
			logger.warn(elect + " missing " + (petrol==null?"petrol":"") 
					+ " " + (social==null?"social":"") 
					+ " " + (water==null?"water":""));
		}

		return petrol != null && social != null && water != null;
	}
	
	private boolean setUpPetrol(LocalPetrolElement petrol) {
		ElectElement elect = null;
		SocialElement social = null;
		for(Element element : getElements()) {
			if(element instanceof ElectElement 
					&& petrol.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				petrol.setCustomer(elect);
				petrol.setElectSupplier(elect);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof SocialElement 
					&& petrol.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				petrol.setCustomer(social);
				break;
			}
		}
		if(elect==null || social==null) {
			logger.warn(petrol + " missing " + (elect==null?"elect":"") 
					+ " " + (social==null?"social":""));
		}

		return elect != null && social != null;
	}
	
	private boolean setUpSocial(LocalSocialElement social) {
		ElectElement elect = null;
		PetrolElement petrol = null;
		WaterElement water = null;
		for(Element element : getElements()) {
			if(element instanceof ElectElement 
					&& social.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				social.setElectSupplier(elect);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof PetrolElement 
					&& social.getLocation().equals(element.getLocation())) {
				petrol = (PetrolElement) element;
				social.setPetrolSupplier(petrol);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof WaterElement 
					&& social.getLocation().equals(element.getLocation())) {
				water = (WaterElement) element;
				social.setWaterSupplier(water);
				break;
			}
		}
		if(elect==null || petrol==null || water==null) {
			logger.warn(social + " missing " + (elect==null?"elect":"") 
					+ " " + (petrol==null?"petrol":"") 
					+ " " + (water==null?"water":""));
		}

		return elect != null && petrol != null && water != null;
	}
	
	private boolean setUpWater(LocalWaterElement water) {
		ElectElement elect = null;
		SocialElement social = null;
		for(Element element : getElements()) {
			if(element instanceof ElectElement 
					&& water.getLocation().equals(element.getLocation())) {
				elect = (ElectElement) element;
				water.setElectSupplier(elect);
				break;
			}
		}
		for(Element element : getElements()) {
			if(element instanceof SocialElement 
					&& water.getLocation().equals(element.getLocation())) {
				social = (SocialElement) element;
				water.setCustomer(social);
				break;
			}
		}
		if(elect==null || social==null) {
			logger.warn(water + " missing " + (elect==null?"elect":"") 
					+ " " + (social==null?"social":""));
		}

		return elect != null && social != null;
	}
	
	public void advance() {
		for(int i = 0; i < numIterations; i++) {
			for(SimEntity entity : localObjects) {
				entity.iterateTick(timeStep);
			}
			for(SimEntity entity : localObjects) {
				entity.iterateTock();
			}
		}
	}
	
	public void disconnect(String federationName) {
	}
}