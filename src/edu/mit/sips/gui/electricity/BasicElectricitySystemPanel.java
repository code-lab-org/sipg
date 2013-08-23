package edu.mit.sips.gui.electricity;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class BasicElectricitySystemPanel.
 */
public class BasicElectricitySystemPanel extends ElectricitySystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset electricityRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();
	DefaultTableXYDataset petroleumConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public BasicElectricitySystemPanel(ElectricitySystem electricitySystem) {
		super(electricitySystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Electricity Revenue (SAR/year)", electricityRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				"Water Consumed (m^3/year)", waterConsumption));
		addTab("Petroleum Consumption", Icons.PETROLEUM_USE, createStackedAreaChart(
				"Petroleum Consumed (bbl/year)", petroleumConsumption));
	}
	
	/**
	 * Gets the electricity system.
	 *
	 * @return the electricity system
	 */
	public ElectricitySystem getElectricitySystem() {
		return (ElectricitySystem) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		electricityRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
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
		updateSeries(electricityRevenue, "Revenue", year, 
				getElectricitySystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getElectricitySystem().getWaterConsumption());
		updateSeries(petroleumConsumption, "Consumption", year, 
				getElectricitySystem().getPetroleumConsumption());
	}
}
