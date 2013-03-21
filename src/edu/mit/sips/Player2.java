package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.core.water.CityWaterSystem;
import edu.mit.sips.core.water.NationalWaterSystem;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class Player2 {
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
				new City(riyadh, 
						null,
						new CityWaterSystem(true, 3e9, 3e9, 3e6, 0),
						null,
						null),
				new City(jeddah, 
						null,
						new CityWaterSystem(true, 2e9, 2e9, 2e6, 0),
						null,
						new CitySocialSystem(new LogisticGrowthModel(1950, 100000, 0.07, 10000000))),
				new City(sakakah, 
						null,
						new CityWaterSystem(false, 1e9, 1e9, 1e6, 0),
						null,
						null)
			));

		NationalWaterSystem nws = (NationalWaterSystem) ksa.getWaterSystem();
		nws.addElement(ElementFactory.createAquiferWell(riyadh, 1940));
		nws.addElement(ElementFactory.createAquiferWell(riyadh, 1965));

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
