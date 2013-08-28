package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.gui.ConsoleLogger;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.scenario.SaudiScenario1;
import edu.mit.sips.scenario.Sector;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class SuperPlayer {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final Simulator simulator = new Simulator(new SaudiScenario1(
				Arrays.asList(SaudiScenario1.INDUSTRIAL, 
						SaudiScenario1.URBAN, 
						SaudiScenario1.RURAL),
				Arrays.asList(Sector.AGRICULTURE,
						Sector.WATER,
						Sector.ELECTRICITY,
						Sector.PETROLEUM)));
		simulator.addUpdateListener(new ConsoleLogger());
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					DataFrame frame = new DataFrame();
					frame.initialize(simulator);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// set federate name after data is loaded in initialization
		simulator.getConnection().setFederateName("Super Player");
	}
}
