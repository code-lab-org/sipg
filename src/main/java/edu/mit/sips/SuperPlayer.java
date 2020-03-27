package edu.mit.sips;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.mit.sips.gui.ConsoleLogger;
import edu.mit.sips.gui.DataFrame;
import edu.mit.sips.scenario.GameScenario;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.scenario.Sector;
import edu.mit.sips.sim.DefaultConnection;
import edu.mit.sips.sim.DefaultSimulator;
import edu.mit.sips.sim.Simulator;

/**
 * The Class BalancingProgram.
 */
public class SuperPlayer {
	private static Logger logger = Logger.getLogger("edu.mit.sips");
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		boolean isTeamScoreDisplayed = true;
		
		logger.debug("Creating scenario.");
		Scenario scenario = new GameScenario(
				Arrays.asList(GameScenario.INDUSTRIAL, 
						GameScenario.URBAN, 
						GameScenario.RURAL),
				Arrays.asList(Sector.AGRICULTURE,
						Sector.WATER,
						Sector.ELECTRICITY,
						Sector.PETROLEUM), isTeamScoreDisplayed);
		
		logger.debug("Creating simulator.");
		//final Simulator simulator = new HlaSimulator(scenario);
		final Simulator simulator = new DefaultSimulator(scenario, new DefaultConnection());
		simulator.addUpdateListener(new ConsoleLogger());

		logger.debug("Launching graphical user interface.");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					DataFrame frame = new DataFrame();
					frame.initialize(simulator);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		simulator.getConnection().setFederateName("Super Player");
	}
}