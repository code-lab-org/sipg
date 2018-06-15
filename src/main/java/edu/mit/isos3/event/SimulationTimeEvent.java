/*
 * Copyright (c) 2014, Paul T. Grogan/M.I.T., All rights reserved.
 * 
 * This file is a part of the FSS Simulation Toolkit. 
 * Please see license.txt for details.
 */
package edu.mit.isos3.event;

import java.util.EventObject;

/**
 * An event object which notifies of a change in a simulation time.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
 * @version 0.1.0
 * @since 0.1.0
 */
public class SimulationTimeEvent extends EventObject {
	private static final long serialVersionUID = -5707468210897815237L;
	
	private final long time, duration;
	
	/**
	 * Instantiates a new execution control event.
	 *
	 * @param source the source
	 * @param time the time
	 */
	public SimulationTimeEvent(Object source, long time, long duration) {
		super(source);
		this.time = time;
		this.duration = duration;
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
}
