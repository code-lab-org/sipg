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

/**
 * Enumeration of different infrastructure sectors.
 * 
 * @author Paul T. Grogan
 */
public enum Sector { 
	AGRICULTURE("Agriculture"), 
	WATER("Water"),
	ELECTRICITY("Electricity"),
	PETROLEUM("Petroleum");
	
	private final String name;
	
	/**
	 * Instantiates a new sector.
	 *
	 * @param name the name
	 */
	private Sector(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
};