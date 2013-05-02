package edu.mit.sips.gui.energy;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class BasicEnergySystemPanel.
 */
public class BasicEnergySystemPanel extends EnergySystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset energyRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityConsumption = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic energy system panel.
	 *
	 * @param energySystem the energy system
	 */
	public BasicEnergySystemPanel(EnergySystem energySystem) {
		super(energySystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Energy Revenue (SAR/year)", energyRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				"Water Consumed (m^3/year)", waterConsumption));
		addTab("Electricity Consumption", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Consumed (MWh/year)", electricityConsumption));
		addTab("Petroleum Consumption", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Consumed (bbl/year)", petroleumConsumption));
	}
	
	/**
	 * Gets the energy system.
	 *
	 * @return the energy system
	 */
	public EnergySystem getEnergySystem() {
		return (EnergySystem) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		energyRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
		electricityConsumption.removeAllSeries();
		petroleumConsumption.removeAllSeries();
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
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		// nothing to do here
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#update(int)
	 */
	@Override
	public void update(int year) {
		updateSeries(energyRevenue, "Revenue", year, 
				getEnergySystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getEnergySystem().getWaterConsumption());
		updateSeries(electricityConsumption, "Consumption", year, 
				getEnergySystem().getElectricityConsumption());
		updateSeries(petroleumConsumption, "Consumption", year, 
				getEnergySystem().getPetroleumConsumption());
	}
}
