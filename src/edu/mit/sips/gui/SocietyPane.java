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
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.agriculture.AgricultureSystemPanel;
import edu.mit.sips.gui.agriculture.BasicAgricultureSystemPanel;
import edu.mit.sips.gui.agriculture.LocalAgricultureSystemPanel;
import edu.mit.sips.gui.energy.BasicEnergySystemPanel;
import edu.mit.sips.gui.energy.EnergySystemPanel;
import edu.mit.sips.gui.energy.LocalEnergySystemPanel;
import edu.mit.sips.gui.water.BasicWaterSystemPanel;
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
	private final EnergySystemPanel energyTab;
	private final SocialSystemPanel socialTab;
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

		socialTab = new SocialSystemPanel(society.getSocialSystem());
		addTab(society.getSocialSystem().getName(), getIcon(society), socialTab);
		
		if(society instanceof City) { // || localSystemCount <= 1) {
			// add infrastructure directly to society panel if city
			// or if there are only 0 or 1 local infrastructure
			infraPane = this;
		} else {
			// otherwise create sub-tab for infrastructure
			infraPane = new JTabbedPane();
			addTab("Infrastructure", Icons.INFRASTRUCTURE, infraPane);
		}
		
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			agricultureTab = new LocalAgricultureSystemPanel(
					(AgricultureSystem.Local)society.getAgricultureSystem());
		} else {
			agricultureTab = new BasicAgricultureSystemPanel(society.getAgricultureSystem());
		}
		infraPane.addTab(society.getAgricultureSystem().getName(), 
				Icons.AGRICULTURE, agricultureTab);
		
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			waterTab = new LocalWaterSystemPanel(
					(WaterSystem.Local) society.getWaterSystem());	
		} else {
			waterTab = new BasicWaterSystemPanel(society.getWaterSystem());
		}
		infraPane.addTab(society.getWaterSystem().getName(), 
				Icons.WATER, waterTab);		
		
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			energyTab = new LocalEnergySystemPanel(
					(EnergySystem.Local) society.getEnergySystem());
		} else {
			energyTab = new BasicEnergySystemPanel(society.getEnergySystem());
		}
		infraPane.addTab(society.getEnergySystem().getName(), 
				Icons.ENERGY, energyTab);

		for(Society nestedSociety : society.getNestedSocieties()) {
			SocietyPane subPane = new SocietyPane(nestedSociety);
			nestedPaneList.add(subPane);
			addTab(nestedSociety.getName(), getIcon(nestedSociety), subPane);
		}

		if(society instanceof Country) {
			addTab("Globals", Icons.CONFIGURATION, 
					new GlobalsPane((Country)society));
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
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		
		if(waterTab != null) {
			waterTab.initialize();
		}
		
		agricultureTab.initialize();
		
		if(energyTab != null) {
			energyTab.initialize();
		}
		
		socialTab.initialize();
		
		for(SocietyPane subPane : nestedPaneList) {			
			subPane.initialize();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		// nothing to do here
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		waterTab.simulationInitialized(event);
		agricultureTab.simulationInitialized(event);
		socialTab.simulationInitialized(event);
		energyTab.simulationInitialized(event);
		
		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.simulationInitialized(event);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		waterTab.simulationUpdated(event);
		agricultureTab.simulationUpdated(event);
		energyTab.simulationUpdated(event);
		socialTab.simulationUpdated(event);
		
		for(SocietyPane nestedPane : nestedPaneList) {
			nestedPane.simulationUpdated(event);
		}
	}

	/**
	 * Update datasets.
	 *
	 * @param year the year
	 * @param superSystem the system
	 */
	public void updateDatasets(int year) {
		infraPane.setTitleAt(infraPane.indexOfComponent(waterTab), 
				waterTab.getInfrastructureSystem().getName());
		waterTab.update(year);

		infraPane.setTitleAt(infraPane.indexOfComponent(agricultureTab), 
				agricultureTab.getInfrastructureSystem().getName());
		agricultureTab.update(year);
		
		infraPane.setTitleAt(infraPane.indexOfComponent(energyTab), 
				energyTab.getInfrastructureSystem().getName());
		energyTab.update(year);

		setTitleAt(indexOfComponent(socialTab), 
				socialTab.getInfrastructureSystem().getName());
		socialTab.update(year);
		
		for(SocietyPane subPane : nestedPaneList) {			
			subPane.updateDatasets(year);
		}
	}
}
