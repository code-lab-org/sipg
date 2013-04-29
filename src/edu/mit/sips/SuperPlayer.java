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
public class SuperPlayer {
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
						new DefaultWaterSystem.Local(true, 3e9, 3e9, 3e6, 0),
						new DefaultEnergySystem.Local(1e10, 1e10),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 50000, 0.08, 20000000))),
				new City(jeddah, 
						new DefaultAgricultureSystem.Local(4000),
						new DefaultWaterSystem.Local(true, 2e9, 2e9, 1e6, 0),
						new DefaultEnergySystem.Local(0, 0),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 100000, 0.07, 10000000))),
				new City(sakakah, 
						new DefaultAgricultureSystem.Local(10000),
						new DefaultWaterSystem.Local(false, 3e9, 3e9, 3e6, 0),
						new DefaultEnergySystem.Local(0, 0),
						new DefaultSocialSystem.Local(new LogisticGrowthModel(1950, 10000, 0.05, 750000)))
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

		DefaultWaterSystem.Local jws = (DefaultWaterSystem.Local) ksa.getCity(jeddah).getWaterSystem();
		jws.addElement(ElementFactory.createAquiferWell(riyadh, 1940));
		jws.addElement(ElementFactory.createAquiferWell(riyadh, 1965));
		

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

		final Simulator simulator = new Simulator("Super Player", ksa);
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
