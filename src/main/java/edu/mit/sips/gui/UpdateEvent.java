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
package edu.mit.sips.gui;

import java.util.EventObject;

import edu.mit.sips.core.Country;

/**
 * The update event object is a change triggered by a simulation event.
 * 
 * @author Paul T. Grogan
 */
public class UpdateEvent extends EventObject {
	private static final long serialVersionUID = 5248912198162523521L;
	
	private final long time;
	private final Country country;
	
	/**
	 * Instantiates a new update event.
	 *
	 * @param source the source
	 * @param time the time
	 * @param country the country
	 */
	public UpdateEvent(Object source, long time, Country country) {
		super(source);
		this.time = time;
		this.country = country;
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
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}
}
