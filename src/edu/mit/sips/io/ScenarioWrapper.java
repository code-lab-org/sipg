package edu.mit.sips.io;

import edu.mit.sips.scenario.Scenario;

/**
 * The Class ScenarioWrapper.
 * 
 * Required to fix a "bug" (feature) of GSON not recognizing interfaces for
 * the top-level serialized class.
 */
public class ScenarioWrapper {
	public final Scenario scenario;
	
	public ScenarioWrapper(Scenario scenario) {
		this.scenario = scenario;
	}
}
