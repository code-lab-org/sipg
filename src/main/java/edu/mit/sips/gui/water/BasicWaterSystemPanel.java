package edu.mit.sips.gui.water;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class BasicWaterSystemPanel.
 */
public class BasicWaterSystemPanel extends WaterSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset waterRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic water system panel.
	 *
	 * @param waterSystem the water system
	 */
	public BasicWaterSystemPanel(WaterSystem waterSystem) {
		super(waterSystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getWaterSystem().getName() + " Cash Flow",
				"Water Revenue (SAR/year)", waterRevenue));
		addTab("Electricity Consumption", Icons.ELECTRICITY_USE, createStackedAreaChart(
				getWaterSystem().getName() + " Electricity Use",
				"Electricity Consumed (m^3/year)", electricityConsumption));
	}
	
	/**
	 * Gets the water system.
	 *
	 * @return the water system
	 */
	public WaterSystem getWaterSystem() {
		return (WaterSystem) getInfrastructureSystem();
	}

	private void initialize() {
		waterRevenue.removeAllSeries();
		electricityConsumption.removeAllSeries();
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
		initialize();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
	}

	private void update(int year) {
		updateSeries(waterRevenue, "Revenue", year, 
				getWaterSystem().getCashFlow());
		updateSeries(electricityConsumption, "Consumption", year, 
				getWaterSystem().getElectricityConsumption());
	}
}