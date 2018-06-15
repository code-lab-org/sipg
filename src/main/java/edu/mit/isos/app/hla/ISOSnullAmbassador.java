package edu.mit.isos.app.hla;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.mit.isos.core.context.Scenario;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;
import edu.mit.isos.core.hla.ISOSambassador;
import edu.mit.isos.core.sim.SimEntity;

public class ISOSnullAmbassador extends ISOSdefaultAmbassador implements ISOSambassador {
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