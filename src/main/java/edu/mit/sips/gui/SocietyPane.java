/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
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
import edu.mit.sips.gui.agriculture.BasicAgricultureSystemPanel;
import edu.mit.sips.gui.agriculture.LocalAgricultureSystemPanel;
import edu.mit.sips.gui.electricity.BasicElectricitySystemPanel;
import edu.mit.sips.gui.electricity.ElectricitySystemPanel;
import edu.mit.sips.gui.electricity.LocalElectricitySystemPanel;
import edu.mit.sips.gui.petroleum.BasicPetroleumSystemPanel;
import edu.mit.sips.gui.petroleum.LocalPetroleumSystemPanel;
import edu.mit.sips.gui.petroleum.PetroleumSystemPanel;
import edu.mit.sips.gui.social.SocialSystemPanel;
import edu.mit.sips.gui.water.BasicWaterSystemPanel;
import edu.mit.sips.gui.water.LocalWaterSystemPanel;
import edu.mit.sips.gui.water.WaterSystemPanel;
import edu.mit.sips.io.Icons;
import edu.mit.sips.scenario.Scenario;

/**
 * The society pane composes key information for a unit of society. It includes
 * infrastructure system panels for each locally-controlled sector and a 
 * score tab for a top-level country society.
 * 
 * @author Paul T. Grogan
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
	 * Instantiates a new society pane.
	 *
	 * @param scenario the scenario
	 * @param useBasicRemotePanels the use basic remote panels
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
		tabbedPane.addTab("Finances", Icons.FUNDS, socialTab);
		
		scoreTab = new ScorePanel(scenario, scoreLabel);
		tabbedPane.addTab("Scores", Icons.METRICS, scoreTab);

		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			agricultureTab = new LocalAgricultureSystemPanel(
					(AgricultureSystem.Local)country.getAgricultureSystem());
			tabbedPane.addTab("Agriculture Sector", Icons.AGRICULTURE, agricultureTab);
		} else if (scenario.isRemotePanelsDisplayed()) {
			agricultureTab = new BasicAgricultureSystemPanel(country.getAgricultureSystem());
			tabbedPane.addTab("Agriculture Sector", Icons.AGRICULTURE, agricultureTab);
		} else {
			agricultureTab = null;
		}

		if(country.getWaterSystem() instanceof WaterSystem.Local) {
			waterTab = new LocalWaterSystemPanel(
					(WaterSystem.Local) country.getWaterSystem());	
			tabbedPane.addTab("Water Sector", Icons.WATER, waterTab);	
		} else if (scenario.isRemotePanelsDisplayed()) {	
			waterTab = new BasicWaterSystemPanel(country.getWaterSystem());
			tabbedPane.addTab("Water Sector", Icons.WATER, waterTab);	
		} else {
			waterTab = null;
		}

		if(country.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			petroleumTab = new LocalPetroleumSystemPanel(
					(PetroleumSystem.Local) country.getPetroleumSystem());
			tabbedPane.addTab("Petroleum Sector", Icons.PETROLEUM, petroleumTab);
		} else if (scenario.isRemotePanelsDisplayed()) {	
			petroleumTab = new BasicPetroleumSystemPanel(country.getPetroleumSystem());
			tabbedPane.addTab("Petroleum Sector", Icons.PETROLEUM, petroleumTab);
		} else {
			petroleumTab = null;
		}

		if(country.getElectricitySystem() instanceof ElectricitySystem.Local) {
			electricityTab = new LocalElectricitySystemPanel(
					(ElectricitySystem.Local) country.getElectricitySystem());
			tabbedPane.addTab("Electricity Sector", Icons.ELECTRICITY, electricityTab);
		} else if (scenario.isRemotePanelsDisplayed()) {	
			electricityTab = new BasicElectricitySystemPanel(country.getElectricitySystem());
			tabbedPane.addTab("Electricity Sector", Icons.ELECTRICITY, electricityTab);
		} else {
			electricityTab = null;
		}
	}

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
