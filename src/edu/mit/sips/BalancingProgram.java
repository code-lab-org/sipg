package edu.mit.sips;

import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.CityAgricultureSystem;
import edu.mit.sips.core.agriculture.NationalAgricultureSystem;
import edu.mit.sips.core.energy.CityEnergySystem;
import edu.mit.sips.core.energy.NationalEnergySystem;
import edu.mit.sips.core.social.CitySocialSystem;
import edu.mit.sips.core.social.LogisticGrowthModel;
import edu.mit.sips.core.social.NationalSocialSystem;
import edu.mit.sips.core.water.CityWaterSystem;
import edu.mit.sips.core.water.NationalWaterSystem;
import edu.mit.sips.gui.ConsoleLogger;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.hla.SimAmbassador;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class BalancingProgram {
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String riyadh = "Riyadh";
		String jeddah = "Jeddah";
		String sakakah = "Sakakah";
		
		final Country ksa = new Country("KSA", Arrays.asList(
				new City(riyadh, 
						new CityAgricultureSystem(3000),
						new CityWaterSystem(true, 3e9, 3e9, 3e6, 0),
						new CityEnergySystem(1e10, 1e10),
						new CitySocialSystem(new LogisticGrowthModel(1950, 50000, 0.08, 20000000))),
				new City(jeddah, 
						new CityAgricultureSystem(4000),
						new CityWaterSystem(true, 2e9, 2e9, 2e6, 0),
						new CityEnergySystem(0, 0),
						new CitySocialSystem(new LogisticGrowthModel(1950, 100000, 0.07, 10000000))),
				new City(sakakah, 
						new CityAgricultureSystem(5000),
						new CityWaterSystem(false, 1e9, 1e9, 1e6, 0),
						new CityEnergySystem(0, 0),
						new CitySocialSystem(new LogisticGrowthModel(1950, 10000, 0.05, 750000)))
			),
			new NationalAgricultureSystem(),
			new NationalWaterSystem(),
			new NationalEnergySystem(),
			new NationalSocialSystem(),
			100000);
		
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
		
		NationalEnergySystem nes = (NationalEnergySystem) ksa.getEnergySystem();
		
		for(City origin : ksa.getCities()) {
			for(City destination : ksa.getCities()) {
				if(!origin.equals(destination)) {
					nas.addElement(ElementFactory.createDefaultFoodDistribution(
							origin.getName(), destination.getName()));
					nes.addElement(ElementFactory.createDefaultPetroleumDistribution(
							origin.getName(), destination.getName()));
				}
			}
		}

		NationalWaterSystem nws = (NationalWaterSystem) ksa.getWaterSystem();
		nws.addElement(ElementFactory.createAquiferWell(riyadh, 1940));
		nws.addElement(ElementFactory.createAquiferWell(riyadh, 1965));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1942));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1955));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1965));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));
		nes.addElement(ElementFactory.createPetroleumWell(riyadh, 1975));

		final Simulator simulator = new Simulator(ksa);
		simulator.addUpdateListener(new ConsoleLogger());

		try {
			final SimAmbassador ambassador = new SimAmbassador(ksa);
			ambassador.connectAndJoin("SuperUser");
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					DataFrame frame = new DataFrame();
					frame.setSimulator(simulator);
					frame.pack();
					frame.setVisible(true);
					frame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							try {
								ambassador.resignAndDisconnect();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
