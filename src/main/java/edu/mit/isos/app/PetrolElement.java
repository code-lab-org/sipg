package edu.mit.isos.app;

import edu.mit.isos.core.element.Element;

public interface PetrolElement extends Element {
	public double getElectReceived();
	public double getPetrolSentToElect();
	public double getPetrolSentToSocial();
}
