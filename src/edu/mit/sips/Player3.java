package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.CityAgricultureSystem;
import edu.mit.sips.core.agriculture.NationalAgricultureSystem;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
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
		String riyadh = "Riyadh";
		String jeddah = "Jeddah";
		String sakakah = "Sakakah";
		
		final Country ksa = Country.buildCountry("KSA", Arrays.asList(
				new City(riyadh, 
						new CityAgricultureSystem(3000),
						null,
						null,
						null),
				new City(jeddah, 
						new CityAgricultureSystem(4000),
						null,
						null,
						null),
				new City(sakakah, 
						new CityAgricultureSystem(5000),
						null,
						null,
						new CitySocialSystem(new LogisticGrowthModel(1950, 10000, 0.05, 750000)))
			));
		
		NationalAgricultureSystem nas = (NationalAgricultureSystem) ksa.getAgricultureSystem();
		nas.addElement(ElementFactory.createGrazingLand(sakakah));
		nas.addElement(ElementFactory.createDateFarm(sakakah));
		nas.addElement(ElementFactory.createDateFarm(sakakah));
		nas.addElement(ElementFactory.createDateFarm(riyadh));
		nas.addElement(ElementFactory.createDateFarm(riyadh));
		nas.addElement(ElementFactory.createDateFarm(riyadh));
		nas.addElement(ElementFactory.createDateFarm(jeddah));
		nas.addElement(ElementFactory.createDateFarm(jeddah));
		nas.addElement(ElementFactory.createDateFarm(jeddah));
		nas.addElement(ElementFactory.createDateFarm(jeddah));
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					nas.addElement(ElementFactory.createDefaultFoodDistribution(
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
