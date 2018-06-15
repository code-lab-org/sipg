package edu.mit.isos3.element;

import edu.mit.isos3.element.Element;

public interface ElectElement extends Element {
	public double getPetrolReceived();
	public double getElectSentToPetrol();
	public double getElectSentToSocial();
	public double getElectSentToWater();
}
