package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class Player3 {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final Country ksa = CountryFactory.createSaudiCountry(
				Arrays.asList(CityTemplate.RURAL),
				Arrays.asList(Sector.AGRICULTURE));
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					DefaultAgricultureSystem.Local las = (DefaultAgricultureSystem.Local) origin.getAgricultureSystem();
					las.addElement(ElementFactory.createDefaultFoodDistribution(
							origin.getName(), destination.getName()));
				}
			}
		}

		final Simulator simulator = new Simulator("Agriculture Player", ksa);
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
