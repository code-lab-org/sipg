package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class NullProgram {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String riyadh = "Riyadh";
		String jeddah = "Jeddah";
		String sakakah = "Sakakah";
		
		final Country ksa = Country.buildCountry("KSA", Arrays.asList(
				new City(riyadh, null, null, null, null),
				new City(jeddah, null, null, null, null),
				new City(sakakah, null, null, null, 
						new CitySocialSystem(new LogisticGrowthModel(1950, 10000, 0.05, 750000)))
			));
		final Simulator simulator = new Simulator(ksa);
		//simulator.addUpdateListener(new ConsoleLogger());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DataFrame frame = new DataFrame();
				frame.initialize(simulator);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
