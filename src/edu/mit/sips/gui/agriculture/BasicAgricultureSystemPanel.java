package edu.mit.sips.gui.agriculture;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

public class BasicAgricultureSystemPanel extends AgricultureSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();

	public BasicAgricultureSystemPanel(AgricultureSystem agricultureSystem) {
		super(agricultureSystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Agriculture Revenue (SAR/year)", agricultureRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				"Water Consumed (m^3/year)", waterConsumption));
	}
	
	/**
	 * Gets the agriculture system.
	 *
	 * @return the agriculture system
	 */
	public AgricultureSystem getAgricultureSystem() {
		return (AgricultureSystem) getInfrastructureSystem();
	}

	@Override
	public void initialize() {
		agricultureRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
	}

	@Override
	public void update(int year) {
		updateSeries(agricultureRevenue, "Revenue", year, 
				getAgricultureSystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getAgricultureSystem().getWaterConsumption());
	}

	@Override
	public void simulationInitialized(UpdateEvent event) { }

	@Override
	public void simulationUpdated(UpdateEvent event) { }
}
