package edu.mit.sips;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class NullPlayer {
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
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote()),
				new City(jeddah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote()),
				new City(sakakah, 
						new DefaultAgricultureSystem.Remote(),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote())
			));

		final Simulator simulator = new Simulator("Null Player", ksa);
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
