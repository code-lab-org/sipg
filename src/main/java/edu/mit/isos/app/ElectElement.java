package edu.mit.isos.app;

import edu.mit.isos.core.element.Element;

public interface ElectElement extends Element {
	public double getPetrolReceived();
	public double getElectSentToPetrol();
	public double getElectSentToSocial();
	public double getElectSentToWater();
}
