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
						new DefaultAgricultureSystem.Local(3000),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote()),
				new City(jeddah, 
						new DefaultAgricultureSystem.Local(4000),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Remote()),
				new City(sakakah, 
						new DefaultAgricultureSystem.Local(5000),
						new DefaultWaterSystem.Remote(),
						new DefaultEnergySystem.Remote(),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 10000, 0.05, 750000)))
			));
		
		DefaultAgricultureSystem.Local ras = (DefaultAgricultureSystem.Local) ksa.getCity(riyadh).getAgricultureSystem();
		DefaultAgricultureSystem.Local sas = (DefaultAgricultureSystem.Local) ksa.getCity(sakakah).getAgricultureSystem();
		DefaultAgricultureSystem.Local jas = (DefaultAgricultureSystem.Local) ksa.getCity(jeddah).getAgricultureSystem();
		sas.addElement(ElementFactory.createGrazingLand(sakakah));
		sas.addElement(ElementFactory.createDateFarm(sakakah));
		sas.addElement(ElementFactory.createDateFarm(sakakah));
		ras.addElement(ElementFactory.createDateFarm(riyadh));
		ras.addElement(ElementFactory.createDateFarm(riyadh));
		ras.addElement(ElementFactory.createDateFarm(riyadh));
		jas.addElement(ElementFactory.createDateFarm(jeddah));
		jas.addElement(ElementFactory.createDateFarm(jeddah));
		jas.addElement(ElementFactory.createDateFarm(jeddah));
		jas.addElement(ElementFactory.createDateFarm(jeddah));
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					DefaultAgricultureSystem.Local las = (DefaultAgricultureSystem.Local) origin.getAgricultureSystem();
					las.addElement(ElementFactory.createDefaultFoodDistribution(
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
