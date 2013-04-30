package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
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
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Local(1e10, 1e10),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 50000, 0.08, 20000000), 1000)),
				new City(jeddah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Local(0, 0),
						new DefaultSocialSystem.Remote()),
				new City(sakakah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Local(0, 0),
						new DefaultSocialSystem.Remote())
			));
		
		DefaultEnergySystem.Local res = (DefaultEnergySystem.Local) ksa.getCity(riyadh).getEnergySystem();
		res.addElement(ElementFactory.createPetroleumWell(riyadh, 1942));
		res.addElement(ElementFactory.createPetroleumWell(riyadh, 1955));
		res.addElement(ElementFactory.createPetroleumWell(riyadh, 1965));
		res.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));
		res.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					DefaultEnergySystem.Local les = (DefaultEnergySystem.Local) origin.getEnergySystem();
					les.addElement(ElementFactory.createDefaultPetroleumDistribution(
							origin.getName(), destination.getName()));
				}
			}
		}

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
