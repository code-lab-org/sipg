package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.agriculture.AgricultureSystemPanel;
import edu.mit.sips.gui.agriculture.LocalAgricultureSystemPanel;
import edu.mit.sips.gui.electricity.ElectricitySystemPanel;
import edu.mit.sips.gui.electricity.LocalElectricitySystemPanel;
import edu.mit.sips.gui.petroleum.LocalPetroleumSystemPanel;
import edu.mit.sips.gui.petroleum.PetroleumSystemPanel;
import edu.mit.sips.gui.social.SocialSystemPanel;
import edu.mit.sips.gui.water.LocalWaterSystemPanel;
import edu.mit.sips.gui.water.WaterSystemPanel;
import edu.mit.sips.io.Icons;
import edu.mit.sips.scenario.Scenario;

/**
 * The Class SocietyPane.
 */
public class SocietyPane extends JPanel implements UpdateListener {
	private static final long serialVersionUID = -2070963615190359867L;

	private final WaterSystemPanel waterTab;
	private final AgricultureSystemPanel agricultureTab;
	private final ElectricitySystemPanel electricityTab;
	private final PetroleumSystemPanel petroleumTab;
	private final SocialSystemPanel socialTab;
	private final ScorePanel scoreTab;

	/**
	 * Instantiates a new social system pane.
	 *
	 * @param society the society
	 */
	public SocietyPane(Scenario scenario) {
		Country country = scenario.getCountry();
		
		setLayout(new BorderLayout());
		
		JLabel scoreLabel = new JLabel("");
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		add(scoreLabel, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		
		socialTab = new SocialSystemPanel(country.getSocialSystem());
		// TODO tabbedPane.addTab(country.getName(), Icons.COUNTRY, socialTab);
		
		scoreTab = new ScorePanel(scenario, scoreLabel);
		tabbedPane.addTab("Scores", Icons.METRICS, scoreTab);

		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			agricultureTab = new LocalAgricultureSystemPanel(
					(AgricultureSystem.Local)country.getAgricultureSystem());
			tabbedPane.addTab("Agriculture Sector", Icons.AGRICULTURE, agricultureTab);
		} else {
			// agricultureTab = new BasicAgricultureSystemPanel(society.getAgricultureSystem());
			agricultureTab = null;
		}

		if(country.getWaterSystem() instanceof WaterSystem.Local) {
			waterTab = new LocalWaterSystemPanel(
					(WaterSystem.Local) country.getWaterSystem());	
			tabbedPane.addTab("Water Sector", Icons.WATER, waterTab);		
		} else {
			// waterTab = new BasicWaterSystemPanel(society.getWaterSystem());
			waterTab = null;
		}

		if(country.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			petroleumTab = new LocalPetroleumSystemPanel(
					(PetroleumSystem.Local) country.getPetroleumSystem());
			tabbedPane.addTab("Petroleum Sector", Icons.PETROLEUM, petroleumTab);
		} else {
			// petroleumTab = new BasicPetroleumSystemPanel(society.getPetroleumSystem());
			petroleumTab = null;
		}

		if(country.getElectricitySystem() instanceof ElectricitySystem.Local) {
			electricityTab = new LocalElectricitySystemPanel(
					(ElectricitySystem.Local) country.getElectricitySystem());
			tabbedPane.addTab("Electricity Sector", Icons.ELECTRICITY, electricityTab);
		} else {
			// electricityTab = new BasicElectricitySystemPanel(society.getElectricitySystem());
			electricityTab = null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(final UpdateEvent event) {
		// Note: must use SwingUtilities.invokeAndWait method here because
		// the UpdateEvent passes the "active" Country instance. If we don't
		// wait for the GUI to update, the simulation will race ahead, causing
		// it to display data for future time periods!
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if(scoreTab != null) {
						scoreTab.simulationCompleted(event);
					}
					if(waterTab != null) {
						waterTab.simulationCompleted(event);
					}
					if(agricultureTab != null) {
						agricultureTab.simulationCompleted(event);
					}
					if(electricityTab != null) {
						electricityTab.simulationCompleted(event);
					}
					if(petroleumTab != null) {
						petroleumTab.simulationCompleted(event);
					}
					if(socialTab != null) {
						socialTab.simulationCompleted(event);
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(final UpdateEvent event) {
		// Note: must use SwingUtilities.invokeAndWait method here because
		// the UpdateEvent passes the "active" Country instance. If we don't
		// wait for the GUI to update, the simulation will race ahead, causing
		// it to display data for future time periods!
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if(scoreTab != null) {
						scoreTab.simulationInitialized(event);
					}
					if(waterTab != null) {
						waterTab.simulationInitialized(event);
					}
					if(agricultureTab != null) {
						agricultureTab.simulationInitialized(event);
					}
					if(electricityTab != null) {
						electricityTab.simulationInitialized(event);
					}
					if(petroleumTab != null) {
						petroleumTab.simulationInitialized(event);
					}
					if(socialTab != null) {
						socialTab.simulationInitialized(event);
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(final UpdateEvent event) {
		// Note: must use SwingUtilities.invokeAndWait method here because
		// the UpdateEvent passes the "active" Country instance. If we don't
		// wait for the GUI to update, the simulation will race ahead, causing
		// it to display data for future time periods!
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if(scoreTab != null) {
						scoreTab.simulationUpdated(event);
					}
					if(waterTab != null) {
						waterTab.simulationUpdated(event);
					}
					if(agricultureTab != null) {
						agricultureTab.simulationUpdated(event);
					}
					if(electricityTab != null) {
						electricityTab.simulationUpdated(event);
					}
					if(petroleumTab != null) {
						petroleumTab.simulationUpdated(event);
					}
					if(socialTab != null) {
						socialTab.simulationUpdated(event);
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
