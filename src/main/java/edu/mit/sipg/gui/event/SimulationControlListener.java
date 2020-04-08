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
package edu.mit.sipg.gui.event;

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
 * @author Paul T. Grogan
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
