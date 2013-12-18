package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.Region;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
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

/**
 * The Class SocietyPane.
 */
public class SocietyPane extends JTabbedPane implements UpdateListener {
	private static final long serialVersionUID = -2070963615190359867L;

	private final List<SocietyPane> nestedPaneList = 
			new ArrayList<SocietyPane>();

	private final WaterSystemPanel waterTab;
	private final AgricultureSystemPanel agricultureTab;
	private final ElectricitySystemPanel electricityTab;
	private final PetroleumSystemPanel petroleumTab;
	private final SocialSystemPanel socialTab;
	private final ScorePanel scoreTab;
	private final JTabbedPane infraPane;

	/**
	 * Instantiates a new social system pane.
	 *
	 * @param society the society
	 */
	public SocietyPane(Society society) {
		/*
		int localSystemCount = 0;
		for(InfrastructureSystem system : society.getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				localSystemCount++;
			}
		}
		 */

		if(society instanceof Country) {
			socialTab = new SocialSystemPanel(society.getSocialSystem());
			scoreTab = new ScorePanel((Country) society);
			addTab("Metrics", Icons.METRICS, scoreTab);
		} else if(society.getSocialSystem() instanceof SocialSystem.Local) {
			socialTab = new SocialSystemPanel(society.getSocialSystem());
			addTab(society.getSocialSystem().getName(), getIcon(society), socialTab);
			scoreTab = null;
		} else {
			socialTab = null;
			scoreTab = null;
		}

		if(society instanceof City) { // || localSystemCount <= 1) {
			// add infrastructure directly to society panel if city
			// or if there are only 0 or 1 local infrastructure
			infraPane = this;
		} else {
			// otherwise create sub-tab for infrastructure
			infraPane = new JTabbedPane();
			infraPane.addTab(society.getSocialSystem().getName(), getIcon(society), socialTab);
			addTab(society.getName(), getIcon(society), infraPane);
		}

		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			agricultureTab = new LocalAgricultureSystemPanel(
					(AgricultureSystem.Local)society.getAgricultureSystem());
			infraPane.addTab(society.getAgricultureSystem().getName(), 
					Icons.AGRICULTURE, agricultureTab);
		} else {
			// agricultureTab = new BasicAgricultureSystemPanel(society.getAgricultureSystem());
			agricultureTab = null;
		}

		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			waterTab = new LocalWaterSystemPanel(
					(WaterSystem.Local) society.getWaterSystem());	
			infraPane.addTab(society.getWaterSystem().getName(), 
					Icons.WATER, waterTab);		
		} else {
			// waterTab = new BasicWaterSystemPanel(society.getWaterSystem());
			waterTab = null;
		}

		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			petroleumTab = new LocalPetroleumSystemPanel(
					(PetroleumSystem.Local) society.getPetroleumSystem());
			infraPane.addTab(society.getPetroleumSystem().getName(), 
					Icons.PETROLEUM, petroleumTab);
		} else {
			// petroleumTab = new BasicPetroleumSystemPanel(society.getPetroleumSystem());
			petroleumTab = null;
		}

		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			electricityTab = new LocalElectricitySystemPanel(
					(ElectricitySystem.Local) society.getElectricitySystem());
			infraPane.addTab(society.getElectricitySystem().getName(), 
					Icons.ELECTRICITY, electricityTab);
		} else {
			// electricityTab = new BasicElectricitySystemPanel(society.getElectricitySystem());
			electricityTab = null;
		}

		for(Society nestedSociety : society.getNestedSocieties()) {
			SocietyPane subPane = new SocietyPane(nestedSociety);
			nestedPaneList.add(subPane);
			addTab(nestedSociety.getName(), getIcon(nestedSociety), subPane);
		}
	}

	/**
	 * Gets the icon.
	 *
	 * @param society the society
	 * @return the icon
	 */
	private Icon getIcon(Society society) {
		if(society instanceof Country) {
			return Icons.COUNTRY;
		} else if(society instanceof Region) {
			return Icons.REGION;
		} else if(society instanceof City) {
			return Icons.CITY;
		} else {
			throw new IllegalArgumentException("Unknown society.");
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

		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.simulationCompleted(event);
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

		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.simulationInitialized(event);
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
			infraPane.setTitleAt(infraPane.indexOfComponent(waterTab), 
					waterTab.getInfrastructureSystem().getName());
			waterTab.simulationUpdated(event);
		}
		if(agricultureTab != null) {
			infraPane.setTitleAt(infraPane.indexOfComponent(agricultureTab), 
					agricultureTab.getInfrastructureSystem().getName());
			agricultureTab.simulationUpdated(event);
		}
		if(electricityTab != null) {
			infraPane.setTitleAt(infraPane.indexOfComponent(electricityTab), 
					electricityTab.getInfrastructureSystem().getName());
			electricityTab.simulationUpdated(event);
		}
		if(petroleumTab != null) {
			infraPane.setTitleAt(infraPane.indexOfComponent(petroleumTab), 
					petroleumTab.getInfrastructureSystem().getName());
			petroleumTab.simulationUpdated(event);
		}
		if(socialTab != null) {
			if(indexOfComponent(socialTab) >= 0) {
				setTitleAt(indexOfComponent(socialTab), 
						socialTab.getInfrastructureSystem().getName());
			} else if(infraPane.indexOfComponent(socialTab) >= 0) {
				infraPane.setTitleAt(infraPane.indexOfComponent(socialTab), 
						socialTab.getInfrastructureSystem().getName());
			}
			socialTab.simulationUpdated(event);
		}

		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.simulationUpdated(event);
		}
	}
}
