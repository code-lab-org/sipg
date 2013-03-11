package edu.mit.sips.gui;

import java.util.EventListener;

/**
 * The listener interface for receiving simulationControl events.
 * The class that is interested in processing a simulationControl
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSimulationControlListener<code> method. When
 * the simulationControl event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SimulationControlEvent
 */
public interface SimulationControlListener extends EventListener {
	
	/**
	 * Initialize simulation.
	 *
	 * @param event the event
	 */
	public void initializeSimulation(SimulationControlEvent.Initialize event);
	
	/**
	 * Advance simulation.
	 *
	 * @param event the event
	 */
	public void advanceSimulation(SimulationControlEvent.Advance event);
	
	/**
	 * Advance simulation to end.
	 *
	 * @param event the event
	 */
	public void advanceSimulationToEnd(SimulationControlEvent.AdvanceToEnd event);
	
	/**
	 * Execute simulation.
	 *
	 * @param event the event
	 */
	public void executeSimulation(SimulationControlEvent.Execute event);
	
	/**
	 * Reset simulation.
	 *
	 * @param event the event
	 */
	public void resetSimulation(SimulationControlEvent.Reset event);
}
