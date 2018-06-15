package edu.mit.sips.gui.agriculture;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class BasicAgricultureSystemPanel.
 */
public class BasicAgricultureSystemPanel extends AgricultureSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset agricultureRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset waterConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic agriculture system panel.
	 *
	 * @param agricultureSystem the agriculture system
	 */
	public BasicAgricultureSystemPanel(AgricultureSystem agricultureSystem) {
		super(agricultureSystem);

		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				getAgricultureSystem().getName() + " Cash Flow",
				"Agriculture Revenue (SAR/year)", agricultureRevenue));
		addTab("Water Consumption", Icons.WATER_USE, createStackedAreaChart(
				getAgricultureSystem().getName() + " Water Use",
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

	private void initialize() {
		agricultureRevenue.removeAllSeries();
		waterConsumption.removeAllSeries();
	}

	private void update(int year) {
		updateSeries(agricultureRevenue, "Revenue", year, 
				getAgricultureSystem().getCashFlow());
		updateSeries(waterConsumption, "Consumption", year, 
				getAgricultureSystem().getWaterConsumption());
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
}
