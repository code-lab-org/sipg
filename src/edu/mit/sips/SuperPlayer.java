package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.Country;
import edu.mit.sips.gui.ConsoleLogger;
import edu.mit.sips.gui.DataFrame;
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
		final Country ksa = CountryFactory.createValidatedCountry(
				Arrays.asList(
						CityTemplate.INDUSTRIAL_VALIDATED, 
						CityTemplate.URBAN_VALIDATED, 
						CityTemplate.RURAL_VALIDATED),
				Arrays.asList(
						Sector.AGRICULTURE,
						Sector.WATER,
						Sector.ELECTRICITY,
						Sector.PETROLEUM));

		final Simulator simulator = new Simulator(ksa);
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
