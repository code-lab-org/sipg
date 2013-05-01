package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.SocietyFactory.CityAssignment;
import edu.mit.sips.SocietyFactory.SectorAssignment;
import edu.mit.sips.core.Country;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class Player1 {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final Country ksa = SocietyFactory.createSaudiCountry(
				Arrays.asList(CityAssignment.INDUSTRIAL),
				Arrays.asList(SectorAssignment.ENERGY));

		final Simulator simulator = new Simulator("Energy Player", ksa);
		//simulator.addUpdateListener(new ConsoleLogger());
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
		
		try {
			simulator.getAmbassador().connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
