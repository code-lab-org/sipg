package edu.mit.isos.core.hla;

import java.util.Collection;

import edu.mit.isos.core.context.Scenario;
import edu.mit.isos.core.element.Element;

public interface ISOSambassador {
	public Collection<Element> getElements();
	
	public void connect(String federationName, String fomPath, 
			String federateName, String federateType);
	
	public void initialize(Scenario scenario, int numIterations, long timeStep);
			
	public void advance();
	
	public void disconnect(String federationName);
}