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
package edu.mit.sipg.scenario;

import edu.mit.sipg.core.base.InfrastructureElement;

/**
 * An interface to a template that can generate infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public interface ElementTemplate {
	
	/**
	 * Creates the element.
	 *
	 * @param timeInitialized the time initialized
	 * @param origin the origin
	 * @param destination the destination
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long timeInitialized,
			String origin, String destination);
	
	/**
	 * Creates the element.
	 *
	 * @param timeInitialized the time initialized
	 * @param operationsDuration the operations duration
	 * @param origin the origin
	 * @param destination the destination
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement(long timeInitialized,
			long operationsDuration, String origin, String destination);
	
	/**
	 * Gets the max operations.
	 *
	 * @return the max operations
	 */
	public long getMaxOperations();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public Sector getSector();
	
	/**
	 * Gets the time available.
	 *
	 * @return the time available
	 */
	public long getTimeAvailable();

	/**
	 * Checks if is transport.
	 *
	 * @return true, if is transport
	 */
	public boolean isTransport();
}
