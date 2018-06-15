package edu.mit.sips.core;

import java.util.List;

public interface InfrastructureSoS extends InfrastructureSystem {
	
	/**
	 * Gets the nested systems.
	 *
	 * @return the nested systems
	 */
	public List<? extends InfrastructureSystem> getNestedSystems();
}
