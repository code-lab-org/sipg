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
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6, 0),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote()),
				new City(jeddah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Local(true, 2e9, 2e9, 2e6, 0),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 100000, 0.07, 10000000))),
				new City(sakakah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Local(false, 1e9, 1e9, 1e6, 0),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote())
			));

		DefaultWaterSystem.Local jws = (DefaultWaterSystem.Local) ksa.getCity(jeddah).getWaterSystem();
		jws.addElement(ElementFactory.createAquiferWell(riyadh, 1940));
		jws.addElement(ElementFactory.createAquiferWell(riyadh, 1965));

		final Simulator simulator = new Simulator("Water Player", ksa);
		//simulator.addUpdateListener(new ConsoleLogger());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DataFrame frame = new DataFrame();
				frame.initialize(simulator);
				frame.pack();
				frame.setVisible(true);
			}
		});
		try {
			simulator.getAmbassador().connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
