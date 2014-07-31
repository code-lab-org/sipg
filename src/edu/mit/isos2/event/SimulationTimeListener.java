/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos2.event;

import java.util.EventListener;

/**
 * The listener interface for receiving simulationTime events.
 * The class that is interested in processing a simulationTime
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSimulationTimeListener<code> method. When
 * the simulationTime event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SimulationTimeEvent
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public interface SimulationTimeListener extends EventListener {
	
	/**
	 * Time advanced.
	 *
	 * @param event the event
	 */
	public void timeAdvanced(SimulationTimeEvent event);
}
