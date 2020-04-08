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

import java.util.EventObject;

/**
 * The abstract superclass of all simulation control events.
 * 
 * @author Paul T. Grogan
 */
public abstract class SimulationControlEvent extends EventObject {
	private static final long serialVersionUID = -5707468210897815237L;
	
	/**
	 * Instantiates a new simulation control event.
	 *
	 * @param source the source
	 */
	public SimulationControlEvent(Object source) {
		super(source);
	}
	
	/**
	 * The initialize simulation control event. Requests a simulation be 
	 * initialized to a start time and configured to run until an end time.
	 * 
	 * @author Paul T. Grogan
	 */
	public static class Initialize extends SimulationControlEvent {
		private static final long serialVersionUID = 1063804393763397020L;
		
		private final long startTime;
		private final long endTime;
		
		/**
		 * Instantiates a new initialize.
		 *
		 * @param source the source
		 * @param startTime the start time
		 * @param endTime the end time
		 */
		public Initialize(Object source, long startTime, long endTime) {
			super(source);
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		/**
		 * Gets the start time.
		 *
		 * @return the start time
		 */
		public long getStartTime() {
			return startTime;
		}
		
		/**
		 * Gets the end time.
		 *
		 * @return the end time
		 */
		public long getEndTime() {
			return endTime;
		}
	}
	
	/**
	 * The advance simulation control event. Requests a simulation be 
	 * advanced by a specified duration.
	 * 
	 * @author Paul T. Grogan
	 */
	public static class Advance extends SimulationControlEvent {
		private static final long serialVersionUID = -5544757601132237751L;
		
		private final long duration;
		
		/**
		 * Instantiates a new advance.
		 *
		 * @param source the source
		 * @param duration the duration
		 */
		public Advance(Object source, long duration) {
			super(source);
			this.duration = duration;
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
	
	/**
	 * The advance-to-end simulation control event. Requests a simulation be 
	 * advanced until reaching the end time.
	 * 
	 * @author Paul T. Grogan
	 */
	public static class AdvanceToEnd extends SimulationControlEvent {
		private static final long serialVersionUID = 5952299548097610846L;

		/**
		 * Instantiates a new advance to end.
		 *
		 * @param source the source
		 */
		public AdvanceToEnd(Object source) {
			super(source);
		}
	}
	
	/**
	 * The reset simulation control event. Requests a simulation be 
	 * reset to the original initial conditions.
	 * 
	 * @author Paul T. Grogan
	 */
	public static class Reset extends SimulationControlEvent {
		private static final long serialVersionUID = 5952299548097610846L;

		/**
		 * Instantiates a new reset.
		 *
		 * @param source the source
		 */
		public Reset(Object source) {
			super(source);
		}
	}
	
	/**
	 * The execute simulation control event. Requests a simulation be 
	 * initialized and advanced until reaching the end time.
	 * 
	 * @author Paul T. Grogan
	 */
	public static class Execute extends SimulationControlEvent {
		private static final long serialVersionUID = 5952299548097610846L;
		
		private final long startTime;
		private final long endTime;

		/**
		 * Instantiates a new execute.
		 *
		 * @param source the source
		 * @param startTime the start time
		 * @param endTime the end time
		 */
		public Execute(Object source, long startTime, long endTime) {
			super(source);
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		/**
		 * Gets the start time.
		 *
		 * @return the start time
		 */
		public long getStartTime() {
			return startTime;
		}
		
		/**
		 * Gets the end time.
		 *
		 * @return the end time
		 */
		public long getEndTime() {
			return endTime;
		}
	}
}
