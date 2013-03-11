package edu.mit.sips.core;

import java.util.List;

/**
 * The Interface InfrastructureSoS.
 */
public interface InfrastructureSoS extends InfrastructureSystem {

	/**
	 * Gets the systems.
	 *
	 * @return the systems
	 */
	public List<? extends InfrastructureSystem> getSystems();
	
	/**
	 * The Interface Local.
	 */
	public static interface Local extends InfrastructureSoS, InfrastructureSystem.Local {

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSoS#getSystems()
		 */
		public List<? extends InfrastructureSystem.Local> getSystems();
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends InfrastructureSoS, InfrastructureSystem.Remote {

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSoS#getSystems()
		 */
		public List<? extends InfrastructureSystem.Remote> getSystems();
	}
}
