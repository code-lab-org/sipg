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
package edu.mit.sipg.core.lifecycle;

import edu.mit.sipg.units.CurrencyUnits;
import edu.mit.sipg.units.TimeUnits;

/**
 * The default implementation of the lifecycle model interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultLifecycleModel implements LifecycleModel, EditableLifecycleModel {
	private static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	private static final TimeUnits timeUnits = TimeUnits.year;

	@Override
	public LifecycleModel createLifecycleModel() {
		return new DefaultLifecycleModel();
	}

	@Override
	public double getCapitalExpense() {
		return 0;
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return timeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	@Override
	public double getDecommissionExpense() {
		return 0;
	}

	@Override
	public double getFixedOperationsExpense() {
		return 0;
	}

	@Override
	public EditableLifecycleModel getMutableLifecycleModel() {
		return this;
	}

	@Override
	public TimeUnits getTimeUnits() {
		return timeUnits;
	}

	@Override
	public void initialize(long time) { }

	@Override
	public boolean isExists() {
		return true;
	}

	@Override
	public boolean isOperational() {
		return true;
	}

	@Override
	public void tick() { }

	@Override
	public void tock() { }
}
