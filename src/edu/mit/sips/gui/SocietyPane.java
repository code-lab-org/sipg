package edu.mit.sips.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
		tabbedPane.addTab(country.getName(), Icons.COUNTRY, socialTab);
		
		scoreTab = new ScorePanel(scenario, scoreLabel);
		tabbedPane.addTab("Scores", Icons.METRICS, scoreTab);

		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			agricultureTab = new LocalAgricultureSystemPanel(
					(AgricultureSystem.Local)country.getAgricultureSystem());
			tabbedPane.addTab("Agriculture", Icons.AGRICULTURE, agricultureTab);
		} else {
			// agricultureTab = new BasicAgricultureSystemPanel(society.getAgricultureSystem());
			agricultureTab = null;
		}

		if(country.getWaterSystem() instanceof WaterSystem.Local) {
			waterTab = new LocalWaterSystemPanel(
					(WaterSystem.Local) country.getWaterSystem());	
			tabbedPane.addTab("Water", Icons.WATER, waterTab);		
		} else {
			// waterTab = new BasicWaterSystemPanel(society.getWaterSystem());
			waterTab = null;
		}

		if(country.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			petroleumTab = new LocalPetroleumSystemPanel(
					(PetroleumSystem.Local) country.getPetroleumSystem());
			tabbedPane.addTab("Petroleum", Icons.PETROLEUM, petroleumTab);
		} else {
			// petroleumTab = new BasicPetroleumSystemPanel(society.getPetroleumSystem());
			petroleumTab = null;
		}

		if(country.getElectricitySystem() instanceof ElectricitySystem.Local) {
			electricityTab = new LocalElectricitySystemPanel(
					(ElectricitySystem.Local) country.getElectricitySystem());
			tabbedPane.addTab("Electricity", Icons.ELECTRICITY, electricityTab);
		} else {
			// electricityTab = new BasicElectricitySystemPanel(society.getElectricitySystem());
			electricityTab = null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
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
}
