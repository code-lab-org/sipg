package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.energy.CityEnergySystem;
import edu.mit.sips.core.energy.NationalEnergySystem;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
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
		String riyadh = "Riyadh";
		String jeddah = "Jeddah";
		String sakakah = "Sakakah";
		
		final Country ksa = Country.buildCountry("KSA", Arrays.asList(
				new City(riyadh, 
						null,
						null,
						new CityEnergySystem(1e10, 1e10),
						new CitySocialSystem(new LogisticGrowthModel(1950, 50000, 0.08, 20000000))),
				new City(jeddah, 
						null,
						null,
						new CityEnergySystem(0, 0),
						null),
				new City(sakakah, 
						null,
						null,
						new CityEnergySystem(0, 0),
						null)
			));
		
		NationalEnergySystem nes = (NationalEnergySystem) ksa.getEnergySystem();
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1942));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1955));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1965));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					nes.addElement(ElementFactory.createDefaultPetroleumDistribution(
							origin.getName(), destination.getName()));
				}
			}
		}

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
