package edu.mit.sips.gui.petroleum;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.UpdateEvent;
import edu.mit.sips.io.Icons;

/**
 * The Class BasicPetroleumSystemPanel.
 */
public class BasicPetroleumSystemPanel extends PetroleumSystemPanel {
	private static final long serialVersionUID = 569560127649283731L;

	DefaultTableXYDataset petroleumRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset electricityConsumption = new DefaultTableXYDataset();

	/**
	 * Instantiates a new basic petroleum system panel.
	 *
	 * @param petroleumSystem the petroleum system
	 */
	public BasicPetroleumSystemPanel(PetroleumSystem petroleumSystem) {
		super(petroleumSystem);
		
		addTab("Revenue", Icons.REVENUE, createStackedAreaChart(
				"Petroleum Revenue (SAR/year)", petroleumRevenue));
		addTab("Electricity Consumption", Icons.ELECTRICITY_USE, createStackedAreaChart(
				"Electricity Consumed (toe/year)", electricityConsumption));
	}
	
	/**
	 * Gets the petroleum system.
	 *
	 * @return the petroleum system
	 */
	public PetroleumSystem getPetroleumSystem() {
		return (PetroleumSystem) getInfrastructureSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.InfrastructureSystemPanel#initialize()
	 */
	@Override
	public void initialize() {
		petroleumRevenue.removeAllSeries();
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
		updateSeries(petroleumRevenue, "Revenue", year, 
				getPetroleumSystem().getCashFlow());
		updateSeries(electricityConsumption, "Consumption", year, 
				getPetroleumSystem().getElectricityConsumption());
	}
}
