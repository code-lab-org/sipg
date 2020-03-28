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
package edu.mit.sips.core.base;

import edu.mit.sips.core.Society;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * A default implementation of the infrastructure system interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultInfrastructureSystem implements InfrastructureSystem {
	protected static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	protected static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	private String name;
	private transient Society society;
	
	/**
	 * Instantiates a new default infrastructure system.
	 */
	protected DefaultInfrastructureSystem() {
		this.name = "";
	}
	
	/**
	 * Instantiates a new default infrastructure system.
	 *
	 * @param name the name
	 */
	public DefaultInfrastructureSystem(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
	}
	
	@Override
	public double getCapitalExpense() {
		return 0;
	}

	@Override
	public double getCashFlow() {
		return 0;
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public final Society getSociety() {
		return society;
	}

	@Override
	public void initialize(long time) { }
	
	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	@Override
	public void tick() { }

	@Override
	public void tock() { }
}
