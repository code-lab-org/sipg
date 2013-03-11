package edu.mit.sips.gui;

import java.util.EventObject;

/**
 * The Class AdvanceSimulationEvent.
 */
public abstract class SimulationControlEvent extends EventObject {
	private static final long serialVersionUID = -5707468210897815237L;
	
	/**
	 * Instantiates a new advance simulation event.
	 *
	 * @param source the source
	 */
	public SimulationControlEvent(Object source) {
		super(source);
	}
	
	/**
	 * The Class Advance.
	 */
	public static class Initialize extends SimulationControlEvent {
		private static final long serialVersionUID = 1063804393763397020L;
		
		private final long startTime;
		private final long endTime;
		
		/**
		 * Instantiates a new advance.
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
	 * The Class Advance.
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
	 * The Class AdvanceToEnd.
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
	 * The Class Reset.
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
	 * The Class Execute.
	 */
	public static class Execute extends SimulationControlEvent {
		private static final long serialVersionUID = 5952299548097610846L;
		
		private final long startTime;
		private final long endTime;

		/**
		 * Instantiates a new advance to end.
		 *
		 * @param source the source
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
